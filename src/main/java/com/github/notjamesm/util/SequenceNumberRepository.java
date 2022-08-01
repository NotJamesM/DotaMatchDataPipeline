package com.github.notjamesm.util;

import com.github.notjamesm.settings.SequenceNumberRepositorySettings;
import org.slf4j.Logger;

import java.io.IOException;

import static java.lang.Long.parseLong;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;
import static org.apache.commons.lang3.StringUtils.strip;

public class SequenceNumberRepository {

    private final SequenceNumberRepositorySettings sequenceNumberRepositorySettings;
    private final Logger applicationLogger;

    public SequenceNumberRepository(SequenceNumberRepositorySettings sequenceNumberRepositorySettings, Logger applicationLogger) {
        this.sequenceNumberRepositorySettings = sequenceNumberRepositorySettings;
        this.applicationLogger = applicationLogger;
    }

    public long getCurrentSequenceNumber() {
        try {
            final long sequenceNumber = parseLong(strip(readString(sequenceNumberRepositorySettings.getSequenceNumberFilePath())));
            applicationLogger.info("Getting current sequence number of: {}", sequenceNumber);
            return sequenceNumber;
        } catch (IOException e) {
            applicationLogger.error("Failed to get sequence number due to:\n{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateSequenceNumber(long newSequenceNumber) {
        try {
            applicationLogger.info("Updating sequence number to: {}", newSequenceNumber);
            writeString(sequenceNumberRepositorySettings.getSequenceNumberFilePath(), Long.toString(newSequenceNumber));
        } catch (IOException e) {
            applicationLogger.error("Failed to update sequence number {} due to:\n{}", e, newSequenceNumber);
            throw new RuntimeException(e);
        }
    }
}
