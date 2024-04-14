package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     * <p>
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    private Repository() {
    }

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The message when added file not found. */
    private static final String FILE_NOT_FOUND_MESSAGE = "File does not exist.";

    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that contains no files and
     * has the commit message initial commit (just like that, with no punctuation).
     * It will have a single branch: master, which initially points to this initial commit, and
     * master will be the current branch.
     * The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in
     * whatever format you choose for dates (this is called "The (Unix) Epoch", represented
     * internally
     * by the time 0.)
     * Since the initial commit in all repositories created by Gitlet will have exactly the same
     * content,
     * it follows that all repositories will automatically share this commit (they will all have
     * the same UID)
     * and all commits in all repositories will trace back to it.
     */
    public static void init() {
        GitRepo.init();
    }


    /**
     * Adds a copy of the file as it currently exists to the staging area (see the description of
     * the commit command).
     * Staging an already-staged file overwrites the previous entry in the staging area with the
     * new contents.
     * The staging area should be somewhere in .gitlet.
     * If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it's original
     * version).
     */
    public static String add(String fileName) {
        File addedFile = Utils.join(CWD, fileName);
        if (!addedFile.exists()) {
            return FILE_NOT_FOUND_MESSAGE;
        }
        return GitRepo.add(new MediatorFile(addedFile, fileName));
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area so they can be
     * restored at a later time, creating a new commit.
     * The commit is said to be tracking the saved files.
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s
     * snapshot of files; it will keep versions of files exactly as they are, and not update them.
     * <p>
     * A commit will only update the contents of files it is tracking that have been staged for
     * addition at the time of commit, in which case the commit will now include the version of
     * the file that was staged instead of the version it got from its parent.
     * <p>
     * A commit will save and start tracking any files that were staged for addition but weren’t
     * tracked by its parent.
     * <p>
     * Finally, files tracked in the current commit may be untracked in the new commit as a result
     * being staged for removal by the rm command (below).
     */
    public static String commit(String message) {
        return GitRepo.commit(message);
    }

    /** Returns true if the GIT_REPO exists, false otherwise. */
    public static boolean isInitialized() {
        return GitRepo.isInitialized();
    }

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for
     * removal and remove the file from the working directory if the
     * user has not already done so.
     * (do not remove it unless it is tracked in the current commit)
     */
    public static String rm(String fileName) {
        File file = Utils.join(CWD, fileName);

        /* If the file does not exist. */
        if (!file.exists()) {
            return FILE_NOT_FOUND_MESSAGE;
        }

        String message = GitRepo.rm(fileName);

        /* Add the file to the removed area which means remove from working copy. */
        if ("remove".equals(message)) {
            file.delete();
            return null;
        }

        return message;
    }

    /** Returns the log starting at current commit. */
    public static String log() {
        return GitRepo.log();
    }

    /** Returns all commit logs with arbitrary order */
    public static String globalLog() {
        return GitRepo.globalLog();
    }

    /** Returns the one-line ids of commits with specific commit message. */
    public static String find(String commitMessage) {
        return GitRepo.find(commitMessage);
    }


    /**
     * Returns the status of current git repository
     * Example:
     * === Branches ===
     * *master
     * other-branch
     * <p>
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     * <p>
     * === Removed Files ===
     * goodbye.txt
     * <p>
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     * <p>
     * === Untracked Files ===
     * random.stuff
     */
    public static String status() {
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        ArrayList<MediatorFile> filesInWorkingDir = new ArrayList<>();
        /* Add all files into the list. */
        for (String fileName : fileNames) {
            File file = Utils.join(CWD, fileName);
            filesInWorkingDir.add(new MediatorFile(file, fileName));
        }
        return GitRepo.status(filesInWorkingDir);
    }

    /** Check out file from current commit. */
    public static String checkoutFileFromCurrentCommit(String fileName) {
        return checkoutFileFromSpecificCommit(null, fileName);
    }

    /**
     * Check out file from current commit.
     * checkout from current commit if commitId == null.
     */
    public static String checkoutFileFromSpecificCommit(String commitId, String fileName) {
        MediatorFile mediatorFile = new MediatorFile(fileName);
        String message = GitRepo.checkoutCommitAndFile(commitId, fileName, mediatorFile);

        /* checkout fail. */
        if (message != null) {
            return message;
        }


        File file = Utils.join(CWD, fileName);
        String content = mediatorFile.getContent();
        Utils.writeContents(file, content);

        return null;  // checkout successfully, no message to print.
    }

    /** Checkout branch. */
    public static String checkoutBranch(String branch) {
        HashMap<String, MediatorFile> filesInWorkingDir = new HashMap<>();
        for (String fileName : Utils.plainFilenamesIn(CWD)) {
            File file = Utils.join(CWD, fileName);
            filesInWorkingDir.put(fileName, new MediatorFile(file, fileName));
        }

        filesInWorkingDir.remove(GitRepo.GIT_REPO.getName());

        HashSet<MediatorFile> filesToWrite = new HashSet<>();
        HashSet<String> filesToDelete = new HashSet<>();

        String message =
                GitRepo.checkoutBranch(branch, filesInWorkingDir, filesToWrite, filesToDelete);

        if (message != null) {
            return message;
        }

        for (MediatorFile fileToWrite: filesToWrite) {
            File file = Utils.join(CWD, fileToWrite.getFileName());
            Utils.writeContents(file, fileToWrite.getContent());
        }

        for (String fileNameToDelete: filesToDelete) {
            File file = Utils.join(CWD, fileNameToDelete);
            file.delete();
        }

        return null;
    }

    /**
     * Create a new branch pointing to the current commit.
     * If the branch has already existed, returns the fail message.
     */
    public static String createBranch(String branchName) {
        return GitRepo.createBranch(branchName);
    }
}
