package gitlet;

import java.io.File;
import java.util.Map;
import java.util.Set;

/** GitRepo class represents the .gitlet directory. */
public class GitRepo {

    /** The .getlet directory */
    public final static File GIT_REPO = Utils.join(Repository.CWD, ".gitlet");
    public final static String DEFAULT_INITIAL_BRANCH = "master";

    /** The message when there is no files have been staged. */
    private final static String NO_STAGED_FILE_MESSAGE = "No changes added to the commit.";

    /** The message when no reason to remove the file. */
    private final static String NO_REASON_TO_REMOVE_MESSAGE = "No reason to remove the file.";

    /** The delimiter between two log. */
    private final static String LOGS_DELIMITER = "===";


    /**
     * 1. creates the .git/object/ directory (Commit to do).
     * 2. creates the initial commit (Commit to do).
     * 2. creates the index file. (Staged Files to do).
     * 3. creates the HEAD files. (HEAD to do)
     * 4. creates the refs. (Refs to do).
     */
    public static void init() {
        GIT_REPO.mkdir();
        String initialCommitId = Commit.init();
        Blob.init();
        StagingArea.init();
        Reference.init(DEFAULT_INITIAL_BRANCH, initialCommitId);
        HEADPointer.init(DEFAULT_INITIAL_BRANCH);
    }

    /** Returns true if the GIT_REPO exists, false otherwise. */
    public static boolean isInitialized() {
        return GIT_REPO.exists();
    }

    /**
     * 1. Check if the current working version of the file is identical to the version in the
     * current commit.
     * 2. Store the file as a blob object
     * 3. add the Map in the INDEX file.
     */
    public static String add(MediatorFile file) {

        Commit commit = HEADPointer.currentCommit();
        if (commit.containsAndIsIdentical(file)) {
            return null;  // no message to print
        }

        Blob blob = new Blob(file.getContent());
        blob.saveBlob();

        StagingArea.addToStagedArea(file.getFileName(), blob.getBlobId());

        return null;  // no message to print
    }


    /**
     * 1. get all files in staging area
     * 2. copy the last commit and change the map
     * 3. save the commit
     * 4. clean the staging area
     * 5. Move the branch
     */
    public static String commit(String message) {
        if (!StagingArea.changed()) {
            return NO_STAGED_FILE_MESSAGE;
        }

        Commit currentCommit = HEADPointer.currentCommit();
        /* Clone the file-blob map from the  */
        Commit newCommit = Commit.clone(message, currentCommit);

        /* Add or modify the mapping from file name to blob file. */
        StagingArea.addOrModifyFilesToCommit(newCommit);
        /* Remove the files. */
        StagingArea.removeFilesFromCommit(newCommit);

        /* Save the new commit. */
        String newCommitId = newCommit.saveCommit();

        /* Update the state of git repository. */
        StagingArea.clear();
        Reference.move(HEADPointer.currentBranch(), newCommitId);

        return null; // no message to print.
    }

    /**
     * 1. if the fileName is in staged area, remove it from staged area
     * 2. else if the fileName is in the current commit, add it to removed Area and remove it from
     * working copy.
     * 3. else, print "No reason to remove the file."
     *
     * Returns "" when add the file to removed area.
     */
    public static String rm(String fileName) {

        /* Check if the file is in the staged area to be added or modified. */
        if (StagingArea.isAddedOrModified(fileName)) {
            StagingArea.removeFromStagedArea(fileName);
            return null;  // no message to be printed.
        }

        /* Check if the file is in the latest commit. */
        Commit commit = HEADPointer.currentCommit();
        if (commit.contains(fileName)) {
            StagingArea.addToRemovedArea(fileName);
            return "remove";  // no message to be printed.
        }

        return NO_REASON_TO_REMOVE_MESSAGE;
    }

    /** Returns the logs starting at the commit head points to. */
    public static String log() {
        StringBuilder logs = new StringBuilder();
        Commit currentCommit = HEADPointer.currentCommit();
        while (currentCommit != null) {
            logs.append(LOGS_DELIMITER);
            logs.append("\n");
            logs.append(currentCommit.logInfo());
            logs.append("\n");
            currentCommit = currentCommit.getParentCommit();
        }

        return logs.toString();
    }
}
