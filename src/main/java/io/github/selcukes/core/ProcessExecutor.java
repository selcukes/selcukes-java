package io.github.selcukes.core;

import io.github.selcukes.core.exception.CommandException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

import java.io.IOException;

public class ProcessExecutor {
    final Logger logger = LoggerFactory.getLogger(ProcessExecutor.class);

    public Process runCommand(String command) {
        logger.info(() -> "Trying to execute command : " + command);
        Process process;
        ProcessBuilder pb = new ProcessBuilder().inheritIO().command(command.split("\\s"));
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        pb.redirectError(ProcessBuilder.Redirect.PIPE);
        try {
            synchronized (this) {
                process = pb.start();
            }

        } catch (IOException e) {
            logger.error(() -> "Unable to execute command: " + command);
            throw new CommandException(e.getMessage());
        }
        return process;
    }
}
