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

package io.github.selcukes.reports.screen;

import io.github.selcukes.reports.ReportDriver;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;

@UtilityClass
public class ScreenPlayBuilder {

    public ScreenPlay getScreenPlay(WebDriver driver) {
        return new ScreenPlayImpl(driver);
    }

    public ScreenPlay getScreenPlay() {
        return new ScreenPlayImpl(ReportDriver.getReportDriver());
    }

}
