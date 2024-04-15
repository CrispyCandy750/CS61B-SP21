package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/** Represents the Staging Area. */
public class StagingArea {

    /** Represents the .gitlet/index file. */
    public final static File INDEX_FILE = Utils.join(GitRepo.GIT_REPO, "index");
    private static StagedAndRemovedArea stagedAndRemovedArea = null;

    /** Initialization: Create the .gitlet/index file. */
    public static void init() {
        clear();
    }

    /** Add the file and content in the staged area. */
    public static void addToStagedArea(String fileName, String content) {
        addToStagedArea(new MediatorFile(fileName, content));
    }

    /** Add the mediator file in the staged area. */
    public static void addToStagedArea(MediatorFile file) {
        addAllToStagedArea(Arrays.asList(file));
    }

    /** Add all the files to the staged area. */
    public static void addAllToStagedArea(Collection<MediatorFile> files) {
        addAllToStagedAndRemovedArea(files, null);
    }

    /** Add the file to the removed area. */
    public static void addToRemovedArea(String fileName) {
        addAllToRemovedArea(Arrays.asList(fileName));
    }

    public static void addAllToRemovedArea(Collection<String> filesToRemove) {
        addAllToStagedAndRemovedArea(null, filesToRemove);
    }

    public static void addAllToStagedAndRemovedArea(
            Collection<MediatorFile> filesToAddToStagedArea,
            Collection<String> filesToAddToRemovedArea) {

        stagedAndRemovedArea = getStagedAndRemovedArea();

        /* Add to staged file. */
        if (filesToAddToStagedArea != null) {
            for (MediatorFile fileToAddToStagedArea : filesToAddToStagedArea) {
                Blob blob = new Blob(fileToAddToStagedArea.getContent());
                blob.saveBlob();
                stagedAndRemovedArea.addToStagedArea(fileToAddToStagedArea.getFileName(),
                        blob.getBlobId());
            }
        }

        if (filesToAddToRemovedArea != null) {
            for (String fileToAddToRemovedArea : filesToAddToRemovedArea) {
                stagedAndRemovedArea.addToRemovedArea(fileToAddToRemovedArea);
            }
        }

        stagedAndRemovedArea.save();
    }

    /** Clear the mapping with empty StagedAndRemovedArea object. */
    public static void clear() {
        stagedAndRemovedArea = new StagedAndRemovedArea();
        stagedAndRemovedArea.save();
    }

    /** Returns whether there are any files to be changed. */
    public static boolean haveStagedOrRemovedFiles() {
        return haveFilesInStagedArea() || haveFilesInRemovedArea();
    }

    /** Returns whether there are any files to be added or modified. */
    public static boolean haveFilesInStagedArea() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return !stagedAndRemovedArea.filesToAddOrModify.isEmpty();
    }

    /** Returns whether there are any files to be removed. */
    public static boolean haveFilesInRemovedArea() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return !stagedAndRemovedArea.filesToRemove.isEmpty();
    }

    /** Returns true if the file is in staged area to be added or modified. */
    public static boolean inStagedArea(String fileName) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return stagedAndRemovedArea.filesToAddOrModify.containsKey(fileName);
    }

    /** Returns true if the file is in staged area and content is identical */
    public static boolean inStagedArea(String fileName, String content) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        String blobId = stagedAndRemovedArea.filesToAddOrModify.getOrDefault(fileName, null);
        return blobId != null && blobId.equals(Utils.sha1(content));
    }

    /** Returns true if the file is in removed area. */
    public static boolean inRemovedArea(String fileName) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return stagedAndRemovedArea.filesToRemove.contains(fileName);
    }

    /** Remove the file from the staged area. */
    public static void removeFromStagedArea(String fileName) {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        stagedAndRemovedArea.filesToAddOrModify.remove(fileName);
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
        for (String fileName : filesToRemove) {
            commit.remove(fileName);
        }
    }

    /**
     * Returns the status of staged files
     * Example:
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     */
    public static String statusOfStagedFiles() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        ArrayList<String> stagedFiles =
                new ArrayList<>(stagedAndRemovedArea.filesToAddOrModify.keySet());

        Collections.sort(stagedFiles);

        return Utils.FormatStrings(stagedFiles, "=== Staged Files ===");
    }

    /**
     * Returns the status of removed files
     * Example:
     * === Removed Files ===
     * goodbye.txt
     */
    public static String statusOfRemovedFiles() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        ArrayList<String> removedFiles = new ArrayList<>(stagedAndRemovedArea.filesToRemove);

        Collections.sort(removedFiles);
        return Utils.FormatStrings(removedFiles, "=== Removed Files ===");
    }

    /** Returns all names of staged file. */
    public static Set<String> getStagedFiles() {
        stagedAndRemovedArea = getStagedAndRemovedArea();
        return stagedAndRemovedArea.filesToAddOrModify.keySet();
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
        void addToStagedArea(String fileName, String blobId) {
            filesToAddOrModify.put(fileName, blobId);
        }

        /** Add the file to removed area. */
        void addToRemovedArea(String fileName) {
            filesToRemove.add(fileName);
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
