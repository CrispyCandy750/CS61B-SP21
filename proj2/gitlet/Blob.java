package gitlet;

import java.io.File;

/** Represents the gitlet blob object and manipulate all blob objects. */
public class Blob {

    public final static File BLOB_DIR = Utils.join(Utils.join(GitRepo.GIT_REPO, "objects"),
            "blobs");

    /** The hash code of content of the blob. */
    private String blobId;

    /** The content of the blob file. */
    private String content;

    public Blob(String content) {
        this.content = content;
        this.blobId = Utils.sha1(content);
    }

    /** Creates the .gitlet/objects/ directory. */
    public static void init() {
        BLOB_DIR.mkdirs();
    }

    /** Returns the blob saved in BLOB_DIR by id. */
    public static Blob fromBlobId(String blobId) {
        File blobFile = Utils.join(Utils.join(BLOB_DIR, blobId.substring(0, 2)),
                blobId.substring(2));
        String content = new String(Utils.readContents(blobFile));
        return new Blob(blobId, content);
    }

    /* ----------------------------------- instance methods ----------------------------------- */

    /** Returns the hash code of content of the blob. */
    public String getBlobId() {
        return blobId;
    }

    /** Save the blob in the objects directory with the name as its hashcode. */
    public void saveBlob() {
        String blobId = this.getBlobId();

        File blobDir = Utils.join(BLOB_DIR, blobId.substring(0, 2));
        if (!blobDir.exists()) {
            blobDir.mkdir();
        }

        File blobFile = Utils.join(blobDir, blobId.substring(2));
        Utils.writeContents(blobFile, this.content);
    }

    /** Returns the content. */
    public String getContent() {
        return content;
    }

    /* ----------------------------------- private methods ----------------------------------- */
    private Blob(String blobId, String content) {
        this.blobId = blobId;
        this.content = content;
    }
}
