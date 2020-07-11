package io.github.selcukes.core.driver;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

public final class DriverFactory<D extends RemoteWebDriver> {

    private static ThreadLocal<Object> DRIVER_THREAD = new ThreadLocal<>();

    private static List<Object> STORED_DRIVER = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> STORED_DRIVER.forEach(d -> ((RemoteWebDriver) d).quit())));
    }

    private DriverFactory() {
    }

    public static <D> D getDriver() {
        return (D) DRIVER_THREAD.get();
    }

    public static <D> void setDriver(D driveThread) {
        STORED_DRIVER.add(driveThread);
        DRIVER_THREAD.set(driveThread);
    }

    public static void removeDriver() {
        STORED_DRIVER.remove(DRIVER_THREAD.get());
        DRIVER_THREAD.remove();
    }
}
