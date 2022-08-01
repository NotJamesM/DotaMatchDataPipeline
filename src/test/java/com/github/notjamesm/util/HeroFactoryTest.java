package com.github.notjamesm.util;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeroFactoryTest {

    private static final int HERO_COUNT = 123;

    @Test
    void shouldInitialiseHeroMap() throws IOException {
        final Map<Integer, HeroFactory.HeroNameColumnIndex> heroes = new HeroFactory().initialiseHeroes(Path.of("config/heroes.json"));
        assertEquals(HERO_COUNT, heroes.entrySet().size());
        assertEquals("antimage", heroes.get(1).getName());
        assertEquals(1, heroes.get(1).getColumnIndex());
        assertEquals("primal_beast", heroes.get(137).getName());
        assertEquals(123, heroes.get(137).getColumnIndex());
    }
}
