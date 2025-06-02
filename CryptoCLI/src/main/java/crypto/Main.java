package crypto;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "cryptocli",
        subcommands = {
                EncryptFileCommand.class,
                DecryptFileCommand.class,
                EncryptDirCommand.class,
                DecryptDirCommand.class
        },
        description = "Symmetric file encryption CLI.",
        mixinStandardHelpOptions = true)
public class Main implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    public void run() {
        System.out.println("Use a subcommand like encrypt-file or decrypt-file.");
    }
}
