package io.github.selcukes.core.driver;

import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.core.enums.DeviceType;
import org.openqa.selenium.remote.RemoteWebDriver;


public class DriverManager<D extends RemoteWebDriver> {
    private final Logger logger = LoggerFactory.getLogger(DriverManager.class);

    public D createDriver(DeviceType deviceType) {

        if (DriverFactory.getDriver() == null) {
            logger.info(() -> "Creating new session..." + deviceType);
            switch (deviceType) {
                case BROWSER:
                    DriverFactory.setDriver(new WebManager().createDriver());
                    break;
                case WINDOWS:
                case MOBILE:
                    DriverFactory.setDriver(new WindowsManager().createDriver());
                    break;
                default:
                    throw new DriverSetupException("Unable to create new driver session for Driver Type[" + deviceType + "]");
            }
        }
        return DriverFactory.getDriver();
    }

}