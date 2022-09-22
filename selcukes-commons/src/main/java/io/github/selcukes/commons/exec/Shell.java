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

package io.github.selcukes.commons.exec;

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exception.CommandException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.os.Platform;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Shell {
    private static final Logger logger = LoggerFactory.getLogger(Shell.class);

    private String pid;

    /**
     * It executes a command and returns the results of the execution
     *
     * @param  command The command to be executed.
     * @return         ExecResults
     */
    public ExecResults runCommand(final String command) {
        ExecResults results;
        Process process = null;
        try {
            logger.info(() -> String.format("Executing the command [%s]", command));
            process = new ProcessBuilder(command.split("\\s"))
                    .start();
            results = interactWithProcess(process);
        } catch (IOException e) {
            logger.error(e, () -> "There was a problem executing command : " + command);
            throw new CommandException(e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        if (results.hasErrors()) {
            logger.warn(() -> String.format("Results of the command execution : %s", results.getError()));
        }
        return results;
    }

    private void extractPidOf(final Process process) {
        pid = process.toString().split(",")[0].split("=")[1];
        logger.debug(() -> "Process Id: " + pid);
    }

    /**
     * It runs a command that sends a Ctrl+C signal to the process with the
     * given pid
     *
     * @param folder The folder where the SendSignalCtrlC.exe is located.
     */
    public void sendCtrlC(final String folder) {
        String killCommand = folder + "SendSignalCtrlC.exe " + pid;
        runCommand(killCommand);
    }

    private ExecResults interactWithProcess(final Process process) {
        if (Platform.isWindows()) {
            extractPidOf(process);
        }
        StreamGuzzler output = new StreamGuzzler(process.getInputStream());
        StreamGuzzler error = new StreamGuzzler(process.getErrorStream());
        ExecutorService executors = Executors.newFixedThreadPool(2);
        executors.submit(error);
        executors.submit(output);
        executors.shutdown();
        while (!executors.isTerminated()) {
            // Wait for all the tasks to complete.
            Await.until(1);
        }
        return new ExecResults(output.getContent(), error.getContent(), process.exitValue());
    }

    /**
     * "Run the command asynchronously and return a CompletableFuture that will
     * contain the results of the command."
     * <p>
     * The first thing to notice is that the return type is
     * {@literal CompletableFuture<ExecResults>}. This means that the function
     * will return a CompletableFuture that will contain the results of the
     * command
     *
     * @param  command The command to run.
     * @return         A CompletableFuture that will return an ExecResults
     *                 object.
     */
    public CompletableFuture<ExecResults> runCommandAsync(final String command) {

        return CompletableFuture.supplyAsync(() -> runCommand(command));
    }
}
