package util;

import org.slf4j.Logger;
import settings.SequenceNumberRepositorySettings;

import java.io.IOException;

import static java.lang.Long.parseLong;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;

public class SequenceNumberRepository {

    private final SequenceNumberRepositorySettings sequenceNumberRepositorySettings;
    private final Logger applicationLogger;

    public SequenceNumberRepository(SequenceNumberRepositorySettings sequenceNumberRepositorySettings, Logger applicationLogger) {
        this.sequenceNumberRepositorySettings = sequenceNumberRepositorySettings;
        this.applicationLogger = applicationLogger;
    }

    public long getCurrentSequenceNumber() {
        try {
            return parseLong(readString(sequenceNumberRepositorySettings.getSequenceNumberFilePath()));
        } catch (IOException e) {
            applicationLogger.error("Failed to get sequence number due to:\n", e);
            throw new RuntimeException(e);
        }
    }

    public void updateSequenceNumber(long newSequenceNumber) {
        try {
            writeString(sequenceNumberRepositorySettings.getSequenceNumberFilePath(), Long.toString(newSequenceNumber));
        } catch (IOException e) {
            applicationLogger.error("Failed to update sequence number {} due to:\n{}", e, newSequenceNumber);
            throw new RuntimeException(e);
        }
    }
}
