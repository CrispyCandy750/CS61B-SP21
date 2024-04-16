package gitlet;

import java.io.File;
import java.util.List;

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
    public static String createNewBranch(String branchName, String commitId) {
        if (containsBranch(branchName)) {
            return Message.BRANCH_HAS_EXISTED_MESSAGE;
        }
        moveBranch(branchName, commitId);
        return null;
    }

    /** Returns the furthest commit in the specific branch. */
    public static Commit furthestCommit(String branch) {
        return Commit.fromCommitId(furthestCommitId(branch));
    }

    /** Returns the hash id of the furthest commit in the branch. */
    public static String furthestCommitId(String branch) {
        File branchFile = Utils.join(REF_DIR, branch);
        return new String(Utils.readContents(branchFile));
    }

    /** Move the branch pointer to the new commit. */
    public static void moveBranch(String branch, String commitId) {
        File branchFile = Utils.join(REF_DIR, branch);
        Utils.writeContents(branchFile, commitId);
    }

    /** Returns true if the branch exists, false otherwise. */
    public static boolean containsBranch(String branch) {
        File branchFile = Utils.join(REF_DIR, branch);
        return branchFile.exists();
    }

    /** Returns all branches. */
    public static List<String> branches() {
        return Utils.plainFilenamesIn(REF_DIR);
    }

    /** Remove the branch. */
    public static void removeBranch(String branchName) {
        File branchFile = Utils.join(REF_DIR, branchName);
        branchFile.delete();
    }
}
