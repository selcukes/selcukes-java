/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
