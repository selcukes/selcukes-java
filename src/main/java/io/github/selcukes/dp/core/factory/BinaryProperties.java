package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.TargetArch;

import java.io.File;
import java.net.URL;

/**
 * The interface Binary properties.
 */
public interface BinaryProperties {
    /**
     * Gets download url.
     *
     * @return the download url
     */
    URL getDownloadURL();

    /**
     * Gets binary environment.
     *
     * @return the binary environment
     */
    Environment getBinaryEnvironment();

    /**
     * Gets compressed binary file.
     *
     * @return the compressed binary file
     */
    File getCompressedBinaryFile();

    /**
     * Gets compressed binary type.
     *
     * @return the compressed binary type
     */
    DownloaderType getCompressedBinaryType();

    /**
     * Gets binary filename.
     *
     * @return the binary filename
     */
    String getBinaryFilename();

    /**
     * Gets binary directory.
     *
     * @return the binary directory
     */
    String getBinaryDirectory();

    /**
     * Gets binary driver name.
     *
     * @return the binary driver name
     */
    String getBinaryDriverName();

    /**
     * Gets binary version.
     *
     * @return the binary version
     */
    String getBinaryVersion();

    /**
     * Sets binary architecture.
     *
     * @param targetArch the target arch
     */
    void setBinaryArchitecture(TargetArch targetArch);
}