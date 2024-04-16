package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a gitlet commit object.
 *
 * @author Crispy Candy
 */

/** Represents a gitlet commit object and manipulate all commits objects. */
public class Commit implements Serializable {
    /**
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private static final File COMMITS_DIR = Utils.join(Utils.join(GitRepo.GIT_REPO, "objects"),
            "commits");

    /** The format of the time in log command, eg. Thu Nov 9 20:00:05 2017 -0800 */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z",
            Locale.ENGLISH);

    /** The message of the first commit when initialization. */
    private static final String INITIAL_COMMIT_MESSAGE = "initial commit";

    /** The length of the abbreviated commit id. */
    private static final int ABBREVIATE_COMMIT_LENGTH = 7;

    /** The length of the normal commit. */
    private static final int NORMAL_COMMIT_ID_LENGTH = Utils.UID_LENGTH;

    /** The delimiter between two log. */
    private static final String LOGS_DELIMITER = "===";

    /* -------------------------------- instance variables -------------------------------- */

    /** The message of this Commit. */
    private String message;
    private Timestamp timestamp;

    /** The hash code of last parent. */
    private String parent;

    /** The second parent which is the given branch commit when merging. */
    private String secondParent;

    /** The mapping from file name to blob file. */
    private Map<String, String> fileBlobMap;

    /** The sha1 hashcode of this commit. */
    private transient String sha1;

    public Commit(String message, String parent, Map<String, String> fileBlobMap) {
        this(message, parent, System.currentTimeMillis(), fileBlobMap);
    }

    private Commit(String message, String parent, long time, Map<String, String> fileBlobMap) {
        this(message, parent, null, time, fileBlobMap);
    }

    private Commit(String message, String parent, String secondParent,
            Map<String, String> fileBlobMap
    ) {
        this(message, parent, secondParent, System.currentTimeMillis(), fileBlobMap);
    }

    private Commit(String message, String parent, String secondParent, long time,
            Map<String, String> fileBlobMap
    ) {
        this.message = message;
        this.parent = parent;
        this.secondParent = secondParent;
        this.timestamp = new Timestamp(time);
        this.fileBlobMap = fileBlobMap;
    }

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
        File commitFile = getCommitFile(commitId);

        if (commitFile == null || !commitFile.exists()) {
            return null;
        }

        Commit commit = Utils.readObject(commitFile, Commit.class);
        commit.sha1 = commitFile.getName();
        return commit;
    }

    /** Clone the commit with new commit message. */
    public static Commit clone(String newMessage, Commit commit, String secondParent) {
        return new Commit(newMessage, commit.sha1(), secondParent,
                new HashMap<>(commit.fileBlobMap));
    }

    /** Generate the log of the commit iterator. */
    public static String generateLogs(Iterable<Commit> commits) {
        ArrayList<String> logs = new ArrayList<>();
        for (Commit commit : commits) {
            logs.add(commit.logInfo());
        }
        return String.join("\n", logs);
    }

    /** Returns all commit information with arbitrary message. */
    public static String globalLog() {
        Iterable<Commit> commits = allCommits();
        String logs = generateLogs(commits);
        return logs;
    }

    /** Returns the commits from specific to the initial commit. */
    public static Iterable<Commit> getCommitsStartingAt(String commitId) {
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

    /** Returns one-line ids of commits with specific message */
    public static String findCommitsWithMessage(String message) {
        List<String> commitIds = new ArrayList<>();
        Iterable<Commit> commits = allCommits();
        for (Commit commit : commits) {
            if (commit.message.equals(message)) {
                commitIds.add(commit.sha1);
            }
        }

        if (commitIds.size() == 0) {
            return Message.NO_COMMIT_WITH_SPECIFIC_MESSAGE;
        }

        return String.join("\n", commitIds);
    }

    /** Returns the latest common ancestor of two commits. */
    public static Commit getLatestCommonAncestor(Commit commit1, Commit commit2) {
        Iterator<Commit> commitIt1 = getCommitsStartingAt(commit1.sha1).iterator();
        Iterator<Commit> commitIt2 = getCommitsStartingAt(commit2.sha1).iterator();
        HashSet<String> visitedCommit = new HashSet<>();
        while (commitIt1.hasNext() || commitIt2.hasNext()) {
            if (commitIt1.hasNext()) {
                Commit next = commitIt1.next();
                if (visitedCommit.contains(next.sha1)) {
                    return next;
                }
                visitedCommit.add(next.sha1);
            }

            if (commitIt2.hasNext()) {
                Commit next = commitIt2.next();
                if (visitedCommit.contains(next.sha1)) {
                    return next;
                }
                visitedCommit.add(next.sha1);
            }
        }
        return null;
    }

    /**
     * Differentiate which files need to be written, which files need to be deleted, and which
     * files have conflicts, and then fill them into the collection.
     */
    public static void differentiateFiles(
            Commit currentCommit, Commit givenCommit,
            Commit splitPointCommit, List<MediatorFile> filesToWrite,
            List<String> filesToDelete, List<String> filesHaveConflicts
    ) {
        Set<String> filesInSplitCommit = splitPointCommit.getFiles();
        for (String fileName : filesInSplitCommit) {

            if (isContentEquals(fileName, splitPointCommit, currentCommit)
                    && !givenCommit.contains(fileName)) {
                /* Any files present at the split point, unmodified in the current branch,
                and absent in the given branch should be removed (and untracked).*/

                filesToDelete.add(fileName);
            } else if (isContentEquals(fileName, splitPointCommit, givenCommit)
                    && !currentCommit.contains(fileName)) {
                /* Any files present at the split point, unmodified in the given branch,
                and absent in the current branch should remain absent. */
                continue; // do nothing
            } else if (isContentEquals(fileName, splitPointCommit, currentCommit)
                    && !isContentEquals(fileName, splitPointCommit, givenCommit)) {
                /* Any files that have been modified in the given branch since the split point,
                but not modified in the current branch since the split point should be changed
                to their versions in the given branch */
                filesToWrite.add(new MediatorFile(fileName, givenCommit.getContent(fileName)));
            } else if (!isContentEquals(fileName, splitPointCommit, currentCommit)
                    && isContentEquals(fileName, splitPointCommit, givenCommit)) {
                /* Any files that have been modified in the current branch but not in the given
            branch since the split point should stay as they are. */
                continue; // do nothing
            } else if (isContentEquals(fileName, currentCommit, givenCommit)) {
                /* Any files that have been modified in both the current and given branch in the
                same
             way unchanged by the merge. */
                continue; // do nothing
            } else {
                /* Files modified in different ways in the current and given branches or are only
            present at one commit and modified in the other commit are in conflict. */
                filesHaveConflicts.add(fileName);
            }
        }

        /* Get the files only present at given commit but not split point commit. */
        Set<String> filesOnlyInGivenCommitButNotSplitPoint = givenCommit.getFiles();
        filesOnlyInGivenCommitButNotSplitPoint.removeAll(filesInSplitCommit);

        /* The files in 'filesInGivenCommit' is only in given commit (likely in current commit)
        but not in split point commit. */
        for (String fileName : filesOnlyInGivenCommitButNotSplitPoint) {
            /* Any files that were not present at the split point and are present only in the
            given branch should be checked out and staged. */
            if (!currentCommit.contains(fileName)) {
                filesToWrite.add(new MediatorFile(fileName, givenCommit.getContent(fileName)));
            } else if (!isContentEquals(fileName, currentCommit, givenCommit)) {
                /* The files are present at the given commit and current commit but not split point
            commit has different content, which means have conflict. */
                filesHaveConflicts.add(fileName);
            }
        }

        /* The rest are files that were not present at the split point and are present only in
        the given branch which should be checked out and staged. Do nothing. */
    }

    /** Merge each file having conflict. */
    public static List<MediatorFile> mergeConflictFiles(
            List<String> filesHaveConflict, Commit currentCommit, Commit givenCommit
    ) {
        ArrayList<MediatorFile> conflictFiles = new ArrayList<>(filesHaveConflict.size());
        for (String fileName : filesHaveConflict) {
            conflictFiles.add(mergeConflictFile(fileName, currentCommit, givenCommit));
        }
        return conflictFiles;
    }

    /* ----------------------------------- instance methods ----------------------------------- */

    /** Returns the sha1 of this commit. */
    public String sha1() {
        if (sha1 == null) {
            sha1 = Utils.sha1(Utils.serialize(this));
        }
        return sha1;
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
     * Returns true if the commit contains the file and the contents are identical,
     * false otherwise.
     */
    public boolean contains(String fileName, String content) {
        String blobId = fileBlobMap.getOrDefault(fileName, null);
        return blobId != null && blobId.equals(Utils.sha1(content));
    }

    /** Returns the files in this commit. (create new files) */
    public Set<String> getFiles() {
        return new HashSet<>(fileBlobMap.keySet());
    }

    /**
     * Returns the content corresponding the fileName, returns null if the fileName does not
     * exist.
     */
    public String getContent(String fileName) {
        return getContentOrDefault(fileName, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Commit)) {
            return false;
        }

        Commit o = (Commit) obj;
        return this.sha1.equals(o.sha1);
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.sha1);
    }

    @Override
    public String toString() {
        return this.message;
    }

    /**
     * Returns the content of the file in the commit, returns null if the commit do not contain
     * the file.
     */
    public String getContentOrDefault(String fileName, String defaultRes) {
        String blobId = fileBlobMap.get(fileName);
        if (blobId == null) {
            return defaultRes;
        }
        return Blob.fromBlobId(blobId).getContent();
    }

    /* -------------------------- private instance methods -------------------------- */

    /**
     * Returns the log information of this commit.
     * Example:
     * ===
     * commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
     * Date: Thu Nov 9 20:00:05 2017 -0800
     * A commit message.
     * <p>
     * <<<
     */
    private String logInfo() {
        List<String> logLines = new ArrayList<>();

        logLines.add(LOGS_DELIMITER);
        logLines.add("commit " + this.sha1);

        if (isMergedCommit()) {
            logLines.add(getMergeInfoLine());
        }

        logLines.add("Date: " + SDF.format(new Date(timestamp.getTime())));
        logLines.add(message);
        logLines.add("");  // "" for appending a \n

        return String.join("\n", logLines);
    }

    /**
     * Returns the merge info line.
     * Example: Merge: 4975af1 2c1ead1
     */
    private String getMergeInfoLine() {
        return String.join(" ", "Merge:",
                abbreviateCommitId(parent), abbreviateCommitId(secondParent));
    }

    /** Returns true if the commit is from merging, i.e. the commit has two parent */
    private boolean isMergedCommit() {
        return this.secondParent != null;
    }

    /* -------------------------- private class & methods -------------------------- */

    /** The Commit Iterator with the reverse commit order starting at specific commit id. */
    private static class CommitIteratorStartingAtSpecificCommit implements Iterator<Commit> {
        Queue<String> commitIds;
        Set<String> visitedCommitId;

        CommitIteratorStartingAtSpecificCommit(String startCommitId) {
            commitIds = new LinkedList<>();
            visitedCommitId = new HashSet<>();
            commitIds.add(startCommitId);
        }

        @Override
        public boolean hasNext() {
            return !commitIds.isEmpty();
        }

        @Override
        public Commit next() {
            String commitId = commitIds.poll();
            Commit commit = Commit.fromCommitId(commitId);
            visitedCommitId.add(commitId);

            addToCommitIds(commit.parent);
            addToCommitIds(commit.secondParent);

            return commit;
        }

        void addToCommitIds(String commitId) {
            if (commitId != null && !visitedCommitId.contains(commitId)) {
                commitIds.add(commitId);
            }
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

    /**
     * Returns true iff commit1 and commit2 both contains file and the content is equivalent or the
     * both do not contains the file, false otherwise.
     */
    private static boolean isContentEquals(String fileName, Commit commit1, Commit commit2) {
        String blobId1 = commit1.fileBlobMap.getOrDefault(fileName, null);
        String blobId2 = commit2.fileBlobMap.getOrDefault(fileName, null);

        if (blobId1 == null && blobId2 == null) {
            return true;
        }

        return blobId1 != null && blobId1.equals(blobId2);
    }

    /** Merge all files having conflict. */
    private static MediatorFile mergeConflictFile(
            String fileHasConflict, Commit currentCommit, Commit givenCommit
    ) {
        return new MediatorFile(fileHasConflict,
                mergeConflictContent(
                        currentCommit.getContent(fileHasConflict),
                        givenCommit.getContent(fileHasConflict)
                )
        );
    }

    /**
     * Merge the conflict content.
     * <p>
     * Example:
     * <<<<<<< HEAD
     * contents of file in current branch
     * =======
     * contents of file in given branch
     * >>>>>>>
     */
    private static String mergeConflictContent(String currentContent, String givenContent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<<<<<<< HEAD\n");
        if (currentContent != null) {
            stringBuilder.append(currentContent);
        }
        stringBuilder.append("=======\n");
        if (givenContent != null) {
            stringBuilder.append(givenContent);
        }
        stringBuilder.append(">>>>>>>");
        return stringBuilder.toString();
    }

    /** Returns the abbreviated commit id. */
    private static String abbreviateCommitId(String commitId) {
        return commitId.substring(0, ABBREVIATE_COMMIT_LENGTH);
    }

    /** Returns the commit file according the commitId. */
    private static File getCommitFile(String commitId) {
        if (commitId.length() == NORMAL_COMMIT_ID_LENGTH) {
            return Utils.join(COMMITS_DIR, commitId);
        } else if (commitId.length() < NORMAL_COMMIT_ID_LENGTH) {

            List<String> fileNames = Utils.plainFilenamesIn(COMMITS_DIR);

            for (String fileName : fileNames) {
                if (fileName.startsWith(commitId)) {
                    return Utils.join(COMMITS_DIR, fileName);
                }
            }
        }
        return null;
    }
}
