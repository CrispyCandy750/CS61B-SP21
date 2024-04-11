package gitlet;

import java.io.File;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that contains no files and
     * has the commit message initial commit (just like that, with no punctuation).
     * It will have a single branch: master, which initially points to this initial commit, and
     * master will be the current branch.
     * The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in
     * whatever format you choose for dates (this is called "The (Unix) Epoch", represented
     * internally
     * by the time 0.)
     * Since the initial commit in all repositories created by Gitlet will have exactly the same
     * content,
     * it follows that all repositories will automatically share this commit (they will all have
     * the same UID)
     * and all commits in all repositories will trace back to it.
     */
    public static void init() {
        GitRepo.init();
    }


    /**
     * Adds a copy of the file as it currently exists to the staging area (see the description of
     * the commit command).
     * Staging an already-staged file overwrites the previous entry in the staging area with the
     * new contents.
     * The staging area should be somewhere in .gitlet.
     * If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it's original
     * version).
     */
    public static void add(String fileName) {
        File addedFile = Utils.join(CWD, fileName);
        if (!addedFile.exists()) {
            return;
        }
        GitRepo.add(new MediatorFile(addedFile));
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area so they can be
     * restored at a later time, creating a new commit.
     * The commit is said to be tracking the saved files.
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s
     * snapshot of files; it will keep versions of files exactly as they are, and not update them.
     * <p>
     * A commit will only update the contents of files it is tracking that have been staged for
     * addition at the time of commit, in which case the commit will now include the version of
     * the file that was staged instead of the version it got from its parent.
     * <p>
     * A commit will save and start tracking any files that were staged for addition but weren’t
     * tracked by its parent.
     * <p>
     * Finally, files tracked in the current commit may be untracked in the new commit as a result
     * being staged for removal by the rm command (below).
     */
    public static String commit(String message) {
        return GitRepo.commit(message);
    }
}
