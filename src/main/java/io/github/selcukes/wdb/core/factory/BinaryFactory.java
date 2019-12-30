package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.util.Platform;
import io.github.selcukes.wdb.util.TempFileUtil;

import java.io.File;
import java.net.URL;

public interface BinaryFactory {
    URL getDownloadURL();

    Platform getBinaryEnvironment();

    default File getCompressedBinaryFile() {
        String file = TempFileUtil.getTempDirectory() +
            "/" +
            getBinaryDriverName().toLowerCase() +
            "_" +
            getBinaryVersion() +
            "." + getCompressedBinaryType().getName();
        return new File(file);
    }

    default DownloaderType getCompressedBinaryType() {
        return DownloaderType.ZIP;
    }

    default String getBinaryFileName() {
        return getBinaryEnvironment().getOSType().equals(OSType.WIN) ? getBinaryDriverName() + ".exe" : getBinaryDriverName();
    }

    default String getBinaryDirectory() {
        return getBinaryDriverName().toLowerCase() + "_" + getBinaryVersion();
    }

    String getBinaryDriverName();

    String getBinaryVersion();

    DriverType getDriverType();

    void setVersion(String version);

    void setTargetArch(TargetArch targetArch);

    void setProxy(String proxy);

}