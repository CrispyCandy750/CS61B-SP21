package gitlet;

import java.io.File;
import java.util.*;

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
    public static String init() {
        if (GitRepo.isInitialized()) {
            return Message.GIT_REPO_ALREADY_EXISTS_MESSAGE;
        }
        GitRepo.init();
        return null;
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

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        File addedFile = Utils.join(CWD, fileName);
        if (!addedFile.exists()) {
            return Message.FILE_NOT_FOUND_MESSAGE;
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
        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }
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

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        File file = Utils.join(CWD, fileName);
        /* If the file does not exist. */
        if (!file.exists()) {
            return Message.FILE_NOT_FOUND_MESSAGE;
        }
        List<String> filesToRemove = new ArrayList<>();

        String message = GitRepo.rm(fileName, filesToRemove);
        /* Add the file to the removed area which means remove from working copy. */
        deleteFiles(filesToRemove);
        return message;
    }

    /** Returns the log starting at current commit. */
    public static String log() {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        return GitRepo.log();
    }

    /** Returns all commit logs with arbitrary order */
    public static String globalLog() {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        return GitRepo.globalLog();
    }

    /** Returns the one-line ids of commits with specific commit message. */
    public static String find(String commitMessage) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

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

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        return GitRepo.status(getAllFiles());
    }

    /** Check out file from current commit. */
    public static String checkoutFileFromCurrentCommit(String fileName) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        return checkoutFileFromSpecificCommit(null, fileName);
    }

    /**
     * Check out file from current commit.
     * checkout from current commit if commitId == null.
     */
    public static String checkoutFileFromSpecificCommit(String commitId, String fileName) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

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

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        Map<String, MediatorFile> filesInWorkingDir = getAllFiles();

        List<MediatorFile> filesToWrite = new ArrayList<>();
        List<String> filesToDelete = new ArrayList<>();

        String message =
                GitRepo.checkoutBranch(branch, filesInWorkingDir, filesToWrite, filesToDelete);

        writeFiles(filesToWrite);
        deleteFiles(filesToDelete);

        return message;
    }

    /** Reset from the specific commit. */
    public static String reset(String commitId) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        Map<String, MediatorFile> filesInWorkingDir = getAllFiles();
        List<MediatorFile> filesToWrite = new ArrayList<>();
        List<String> filesToDelete = new ArrayList<>();

        String message =
                GitRepo.reset(commitId, filesInWorkingDir, filesToWrite, filesToDelete);

        if (message == null) {  // success to check out
            writeFiles(filesToWrite);
            deleteFiles(filesToDelete);
        }

        return message;
    }

    /** Merge the specific branch into the current branch. */
    public static String merge(String branchName) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        List<MediatorFile> filesToWrite = new ArrayList<>();
        List<String> filesToDelete = new ArrayList<>();

        String message = GitRepo.merge(branchName, getAllFiles(), filesToWrite, filesToDelete);

        deleteFiles(filesToDelete);
        writeFiles(filesToWrite);

        return message;
    }

    /**
     * Create a new branch pointing to the current commit.
     * If the branch has already existed, returns the fail message.
     */
    public static String createBranch(String branchName) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        return GitRepo.createBranch(branchName);
    }

    /** Remove the branch. */
    public static String removeBranch(String branchName) {

        if (!GitRepo.isInitialized()) {
            return Message.NOT_IN_GIT_REPO_MESSAGE;
        }

        return GitRepo.removeBranch(branchName);
    }

    /** Write the files to the CWD. */
    private static void writeFiles(List<MediatorFile> filesToWrite) {
        for (MediatorFile fileToWrite : filesToWrite) {
            File file = Utils.join(CWD, fileToWrite.getFileName());
            Utils.writeContents(file, fileToWrite.getContent());
        }
    }

    /** Delete the files from the CWD. */
    private static void deleteFiles(List<String> filesToDelete) {
        for (String fileNameToDelete : filesToDelete) {
            File file = Utils.join(CWD, fileNameToDelete);
            file.delete();
        }
    }

    /** Returns all files map from file name to mediator file except the .gitlet */
    private static Map<String, MediatorFile> getAllFiles() {
        HashMap<String, MediatorFile> filesInWorkingDir = new HashMap<>();
        for (String fileName : Utils.plainFilenamesIn(CWD)) {
            File file = Utils.join(CWD, fileName);
            filesInWorkingDir.put(fileName, new MediatorFile(file, fileName));
        }

        filesInWorkingDir.remove(GitRepo.GIT_REPO.getName());

        return filesInWorkingDir;
    }
}
