import domain.valve.MatchHistoryResult;
import org.apache.logging.log4j.Logger;
import services.DataRenderer;
import util.DataFixer;
import util.Scraper;

public class DataPipeline {

    private final Scraper scraper;
    private final DataFixer dataFixer;
    private final DataRenderer dataRenderer;
    private final Logger logger;

    public DataPipeline(Scraper scraper, DataFixer dataFixer, DataRenderer dataRenderer, Logger logger) {
        this.scraper = scraper;
        this.dataFixer = dataFixer;
        this.dataRenderer = dataRenderer;
        this.logger = logger;
    }

    public void getAndExportData() {
        try {
            final MatchHistoryResult matchHistoryResult = scraper.scrapeRecentMatches(100);
            dataRenderer.exportDataToModelFormat(matchHistoryResult);
            dataFixer.removeDuplicates("");
        } catch (Exception e) {
            logger.error("Uncaught Exception: \n", e);
        }
    }
}
