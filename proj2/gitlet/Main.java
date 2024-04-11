package gitlet;

import gitlet.git.Repository;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    /** The message when forget inputting the commit message. */
    private final static String NO_COMMIT_MESSAGE_MESSAGE = "Please enter a commit message";

    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                String fileName = args[1];
                Repository.add(fileName);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (args.length < 2) {
                    printMessage(NO_COMMIT_MESSAGE_MESSAGE);
                } else {
                    String commitMessage = args[1];
                    String message = Repository.commit(commitMessage);
                    System.out.println(message);
                }
                break;
        }
        System.exit(0);
    }

    /** Prints the message when the message is not null or "" */
    private static void printMessage(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
    }
}
