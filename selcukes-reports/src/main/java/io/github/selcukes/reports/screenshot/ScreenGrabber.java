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
