package gitlet;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/** GitRepo class represents the .gitlet directory. */
public class GitRepo {

    /** The .getlet directory */
    public final static File GIT_REPO = Utils.join(Repository.CWD, ".gitlet");
    public final static String DEFAULT_INITIAL_BRANCH = "master";

    /** The message when no reason to remove the file. */
    private final static String NO_REASON_TO_REMOVE_MESSAGE = "No reason to remove the file.";


    /** The message when file does not exist in the commit. */
    private final static String FILE_DOES_NOT_EXISTS_MESSAGE = "File does not exist in that " +
            "commit.";

    /** The message when checkout branch and no such branch exists. */
    private final static String NO_SUCH_BRANCH_MESSAGE = "No such branch exists.";

    /** The message when no need to check out branch. */
    private final static String NO_NEED_TO_CHECKOUT_BRANCH_MESSAGE = "No need to checkout the " +
            "current branch.";

    /** The message when removed branch is current branch. */
    private final static String CANNOT_REMOVE_CURRENT_BRANCH_MESSAGE = "Cannot remove the current" +
            " branch.";




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
        if (commit.contains(file.getFileName(), file.getContent())) {
            if (StagingArea.inStagedArea(file.getFileName())) {
                StagingArea.removeFromStagedArea(file.getFileName());
            }
            return null;
        }

        StagingArea.addToStagedArea(file.getFileName(), file.getContent());
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
        if (!StagingArea.haveStagedOrRemovedFiles()) {
            return Message.COMMIT_BUT_NO_STAGED_FILE_MESSAGE;
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
        Reference.moveBranch(HEADPointer.currentBranch(), newCommitId);

        return null; // no message to print.
    }

    /**
     * 1. if the fileName is in staged area, remove it from staged area
     * 2. else if the fileName is in the current commit, add it to removed Area and remove it from
     * working copy.
     * 3. else, print "No reason to remove the file."
     * <p>
     * Returns "" when add the file to removed area.
     */
    public static String rm(String fileName, Collection<String> filesToRemove) {

        /* Check if the file is in the staged area to be added or modified. */
        if (StagingArea.inStagedArea(fileName)) {
            StagingArea.removeFromStagedArea(fileName);
            return null;  // no message to be printed.
        }

        /* Check if the file is in the latest commit. */
        Commit commit = HEADPointer.currentCommit();
        if (commit.contains(fileName)) {
            StagingArea.addToRemovedArea(fileName);
            filesToRemove.add(fileName);
            /* no message to be printed and remove the file from working copy. */
            return null;
        }

        return NO_REASON_TO_REMOVE_MESSAGE;
    }

    /** Returns the logs starting at the commit head points to. */
    public static String log() {
        String currentCommitId = HEADPointer.currentCommitId();

        /* Generate the commits from current commit to initial commit. */
        Iterable<Commit> commits = Commit.getCommitsStartingAt(currentCommitId);
        String logs = Commit.generateLogs(commits);

        return logs;
    }

    /** Returns all commit logs with arbitrary order. */
    public static String globalLog() {
        return Commit.globalLog();
    }

