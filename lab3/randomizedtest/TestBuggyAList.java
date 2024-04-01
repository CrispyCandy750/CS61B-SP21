package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> rightList = new AListNoResizing<>();
        BuggyAList<Integer> buggyList = new BuggyAList<>();

        rightList.addLast(4);
        rightList.addLast(5);
        rightList.addLast(6);

        buggyList.addLast(4);
        buggyList.addLast(5);
        buggyList.addLast(6);

        assertEquals(rightList.size(), buggyList.size());

        assertEquals(rightList.removeLast(), buggyList.removeLast());
        assertEquals(rightList.removeLast(), buggyList.removeLast());
        assertEquals(rightList.removeLast(), buggyList.removeLast());
    }

    @Test
    public void randomizedCallsTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> buggyL = new BuggyAList<>();

        int N = 1000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                buggyL.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(L.size(), buggyL.size());
            } else if (operationNumber == 2 && L.size() > 0 && buggyL.size() > 0) {
                // getLast
                assertEquals(L.getLast(), buggyL.getLast());
            } else if (operationNumber == 3 && L.size() > 0 && buggyL.size() > 0) {
                assertEquals(L.removeLast(), buggyL.removeLast());
            }
        }
    }
}
