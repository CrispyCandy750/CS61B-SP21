package gitlet.test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class FileTest {

    @Test
    public void createFileTest() throws IOException {
        File dir = new File("dir1");
        dir.mkdir();
        File file = new File("dir1/hello.txt");
        String content = "123";
        Utils.writeContents(file, content);
    }

    @Test
    public void createDir() throws IOException {
        File dir = new File("dir1");
        dir.mkdir();
    }

    @Test
    public void testFileName() {
        File file = new File("dir1/dir2");
        String actual = file.getName();
        String expect = "dir2";

        assertEquals(expect, actual);
    }
}
