package com.example.android.heydj.SpotifyClasses;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class SpotifyClient {
    private static final int SECONDS_TO_MILLIS = 1000;
    private static final long SLACK_TIME = 5 * SECONDS_TO_MILLIS;

    public static final String AUTH_BASE_URL = "https://accounts.spotify.com/";
    public static final String API_BASE_URL = "https://api.spotify.com/v1/";

    private String clientId;
    private String clientSecret;

    private String refreshToken;

    private String accessToken;
    private Long expiryTime;

    public SpotifyClient(String clientId, String clientSecret, String refreshToken) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    private void obtainAccessToken() throws IOException {

        URL url = new URL(AUTH_BASE_URL + "api/token");

        Date now = new Date();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");

            // send post body
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder
                    .append("client_id=").append(clientId)
                    .append("&client_secret=").append(clientSecret)
                    .append("&refresh_token=").append(refreshToken)
                    .append("&grant_type=refresh_token");
            wr.writeBytes(bodyBuilder.toString());
            wr.flush();
            wr.close();

            // read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            TokenResponse resp = new Gson().fromJson(reader, TokenResponse.class);

            this.accessToken = resp.accessToken;
            this.expiryTime = now.getTime() + resp.getExpiresIn() * SECONDS_TO_MILLIS;

            reader.close();
        } finally {
            conn.disconnect();
        }
    }

    private boolean isTokenExpired() {
        if (expiryTime == null)
            return true;
        return new Date().getTime() + SLACK_TIME > expiryTime;
    }

    // @see https://developer.spotify.com/console/get-search-item

    public String search(String query, String type) throws IOException {
        return search(query, type, null);
    }

    public String search(String query, String type, String market) throws IOException {
        return search(query, type, market, null, null);
    }

    public String search(String query, String type, int limit) throws IOException {
        return search(query, type, null, limit, null);
    }

    public String search(String query, String type, Integer limit, Integer offset) throws IOException {
        return search(query, type, null, limit, offset);
    }

    public String search(String query, String type, String market, Integer limit, Integer offset)
            throws IOException {

        if (isTokenExpired())
            obtainAccessToken();

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append(API_BASE_URL)
                .append("search").append('?')
                .append("q=").append(query)
                .append("&type=").append(type);

        if (market != null)
            urlBuilder.append("&market=").append(market);
        if (limit != null)
            urlBuilder.append("&limit=").append(limit);
        if (offset != null)
            urlBuilder.append("&offset=").append(offset);

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            // read response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            return stringBuilder.toString();
        } finally{
            conn.disconnect();
        }
    }

    class TokenResponse {

        @SerializedName("access_token")
        private String accessToken;

        @SerializedName("token_type")
        private String tokenType;

        @SerializedName("expires_in")
        private long expiresIn;

        @SerializedName("scope")
        private String scope;

        public TokenResponse() {
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }
}
