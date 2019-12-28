package io.github.selcukes.wdb;

import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.util.WebDriverBinaryUtil;
import io.github.selcukes.wdb.util.TempFileUtil;

public class WebDriverBinary {
    private DriverType driverType;
    private String release;
    private TargetArch targetArch;
    private String downloadLocation = TempFileUtil.getTempDirectory();
    private String proxyUrl;

    public static Builder chromeDriver() {
        return new WebDriverBinary().new Builder(DriverType.CHROME);
    }

    public static Builder firefoxDriver() {
        return new WebDriverBinary().new Builder(DriverType.FIREFOX);
    }

    public static Builder ieDriver() {
        return new WebDriverBinary().new Builder(DriverType.IEXPLORER);
    }

    public static Builder edgeDriver() {
        return new WebDriverBinary().new Builder(DriverType.EDGE);
    }
    public static Builder grid() {
        return new WebDriverBinary().new Builder(DriverType.GRID);
    }

    public class Builder {
        public Builder(DriverType driverType) {
            WebDriverBinary.this.driverType = driverType;
        }


        public Builder version(String version) {
            WebDriverBinary.this.release = version;
            return this;
        }

        public Builder arch64() {
            WebDriverBinary.this.targetArch = TargetArch.X64;
            return this;
        }

        public Builder arch32() {
            WebDriverBinary.this.targetArch = TargetArch.X32;
            return this;
        }


        public Builder targetPath(String targetPath) {
            WebDriverBinary.this.downloadLocation = targetPath;
            return this;
        }

        public Builder proxy(String proxy) {
            WebDriverBinary.this.proxyUrl = proxy;
            return this;
        }

        public BinaryInfo setup() {
            return new WebDriverBinaryUtil(WebDriverBinary.this.driverType,
                WebDriverBinary.this.release,
                WebDriverBinary.this.targetArch,
                WebDriverBinary.this.downloadLocation,
                WebDriverBinary.this.proxyUrl).downloadAndSetupBinaryPath();
        }
    }

}
