package io.github.selcukes.dp.util;

import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

public final class HttpUtils {
    private HttpUtils() {

    }

    private static Function<String, HttpURLConnection> connection = endpoint -> {
        HttpURLConnection.setFollowRedirects(false);

        final HttpURLConnection httpURLConnection;

        try {
            URL url = new URL(endpoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return httpURLConnection;
        } catch (Exception e) {
            throw new DriverPoolException(e);
        } finally {
            HttpURLConnection.setFollowRedirects(true);
        }
    };

    public static String getLocation(String endpoint) {
        return extract(connection.apply(endpoint), t -> t.getHeaderField("Location"));
    }

    public static InputStream getResponseInputStream(String endpoint) {
        return extract(connection.apply(endpoint), t -> {
            try {
                return t.getInputStream();
            } catch (IOException e) {
                throw new DriverPoolException(e);
            }
        });
    }

    private static <T> T extract(HttpURLConnection connection, Function<HttpURLConnection, T> condition) {
        return condition.apply(connection);
    }
}