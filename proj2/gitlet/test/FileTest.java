package gitlet.test;

import gitlet.test.Utils;
import org.junit.Test;

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
}
