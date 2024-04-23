package gitlet;

import java.io.File;
import java.util.*;

/**
 * Represents a gitlet repository.
 *
 * @author Crispy Candy
 */
public class Repository {
    /**
     * <p>
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    private Repository() {
    }

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** Initialize the git repository. */
    public static String init() {
        if (GitRepo.isInitialized()) {
            return Message.GIT_REPO_ALREADY_EXISTS_MESSAGE;
        }
        GitRepo.init();
        return null;
    }


    /** add a file to the staged area. */
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

    /** Create a commit. */
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
     * 1. if the fileName is in staged area, remove it from staged area
     * 2. else if the fileName is in the current commit, add it to removed Area and remove it from
     * working copy.
     * 3. else, print "No reason to remove the file."
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

        /* Add the file to the removed area which means remove from working copy. */
        String message = GitRepo.rm(fileName, filesToRemove);
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
