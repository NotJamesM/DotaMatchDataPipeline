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

    public void getAndExportRecentData() {
        try {
            final List<MatchModel> matchModels = scraper.scrapeRecentMatches(100);
            dataRenderer.exportDataToModelFormat(matchModels);
        } catch (Exception e) {
            logger.error("Uncaught Exception: \n", e);
        }
    }

    public void getAndExportDataBySequenceNumber() {
        try {
            final List<MatchModel> matchModels = scraper.scrapeMatchesBySequenceNumber(10);
            dataRenderer.exportDataToModelFormat(matchModels);
        } catch (Exception e) {
            logger.error("Uncaught Exception: \n", e);
        }
    }
}
