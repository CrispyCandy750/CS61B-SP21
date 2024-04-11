package gitlet.git;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Represents the Staging Area. */
public class StagingArea{

    /** Represents the .gitlet/index file. */
    public final static File INDEX_FILE = Utils.join(GitRepo.GIT_REPO, "index");

    /** Represents whether there are any files to be added or modified */
    private static boolean hasAddOrModify;

    /** Represents whether there are any files to be removed */
    private static boolean hasRemoved;

    /** Initialization: Create the .gitlet/index file. */
    public static void init() {
        clear();
    }

    /** Add the pointer from filename to blob. */
    public static void add(String fileName, String blobId) {
        StagedAndRemovedArea stagingArea = getStagingArea();
        stagingArea.add(fileName, blobId);
        stagingArea.save();
        hasAddOrModify = true;
    }

    /** Returns the mapping from files to be added or modified to blob. */
    public static Map<String, String> getFilesToAddOrModify() {
        return getStagingArea().filesToAddOrModify;
    }

    /** Returns the files to be removed. */
    public static Set<String> getFilesToRemove() {
        return getStagingArea().filesToRemove;
    }

    /** Clear the mapping with empty StagedAndRemovedArea object. */
    public static void clear() {
        StagedAndRemovedArea stagedAndRemovedArea = new StagedAndRemovedArea();
        stagedAndRemovedArea.save();
        hasAddOrModify = false;
        hasRemoved = false;
    }

    /** Returns whether there are any files to be changed. */
    public static boolean changed() {
        return haveFilesToBeAddedOrModified() || haveFilesToBeRemoved();
    }

    /** Returns whether there are any files to be added or modified. */
    public static boolean haveFilesToBeAddedOrModified() {
        return hasAddOrModify;
    }

    /** Returns whether there are any files to be removed. */
    public static boolean haveFilesToBeRemoved() {
        return hasRemoved;
    }

    /* ---------------------------- private class & methods ---------------------------- */
    private static class StagedAndRemovedArea implements Serializable {
        /** Represents the pointer from files to blob */
        private Map<String, String> filesToAddOrModify;
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
    private static StagedAndRemovedArea getStagingArea() {
        return Utils.readObject(INDEX_FILE, StagedAndRemovedArea.class);
    }
}
