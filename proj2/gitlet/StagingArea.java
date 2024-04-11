package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Represents the Staging Area. */
public class StagingArea {

    /** Represents the .gitlet/index file. */
    public final static File INDEX_FILE = Utils.join(GitRepo.GIT_REPO, "index");
    private static StagedAndRemovedArea stagedAndRemovedArea = null;

    /** Initialization: Create the .gitlet/index file. */
    public static void init() {
        clear();
    }

    /** Add the pointer from filename to blob. */
    public static void addToStagedArea(String fileName, String blobId) {
        StagedAndRemovedArea stagingArea = getStagedAndRemovedArea();
        stagingArea.add(fileName, blobId);
        stagingArea.save();
    }

    /** Clear the mapping with empty StagedAndRemovedArea object. */
    public static void clear() {
        StagedAndRemovedArea stagedAndRemovedArea = new StagedAndRemovedArea();
        stagedAndRemovedArea.save();
    }

    /** Returns whether there are any files to be changed. */
    public static boolean changed() {
        return haveFilesToBeAddedOrModified() || haveFilesToBeRemoved();
    }

    /** Returns whether there are any files to be added or modified. */
    public static boolean haveFilesToBeAddedOrModified() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return !stagedAndRemovedArea.filesToAddOrModify.isEmpty();
    }

    /** Returns whether there are any files to be removed. */
    public static boolean haveFilesToBeRemoved() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return !stagedAndRemovedArea.filesToRemove.isEmpty();
    }

    /** Returns true if the file is in staged area to be added or modified. */
    public static boolean isAddedOrModified(String fileName) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return stagedAndRemovedArea.filesToAddOrModify.containsKey(fileName);
    }

    /** Remove the file from the staged area. */
    public static void removeFromStagedArea(String fileName) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        stagedAndRemovedArea.filesToAddOrModify.remove(fileName);
        stagedAndRemovedArea.save();
    }

    /** Add the file to the removed area. */
    public static void addToRemovedArea(String fileName) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        stagedAndRemovedArea.filesToRemove.add(fileName);
        stagedAndRemovedArea.save();
    }

    /** Updates the file-blob map of the commit with staged area. */
    public static void addOrModifyFilesToCommit(Commit commit) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        Map<String, String> filesToAddOrModify = stagedAndRemovedArea.filesToAddOrModify;
        for (String fileName : filesToAddOrModify.keySet()) {
            commit.put(fileName, filesToAddOrModify.get(fileName));
        }
    }

    /** Remove the files in the commit with removed area. */
    public static void removeFilesFromCommit(Commit commit) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        Set<String> filesToRemove = stagedAndRemovedArea.filesToRemove;
        for (String fileName: filesToRemove) {
            commit.remove(fileName);
        }
    }

    /* ---------------------------- private class & methods ---------------------------- */
    private static class StagedAndRemovedArea implements Serializable {
        /** Represents the `staged area` and mapping from fileNames to blobId */
        private Map<String, String> filesToAddOrModify;

        /** Represents the removed area. */
        private Set<String> filesToRemove;

        StagedAndRemovedArea() {
            filesToAddOrModify = new HashMap<>();
            filesToRemove = new HashSet<>();
        }

        /** Save the StagedFiles and removedFiles. */
        void save() {
            Utils.writeObject(INDEX_FILE, this);
        }

        /** Add the pointer from filename to blob. */
        void add(String fileName, String blobId) {
            filesToAddOrModify.put(fileName, blobId);
        }
    }

    /** Returns the stagedArea. */
    private static StagedAndRemovedArea getStagedAndRemovedArea() {
        if (stagedAndRemovedArea == null) {
            stagedAndRemovedArea = Utils.readObject(INDEX_FILE, StagedAndRemovedArea.class);
        }
        return stagedAndRemovedArea;
    }
}
