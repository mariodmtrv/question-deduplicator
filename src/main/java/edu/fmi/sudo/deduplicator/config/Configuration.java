package edu.fmi.sudo.deduplicator.config;

/**
 * Created by Miroslav on 1/22/2017.
 */
public class Configuration {
    private static String basePath;

    public static void setBasePath(String basePath) {
        Configuration.basePath = basePath;
    }

    public static String getBasePath() {
        return basePath;
    }
}
