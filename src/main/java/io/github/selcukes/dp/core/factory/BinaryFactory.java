package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OSType;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public interface BinaryFactory {
    Optional<URL> getDownloadURL();

    Environment getBinaryEnvironment();

    File getCompressedBinaryFile();

    default DownloaderType getCompressedBinaryType(){
        return  DownloaderType.ZIP;
    }

    default String getBinaryFileName() {
        return getBinaryEnvironment().getOSType().equals(OSType.WIN) ? getBinaryDriverName().toLowerCase() + ".exe" : getBinaryDriverName().toLowerCase();
    }

    default String getBinaryDirectory(){
        return getBinaryDriverName().toLowerCase()+"_" + getBinaryVersion();
    }

    String getBinaryDriverName();

    String getBinaryVersion();

}