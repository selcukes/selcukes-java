package io.github.selcukes.wdb.util;

import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public final class BinaryDownloadUtil {


    private static final Logger logger = LoggerFactory.getLogger(BinaryDownloadUtil.class);

    private BinaryDownloadUtil() {

    }

    public static void downloadBinary(URL downloadURL, File downloadTo) {
        download(downloadURL, downloadTo, false);
    }

    public static String downloadAndReadFile(URL downloadURL) {
        File destinationFile = TempFileUtil.createTempFileAndDeleteOnExit();

        download(downloadURL, destinationFile, true);

        if (destinationFile.exists()) {
            try {
                return FileUtils.readFileToString(destinationFile, Charset.defaultCharset()).trim();
            } catch (IOException e) {
                throw new WebDriverBinaryException(e);
            }
        }
        throw new WebDriverBinaryException("Unable to download file from: " + getAbsoluteURL(downloadURL));
    }

    private static void download(URL downloadURL, File downloadTo, boolean silentDownload) {
        try {
            long downloadStartTime = System.nanoTime();

            if (!silentDownload) {
                logger.info(()->"Downloading driver binary from: " + getAbsoluteURL(downloadURL));
            }

            FileUtils.copyURLToFile(downloadURL, downloadTo);

            long downloadEndTime = System.nanoTime() - downloadStartTime;

            if (!silentDownload) {
                logger.info(()->(TimeUnit.NANOSECONDS.toHours(downloadEndTime)+" min "+getSeconds(downloadEndTime)+" secs"));
            }
        } catch (IOException e) {
            throw new WebDriverBinaryException(e);
        }
    }
    private static long getSeconds(long downloadEndTime)
    {
        return TimeUnit.NANOSECONDS.toSeconds(downloadEndTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(downloadEndTime));
    }
    private static String getAbsoluteURL(URL url) {
        return url.getProtocol() + "://" + url.getHost() + url.getPath();
    }
}