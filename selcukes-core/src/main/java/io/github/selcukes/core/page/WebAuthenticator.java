package io.github.selcukes.core.page;

import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;

class WebAuthenticator {
    private final WebDriver driver;
    private VirtualAuthenticator virtualAuthenticator;

    public WebAuthenticator(WebDriver driver) {
        this.driver = driver;
    }

    public VirtualAuthenticator addVirtualAuthenticator() {
        if (virtualAuthenticator == null) getVirtualAuthenticator();
        return virtualAuthenticator;
    }

    private void getVirtualAuthenticator() {
        this.virtualAuthenticator = ((HasVirtualAuthenticator) driver)
            .addVirtualAuthenticator(new VirtualAuthenticatorOptions());
    }

    public void removeVirtualAuthenticator() {
        ((HasVirtualAuthenticator) driver).removeVirtualAuthenticator(virtualAuthenticator);
    }

    public void basicAuth(String username, String password) {
        ((HasAuthentication) driver).register(UsernameAndPassword.of(username, password));
    }
}
