package io.github.selcukes.core.tests;

import io.github.selcukes.core.page.WebPage;
import lombok.CustomLog;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.UUID;

@CustomLog
public class WebAuthTest extends BaseTest {

    @Test
    public void testVirtualAuth() {
        WebPage page = new WebPage(driver);
        page.open("https://webauthn.io/");
        page.addVirtualAuthenticator();

        String randomId = UUID.randomUUID().toString();
        logger.info(() -> "Username:" + randomId);
        driver.findElement(By.id("input-email")).sendKeys(randomId);
        driver.findElement(By.id("register-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("popover-body"), "Success! Now try logging in"));

        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("main-content"), "You're logged in!"));
        page.removeVirtualAuthenticator();

    }

    @Test
    public void testBasicAuth() {
        WebPage page = new WebPage(driver);
        page.basicAuth("admin", "admin");
        page.open("https://the-internet.herokuapp.com/basic_auth");
    }
}
