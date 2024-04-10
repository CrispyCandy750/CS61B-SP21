package gitlet.git;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author Crispy Candy
 */

/** Represents a gitlet commit object and manipulate all commits objects. */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private final static File COMMITS_DIR = Utils.join(GitRepo.GIT_REPO, "objects");

    /** The message of this Commit. */
    private String message;
    private Timestamp timestamp;

    /** The hash code of last parent. */
    private String parent;

    /** The mapping from file name to blob file. */
    private Map<String, String> fileBlobMap;

    public Commit(String message, String parent, Map<String, String> fileBlobMap) {
        this.message = message;
        this.parent = parent;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.fileBlobMap = fileBlobMap;
    }

    private Commit(String message, String parent, long time, Map<String, String> fileBlobMap) {
        this.message = message;
        this.parent = parent;
        this.timestamp = new Timestamp(time);
        this.fileBlobMap = fileBlobMap;
    }

    /* TODO: fill in the rest of this class. */

    /** Create the .gitlet/objects/ directory and initial commit. */
    public static String init() {
        /* Creates */
        COMMITS_DIR.mkdir();
        Commit initialCommit = initialCommit();
        return initialCommit.saveCommit();
    }

    /** Creates the initial commit. */
    private static Commit initialCommit() {
        return new Commit("initial message", null, 0, new HashMap<>());
    }

    /** Returns commit by commit id. */
    public static Commit fromCommitId(String commitId) {
        File commitFile = Utils.join(Utils.join(COMMITS_DIR, commitId.substring(0, 2)),
                commitId.substring(2));
        return Utils.readObject(commitFile, Commit.class);
    }

    /* ----------------------------------- instance methods ----------------------------------- */

    /** Returns the sha1 of this commit. */
    public String sha1() {
        return Utils.sha1(Utils.serialize(this));
    }

    /**
     * Returns true if the commit contains the file and is identical to the version in the
     * current commit, false otherwise.
     */
    public boolean containsAndIsIdentical(MediatorFile file) {
        String blobId = fileBlobMap.getOrDefault(file.getFileName(), null);
        return blobId != null && blobId.equals(file.sha1());
    }

    /** Save the commit in the objects directory with the name as its hashcode. */
    public String saveCommit() {
        String commitId = this.sha1();

        /* Creates the directory contains this commit. */
        File commitDir = Utils.join(COMMITS_DIR, commitId.substring(0, 2));
        if (!commitDir.exists()) {
            commitDir.mkdir();
        }

        /* Save the commit. */
        File commitFile = Utils.join(commitDir, commitId.substring(2));
        Utils.writeObject(commitFile, this);

        return commitId;
    }

    /** Returns the mapping from */
    public Map<String, String> getFileBlobMap() {
        return fileBlobMap;
    }

    /** Add or modify the mapping from fileName to blobId. */
    public void put(String fileName, String blobId) {
        fileBlobMap.put(fileName, blobId);
    }

    /** Remove the file. */
    public void remove(String fileName) {
        fileBlobMap.remove(fileName);
    }

    /* ----------------------------------- private methods ----------------------------------- */

}
