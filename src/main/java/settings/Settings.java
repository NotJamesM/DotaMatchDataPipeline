package settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Settings implements ValveSettings {

    private final Properties properties;

    public Settings(String path) throws IOException {
        properties = new Properties();
        properties.load(Files.newBufferedReader(Path.of(path)));
    }

    @Override
    public String valveApiKey() {
        return properties.getProperty("valve.api.key");
    }

    @Override
    public double rateLimit() {
        return Double.parseDouble(properties.getProperty("rate.limit"));
    }

    @Override
    public String baseValveApiUrl() {
        return properties.getProperty("base.valve.api.url");
    }

    @Override
    public String getMatchHistoryPath() {
        return properties.getProperty("get.match.history.path");
    }

    @Override
    public String getMatchHistoryBySeqNumPath() {
        return properties.getProperty("get.match.history.by.seq.num.path");
    }

    @Override
    public String getMatchDetailsPath() {
        return properties.getProperty("get.match.details.path");
    }
}
