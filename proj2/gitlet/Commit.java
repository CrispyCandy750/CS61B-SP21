package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private final static File COMMITS_DIR = Utils.join(Utils.join(GitRepo.GIT_REPO, "objects"),
            "commits");

    /** The format of the time in log command, eg. Thu Nov 9 20:00:05 2017 -0800 */
    private final static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z",
            Locale.ENGLISH);

    /** The message of the first commit when initialization. */
    private final static String INITIAL_COMMIT_MESSAGE = "initial message";

    /** The message of this Commit. */
    private String message;
    private Timestamp timestamp;

    /** The hash code of last parent. */
    private String parent;

    /** The mapping from file name to blob file. */
    private Map<String, String> fileBlobMap;

    /** The sha1 hashcode of this commit. */
    private transient String sha1;

    /** The delimiter between two log. */
    private final static String LOGS_DELIMITER = "===";

    public Commit(String message, String parent, Map<String, String> fileBlobMap) {
        this(message, parent, System.currentTimeMillis(), fileBlobMap);
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
        COMMITS_DIR.mkdirs();
        Commit initialCommit = initialCommit();
        return initialCommit.saveCommit();
    }

    /** Returns commit by commit id. */
    public static Commit fromCommitId(String commitId) {
        if (commitId == null) {
            return null;
        }
        File commitFile = Utils.join(COMMITS_DIR, commitId);
        Commit commit = Utils.readObject(commitFile, Commit.class);
        commit.sha1 = commitId;
        return commit;
    }

    /** Clone the commit with new commit message. */
    public static Commit clone(String newMessage, Commit commit) {
        return new Commit(newMessage, commit.sha1(), commit.fileBlobMap);
    }

    /** Generate the logs. */
    public static String generateLogs(Iterable<Commit> commits) {
        StringBuilder logs = new StringBuilder();
        for (Commit commit : commits) {
            logs.append(LOGS_DELIMITER);
            logs.append("\n");
            logs.append(commit.logInfo());
            logs.append("\n");
        }
        return logs.deleteCharAt(logs.lastIndexOf("\n")).toString();
    }

    /** Returns the commits from specific to the initial commit. */
    public static Iterable<Commit> commitsStartingAt(String commitId) {
        return new Iterable<Commit>() {
            @Override
            public Iterator<Commit> iterator() {
                return new CommitIteratorStartingAtSpecificCommit(commitId);
            }
        };
    }

    /** Returns all commits with arbitrarily order. */
    public static Iterable<Commit> allCommits() {
        return new Iterable<Commit>() {
            @Override
            public Iterator<Commit> iterator() {
                return new AllCommitsIterator(Utils.plainFilenamesIn(COMMITS_DIR));
            }
        };
    }

    /* ----------------------------------- instance methods ----------------------------------- */

    /** Returns the sha1 of this commit. */
    public String sha1() {
        this.sha1 = Utils.sha1(Utils.serialize(this));
        return this.sha1;
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
        File commitFile = Utils.join(COMMITS_DIR, commitId);
        Utils.writeObject(commitFile, this);

        return commitId;
    }

    /** Add or modify the mapping from fileName to blobId. */
    public void put(String fileName, String blobId) {
        fileBlobMap.put(fileName, blobId);
    }

    /** Remove the file. */
    public void remove(String fileName) {
        fileBlobMap.remove(fileName);
    }

    /** Returns true if the commit contains the file, false otherwise. */
    public boolean contains(String fileName) {
        return fileBlobMap.containsKey(fileName);
    }

    /**
     * Returns the log information of this commit.
     * Example:
     * commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
     * Date: Thu Nov 9 20:00:05 2017 -0800
     * A commit message.
     */
    public String logInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("commit " + this.sha1 + "\n");
        stringBuilder.append("Date: " + sdf.format(new Date(timestamp.getTime())) + "\n");
        stringBuilder.append(message + "\n");
        return stringBuilder.toString();
    }
    /* -------------------------- private class & methods -------------------------- */

    /** The Commit Iterator with the reverse commit order starting at specific commit id. */
    private static class CommitIteratorStartingAtSpecificCommit implements Iterator<Commit> {
        String curCommitId;

        CommitIteratorStartingAtSpecificCommit(String startCommitId) {
            this.curCommitId = startCommitId;
        }

        @Override
        public boolean hasNext() {
            return curCommitId != null;
        }

        @Override
        public Commit next() {
            Commit commit = Commit.fromCommitId(curCommitId);
            curCommitId = commit.parent;
            return commit;
        }
    }

    /** The commit iterator with arbitrary order. */
    private static class AllCommitsIterator implements Iterator<Commit> {
        Iterator<String> commitFileNameIterator;

        AllCommitsIterator(Iterable<String> commitFileNames) {
            this.commitFileNameIterator = commitFileNames.iterator();
        }

        @Override
        public boolean hasNext() {
            return commitFileNameIterator.hasNext();
        }

        @Override
        public Commit next() {
            return Commit.fromCommitId(commitFileNameIterator.next());
        }
    }

    /** Creates the initial commit. */
    private static Commit initialCommit() {
        return new Commit(INITIAL_COMMIT_MESSAGE, null, 0, new HashMap<>());
    }
}
