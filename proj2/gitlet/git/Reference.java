package gitlet.git;

import java.io.File;
import java.io.IOException;

/** Represents the pointer of branch. */
public class Reference {

    /** Represents .gitlet/refs directory */
    public final static File REF_DIR = Utils.join(Utils.join(GitRepo.GIT_REPO, "refs"), "head");

    /** Creates the refs/head directory and the master branch. */
    public static void init(String initialBranch, String initialCommitId) {
        REF_DIR.mkdirs();
        createNewBranch(initialBranch, initialCommitId);
    }

    /** Create new branch pointing the commit. */
    public static void createNewBranch(String branchName, String commitId) {
        File branch = Utils.join(REF_DIR, branchName);
        Utils.writeContents(branch, commitId);
    }
}
