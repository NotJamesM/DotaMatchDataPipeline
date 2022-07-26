package settings;

public interface ValveSettings {

    String valveApiKey();

    double rateLimit();

    String baseValveApiUrl();

    String getMatchHistoryPath();

    String getMatchHistoryBySeqNumPath();

    String getMatchDetailsPath();
}
