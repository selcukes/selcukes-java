package io.github.selcukes.dp;

import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.util.DriverPoolUtil;
import io.github.selcukes.dp.util.TempFileUtil;

public class DriverPool {
    private DriverType driverType;
    private String release;
    private TargetArch targetArch;
    private String downloadLocation = TempFileUtil.getTempDirectory();
    private String proxyUrl;

    public static Builder chromeDriver() {
        return new DriverPool().new Builder(DriverType.CHROME);
    }

    public static Builder firefoxDriver() {
        return new DriverPool().new Builder(DriverType.FIREFOX);
    }

    public static Builder ieDriver() {
        return new DriverPool().new Builder(DriverType.IEXPLORER);
    }

    public static Builder edgeDriver() {
        return new DriverPool().new Builder(DriverType.EDGE);
    }


    public class Builder {
        public Builder(DriverType driverType) {
            DriverPool.this.driverType = driverType;
        }


        public Builder version(String version) {
            DriverPool.this.release = version;
            return this;
        }

        public Builder arch64() {
            DriverPool.this.targetArch = TargetArch.X64;
            return this;
        }

        public Builder arch32() {
            DriverPool.this.targetArch = TargetArch.X32;
            return this;
        }


        public Builder targetPath(String targetPath) {
            DriverPool.this.downloadLocation = targetPath;
            return this;
        }

        public Builder proxy(String proxy) {
            DriverPool.this.proxyUrl = proxy;
            return this;
        }

        public String setup() {
            return new DriverPoolUtil(DriverPool.this.driverType,
                DriverPool.this.release,
                DriverPool.this.targetArch,
                DriverPool.this.downloadLocation,
                DriverPool.this.proxyUrl).downloadAndSetupBinaryPath();
        }
    }

}
