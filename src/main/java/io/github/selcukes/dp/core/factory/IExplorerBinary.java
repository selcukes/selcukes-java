package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.MirrorUrls;
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
import java.util.Optional;
import java.util.function.Function;

import static io.github.selcukes.dp.util.OptionalUtil.OrElse;
import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

public class IExplorerBinary implements BinaryFactory {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private Function<Environment, String> osArc = osEnvironment -> osEnvironment.getArchitecture() == 32 ? "Win32" : "x64";

    public IExplorerBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        this.release = OrElse(release,getLatestRelease());
        this.targetArch = targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    MirrorUrls.IEDRIVER_URL,
                    getBinaryVersion(),
                    osArc.apply(getBinaryEnvironment()),
                    getBinaryVersion())));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch.map(arch -> Environment.create(arch.getValue())).orElseGet(Environment::create);
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/iedriver_%s.zip", TempFileUtil.getTempDirectory(), getBinaryVersion()));
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return DownloaderType.ZIP;
    }

    @Override
    public String getBinaryFileName() {
        return "IEDriverServer.exe";
    }

    public String getBinaryDirectory() {

        return "iedriver_" + release.orElse("");
    }

    @Override
    public String getBinaryDriverName() {
        return "IEDriver";
    }

    @Override
    public String getBinaryVersion() {
        return unwrap(release);
    }


    private String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL);

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

            final String textContent = element.getTextContent();
            return textContent.substring(0, textContent.indexOf('/'));

        } catch (Exception e) {
            throw new DriverPoolException(e);
        }
    }
}