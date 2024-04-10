package gitlet.git;

import java.io.File;

/** GitRepo class represents the .gitlet directory. */
public class GitRepo {

    /** The .getlet directory */
    public final static File GIT_REPO = Utils.join(Repository.CWD, ".getlet");
    public final static String DEFAULT_INITIAL_BRANCH = "master";

    /**
     * 1. creates the .git/object/ directory (Commit to do).
     * 2. creates the initial commit (Commit to do).
     * 2. creates the index file. (Staged Files to do).
     * 3. creates the HEAD files. (HEAD to do)
     * 4. creates the refs. (Refs to do).
     */
    public static void init() {
        GIT_REPO.mkdir();
        String initialCommitId = Commit.init();
        Blob.init();
        StagingArea.init();
        Reference.init(DEFAULT_INITIAL_BRANCH, initialCommitId);
        HEADPointer.init(DEFAULT_INITIAL_BRANCH);
    }
}
