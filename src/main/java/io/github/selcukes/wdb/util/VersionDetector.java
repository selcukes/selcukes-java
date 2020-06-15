package io.github.selcukes.wdb.util;

import io.github.selcukes.core.Shell;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class VersionDetector {
    Logger logger = LoggerFactory.getLogger(getClass());
    String driverName;

    public VersionDetector(String driverName) {
        this.driverName = driverName;
    }

    public String getVersion() {
        Optional<String> regQuery = Optional.empty();
        Platform platform = new Platform();
        if (platform.isWindows()) {
            if (driverName.equalsIgnoreCase("chromedriver"))
                regQuery = Optional.of("reg query \"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version");
            else if (driverName.equalsIgnoreCase("IEDriverServer"))
                regQuery = Optional.of("reg query \"HKLM\\Software\\Microsoft\\Internet Explorer\" /v Version");
            else if (driverName.equalsIgnoreCase("geckodriver"))
                regQuery = Optional.of("reg query \"HKLM\\Software\\Mozilla\\Mozilla Firefox\" /v CurrentVersion");
            else
                logger.error(() -> "Unable to detect Browser Version using Registry Key for " + driverName);
        } else
            logger.error(() -> "Unable to detect Browser Version using Registry Key for " + driverName + "in Os" + platform.getOS());

        return regQuery.map(this::getBrowserVersionFromRegistry).orElse(null);
    }

    private String getBrowserVersionFromRegistry(String regQuery) {
        Shell shell = new Shell();
        Process process = shell.run(regQuery);
        List<String> lines = null;
        String versionNumber = null;
        try {
            lines = IOUtils.readLines(process.getInputStream(), UTF_8);
            String[] words = lines.get(2).split(" ");
            versionNumber = words[words.length - 1];
            System.out.println("Version Number: " + versionNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return versionNumber;
    }
}
