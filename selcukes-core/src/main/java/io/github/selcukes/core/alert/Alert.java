package io.github.selcukes.core.alert;

public interface Alert extends org.openqa.selenium.Alert{
    void prompt(String text);
    boolean present();
}
