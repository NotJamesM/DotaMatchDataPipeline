package com.github.notjamesm;

import com.github.notjamesm.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        final Logger applicationLogger = LoggerFactory.getLogger("APPLICATION");
        final DataPipeline dataPipeline = new DataPipelineBuilder(
                new Settings(System.getProperty("properties.path"), applicationLogger),
                applicationLogger)
                .build();

        dataPipeline.getAndExportDataBySequenceNumber();
    }
}
