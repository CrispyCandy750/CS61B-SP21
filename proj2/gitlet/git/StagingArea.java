package gitlet.git;

import java.io.File;
import java.io.IOException;

/** Represents the Staging Area. */
public class StagingArea {

    /** Represents the .gitlet/index file. */
    public final static File INDEX_FILE = Utils.join(GitRepo.GIT_REPO, "index");

    /** Initialization: Create the .gitlet/index file. */
    public static void init() {
        try {
            INDEX_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
