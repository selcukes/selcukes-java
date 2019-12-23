package io.github.selcukes.dp.util;

import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;

import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

public final class HttpUtils {
    private static final Logger logger = Logger.getLogger(HttpUtils.class.getName());
    private static String proxy;

    private HttpUtils() {

    }

    private static Function<String, HttpURLConnection> connection = endpoint -> {
        HttpURLConnection.setFollowRedirects(false);

        final HttpURLConnection httpURLConnection;

        try {
            URL url = new URL(endpoint);
            httpURLConnection = isProxy().isPresent() ? (HttpURLConnection) url.openConnection(unwrap(isProxy())) : (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return httpURLConnection;
        } catch (Exception e) {
            throw new DriverPoolException(e);
        } finally {
            HttpURLConnection.setFollowRedirects(true);
        }
    };

    public static String getLocation(String endpoint, String proxyUrl) {
        proxy = proxyUrl;
        return extract(connection.apply(endpoint), t -> t.getHeaderField("Location"));
    }

    public static InputStream getResponseInputStream(String endpoint, String proxyUrl) {
        proxy = proxyUrl;
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

    public static Optional<Proxy> isProxy() {
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
            logger.severe(e.getMessage());
        }
        return proxyUrl;
    }

}