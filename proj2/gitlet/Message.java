package gitlet;

public class Message {
    private Message() {
    }

    /** The message when the inputted command doesn't exist. */
    final static String COMMAND_DOES_NOT_EXIST_MESSAGE = "No command with that name exists.";

    /** When merge and there are staged additions or removals present. */
    static final String MERGE_HAVE_UNCOMMITTED_FILES_MESSAGE = "You have uncommitted changes.";

    /** When the given branch name do not exist. */
    static final String BRANCH_NAME_DO_NOT_EXIST_MESSAGE = "A branch with that name does not " +
            "exist.";

    /** When attempting to merge a branch with itself. */
    static final String MERGE_CURRENT_BRANCH_MESSAGE = "Cannot merge a branch with itself.";

    /** When commit but no files have been staged. */
    static final String COMMIT_BUT_NO_STAGED_FILE_MESSAGE = "No changes added to the commit.";

    /** When merge and the head commit of given branch is the ancestor of current commit. */
    static final String MERGE_ANCESTOR_MESSAGE = "Given branch is an ancestor of the current " +
            "branch.";

    /**
     * When an untracked file in the current commit would be overwritten or deleted by the merge
     * or checkout.
     */
    static final String OVERWRITE_OR_DELETE_UNTRACKED_FILE_MESSAGE = "There is an untracked file " +
            "in the way; delete it, or add and commit it first.";

    /** When the merge encountered a conflict. */
    static final String MERGE_CONFLICT_MESSAGE = "Encountered a merge conflict.";

    /** The message when checkout commit does not exist. */
    final static String COMMIT_DOES_NOT_EXIST_MESSAGE = "No commit with that id exists.";

    /** The message when fast-forwarded merged. */
    final static String FAST_FORWARD_MESSAGE = "Current branch fast-forwarded.";

    /** The message when `gitlet init` and the gitlet repo already exists. */
    final static String GIT_REPO_ALREADY_EXISTS_MESSAGE = "A Gitlet version-control system " +
            "already exists in the current directory.";

    /** The message when gitlet directory is not initialized. */
    final static String NOT_IN_GIT_REPO_MESSAGE = "Not in an initialized Gitlet directory.";

    /** The message when the number of operands is wrong. */
    final static String INCORRECT_OPERANDS_MESSAGE = "Incorrect operands.";

    /** The message when no command. */
    final static String PLEASE_ENTER_COMMAND_MESSAGE = "Please enter a command.";

    /** The message when the file is absent. */
    static final String FILE_NOT_FOUND_MESSAGE = "File does not exist.";

    /** Returns the successful merge message. */
    static String getMergeMessage(String givenBranchName, String currentBranchName) {
        return "Merged " + givenBranchName + " into " + currentBranchName + ".";
    }
}
