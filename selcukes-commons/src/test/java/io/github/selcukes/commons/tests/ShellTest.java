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

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.exec.Shell;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ShellTest {
    // For windows prefix "cmd.exe /c "
    private final String TEST_COMMAND = "echo Hello World!";

    @Test
    public void testRunCommand() {
        var shell = new Shell();
        var result = shell.runCommand(TEST_COMMAND);
        assertEquals(result.getOutput().get(0), "Hello World!");
    }

    @Test
    public void testRunCommandAsync() throws Exception {
        Shell shell = new Shell();
        var future = shell.runCommandAsync(TEST_COMMAND);
        var result = future.get();
        assertEquals(result.getOutput().get(0), "Hello World!");
    }

    @Test
    public void testStartAndKillProcess() {
        Process process = Shell.startProcess("ping localhost -t", "sample.txt");
        Shell.killProcess("ping.exe");
        Assert.assertFalse(process.isAlive());
    }

}
