package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.enums.DownloaderType;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public interface BinaryFactory {
    Optional<URL> getDownloadURL();

    Environment getBinaryEnvironment();

    File getCompressedBinaryFile();

    DownloaderType getCompressedBinaryType();

    String getBinaryFileName();

    String getBinaryDirectory();

    String getBinaryDriverName();

    String getBinaryVersion();
  
}