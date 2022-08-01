package com.github.notjamesm.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataFixerTest {

    public static final String DUPLICATE_EXPORT = "src/test/resources/example_export_with_duplicates.csv";
    public static final String DUPLICATE_FILE_COPY = "src/test/resources/example_export_with_duplicates_copy.csv";
    private final DataFixer dataFixer = new DataFixer(LoggerFactory.getLogger("TEST_LOGGER"));

    @Test
    @Disabled("todo")
    void shouldRemoveDuplicateMatchIds() throws IOException {
        Path duplicatePath = Path.of(DUPLICATE_FILE_COPY);
        Files.copy(Path.of(DUPLICATE_EXPORT), duplicatePath);
        dataFixer.removeDuplicates(DUPLICATE_FILE_COPY);
        final int size = Files.readAllLines(duplicatePath).size();
        assertEquals(6, size);
    }


}
