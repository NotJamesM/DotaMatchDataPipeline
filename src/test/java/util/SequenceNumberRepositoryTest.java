package util;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.SequenceNumberRepositorySettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SequenceNumberRepositoryTest implements WithAssertions {

    public static final String TEST_SEQ_NUMBER = "src/test/resources/test_seqNumber.txt";
    public static final Logger TEST_LOGGER = LoggerFactory.getLogger("TEST_LOGGER");
    private final SequenceNumberRepositorySettings settings = mock(SequenceNumberRepositorySettings.class);
    private final SequenceNumberRepository sequenceNumberRepository = new SequenceNumberRepository(settings, TEST_LOGGER);

    @Test
    void shouldReturnTheCurrentSequenceNumber() {
        final long currentSequenceNumber = sequenceNumberRepository.getCurrentSequenceNumber();
        assertThat(currentSequenceNumber).isEqualTo(15L);
    }

    @Test
    void shouldWriteLatestSequenceNumber() throws IOException {
        sequenceNumberRepository.updateSequenceNumber(20L);
        assertThat(Files.readString(Path.of(TEST_SEQ_NUMBER))).isEqualTo("20");
    }

    @BeforeEach
    void setUp() throws IOException {
        primeSeqNumberFile();
        when(settings.getSequenceNumberFilePath()).thenReturn(Path.of(TEST_SEQ_NUMBER));
    }

    private void primeSeqNumberFile() throws IOException {
        Files.writeString(Path.of(TEST_SEQ_NUMBER), "15");
    }
}