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

package io.github.selcukes.reports.screenshot;

import org.openqa.selenium.WebDriver;

public class ScreenGrabber {
    private boolean fullPage = false;
    private Snapshot snapshot;

    public static synchronized Builder shoot(WebDriver driver) {
        return new ScreenGrabber().new Builder(driver);
    }

    public class Builder {
        public Builder(WebDriver driver) {
            snapshot = new SnapshotImpl(driver);
        }

        public Builder withText(String text) {
            snapshot.withText(text);
            return this;
        }

        public Builder withAddressBar() {
            snapshot.withAddressBar();
            return this;
        }

        public Builder fullPage() {
            fullPage = true;
            return this;
        }

        public String save() {
            return (fullPage) ? snapshot.shootFullPage() : snapshot.shootPage();
        }

        public byte[] saveAsBytes() {
            return (fullPage) ? snapshot.shootFullPageAsBytes() : snapshot.shootPageAsBytes();
        }
    }
}
