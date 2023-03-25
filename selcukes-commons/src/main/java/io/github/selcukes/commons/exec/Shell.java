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
        logger.info(() -> String.format("Executing the command [%s]", command));
        try {
            var process = new ProcessBuilder(command.split("\\s")).start();
            var results = interactWithProcess(process);
            if (results.hasErrors()) {
                logger.warn(() -> String.format("Results of the command execution: %s", results.getError()));
            }
            return results;
        } catch (IOException e) {
            logger.error(e, () -> "There was a problem executing command: " + command);
            throw new CommandException(e);
        }
    }

    private void extractPidOf(final Process process) {
        pid = Long.toString(process.pid());
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
        var output = new StreamGuzzler(process.getInputStream());
        var error = new StreamGuzzler(process.getErrorStream());
        var executors = Executors.newFixedThreadPool(2);
        executors.submit(error);
        executors.submit(output);
        executors.shutdown();
        Await.await()
                .poll(250)
                .atMax(1000)
                .until(executors::isTerminated);

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

    /**
     * Kills all processes whose command ends with the given process name. The
     * method uses ProcessHandle.allProcesses() to get a Stream of all running
     * processes and filters the ones whose command ends with the provided
     * process name. Then, it forcibly destroys each filtered process
     *
     * @param processName The name of the process you want to kill.
     */
    public static void killProcess(String processName) {
        logger.debug(() -> String.format("Killing all [%s] processes.", processName));
        ProcessHandle.allProcesses()
                .filter(process -> process.info().command()
                        .map(command -> command.endsWith(processName))
                        .orElse(false))
                .forEach(ProcessHandle::destroyForcibly);
    }

    /**
     * Starts a new process with the given service path.
     *
     * @param  servicePath      the path to the service executable to start
     * @return                  the Process object representing the started
     *                          process
     * @throws CommandException if the process fails to start
     */
    public static Process startProcess(String servicePath) throws CommandException {
        var processBuilder = new ProcessBuilder(servicePath);
        processBuilder.inheritIO();
        try {
            Process process = processBuilder.start();
            logger.info(() -> servicePath + " started...");
            return process;
        } catch (IOException e) {
            String message = String.format("Unable to start process '%s': %s", servicePath, e.getMessage());
            throw new CommandException(message, e);
        }
    }
}
