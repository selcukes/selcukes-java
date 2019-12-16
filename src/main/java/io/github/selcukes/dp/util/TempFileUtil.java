package io.github.selcukes.dp.util;

import java.io.File;
import java.util.UUID;

public final class TempFileUtil {

    private TempFileUtil() {

    }

    public static File createTempFile() {
        return new File(getTempDirectory() + UUID.randomUUID().toString());
    }

    public static File createTempFileAndDeleteOnExit() {
        final File tmpFile = createTempFile();
        tmpFile.deleteOnExit();
        return tmpFile;
    }

    public static String getTempDirectory() {
        final String fileSeparator = File.separator;
        final String tmpDir = System.getProperty("java.io.tmpdir");

        if (!tmpDir.endsWith(fileSeparator)) {
            return tmpDir + fileSeparator;
        }
        return tmpDir;
    }
}
