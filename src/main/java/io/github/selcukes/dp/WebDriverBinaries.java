package io.github.selcukes.dp;

import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.util.WebDriverBinariesUtil;
import io.github.selcukes.dp.util.TempFileUtil;

public class WebDriverBinaries {
    private DriverType driverType;
    private String release;
    private TargetArch targetArch;
    private String downloadLocation = TempFileUtil.getTempDirectory();
    private String proxyUrl;

    public static Builder chromeDriver() {
        return new WebDriverBinaries().new Builder(DriverType.CHROME);
    }

    public static Builder firefoxDriver() {
        return new WebDriverBinaries().new Builder(DriverType.FIREFOX);
    }

    public static Builder ieDriver() {
        return new WebDriverBinaries().new Builder(DriverType.IEXPLORER);
    }

    public static Builder edgeDriver() {
        return new WebDriverBinaries().new Builder(DriverType.EDGE);
    }


    public class Builder {
        public Builder(DriverType driverType) {
            WebDriverBinaries.this.driverType = driverType;
        }


        public Builder version(String version) {
            WebDriverBinaries.this.release = version;
            return this;
        }

        public Builder arch64() {
            WebDriverBinaries.this.targetArch = TargetArch.X64;
            return this;
        }

        public Builder arch32() {
            WebDriverBinaries.this.targetArch = TargetArch.X32;
            return this;
        }


        public Builder targetPath(String targetPath) {
            WebDriverBinaries.this.downloadLocation = targetPath;
            return this;
        }

        public Builder proxy(String proxy) {
            WebDriverBinaries.this.proxyUrl = proxy;
            return this;
        }

        public String setup() {
            return new WebDriverBinariesUtil(WebDriverBinaries.this.driverType,
                WebDriverBinaries.this.release,
                WebDriverBinaries.this.targetArch,
                WebDriverBinaries.this.downloadLocation,
                WebDriverBinaries.this.proxyUrl).downloadAndSetupBinaryPath();
        }
    }

}
