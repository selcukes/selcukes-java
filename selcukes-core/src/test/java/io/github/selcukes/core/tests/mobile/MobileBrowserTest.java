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

package io.github.selcukes.core.tests.mobile;

import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.core.page.MobilePage;
import io.github.selcukes.core.page.Pages;
import org.testng.annotations.Test;

@Lifecycle
public class MobileBrowserTest {

    @Test(enabled = false)
    public void mobileBrowserTest() {
        MobilePage page = Pages.mobilePage();
        page.open("https://www.google.com/")
                .assertThat().title("Google");
    }
}
