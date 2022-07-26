import Settings.Settings;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        final DataPipeline dataPipeline = new DataPipelineBuilder(
                new Settings("datapipeline.properties"),
                LogManager.getLogger("APPLICATION"))
                .build();

        dataPipeline.getAndExportData();
    }
}
