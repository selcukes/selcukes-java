package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.MirrorUrls;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static io.github.selcukes.dp.util.OptionalUtil.orElse;
import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

public class IExplorerBinary implements BinaryFactory {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;

    public IExplorerBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        this.release = orElse(release, getLatestRelease());
        this.targetArch = targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    MirrorUrls.IEDRIVER_URL,
                    getBinaryVersion(),
                    getBinaryEnvironment().getArchitecture() == 64 ? "x64" : "Win32",
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
    public String getBinaryDriverName() {
        return "IEDriverServer";
    }

    @Override
    public String getBinaryVersion() {
        return unwrap(release);
    }


    private String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL,getProxy());

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl",
                    true);
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