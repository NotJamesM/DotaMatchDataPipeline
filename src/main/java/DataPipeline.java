import domain.model.MatchModel;
import org.slf4j.Logger;
import services.DataRenderer;
import util.DataFixer;
import util.Scraper;

import java.util.List;

public class DataPipeline {

    private final Scraper scraper;
    private final DataFixer dataFixer;
    private final DataRenderer dataRenderer;
    private final Logger logger;

    public DataPipeline(Scraper scraper,
                        DataFixer dataFixer,
                        DataRenderer dataRenderer,
                        Logger logger) {
        this.scraper = scraper;
        this.dataFixer = dataFixer;
        this.dataRenderer = dataRenderer;
        this.logger = logger;
    }

    public void getAndExportData() {
        try {
            final List<MatchModel> matchRecentHistoryResult = scraper.scrapeRecentMatches(100);
            dataRenderer.exportDataToModelFormat(null);
            dataFixer.removeDuplicates("");
        } catch (Exception e) {
            logger.error("Uncaught Exception: \n", e);
        }
    }
}
