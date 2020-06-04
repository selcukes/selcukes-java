/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core;

import io.github.selcukes.core.exception.CommandException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

import java.io.IOException;

public class Shell {
    final Logger logger = LoggerFactory.getLogger(Shell.class);
    private Process process;

    public Process run(String command) {
        logger.info(() -> "Trying to execute command : " + command);

        ProcessBuilder pb = new ProcessBuilder()
            .inheritIO()
            .command(command.split("\\s"))
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE);
        try {
            synchronized (this) {
                process = pb.start();
            }

        } catch (IOException e) {
            logger.error(() -> "There was a problem executing command : " + command);
            throw new CommandException(e.getMessage());
        }
        return process;
    }

    public String getPID() {
        String firstSubString = process.toString().split(",")[0];
        String pid = firstSubString.split("=")[1];
        logger.debug(() -> "Process Id: " + pid);
        return pid;
    }

}
