package io.github.selcukes.wdb;

import io.github.selcukes.wdb.core.factory.*;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.util.TempFileUtil;
import io.github.selcukes.wdb.util.WebDriverBinaryUtil;

public class WebDriverBinary {
    private static DriverType driverType;
    private String downloadLocation = TempFileUtil.getTempDirectory();
    private boolean strictDownload = false;
    private BinaryFactory binaryFactory;

    public static Builder chromeDriver() {
        driverType = DriverType.CHROME;
        return new WebDriverBinary().new Builder(new ChromeBinary());
    }

    public static Builder firefoxDriver() {
        driverType = DriverType.FIREFOX;
        return new WebDriverBinary().new Builder(new GeckoBinary());
    }

    public static Builder ieDriver() {
        driverType = DriverType.IEXPLORER;
        return new WebDriverBinary().new Builder(new IExplorerBinary());
    }

    public static Builder edgeDriver() {
        driverType = DriverType.EDGE;
        return new WebDriverBinary().new Builder(new EdgeBinary());
    }

    public static Builder operaDriver() {
        driverType = DriverType.OPERA;
        return new WebDriverBinary().new Builder(new OperaBinary());
    }

    public static Builder grid() {
        driverType = DriverType.GRID;
        return new WebDriverBinary().new Builder(new SeleniumServerBinary());
    }

    public class Builder {
        public Builder(BinaryFactory binaryFactory) {
            WebDriverBinary.this.binaryFactory = binaryFactory;
        }


        public Builder version(String version) {
            binaryFactory.setVersion(version);
            return this;
        }

        public Builder arch64() {
            binaryFactory.setTargetArch(TargetArch.X64);
            return this;
        }

        public Builder arch32() {
            binaryFactory.setTargetArch(TargetArch.X32);
            return this;
        }


        public Builder targetPath(String targetPath) {
            WebDriverBinary.this.downloadLocation = targetPath;
            return this;
        }

        public Builder proxy(String proxy) {
            binaryFactory.setProxy(proxy);
            return this;
        }

        public Builder strictDownload() {
            WebDriverBinary.this.strictDownload = true;
            return this;
        }

        public BinaryInfo setup() {
            return new WebDriverBinaryUtil(WebDriverBinary.this.binaryFactory,
                WebDriverBinary.driverType,
                WebDriverBinary.this.downloadLocation,
                WebDriverBinary.this.strictDownload).downloadAndSetupBinaryPath();
        }
    }

}
