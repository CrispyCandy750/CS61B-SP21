package gitlet.test;

import org.junit.Test;

import java.io.File;
import java.io.Serializable;

public class InnerClassTest implements Serializable{

    public static void save() {
        File file = new File("test");
        A a = new A();
        Utils.writeObject(file, a);
    }

    private static class A implements Serializable {
        int a;
        public A() {
            a = 10;
        }
    }

    @Test
    public void testInnerClass() {
        save();
    }
}
