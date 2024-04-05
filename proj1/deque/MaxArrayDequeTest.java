package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {


    /** Test the max(Comparator<T> c) interface. */
    @Test
    public void maxWithComparator() {
        MaxArrayDeque<Teacher> studentDeque = new MaxArrayDeque<>(null);
        Teacher nameMax = new Teacher("zed", 1, 150, 120);
        Teacher ageMax = new Teacher("abc", 4, 143, 130);
        Teacher heightMax = new Teacher("bcd", 3, 180, 140);
        Teacher weightMax = new Teacher("def", 2, 160, 180);
        studentDeque.addFirst(nameMax);
        studentDeque.addFirst(ageMax);
        studentDeque.addLast(heightMax);
        studentDeque.addLast(weightMax);

        assertEquals(nameMax, studentDeque.max(Teacher.getNameComparator()));
        assertEquals(ageMax, studentDeque.max(Teacher.getAgeComparator()));
        assertEquals(heightMax, studentDeque.max(Teacher.getHeightComparator()));
        assertEquals(weightMax, studentDeque.max(Teacher.getWeightComparator()));

        MaxArrayDeque<Teacher> emptyDeque = new MaxArrayDeque<>(null);
        assertNull("the returns should be null because the deque is empty", emptyDeque.max(Teacher.getNameComparator()));
    }


    /** Test the max() interface. */
    @Test
    public void max() {
        MaxArrayDeque<Teacher> teacherDeque = new MaxArrayDeque<>(Teacher.getNameComparator());
        Teacher nameMax = new Teacher("zed", 1, 150, 120);
        Teacher ageMax = new Teacher("abc", 4, 143, 130);
        Teacher heightMax = new Teacher("bcd", 3, 180, 140);
        Teacher weightMax = new Teacher("def", 2, 160, 180);
        teacherDeque.addFirst(nameMax);
        teacherDeque.addFirst(ageMax);
        teacherDeque.addLast(heightMax);
        teacherDeque.addLast(weightMax);

        assertEquals(nameMax, teacherDeque.max());

        MaxArrayDeque<Teacher> emptyDeque = new MaxArrayDeque<>(Teacher.getNameComparator());
        assertNull("the returns should be null because the deque is empty", emptyDeque.max());
    }

    private static class Teacher {
        String name;
        int age;
        int height;
        double weight;

        public Teacher(String name, int age, int height, double weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }

        /** Returns comparator based on name. */
        static Comparator<Teacher> getNameComparator() {
            return new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    return o1.name.compareTo(o2.name);
                }
            };
        }

        /** Returns comparator based on age. */
        static Comparator<Teacher> getAgeComparator() {
            return new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    return Integer.compare(o1.age, o2.age);
                }
            };
        }

        /** Returns comparator based on height. */
        static Comparator<Teacher> getHeightComparator() {
            return new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    return Integer.compare(o1.height, o2.height);
                }
            };
        }

        /** Returns comparator based on weight. */
        static Comparator<Teacher> getWeightComparator() {
            return new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    return Double.compare(o1.weight, o2.weight);
                }
            };
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof Teacher)) {
                return false;
            }

            Teacher otherTeacher = (Teacher) obj;
            return this.name.equals(otherTeacher.name) && this.age == otherTeacher.age
                    && this.height == otherTeacher.height && this.weight == otherTeacher.weight;
        }
    }
}