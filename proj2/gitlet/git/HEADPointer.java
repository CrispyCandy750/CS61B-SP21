package gitlet.git;

import java.io.File;
import java.io.IOException;

/** Represents the HEAD pointer and .gitlet/HEAD file */
public class HEADPointer {
    public final static File HEAD_FILE = Utils.join(GitRepo.GIT_REPO, "HEAD");

    public static void init(String branchName) {
        try {
            HEAD_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        moveToRefs(branchName);
    }

    /** Move the HEAD pointer to the commit. */
    public static void moveToCommit(String commitId) {
        Utils.writeContents(HEAD_FILE, commitId);
    }

    public static void moveToRefs(String refsName) {
        Utils.writeContents(HEAD_FILE, "refs: " + refsName);
    }
}
