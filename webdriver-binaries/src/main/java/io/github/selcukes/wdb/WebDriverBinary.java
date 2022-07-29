/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.wdb;

import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.os.Architecture;
import io.github.selcukes.wdb.core.BinaryFactory;
import io.github.selcukes.wdb.core.ChromeBinary;
import io.github.selcukes.wdb.core.EdgeBinary;
import io.github.selcukes.wdb.core.FirefoxBinary;
import io.github.selcukes.wdb.core.IExplorerBinary;
import io.github.selcukes.wdb.core.OperaBinary;
import io.github.selcukes.wdb.util.WebDriverBinaryUtil;

public class WebDriverBinary {
    private String downloadLocation = FileHelper.getTempDir();
    private boolean strictDownload = false;
    private boolean clearBinaryCache = false;
    private boolean autoCheck = true;
    private BinaryFactory binaryFactory;

    public static synchronized Builder chromeDriver() {
        return new WebDriverBinary().new Builder(new ChromeBinary());
    }

    public static synchronized Builder firefoxDriver() {
        return new WebDriverBinary().new Builder(new FirefoxBinary());
    }

    public static synchronized Builder ieDriver() {
        return new WebDriverBinary().new Builder(new IExplorerBinary());
    }

    public static synchronized Builder edgeDriver() {
        return new WebDriverBinary().new Builder(new EdgeBinary());
    }

    public static synchronized Builder operaDriver() {
        return new WebDriverBinary().new Builder(new OperaBinary());
    }

    public class Builder {
        public Builder(final BinaryFactory binaryFactory) {
            WebDriverBinary.this.binaryFactory = binaryFactory;
        }

        public Builder version(final String version) {
            WebDriverBinary.this.autoCheck = false;
            binaryFactory.setVersion(version);
            return this;
        }

        public Builder arch64() {
            binaryFactory.setTargetArch(Architecture.X64);
            return this;
        }

        public Builder arch32() {
            binaryFactory.setTargetArch(Architecture.X32);
            return this;
        }

        public Builder targetPath(final String targetPath) {
            WebDriverBinary.this.downloadLocation = targetPath;
            return this;
        }

        public Builder proxy(final String proxy) {
            binaryFactory.setProxy(proxy);
            return this;
        }

        public Builder strictDownload() {
            WebDriverBinary.this.strictDownload = true;
            return this;
        }

        public Builder disableAutoCheck() {
            WebDriverBinary.this.autoCheck = false;
            return this;
        }

        public Builder clearBinaryCache() {
            WebDriverBinary.this.clearBinaryCache = true;
            return this;
        }

        public BinaryInfo setup() {
            return new WebDriverBinaryUtil(WebDriverBinary.this.binaryFactory,
                    WebDriverBinary.this.downloadLocation,
                    WebDriverBinary.this.strictDownload,
                    WebDriverBinary.this.clearBinaryCache,
                    WebDriverBinary.this.autoCheck)
                    .downloadAndSetupBinaryPath();
        }

    }
}
