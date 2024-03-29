package com.github.notjamesm.util;

import com.github.notjamesm.domain.model.HeroModel;
import com.github.notjamesm.domain.model.MatchModel;
import com.github.notjamesm.settings.DataRenderSettings;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static java.lang.System.lineSeparator;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

public class DataRenderer {

    private static final List<Integer> VALID_GAMEMODES = List.of(22);
    public static final String MATCH_ID_lOG_FILENAME = "MATCH_ID_LOG.txt";
    private final Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap;
    private final DataRenderSettings dataRenderSettings;
    private final Logger applicationLogger;
    private static long ignoreCount = 0;

    public DataRenderer(Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap, DataRenderSettings dataRenderSettings, Logger applicationLogger) {
        this.heroIdMap = heroIdMap;
        this.dataRenderSettings = dataRenderSettings;
        this.applicationLogger = applicationLogger;
    }

    public String exportDataToModelFormat(List<MatchModel> matchModels) {
        return exportDataToModelFormat(matchModels, "");
    }

    public String exportDataToModelFormat(List<MatchModel> matchModels, String filenameToAppendTo) {
        String filename;
        if (filenameToAppendTo.isBlank()) {
            filename = "output-%s.csv".formatted(getTimeStamp());
            writeToFile(generateHeader(), filename);
        } else {
            filename = filenameToAppendTo;
        }

        matchModels.stream()
                .filter(this::filterMatches)
                .sorted()
                .map(this::mapToRow)
                .forEach(row -> writeToFile(row, filename));

        logLatestMatchId(matchModels);
        applicationLogger.info("Completed export, ignored {} matchIds.", ignoreCount);
        return filename;
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
    }

    private void logLatestMatchId(List<MatchModel> matchModels) {
        final List<Long> collect = matchModels.stream().map(MatchModel::matchId).sorted().toList();
        final String head = Long.toString(collect.get(0));
        final String tail = Long.toString(collect.get(collect.size() - 1));
        applicationLogger.info("List head {} | List tail {}", head, tail);
        final String row = "%s | head: %s - tail: %s".formatted(getTimeStamp(), head, tail);
        writeToFile(row, MATCH_ID_lOG_FILENAME);
    }

    private boolean filterMatches(MatchModel match) {
        final boolean isValidGamemode = VALID_GAMEMODES.contains(match.gameMode());
        final boolean isValidGame = isValidGamemode && !match.isAbandoned();
        if (!isValidGame) {
            applicationLogger.info("Ignoring match id {}: isValidGameMode: {} - gamemode(details) {} lobby type - {} | isAbandoned: {} | gameLength: {}",
                    match.matchId(), isValidGamemode, match.gameMode(), match.lobbyType(), match.isAbandoned(), formatDuration(match.duration().toMillis(), "HH:mm:ss"));
            ignoreCount++;
        }
        return isValidGame;
    }

    private void writeToFile(String row, String filename) {
        try {
            if (!Files.exists(dataRenderSettings.getExportDirectoryPath())) {
                Files.createDirectory(dataRenderSettings.getExportDirectoryPath());
            }
            Files.writeString(Path.of("%s/%s".formatted(dataRenderSettings.getExportDirectoryPath(), filename)), row + lineSeparator(), APPEND, CREATE);
        } catch (IOException e) {
            applicationLogger.error("Error writing row to output: {}\n{}", filename, e);
            throw new RuntimeException(e);
        }
    }

    private String mapToRow(MatchModel match) {
        applicationLogger.info("Exporting data for match id: {}", match.matchId());
        StringBuilder sb = new StringBuilder();
        sb.append("%s,%s,%s".formatted(1, match.matchId(), match.radiantWin()));
        for (Map.Entry<Integer, HeroFactory.HeroNameColumnIndex> heroIndexEntry : heroIdMap.entrySet()) {
            List<HeroModel> heroes = match.heroes();
            for (int i = 0; i < heroes.size(); i++) {
                if (appendHeroEntry(sb, heroIndexEntry, heroes, i)) break;
            }
        }

        return sb.toString();
    }

    private boolean appendHeroEntry(StringBuilder sb, Map.Entry<Integer, HeroFactory.HeroNameColumnIndex> heroIndexEntry, List<HeroModel> heroes, int i) {
        HeroModel hero = heroes.get(i);
        if (hero.heroId() == heroIndexEntry.getKey()) {
            if (hero.isRadiant()) {
                sb.append(",1,0");
            } else {
                sb.append(",0,1");
            }
            return true;
        }
        if (i == heroes.size() - 1) {
            sb.append(",0,0");
        }
        return false;
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
