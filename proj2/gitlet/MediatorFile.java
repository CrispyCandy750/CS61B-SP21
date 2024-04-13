package gitlet;

import java.io.File;

/** Represents the middle class between Repository and GitRepo. */
class MediatorFile {

    /** The file path based on the CWD. */
    private String fileName;
    private String content;

    public MediatorFile() {}

    public MediatorFile(String fileName) {
        this.fileName = fileName;
    }

    public MediatorFile(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    /** Returns the sha1 code of the content. */
    String sha1() {
        return Utils.sha1(content);
    }

    /** Returns the relative file name based on the CWD. */
    String getFileName() {
        return fileName;
    }

    /** Returns the content of the file */
    String getContent() {
        return content;
    }

    /** Set the content. */
    void setContent(String content) {
        this.content = content;
    }
}
