package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {


    /** Test the max(Comparator<T> c) interface. */
    @Test
    public void maxWithComparator() {
        MaxArrayDeque<Student> studentDeque = new MaxArrayDeque<>(null);
        Student nameMax = new Student("zed", 1, 150, 120);
        Student ageMax = new Student("abc", 4, 143, 130);
        Student heightMax = new Student("bcd", 3, 180, 140);
        Student weightMax = new Student("def", 2, 160, 180);
        studentDeque.addFirst(nameMax);
        studentDeque.addFirst(ageMax);
        studentDeque.addLast(heightMax);
        studentDeque.addLast(weightMax);

        assertEquals(nameMax, studentDeque.max(Student.getNameComparator()));
        assertEquals(ageMax, studentDeque.max(Student.getAgeComparator()));
        assertEquals(heightMax, studentDeque.max(Student.getHeightComparator()));
        assertEquals(weightMax, studentDeque.max(Student.getWeightComparator()));

        MaxArrayDeque<Student> emptyDeque = new MaxArrayDeque<>(null);
        assertNull("the returns should be null because the deque is empty", emptyDeque.max(Student.getNameComparator()));
    }


    /** Test the max() interface. */
    @Test
    public void max() {
        MaxArrayDeque<Student> studentDeque = new MaxArrayDeque<>(Student.getNameComparator());
        Student nameMax = new Student("zed", 1, 150, 120);
        Student ageMax = new Student("abc", 4, 143, 130);
        Student heightMax = new Student("bcd", 3, 180, 140);
        Student weightMax = new Student("def", 2, 160, 180);
        studentDeque.addFirst(nameMax);
        studentDeque.addFirst(ageMax);
        studentDeque.addLast(heightMax);
        studentDeque.addLast(weightMax);

        assertEquals(nameMax, studentDeque.max());

        MaxArrayDeque<Student> emptyDeque = new MaxArrayDeque<>(Student.getNameComparator());
        assertNull("the returns should be null because the deque is empty", emptyDeque.max());
    }

    private static class Student {
        String name;
        int age;
        int height;
        double weight;

        public Student(String name, int age, int height, double weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }

        /** Returns comparator based on name. */
        static Comparator<Student> getNameComparator() {
            return new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o1.name.compareTo(o2.name);
                }
            };
        }

        /** Returns comparator based on age. */
        static Comparator<Student> getAgeComparator() {
            return new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return Integer.compare(o1.age, o2.age);
                }
            };
        }

        /** Returns comparator based on height. */
        static Comparator<Student> getHeightComparator() {
            return new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return Integer.compare(o1.height, o2.height);
                }
            };
        }

        /** Returns comparator based on weight. */
        static Comparator<Student> getWeightComparator() {
            return new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return Double.compare(o1.weight, o2.weight);
                }
            };
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof Student)) {
                return false;
            }

            Student otherStudent = (Student) obj;
            return this.name.equals(otherStudent.name) && this.age == otherStudent.age
                    && this.height == otherStudent.height && this.weight == otherStudent.weight;
        }
    }
}