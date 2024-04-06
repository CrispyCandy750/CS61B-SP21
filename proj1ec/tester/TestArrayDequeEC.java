package tester;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.Deque;
import student.StudentArrayDeque;

import static org.junit.Assert.*;

/** Test the probably buggy StudentArrayDeque with correct ArrayDeque Solution */
public class TestArrayDequeEC {

    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> buggyDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> correctDeque = new ArrayDequeSolution<>();

        int operationNumber = 0;

        StringBuilder operationSequence = new StringBuilder();

        int N = 10000;

        for (int i = 0; i < N; i++) {
            operationNumber = StdRandom.uniform(0, 5);
            switch (operationNumber) {
                case 0:  // addFirst
                    testAddFirst(buggyDeque, correctDeque, operationSequence);
                    break;
                case 1:  // addLast
                    testAddLast(buggyDeque, correctDeque, operationSequence);
                    break;
                case 2:  // removeFirst
                    testRemoveFirst(buggyDeque, correctDeque, operationSequence);
                    break;
                case 3:  // removeLast
                    testRemoveLast(buggyDeque, correctDeque, operationSequence);
                    break;
                case 4: // size
                    testSize(buggyDeque, correctDeque, operationSequence);
                    break;
            }
        }
    }

    private void testAddFirst(StudentArrayDeque buggyDeque, ArrayDequeSolution correctDeque, StringBuilder operationSequence) {
        int addNumber = StdRandom.uniform(0, 100);
        buggyDeque.addFirst(addNumber);
        correctDeque.addFirst(addNumber);

        String operation = "addFirst(" + addNumber + ")";
        operationSequence.append(operation + "\n");
    }

    private void testAddLast(StudentArrayDeque buggyDeque, ArrayDequeSolution correctDeque, StringBuilder operationSequence) {
        int addNumber = StdRandom.uniform(0, 100);
        buggyDeque.addLast(addNumber);
        correctDeque.addLast(addNumber);

        String operation = "addLast(" + addNumber + ")";
        operationSequence.append(operation + "\n");
    }

    private void testRemoveFirst(StudentArrayDeque buggyDeque, ArrayDequeSolution correctDeque, StringBuilder operationSequence) {
        if (!buggyDeque.isEmpty() && !correctDeque.isEmpty()) {

            String operation = "removeFirst()";
            operationSequence.append(operation + "\n");

            Object expect = correctDeque.removeFirst();
            Object actual = buggyDeque.removeFirst();

            assertEquals(operationSequence.toString(), expect, actual);
        }
    }

    private void testRemoveLast(StudentArrayDeque buggyDeque, ArrayDequeSolution correctDeque, StringBuilder operationSequence) {
        if (!buggyDeque.isEmpty() && !correctDeque.isEmpty()) {

            String operation = "removeLast()";
            operationSequence.append(operation + "\n");

            Object expect = correctDeque.removeLast();
            Object actual = buggyDeque.removeLast();

            assertEquals(operationSequence.toString(), expect, actual);
        }
    }

    private void testSize(StudentArrayDeque buggyDeque, ArrayDequeSolution correctDeque, StringBuilder operationSequence) {
        String operation = "size()";
        operationSequence.append(operation + "\n");

        Object expect = correctDeque.size();
        Object actual = buggyDeque.size();

        assertEquals(operationSequence.toString(), expect, actual);
    }
}
