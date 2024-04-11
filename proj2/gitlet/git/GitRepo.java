package gitlet.git;

import java.io.File;
import java.util.Map;
import java.util.Set;

/** GitRepo class represents the .gitlet directory. */
public class GitRepo {

    /** The .getlet directory */
    public final static File GIT_REPO = Utils.join(Repository.CWD, ".gitlet");
    public final static String DEFAULT_INITIAL_BRANCH = "master";

    /** The message when there is no files have been staged. */
    private final static String NO_STAGED_FILE_MESSAGE = "No changes added to the commit";


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

    /**
     * 1. Check if the current working version of the file is identical to the version in the
     * current commit.
     * 2. Store the file as a blob object
     * 3. add the Map in the INDEX file.
     */
    public static void add(MediatorFile file) {

        Commit commit = HEADPointer.currentCommit();
        if (commit.containsAndIsIdentical(file)) {
            return;
        }

        Blob blob = new Blob(file.getContent());
        blob.saveBlob();

        StagingArea.add(file.getFileName(), blob.getBlobId());
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
        Commit newCommit = new Commit(message, HEADPointer.currentCommitId(),
                currentCommit.getFileBlobMap());

        /* Add or modify the mapping from file name to blob file. */
        if (StagingArea.haveFilesToBeAddedOrModified()) {
            Map<String, String> filesToAddOrModify = StagingArea.getFilesToAddOrModify();
            for (String fileName : filesToAddOrModify.keySet()) {
                newCommit.put(fileName, filesToAddOrModify.get(fileName));
            }
        }

        /* Remove the files. */
        if (StagingArea.haveFilesToBeRemoved()) {
            Set<String> filesToRemove = StagingArea.getFilesToRemove();
            for (String fileName : filesToRemove) {
                newCommit.remove(fileName);
            }
        }

        /* Save the new commit. */
        String newCommitId = newCommit.saveCommit();

        /* Update the state of git repository. */
        StagingArea.clear();
        Reference.move(HEADPointer.currentBranch(), newCommitId);

        return null; // no message to print.
    }
}
