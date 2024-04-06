package tester;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;
import static org.junit.Assert.*;

/** Test the probably buggy StudentArrayDeque with correct ArrayDeque Solution */
public class TestArrayDequeEC {

    @Test
    public void randomizedTest () {
        StudentArrayDeque<Integer> buggyDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> correctDeque = new ArrayDequeSolution<>();
        int N = 10000;

        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            switch (operationNumber) {
                case 0:  // addFirst
                    int addNumber = StdRandom.uniform(0, 100);
                    buggyDeque.addFirst(addNumber);
                    correctDeque.addFirst(addNumber);
                    break;
                case 1:  // addLast
                    addNumber = StdRandom.uniform(0, 100);
                    buggyDeque.addLast(addNumber);
                    correctDeque.addLast(addNumber);
                    break;
                case 2:  // removeFirst
                    if (!buggyDeque.isEmpty() && !correctDeque.isEmpty()) {
                        assertEquals(correctDeque.removeFirst(), buggyDeque.removeFirst());
                    }
                    break;
                case 3:  // removeLast
                    if (!buggyDeque.isEmpty() && !correctDeque.isEmpty()) {
                        assertEquals(correctDeque.removeLast(), buggyDeque.removeLast());
                    }
                    break;
            }
        }
    }
}
