package gitlet;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    /** The message when no argument. */
    private final static String NO_ARGUMENT_MESSAGE = "Please enter a command.";

    /** The message when forget inputting the commit message. */
    private final static String NO_COMMIT_MESSAGE_MESSAGE = "Please enter a commit message.";

    /** The message when the inputted command doesn't exist. */
    private final static String NO_COMMAND_MESSAGE = "No command with that name exists.";

    /** The message when the number of operands is wrong. */
    private final static String WRONG_NUMBER_OPERANDS_MESSAGE = "Incorrect operands.";

    /** The message when there is no initialized .gitlet directory. */
    private final static String NO_GIT_REPO_MESSAGE = "Not in an initialized Gitlet directory.";

    public static void main(String[] args) {

        validateNoArgs(args, NO_ARGUMENT_MESSAGE);

        String firstArg = args[0];
        validateCommandExists(firstArg, NO_COMMAND_MESSAGE);
        validateGitRepoIsInitialized(firstArg, NO_GIT_REPO_MESSAGE);

        String message = null;
        String commitMessage = null;

        switch (firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                validateNumArgs("add", args, 2, WRONG_NUMBER_OPERANDS_MESSAGE);
                String fileName = args[1];
                message = Repository.add(fileName);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                validateNumArgs("commit", args, 2, NO_COMMIT_MESSAGE_MESSAGE);
                commitMessage = args[1];
                message = Repository.commit(commitMessage);
                break;
            case "rm":  // java gitlet.Main rm <file name>
                validateNumArgs("rm", args, 2, WRONG_NUMBER_OPERANDS_MESSAGE);
                String removedFileName = args[1];
                message = Repository.rm(removedFileName);
                break;
            case "log":
                validateNumArgs("log", args, 1, WRONG_NUMBER_OPERANDS_MESSAGE);
                message = Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1, WRONG_NUMBER_OPERANDS_MESSAGE);
                message = Repository.globalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2, WRONG_NUMBER_OPERANDS_MESSAGE);
                commitMessage = args[1];
                message = Repository.find(commitMessage);
                break;
            case "status":
                validateNumArgs("status", args, 1, WRONG_NUMBER_OPERANDS_MESSAGE);
                message = Repository.status();
                break;
            case "checkout":
                validateCheckoutArgs(args, WRONG_NUMBER_OPERANDS_MESSAGE);
                message = Repository.checkout(args);
                break;
            default:
                System.out.println(NO_COMMAND_MESSAGE);
        }
        printMessage(message);
    }

    /** Prints the message when the message is not null or "" */
    private static void printMessage(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * print the message if they do not match.
     *
     * @param cmd  Name of command you are validating
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    private static void validateNumArgs(String cmd, String[] args, int n, String message) {
        if (args.length != n) {
            System.out.println(message);
            System.exit(0);
        }
    }

    /** Validate the number of arguments of checkout command. */
    private static void validateCheckoutArgs(String[] args, String message) {
        if ("--".equals(args[1])) {
            validateNumArgs("checkout", args, 3, message);
        } else if ("--".equals(args[2])) {
            validateNumArgs("checkout", args, 4, message);
        } else {
            validateNumArgs("checkout", args, 2, message);
        }
    }

    /**
     * Checks if there is no arguments.
     * print the message if no arguments.
     */
    private static void validateNoArgs(String[] args, String message) {
        if (args.length == 0) {
            System.out.println(message);
            System.exit(0);
        }
    }

    /** Validate if the git repository is initialized. */
    private static void validateGitRepoIsInitialized(String cmd, String message) {
        if (!cmd.equals("init") && !Repository.isInitialized()) {
            System.out.println(message);
            System.exit(0);
        }
    }

    /** Check if the command exists. */
    private static void validateCommandExists(String cmd, String message) {
        HashSet<String> set = new HashSet<>();
        set.addAll(Arrays.asList("init", "add", "commit", "rm", "log", "global-log", "find",
                "status", "checkout"));
        if (!set.contains(cmd)) {
            System.out.println(message);
            System.exit(0);
        }
    }
}
