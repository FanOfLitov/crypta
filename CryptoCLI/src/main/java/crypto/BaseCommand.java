package crypto;

import picocli.CommandLine;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseCommand implements Runnable {
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    boolean verbose;

    protected Logger logger = Logger.getLogger(getClass().getName());

    protected void setupLogger() {
        if (verbose) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        }
    }
}