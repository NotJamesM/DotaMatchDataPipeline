import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import util.DataFixer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataFixerTest {

    public static final String DUPLICATE_EXPORT = "example_export_with_duplicates.csv";
    public static final String DUPLICATE_FILE_COPY = "example_export_with_duplicates_copy.csv";
    private final DataFixer dataFixer = new DataFixer(LoggerFactory.getLogger("TEST_LOGGER"));

    @Test
    void shouldRemoveDuplicateMatchIds() throws IOException {
        Files.copy(Path.of(DUPLICATE_EXPORT), Path.of(DUPLICATE_FILE_COPY));
        dataFixer.removeDuplicates(DUPLICATE_FILE_COPY);
        final int size = Files.readAllLines(Path.of(DUPLICATE_FILE_COPY)).size();
        assertEquals(6, size);
    }


}
