package gitlet.git;

import java.io.File;

/** Represents the gitlet blob object and manipulate all blob objects. */
public class Blob {

    public final static File BLOB_DIR = Utils.join(GitRepo.GIT_REPO, "objects");

    /** The hash code of content of the blob. */
    private String blobId;

    /** Creates the .gitlet/objects/ directory. */
    public static void init() {
        BLOB_DIR.mkdir();
    }
}
