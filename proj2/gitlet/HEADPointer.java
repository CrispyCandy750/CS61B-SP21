package gitlet;

import java.io.File;
import java.io.IOException;

/** Represents the HEAD pointer and .gitlet/HEAD file */
public class HEADPointer {
    public static final File HEAD_FILE = Utils.join(GitRepo.GIT_REPO, "HEAD");

    /** The prefix of head file content if the head points a reference. */
    public static final String PREFIX_REFS = "refs: ";

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
        Utils.writeContents(HEAD_FILE, PREFIX_REFS + refsName);
    }

    /** Returns the current commit. */
    public static Commit currentCommit() {
        return Commit.fromCommitId(currentCommitId());
    }

    /** Returns the id of the commit which the head points. */
    public static String currentCommitId() {
        String content = new String(Utils.readContents(HEAD_FILE));
        /*
        *//* Check the if the head points a reference *//*
        if (content.startsWith(PREFIX_REFS)) {
            String branch = content.substring(PREFIX_REFS.length());
            return Reference.furthestCommitId(branch);
        }

        *//* The head points a specific head. *//*
        return content;
        */
        return Reference.furthestCommitId(currentBranch());
    }

    /** Returns the current branch name. We suppose no detached head state. */
    public static String currentBranch() {
        String content = new String(Utils.readContents(HEAD_FILE));
        /*
        *//* Check the if the head points a reference *//*
        if (content.startsWith(PREFIX_REFS)) {
            String branch = content.substring(PREFIX_REFS.length());
            return Reference.furthestCommitId(branch);
        }
        */
        return content.substring(PREFIX_REFS.length());
    }

    /* ----------------------------------- private methods ----------------------------------- */
}
