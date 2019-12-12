package io.github.selcukes.dp;

import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.util.DriverPoolUtil;
import io.github.selcukes.dp.util.TempFileUtil;

/**
 * The type Driver pool.
 */
public class DriverPool {
    private DriverType driverType;
    private String release;
    private TargetArch targetArch;
    private String downloadLocation = TempFileUtil.getTempDirectory();

    /**
     * Chrome driver builder.
     *
     * @return the builder
     */
    public static Builder chromeDriver() {
        return new DriverPool().new Builder(DriverType.CHROME);
    }

    /**
     * Firefox driver builder.
     *
     * @return the builder
     */
    public static Builder firefoxDriver() {
        return new DriverPool().new Builder(DriverType.FIREFOX);
    }

    /**
     * Ie driver builder.
     *
     * @return the builder
     */
    public static Builder ieDriver() {
        return new DriverPool().new Builder(DriverType.IEXPLORER);
    }


    /**
     * The type Builder.
     */
    public class Builder {
        /**
         * Instantiates a new Builder.
         *
         * @param driverType the driver type
         */
        public Builder(DriverType driverType) {
            DriverPool.this.driverType = driverType;
        }


        /**
         * Version builder.
         *
         * @param version the version
         * @return the builder
         */
        public Builder version(String version) {
            DriverPool.this.release = version;
            return this;
        }

        /**
         * Arch 64 builder.
         *
         * @return the builder
         */
        public Builder arch64() {
            DriverPool.this.targetArch = TargetArch.X64;
            return this;
        }

        /**
         * Arch 32 builder.
         *
         * @return the builder
         */
        public Builder arch32() {
            DriverPool.this.targetArch = TargetArch.X86;
            return this;
        }


        /**
         * Target path builder.
         *
         * @param targetPath the target path
         * @return the builder
         */
        public Builder targetPath(String targetPath) {
            DriverPool.this.downloadLocation = targetPath;
            return this;
        }

        /**
         * Sets .
         */
        public void setup() {
            new DriverPoolUtil(DriverPool.this.driverType,
                    DriverPool.this.release,
                    DriverPool.this.targetArch,
                    DriverPool.this.downloadLocation).downloadAndSetupBinaryPath();
        }
    }

}
