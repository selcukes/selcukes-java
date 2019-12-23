package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.util.TempFileUtil;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public interface BinaryFactory {
    Optional<URL> getDownloadURL();

    Environment getBinaryEnvironment();

    default File getCompressedBinaryFile() {
        String file = TempFileUtil.getTempDirectory() +
            "/" +
            getBinaryDriverName().toLowerCase() +
            "_" +
            getBinaryVersion() +
            getCompressedBinaryType().name();
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

    default String getProxy() {
      //  "https://127.0.0.1:8080"
        return null;
    }


}