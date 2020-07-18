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

package io.github.selcukes.core.alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

public class AlertBoxImpl implements AlertBox {
    private final org.openqa.selenium.Alert alert;

    public AlertBoxImpl(WebDriver driver) {
        alert = driver.switchTo().alert();
    }

    public AlertBoxImpl(org.openqa.selenium.Alert alert) {
        this.alert = alert;
    }

    public Alert getAlert() {
        return alert;
    }

    @Override
    public void prompt(String text) {
        sendKeys(text);
        accept();
    }

    @Override
    public boolean present() {
        return true;
    }

    public String getText() {
        return getAlert().getText();
    }

    public void accept() {
        getAlert().accept();
    }

    public void sendKeys(String keysToSend) {
        getAlert().sendKeys(keysToSend);
    }

    public void dismiss() {
        getAlert().dismiss();
    }
}
