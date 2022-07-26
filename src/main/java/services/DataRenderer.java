package services;

import domain.valve.Match;
import domain.valve.MatchHistoryResult;
import domain.valve.Player;
import org.apache.logging.log4j.Logger;
import util.HeroFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.time.Duration.ofSeconds;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class DataRenderer {

    private static final List<Long> VALID_GAMEMODES = List.of(22L);
    private final Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap;
    private final Logger applicationLogger;
    private static long ignoreCount = 0;

    public DataRenderer(Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap, Logger applicationLogger) {
        this.heroIdMap = heroIdMap;
        this.applicationLogger = applicationLogger;
    }

    public String exportDataToModelFormat(MatchHistoryResult matchHistoryResult) {
        return exportDataToModelFormat(matchHistoryResult, "");
    }

    public String exportDataToModelFormat(MatchHistoryResult matchHistoryResult, String filenameToAppendTo) {
        String filename;
        if (filenameToAppendTo.isBlank()) {
            filename = format("output-%s.csv", getTimeStamp());
            writeToFile(generateHeader(), filename);
        } else {
            filename = filenameToAppendTo;
        }

        matchHistoryResult.matches().stream()
                .filter(this::filterMatches)
                .sorted()
                .map(this::mapToRow)
                .forEach(row -> writeToFile(row, filename));

        logLatestMatchId(matchHistoryResult);
        applicationLogger.info("Completed export, ignored {} matches.", ignoreCount);
        return filename;
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
    }

    private void logLatestMatchId(MatchHistoryResult matchHistoryResult) {
        final List<Match> collect = matchHistoryResult.matches().stream().sorted().collect(Collectors.toList());
        final String head = Long.toString(collect.get(0).getMatchId());
        final String tail = Long.toString(collect.get(collect.size() - 1).getMatchId());
        applicationLogger.info("List head {} | List tail {}", head, tail);
        final String row = String.format("%s | head: %s - tail: %s", getTimeStamp(), head, tail);
        writeToFile(row, "MATCH_ID_LOG.txt");
    }

    private boolean filterMatches(Match match) {
        final boolean isValidGamemode = VALID_GAMEMODES.contains(match.getGamemode());
        final boolean isValidGame = isValidGamemode && !match.isAbandoned();
        if (!isValidGame) {
            applicationLogger.info("Ignoring match id {}: isValidGameMode: {} - gamemode(details) {} lobby type - {} | isAbandoned: {} | gameLength: {}", match.getMatchId(), isValidGamemode, match.getGamemode(), match.getLobbyType(), match.isAbandoned(),
                    formatDuration(ofSeconds(match.getDuration()).toMillis(), "HH:mm:ss"));
            ignoreCount++;
        }
        return isValidGame;
    }

    private void writeToFile(String row, String filename) {
        try {
            Files.writeString(Path.of(filename), row + System.lineSeparator(), APPEND, CREATE);
        } catch (IOException e) {
            applicationLogger.error(format("Error writing row to output: %s", filename), e);
            throw new RuntimeException(e);
        }
    }

    private String mapToRow(Match match) {
        applicationLogger.info("Exporting data for match id: {}", match.getMatchId());
        StringBuilder sb = new StringBuilder();
        sb.append(format("%s,%s,%s", 1, match.getMatchId(), isRadiantWin(match)));
        for (Map.Entry<Integer, HeroFactory.HeroNameColumnIndex> heroIndexEntry : heroIdMap.entrySet()) {
            List<Player> players = match.getPlayers();
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (player.heroId() == heroIndexEntry.getKey()) {
                    if (player.teamNumber() == 0) {
                        sb.append(",1,0");
                    } else {
                        sb.append(",0,1");
                    }
                    break;
                }
                if (i == players.size() - 1) {
                    sb.append(",0,0");
                }
            }
        }

        return sb.toString();
    }


    private String isRadiantWin(Match match) {
        return match.isRadiantWin() ? "1" : "0";
    }

    private String generateHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,match_id,radiant_win");
        for (HeroFactory.HeroNameColumnIndex index : heroIdMap.values()) {
            sb.append(",r_").append(index.getName()).append(",d_").append(index.getName());
        }
        return sb.toString();
    }
}
