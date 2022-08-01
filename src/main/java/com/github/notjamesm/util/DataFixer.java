package com.github.notjamesm.util;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataFixer {

    private final Logger logger;
    public static final Pattern PATTERN = Pattern.compile("^[0-1],([0-9]+)", Pattern.CASE_INSENSITIVE);

    public DataFixer(Logger logger) {
        this.logger = logger;
    }

    public void removeDuplicates(String filename) throws IOException {
        final List<String> collect = Files.readAllLines(Path.of(filename)).stream()
                .map(this::extractMatchId)
                .collect(Collectors.toList());
    }

    private String extractMatchId(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        } else {
            logger.error("Match Id could not be found line:\n{}", line);
        }
        return null;
    }
}
