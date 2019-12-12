package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.URLLookup;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;
import io.github.selcukes.dp.util.TempFileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

/**
 * The type Explorer binary properties.
 */
public class IExplorerBinaryProperties implements BinaryProperties {
    private final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";
    private String release;
    private TargetArch targetArch;
    private Function<Environment, String> osArc = (osEnvironment) -> osEnvironment.getArchitecture() == 32 ? "Win32" : "x64";

    private IExplorerBinaryProperties() {
        release = getLatestRelease();

        if (release.length() == 0) {
            throw new DriverPoolException("Unable to read the latest IEDriver release from: " + URLLookup.IEDRIVER_LATEST_RELEASE_URL);
        }
    }

    private IExplorerBinaryProperties(String release) {
        this.release = release;
    }

    /**
     * For latest release explorer binary properties.
     *
     * @return the explorer binary properties
     */
    public static IExplorerBinaryProperties forLatestRelease() {
        return new IExplorerBinaryProperties();
    }

    /**
     * For previous release explorer binary properties.
     *
     * @param release the release
     * @return the explorer binary properties
     */
    public static IExplorerBinaryProperties forPreviousRelease(String release) {
        return new IExplorerBinaryProperties(release);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    URLLookup.IEDRIVER_URL,
                    release,
                    osArc.apply(getBinaryEnvironment()),
                    release));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch != null ? Environment.create(targetArch.getValue()) : Environment.create();
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/iedriver_%s.zip", TempFileUtil.getTempDirectory(), release));
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return DownloaderType.ZIP;
    }

    @Override
    public String getBinaryFilename() {
        return "IEDriverServer.exe";
    }

    public String getBinaryDirectory() {
        return release != null ? "iedriver_" + release : "iedriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "IEDriver";
    }

    @Override
    public String getBinaryVersion() {
        return release;
    }

    @Override
    public void setBinaryArchitecture(TargetArch targetArch) {
        this.targetArch = targetArch;
    }

    private String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(URLLookup.IEDRIVER_LATEST_RELEASE_URL);

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(downloadStream);
            doc.getDocumentElement().normalize();

            final XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            Element element = (Element) xpath.evaluate("(//Contents/Key[contains(.,'/IEDriverServer')])[last()]", doc, XPathConstants.NODE);

            if (element == null) {
                return "";
            }

            final String release = element.getTextContent();
            return release.substring(0, release.indexOf("/"));

        } catch (Exception e) {
            throw new DriverPoolException(e);
        }
    }
}