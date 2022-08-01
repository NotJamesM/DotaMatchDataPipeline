package com.github.notjamesm.util;

import com.github.notjamesm.domain.valve.Player;

import java.util.List;

public class DataRendererTest {

//    @Test
//    void shouldExportCorrectly() throws IOException {
//
//        final DataRenderer dataRenderer = new DataRendererFactory(LoggerFactory.getLogger("TEST_LOGGER"), new HeroFactory().initialiseHeroes(Path.of("heroes.json"))).dataRenderer();
//        final Match match = new Match(1, 2, 1, 22, players1);
//        final Match match2 = new Match(2, 2, 1, 22, players2);
//        match.setRadiantWin(true);
//        match2.setRadiantWin(false);
//        match.setAbandoned(false);
//        match2.setAbandoned(false);
//        match.setGamemode(22);
//        match2.setGamemode(22);
//        final MatchHistoryResult matchHistoryResult = new MatchHistoryResult(0, List.of(match, match2), 2, 1, 2);
//        final String filename = dataRenderer.exportDataToModelFormat(matchHistoryResult);
//
//        final String expected = String.join(System.lineSeparator(), Files.readAllLines(Path.of("example_export.csv")));
//        final String actual = String.join(System.lineSeparator(), Files.readAllLines(Path.of(filename)));
//        assertEquals(expected, actual);
//    }

    private static final List<Player> players1 = List.of(
            new Player(1, 1, 1, 0, 1),
            new Player(1, 1, 1, 0, 2),
            new Player(1, 1, 1, 0, 3),
            new Player(1, 1, 1, 0, 4),
            new Player(1, 1, 1, 0, 5),
            new Player(1, 1, 1, 1, 6),
            new Player(1, 1, 1, 1, 7),
            new Player(1, 1, 1, 1, 8),
            new Player(1, 1, 1, 1, 9),
            new Player(1, 1, 1, 1, 10)
    );

    private static final List<Player> players2 = List.of(
            new Player(1, 1, 1, 0, 23),
            new Player(1, 1, 1, 0, 55),
            new Player(1, 1, 1, 0, 100),
            new Player(1, 1, 1, 0, 15),
            new Player(1, 1, 1, 0, 86),
            new Player(1, 1, 1, 1, 2),
            new Player(1, 1, 1, 1, 33),
            new Player(1, 1, 1, 1, 123),
            new Player(1, 1, 1, 1, 80),
            new Player(1, 1, 1, 1, 46)
    );
}

