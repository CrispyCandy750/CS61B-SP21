package gitlet.git;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Represents the Staging Area. */
public class StagingArea implements Serializable {

    /** Represents the .gitlet/index file. */
    public final static File INDEX_FILE = Utils.join(GitRepo.GIT_REPO, "index");

    /** Represents the pointer from files to blob */
    private Map<String, String> stagedFiles;
    private Set<String> removedFiles;


    /** Initialization: Create the .gitlet/index file. */
    public static void init() {
        StagingArea stagingArea = new StagingArea();
        stagingArea.save();
    }

    /** Add the pointer from filename to blob. */
    public static void add(String fileName, String blobId) {
        StagingArea stagingArea = getStagingArea();
        stagingArea.addToStagingArea(fileName, blobId);
        stagingArea.save();
    }

    /* ---------------------------- private class & methods ---------------------------- */

    private StagingArea() {
        stagedFiles = new HashMap<>();
        removedFiles = new HashSet<>();
    }

    private static StagingArea getStagingArea() {
        return Utils.readObject(INDEX_FILE, StagingArea.class);
    }

    /* ----------------------------------- instance methods ----------------------------------- */

    /** Save the StagedFiles and removedFiles. */
    private void save() {
        Utils.writeObject(INDEX_FILE, this);
    }

    /** Add the pointer from filename to blob. */
    private void addToStagingArea(String fileName, String blobId) {
        this.stagedFiles.put(fileName, blobId);
    }
}
