package gitlet;

import java.io.File;

/** Represents the middle class between Repository and GitRepo. */
class MediatorFile {

    /** The file path based on the CWD. */
    private String fileName;
    private File file;
    private String content;

    public MediatorFile(String fileName) {
        this.fileName = fileName;
    }

    public MediatorFile(File file, String fileName) {
        this.fileName = fileName;
        this.file = file;
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
        if (content == null) {
            content = new String(Utils.readContents(file));
        }
        return content;
    }

    /** Set the content. */
    void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof MediatorFile)) {
            return false;
        }
        MediatorFile o = (MediatorFile) obj;
        return this.fileName.equals(o.fileName)
                && this.getContent().equals(o.getContent());
    }
}
