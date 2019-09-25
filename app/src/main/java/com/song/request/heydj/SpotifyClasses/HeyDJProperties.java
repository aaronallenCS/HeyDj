package com.song.request.heydj.SpotifyClasses;
import android.content.res.AssetManager;

import com.song.request.heydj.AddSongRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HeyDJProperties {
    private static final String DEFAULT_CONFIG_FILE = "config.properties";

    private static final String PROP_CLIENT_ID = "CLIENT_ID";
    private static final String PROP_CLIENT_SECRET = "CLIENT_SECRET";
    private static final String PROP_REFRESH_TOKEN = "REFRESH_TOKEN";

    private Properties properties;

    public HeyDJProperties() throws IOException {
        this(DEFAULT_CONFIG_FILE);
    }

    public HeyDJProperties(String configFile) throws IOException {
        readProperties(configFile);
    }

    private void readProperties(String configFile) throws IOException {
        properties = new Properties();

        AssetManager assetManager = AddSongRequest.getContext().getAssets();
        InputStream inputStream = assetManager.open(configFile);

        properties.load(inputStream);
    }

    public String getClientId() {
        return properties.getProperty(PROP_CLIENT_ID);
    }

    public String getClientSecret() {
        return properties.getProperty(PROP_CLIENT_SECRET);
    }

    public String getRefreshToken() {
        return properties.getProperty(PROP_REFRESH_TOKEN);
    }
}
