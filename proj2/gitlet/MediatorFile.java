package gitlet.git;

import java.io.File;

/** Represents the middle class between Repository and GitRepo. */
class MediatorFile {
    private File file;
    private String content;

    public MediatorFile(File file) {
        this.file = file;
        this.content = new String(Utils.readContents(file));
    }

    /** Returns the sha1 code of the content. */
    String sha1() {
        return Utils.sha1(content);
    }

    /** Returns the relative file name based on the CWD. */
    String getFileName() {
        return file.getName();
    }

    /** Returns the content of the file */
    String getContent() {
        return content;
    }
}