    /** Returns one-line ids of commits with specific message */
    public static String find(String message) {
        return Commit.findCommitsWithMessage(message);
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
    public static String status(Map<String, MediatorFile> filesInWorkingDir) {
        /* === Branches === */
        String branchesStatus = getBranchesStatus();

        /* === Staged Files === */
        String stagedFilesStatus = StagingArea.statusOfStagedFiles();

        /* === Removed Files === */
        String removedFilesStatus = StagingArea.statusOfRemovedFiles();

        /* === Modifications Not Staged For Commit === */
        Collection<MediatorFile> mediatorFilesInWorkingDir = filesInWorkingDir.values();
        String modifiedButNotStagedFilesStatus =
                getModifiedButNotStagedFilesStatus(mediatorFilesInWorkingDir);

        /* === Untracked Files === */
        Set<String> fileNamesInWorkingDir = filesInWorkingDir.keySet();
        String untrackedFilesStatus = getUntrackedFilesStatus(fileNamesInWorkingDir);

        /* split the status with '\n' */
        return String.join("\n",
                branchesStatus,
                stagedFilesStatus,
                removedFilesStatus,
                modifiedButNotStagedFilesStatus,
                untrackedFilesStatus);
    }

    /**
     * Returns the branches status
     * Example:
     * === Branches ===
     * *master
     * other-branch
     */
    private static String getBranchesStatus() {
        List<String> branches = Reference.branches();
        String currentBranch = HEADPointer.currentBranch();

        /* Add a * before the current branch. */
        branches.set(branches.indexOf(currentBranch), "*" + currentBranch);

        Collections.sort(branches);

        return Utils.FormatStrings(branches, "=== Branches ===");
    }


    /**
     * Returns the files whose modification is not in staging area.
     * Example:
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     */
    private static String getModifiedButNotStagedFilesStatus(Collection<MediatorFile> filesInWorkingDir) {

        Commit currentCommit = HEADPointer.currentCommit();
        List<String> filesModifiedButNotStaged = new ArrayList<>();

        /* Get the file which is modified but not added to staging area. */
        for (MediatorFile file : filesInWorkingDir) {
            String fileName = file.getFileName();
            String content = file.getContent();
            /* The file is tracked in the current commit, changed in the working directory, but
            not staged */
            if ((currentCommit.contains(fileName) && !currentCommit.contains(fileName, content)
                    && !StagingArea.inStagedArea(fileName))
                    /* Staged for addition, but with different contents than in the working
                    directory */
                    || (StagingArea.inStagedArea(fileName)
                    && !StagingArea.inStagedArea(fileName, content))) {
                filesModifiedButNotStaged.add(fileName + " (modified)");
            }
        }

        Set<String> filesTrackedInCommit = currentCommit.getFiles();
        Set<String> fileNamesInWorkingDir =
                filesInWorkingDir.stream().map(file -> file.getFileName()).collect(Collectors.toSet());
        /* Get the files removed from working directory. */
        filesTrackedInCommit.removeAll(fileNamesInWorkingDir);
        Set<String> deletedFileNames = filesTrackedInCommit; // rename for understanding.

        for (String fileName : deletedFileNames) {
            if (!StagingArea.inRemovedArea(fileName)) {
                filesModifiedButNotStaged.add(fileName + " (deleted)");
            }
        }

        Collections.sort(filesModifiedButNotStaged);
        return Utils.FormatStrings(filesModifiedButNotStaged, "=== Modifications Not Staged For " +
                "Commit ===");
    }

    /**
     * Returns the files which is untracked.
     * Example:
     * === Untracked Files ===
     * random.stuff
     */
    private static String getUntrackedFilesStatus(Set<String> fileNamesInWorkingDir) {
        Commit currentCommit = HEADPointer.currentCommit();
        ArrayList<String> untrackedFiles = new ArrayList<>();
        for (String fileName : fileNamesInWorkingDir) {
            /* files present in the working directory but neither staged for addition nor tracked
            . */
            if (!currentCommit.contains(fileName)
                    && !StagingArea.inStagedArea(fileName)
                    /* Files that have been staged for removal, but then re-created without
                    Gitlet’s knowledge. */
                    || StagingArea.inRemovedArea(fileName)) {
                untrackedFiles.add(fileName);
            }
        }

        Collections.sort(untrackedFiles);
        return Utils.FormatStrings(untrackedFiles, "=== Untracked Files ===");
    }

    /**
     * Checkout a file from the specific commit.
     * If commitId == null, means checkout from current commit.
     */
    public static String checkoutCommitAndFile(String commitId, String fileName,
                                               MediatorFile mediatorFile) {
        if (commitId == null) {
            commitId = HEADPointer.currentCommitId();
        }
        Commit commit = Commit.fromCommitId(commitId);
        if (commit == null) {
            return Message.COMMIT_DOES_NOT_EXIST_MESSAGE;
        }
        if (!commit.contains(fileName)) {
            return FILE_DOES_NOT_EXISTS_MESSAGE;
        }
        String content = commit.getContent(fileName);
        mediatorFile.setContent(content);
        return null;
    }

    /** Checkout the branch. */
    public static String checkoutBranch(
            String branchName, Map<String, MediatorFile> filesInWorkingDir,
            List<MediatorFile> filesToWrite, List<String> filesToDelete) {

        if (!Reference.containsBranch(branchName)) {
            return NO_SUCH_BRANCH_MESSAGE;
        }

        if (HEADPointer.currentBranch().equals(branchName)) {
            return NO_NEED_TO_CHECKOUT_BRANCH_MESSAGE;
        }

        String message =
                checkoutCommit(Reference.furthestCommitId(branchName), filesInWorkingDir,
                        filesToWrite, filesToDelete);

        if (SuccessCheckingOutCommit(message)) {  // success to check out
            HEADPointer.moveToRefs(branchName);
            StagingArea.clear();  // only in gitlet but git
        }


        return message;
    }

    /**
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current `branch’s head` to that commit node.
     * The [commit id] may be abbreviated as for checkout. The staging area is cleared.
     * The command is essentially checkout of an arbitrary commit that also changes the
     * current branch head.
     */
    public static String reset(
            String commitId, Map<String, MediatorFile> filesInWorkingDir,
            List<MediatorFile> filesToWrite, List<String> filesToDelete
    ) {

        String message = checkoutCommit(
                commitId, filesInWorkingDir, filesToWrite, filesToDelete);

        if (SuccessCheckingOutCommit(message)) { // success to check out commit
            Reference.moveBranch(HEADPointer.currentBranch(), commitId);
            StagingArea.clear();  // only in gitlet but git
        }

        return message;
    }

    private static String checkoutCommit(
            String commitId, Map<String, MediatorFile> filesInWorkingDir,
            List<MediatorFile> filesToWrite, List<String> filesToDelete
    ) {
        /* The checked commit is the current commit, abort. */
        if (HEADPointer.currentBranch().equals(commitId)) {
            return null;
        }

        Commit givenCommit = Commit.fromCommitId(commitId);

        if (givenCommit == null) {
            return Message.COMMIT_DOES_NOT_EXIST_MESSAGE;
        }

        String message =
                populateFilesToWriteOrDelete(givenCommit, filesInWorkingDir,
                        filesToWrite, filesToDelete);

        if (!SuccessCheckingOutCommit(message)) {
            filesToWrite.clear();
            filesToDelete.clear();
        }

        return message;
    }

    /**
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory, overwriting the versions
     * of the files that are already there if they exist.
     * <p>
     * Also, at the end of this command, the given branch will now be considered the
     * current branch (HEAD). Any files that are tracked in the current branch but
     * are not present in the checked-out branch are deleted.
     * <p>
     * The staging area is cleared, unless the checked-out branch is the current
     * branch
     * <p>
     * If no branch with that name exists, print No such branch exists.
     * If that branch is the current branch, print No need to checkout
     * the current branch.
     * If a working file is untracked in the current branch and would be
     * overwritten by the checkout, print `There is an untracked file in
     * the way; delete it, or add and commit it first`. and exit;
     * perform this check before doing anything else. Do not change the CWD.
     */
    private static String populateFilesToWriteOrDelete(
            Commit givenCommit, Map<String, MediatorFile> filesInWorkingDir,
            List<MediatorFile> filesToWrite, List<String> filesToDelete) {

        /* Populate files to write: the file and content existing in given commit but not in working
        directory. */
        for (String fileName : givenCommit.getFiles()) {  // iterate the file in working copy
            /* this file is ignore in the process of 'populate the deleted file list'. */
            MediatorFile fileInWorkDir = filesInWorkingDir.remove(fileName);
            if (fileInWorkDir == null) {  // the file is present at given commit but not current
                // commit.
                filesToWrite.add(new MediatorFile(fileName, givenCommit.getContent(fileName)));
                continue;
            }
            String content = fileInWorkDir.getContent();
            if (!givenCommit.contains(fileName, content)) { // the file need to rewrite
                if (isWorkingFileUntracked(fileName)) {  // fail if the file is untracked
                    return Message.OVERWRITE_OR_DELETE_UNTRACKED_FILE_MESSAGE;
                }
                filesToWrite.add(new MediatorFile(fileName, givenCommit.getContent(fileName)));
            }
        }

        /* the rest file in filesInWorkingDir is only in the working directory but not commit,
        which are likely to be deleted. */

        /* Populate files to delete: untracked and exists in working directory but commit. */
        for (String fileNameInWorkingDir : filesInWorkingDir.keySet()) {
            if (!isWorkingFileUntracked(fileNameInWorkingDir)) { // the untracked file is not
                // delete.
                filesToDelete.add(fileNameInWorkingDir);
            }
        }
        return null;
    }

    /**
     * Create a new branch pointing to the current commit.
     * If the branch has already existed, returns the fail message.
     */
    public static String createBranch(String branchName) {
        return Reference.createNewBranch(branchName, HEADPointer.currentCommitId());
    }

    /** Remove the branch. */
    public static String removeBranch(String branchName) {
        if (!Reference.containsBranch(branchName)) {
            return Message.BRANCH_NAME_DO_NOT_EXIST_MESSAGE;
        } else if (HEADPointer.currentBranch().equals(branchName)) {
            return CANNOT_REMOVE_CURRENT_BRANCH_MESSAGE;
        }
        Reference.removeBranch(branchName);
        return null;
    }

    /** Merges files from the given branch into the current branch. */
    public static String merge(
            String givenBranchName, Map<String, MediatorFile> filesInWorkingDir,
            List<MediatorFile> filesToWrite, List<String> filesToDelete
    ) {

        /* If a branch with the given name does not exist. */
        if (!Reference.containsBranch(givenBranchName)) {
            return Message.BRANCH_NAME_DO_NOT_EXIST_MESSAGE;
        }

        /* If attempting to merge a branch with itself. */
        if (HEADPointer.currentBranch().equals(givenBranchName)) {
            return Message.MERGE_CURRENT_BRANCH_MESSAGE;
        }

        /* If there are staged additions or removals present. */
        if (StagingArea.haveStagedOrRemovedFiles()) {
            return Message.MERGE_HAVE_UNCOMMITTED_FILES_MESSAGE;
        }



        Commit givenCommit = Reference.furthestCommit(givenBranchName);
        Commit currentCommit = HEADPointer.currentCommit();
        Commit splitPointCommit = Commit.getLatestCommonAncestor(currentCommit, givenCommit);

        /* The given commit is the ancestor of current commit */
        if (splitPointCommit.equals(givenCommit)) {
            return Message.MERGE_ANCESTOR_MESSAGE;
        }

        /* The current commit is the ancestor of given commit. */
        else if (splitPointCommit.equals(currentCommit)) {
            /* Transfer the commit to files to avoid the untracked file in working directory. */
            String message =
                    checkoutBranch(givenBranchName, filesInWorkingDir, filesToWrite, filesToDelete);

            /* There are untracked files are overwritten or deleted, abort. */
            if (SuccessCheckingOutCommit(message)) {
                message = Message.FAST_FORWARD_MESSAGE;
            }
            return message;
        }

        List<String> filesHaveConflict = new ArrayList<>();
        Commit.differentiateFiles(currentCommit, givenCommit, splitPointCommit,
                filesToWrite, filesToDelete, filesHaveConflict);

        /* There are untracked files are overwritten or deleted, abort. */
        Set<String> fileNamesInWorkingDir = filesInWorkingDir.keySet();
        if (haveUntrackedFile(filesToWrite, fileNamesInWorkingDir)
                || haveUntrackedFileName(filesToDelete, fileNamesInWorkingDir)
                || haveUntrackedFileName(filesHaveConflict, fileNamesInWorkingDir)) {
            filesToWrite.clear();
            filesToDelete.clear();
            return Message.OVERWRITE_OR_DELETE_UNTRACKED_FILE_MESSAGE;
        }

        /* Default message to print. */
        String message = null;

        /* The merge encountered a conflict */
        if (!filesHaveConflict.isEmpty()) {
            message = Message.MERGE_CONFLICT_MESSAGE;

            List<MediatorFile> filesHaveConflictContent =
                    Commit.mergeConflictFiles(filesHaveConflict,
                            currentCommit, givenCommit);

            filesToWrite.addAll(filesHaveConflictContent);
        }

        // add all to staging area
        StagingArea.addAllToStagedAndRemovedArea(filesToWrite, filesToDelete);
        commit(Message.getMergeMessage(givenBranchName, HEADPointer.currentBranch()));

        return message;
    }

    /* ------------------------------- private methods ------------------------------- */

    /** Returns true if the file is untracked in current commit, false otherwise. */
    private static boolean isUntracked(String fileName, Collection<String> filesInWorkingDir) {
        return (filesInWorkingDir.contains(fileName) && isWorkingFileUntracked(fileName));
    }

    /**
     * Returns true if the file is untracked in current commit, false otherwise.
     *
     * @param fileName the file in working directory
     */
    private static boolean isWorkingFileUntracked(String fileName) {
        Commit commit = HEADPointer.currentCommit();
        return (!commit.contains(fileName)
                && !StagingArea.inStagedArea(fileName)
                && !StagingArea.inRemovedArea(fileName));
    }

    /** Returns true if the files to change contains untracked file. */
    private static boolean haveUntrackedFileName(
            Collection<String> filesToChange, Collection<String> filesInWorkingDir) {
        for (String fileName : filesToChange) {
            if (isUntracked(fileName, filesInWorkingDir)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if the files to change contains untracked file. */
    private static boolean haveUntrackedFile(
            Collection<MediatorFile> filesToChange, Collection<String> filesInWorkingDir) {
        for (MediatorFile file : filesToChange) {
            if (isUntracked(file.getFileName(), filesInWorkingDir)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if the checkout successfully, false otherwise. */
    private static boolean SuccessCheckingOutCommit(String checkoutMessage) {
        return checkoutMessage == null || !checkoutMessage.equals(Message.OVERWRITE_OR_DELETE_UNTRACKED_FILE_MESSAGE);
    }

}
