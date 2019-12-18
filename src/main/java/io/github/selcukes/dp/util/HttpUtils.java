package io.github.selcukes.dp.util;

import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Optional;
import java.util.function.Function;

public final class HttpUtils {
    private HttpUtils() {

    }

    private static Function<String, HttpURLConnection> connection = endpoint -> {
        HttpURLConnection.setFollowRedirects(false);

        final HttpURLConnection httpURLConnection;

        try {
            URL url = new URL(endpoint);
            httpURLConnection = (HttpURLConnection) url.openConnection(OptionalUtil.unwrap(createProxy("https://127.0.0.1:0")));
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

    public static Optional<Proxy> createProxy(String proxy) {
        Optional<URL> url = getProxyUrl(proxy);
        if (url.isPresent()) {
            String proxyHost = url.get().getHost();
            int proxyPort = url.get().getPort() == -1 ? 80
                    : url.get().getPort();
            return Optional.of(new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(proxyHost, proxyPort)));
        }
        return Optional.empty();
    }

    public static Optional<URL> getProxyUrl(String proxy) {
        Optional<URL> proxyUrl = Optional.empty();
        try {
            proxyUrl = Optional.of(new URL(proxy));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return proxyUrl;
    }

}