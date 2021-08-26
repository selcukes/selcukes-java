/*
 *
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
 *
 */

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.exec.ExecResults;
import io.github.selcukes.commons.exec.Shell;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.os.Platform;
import org.testng.annotations.Test;

public class RunCommandTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test(enabled = false)
    public void runCommandTest() {
        if (Platform.isLinux()) {
            Shell shell = new Shell();
            ExecResults execResults = shell.runCommand("google-chrome --version");

            String[] words = execResults.getOutput().get(2).split(" ");
            String browserVersion = words[words.length - 1];

            logger.info(() -> "Browser Version Number: " + browserVersion);
        }
    }
}
