package io.github.selcukes.dp.util;

import java.io.File;
import java.util.UUID;

/**
 * The type Temp file util.
 */
public final class TempFileUtil {

    /**
     * Create temp file file.
     *
     * @return the file
     */
    public static File createTempFile() {
        return new File(getTempDirectory() + UUID.randomUUID().toString());
    }

    /**
     * Create temp file and delete on exit file.
     *
     * @return the file
     */
    public static File createTempFileAndDeleteOnExit() {
        final File tmpFile = createTempFile();
        tmpFile.deleteOnExit();
        return tmpFile;
    }

    /**
     * Gets temp directory.
     *
     * @return the temp directory
     */
    public static String getTempDirectory() {
        final String fileSeparator = File.separator;
        final String tmpDir = System.getProperty("java.io.tmpdir");

        if (!tmpDir.endsWith(fileSeparator)) {
            return tmpDir + fileSeparator;
        }
        return tmpDir;
    }
}
