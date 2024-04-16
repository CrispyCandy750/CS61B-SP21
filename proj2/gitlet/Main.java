package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Crispy Candy
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     * java gitlet.Main init
     */

    public static void main(String[] args) {
        String cmd = "";

        if (args.length > 0) {
            cmd = args[0];
        }

        String message;

        switch (cmd) {
            case "init": message = init();
                break;
            case "add": message = add(args);
                break;
            case "commit": message = commit(args);
                break;
            case "rm": message = rm(args);
                break;
            case "log": message = log(args);
                break;
            case "global-log": message = globalLog(args);
                break;
            case "find": message = find(args);
                break;
            case "status": message = status(args);
                break;
            case "checkout": message = checkout(args);
                break;
            case "branch": message = branch(args);
                break;
            case "rm-branch": message = rmBranch(args);
                break;
            case "reset": message = reset(args);
                break;
            case "merge": message = merge(args);
                break;
            case "": message = Message.PLEASE_ENTER_COMMAND_MESSAGE;
                break;
            default: message = Message.COMMAND_DOES_NOT_EXIST_MESSAGE;
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
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    private static String validateOperandsNum(String[] args, int n) {
        return validateOperandsNum(args, n, null);
    }

    private static String validateOperandsNum(String[] args, int n, String message) {
        String validationMessage = null;
        if (args.length != n) {
            if (message == null) {
                validationMessage = Message.INCORRECT_OPERANDS_MESSAGE;
            } else {
                validationMessage = message;
            }
        }
        return validationMessage;
    }

    /** Validate the number of arguments of checkout command. */
    private static String validateCheckoutArgs(String[] args) {
        if (args.length == 3 && "--".equals(args[1])
                || args.length == 4 && "--".equals(args[2])
                || args.length == 2) {
            return null;
        }
        return Message.INCORRECT_OPERANDS_MESSAGE;
    }

    /** Validate the number of arguments of commit command. */
    private static String validateCommitArgs(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        } else if (args[1].length() == 0) {
            return Message.PLEASE_ENTER_COMMIT_MESSAGE;
        }
        return null;
    }


    /** Returns true if the validation is wrong. */
    private static boolean validateWrong(String validateMessage) {
        return validateMessage != null;
    }

    /** Execute the gitlet command. */
    private static String init() {
        return Repository.init();
    }

    private static String add(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }
        String fileName = args[1];
        return Repository.add(fileName);
    }

    private static String commit(String[] args) {
        String message = validateCommitArgs(args);
        if (validateWrong(message)) {
            return message;
        }
        String commitMessage = args[1];
        return Repository.commit(commitMessage);
    }

    private static String rm(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }
        String removedFileName = args[1];
        return Repository.rm(removedFileName);
    }

    private static String log(String[] args) {
        String message = validateOperandsNum(args, 1);
        if (validateWrong(message)) {
            return message;
        }
        return Repository.log();
    }

    private static String globalLog(String[] args) {
        String message = validateOperandsNum(args, 1);
        if (validateWrong(message)) {
            return message;
        }
        return Repository.globalLog();
    }

    private static String find(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }
        String commitMessage = args[1];
        return Repository.find(commitMessage);
    }

    private static String status(String[] args) {
        String message = validateOperandsNum(args, 1);
        if (validateWrong(message)) {
            return message;
        }
        return Repository.status();
    }

    private static String checkout(String[] args) {
        String message = validateCheckoutArgs(args);

        if (validateWrong(message)) {
            return message;
        }

        if (args.length == 3) {
            String fileName = args[2];
            return Repository.checkoutFileFromCurrentCommit(fileName);
        } else if (args.length == 4) {
            String commitId = args[1];
            String fileName = args[3];
            return Repository.checkoutFileFromSpecificCommit(commitId, fileName);
        } else {
            String branchName = args[1];
            return Repository.checkoutBranch(branchName);
        }
    }

    private static String branch(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }
        String branchName = args[1];
        return Repository.createBranch(branchName);
    }

    private static String rmBranch(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }
        String branchName = args[1];
        return Repository.removeBranch(branchName);
    }

    private static String reset(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }
        String commitId = args[1];
        return Repository.reset(commitId);
    }

    private static String merge(String[] args) {
        String message = validateOperandsNum(args, 2);
        if (validateWrong(message)) {
            return message;
        }

        String branchName = args[1];
        return Repository.merge(branchName);
    }
}
