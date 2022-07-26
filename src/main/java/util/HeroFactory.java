package util;

import domain.valve.Hero;
import domain.valve.Heroes;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class HeroFactory {
    public Map<Integer, HeroNameColumnIndex> initialiseHeroes(Path heroFilePath) throws IOException {
        String matches = String.join("", Files.readAllLines(heroFilePath));
        final Heroes heroes = ObjectMapperFactory.createObjectMapper().readValue(matches, Heroes.class);

        final List<Hero> heroList = heroes.heroes();
        return IntStream.range(1, heroList.size() + 1)
                .mapToObj(x -> Pair.of(heroList.get(x - 1).id(), createEntry(heroList.get(x - 1).name(), x)))
                .collect(toMap(Pair::getLeft, Pair::getRight));
    }

    private HeroNameColumnIndex createEntry(String name, int columnIndex) {
        return new HeroNameColumnIndex(name, columnIndex);
    }

    public static class HeroNameColumnIndex {
        private final String name;
        private final int columnIndex;

        public HeroNameColumnIndex(String name, int columnIndex) {
            this.name = name;
            this.columnIndex = columnIndex;
        }

        public String getName() {
            return name;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public String toString() {
            return "HeroNameColumnIndex{" +
                    "name='" + name + '\'' +
                    ", columnIndex=" + columnIndex +
                    '}';
        }
    }
}