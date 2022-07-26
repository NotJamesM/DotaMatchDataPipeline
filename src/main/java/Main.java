import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Settings;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        final Logger applicationLogger = LoggerFactory.getLogger("APPLICATION");
        final DataPipeline dataPipeline = new DataPipelineBuilder(
                new Settings("datapipeline.properties"),
                applicationLogger)
                .build();

        dataPipeline.getAndExportData();
    }
}
