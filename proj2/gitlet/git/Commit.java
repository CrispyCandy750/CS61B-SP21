package gitlet.git;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
/** Represents a gitlet commit object and manipulate all commits objects. */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private final static File COMMITS_DIR = Utils.join(GitRepo.GIT_REPO, "objects");

    /** The message of this Commit. */
    private String message;
    private Timestamp timestamp;
    private String parent;
    private Map<File, Blob> files;

    public Commit(String message, String parent, Map<File, Blob> files) {
        new Commit(message, parent, System.currentTimeMillis(), files);
    }

    private Commit(String message, String parent, long time, Map<File, Blob> files) {
        this.message = message;
        this.parent = parent;
        this.timestamp = new Timestamp(time);
        this.files = files;
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
        return new Commit("initial message", null, 0, null);
    }

    /** Save the commit in the objects directory with the name as its hashcode. */
    private String saveCommit() {
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

    public String sha1() {
        return Utils.sha1(Utils.serialize(this));
    }
}
