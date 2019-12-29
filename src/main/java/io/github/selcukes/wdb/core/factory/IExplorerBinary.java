package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;

import java.net.MalformedURLException;
import java.net.URL;

public class IExplorerBinary extends AbstractBinary {


    public IExplorerBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(MirrorUrls.IEDRIVER_URL + "/" + latestVersion);

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryDriverName() {
        return "IEDriverServer";
    }

    @Override
    protected String getLatestRelease() {
        String arch = getBinaryEnvironment().getArchitecture() == 64 ? "x64" : "Win32";
        String matcher = "IEDriverServer" + "_" + arch;
        return getVersionNumberFromXML(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL, matcher);
    }

}