package io.github.selcukes.core;

import io.github.selcukes.core.exception.CommandException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommandExecutor {
    final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    public static CommandExecutor getCommandExecutor() {
        return new CommandExecutor();
    }

    public Process run(String command) {
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

    public String runAndWait(File folder, String... command) {
        String output = "";
        try {
            Process process = new ProcessBuilder(command).directory(folder)
                .redirectErrorStream(false).start();
            process.waitFor();
            output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error(() -> "There was a problem executing command " + command);
            throw new CommandException(e.getMessage());
        }
        return output.trim();
    }
}
