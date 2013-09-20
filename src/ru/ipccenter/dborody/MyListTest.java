package ru.ipccenter.dborody;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Tests for MyList class.
 */
public class MyListTest {

    @Test
    public void testAddRemove() {
        MyList<Integer> l = new MyList<Integer>();

        Assert.assertTrue(l.add(2));
        Assert.assertTrue(l.add(3));
        Assert.assertTrue(l.add(4));
        Assert.assertTrue(l.add(5));
        Assert.assertTrue(l.add(6));

        Integer[] a = l.<Integer>toArray(new Integer[0]);
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{2, 3, 4, 5, 6}));

        l.remove(0);
        l.remove(3);
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{3, 4, 5}));

        Assert.assertTrue(l.remove(new Integer(4)));
        Assert.assertFalse(l.remove(new Integer(4)));
        Assert.assertFalse(l.remove(new Integer(666)));
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{3, 5}));

        l = new MyList<Integer>();
        l.add(1);
        l.add(3);
        l.add(1, 2);
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{1, 2, 3}));

        try {
            l.add(999, 42);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {

        }

        Assert.assertTrue(l.removeAll(l)) ;
    }

    @Test
    public void testAddSelf() {
        MyList<Integer> l = new MyList<Integer>();

        Assert.assertTrue(l.add(1));
        Assert.assertTrue(l.add(2));
        Assert.assertTrue(l.add(3));

        l.addAll(l);

        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{1, 2, 3, 1, 2, 3}));
    }

    @Test
    public void testGetSet() {
        MyList<Integer> l = new MyList<Integer>();

        Assert.assertTrue(l.add(2));
        Assert.assertTrue(l.add(3));

        try {
            l.get(999);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            l.set(999, 42);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {

        }
    }

    @Test
    public void testIterator() {
        MyList<Integer> l = new MyList<Integer>();

        for (int i = 0; i < 10; ++i)
            l.add(i);

        Iterator<Integer> it = l.iterator();
        while (it.hasNext()) {
            int el = it.next();
            if (el % 2 == 0)
                it.remove();
        }

        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{1, 3, 5, 7, 9}));

        l = new MyList<Integer>();
        l.add(3);
        it = l.iterator();
        try {
            it.remove();
            Assert.fail();
        } catch (IllegalStateException e) {

        }

        l = new MyList<Integer>();
        l.add(3);
        ListIterator<Integer> lit = l.listIterator();
        try {
            lit.set(42);
            Assert.fail();
        } catch (IllegalStateException e) {

        }

        try {
            l.listIterator(999);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {

        }

        lit = l.listIterator();
        lit.next();
        lit.set(42);
        Assert.assertTrue(lit.hasPrevious());
        Assert.assertEquals(42, (int) l.get(0));

        l = new MyList<Integer>();
        lit = l.listIterator();
        lit.add(666);
        lit.add(42);
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[] {666, 42}));

        lit = l.listIterator(1);
        Assert.assertEquals(2, lit.nextIndex());
        Assert.assertEquals(0, lit.previousIndex());

        lit = l.listIterator(l.size() - 1);
        while (lit.hasPrevious()) {
            lit.previous();
            lit.remove();
        }

        Assert.assertTrue(l.isEmpty());
    }

    @Test
    public void testToArray() {
        MyList<Integer> l = new MyList<Integer>();

        for (int i = 0; i < 5; ++i)
            l.add(i);

        Assert.assertTrue(Arrays.equals(l.toArray(), new Integer[]{0, 1, 2, 3, 4}));

        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[]{-1, -1, -1, -1, -1, -1, -1}), new Integer[]{0, 1, 2, 3, 4, null, -1}));
    }

    @Test
    public void testContainsAll() {
        MyList<Integer> l = new MyList<Integer>();

        for (int i = 0; i < 5; ++i)
            l.add(i);

        Assert.assertTrue(l.containsAll(Arrays.asList(new Integer[] {0, 2})));
        Assert.assertTrue(l.containsAll(Arrays.asList(new Integer[] {})));
        Assert.assertFalse(l.containsAll(Arrays.asList(new Integer[]{-1})));
    }

    @Test
    public void testIndexOf() {
        MyList<Integer> l = new MyList<Integer>();

        for (int i = 0; i < 10; ++i)
            l.add(i);

        for (int i = 9; i >= 0; --i)
            l.add(i);

        for (int i = 0; i < 10; ++i) {
            Assert.assertEquals(i, l.indexOf(i));
            Assert.assertEquals(19 - i, l.lastIndexOf(i));
        }

        Assert.assertEquals(-1, l.indexOf(666));
        Assert.assertEquals(-1, l.lastIndexOf(666));
    }

    @Test
    public void testAddAllRemoveAll() {
        MyList<Integer> l = new MyList<Integer>();

        Assert.assertFalse(l.addAll(Arrays.asList(new Integer[] {})));
        Assert.assertTrue(l.addAll(Arrays.asList(new Integer[] {1, 2, 5})));

        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[] {1, 2, 5}));

        Assert.assertTrue(l.addAll(2, Arrays.asList(new Integer[] {3, 4})));
        Assert.assertFalse(l.addAll(0, Arrays.asList(new Integer[] {})));

        try {
            l.addAll(999, Arrays.asList(new Integer[] {}));
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {

        }

        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[] {1, 2, 3, 4, 5}));

        Assert.assertFalse(l.removeAll(Arrays.asList(new Integer[]{})));
        Assert.assertTrue(l.removeAll(Arrays.asList(new Integer[]{1, 3, 5, -1, 666})));
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[] {2, 4}));
    }

    @Test
    public void testSwap() {
        MyList<Integer> l = new MyList<Integer>();

        for (int i = 0; i < 10; ++i)
            l.add(i);

        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        Collections.swap(l, 0, 9);
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{9, 1, 2, 3, 4, 5, 6, 7, 8, 0}));
    }

    @Test
    public void testSublist() {
        MyList<Integer> l = new MyList<Integer>();

        for (int i = 0; i < 10; ++i)
            l.add(i);

        Assert.assertTrue(Arrays.equals(l.subList(0, 10).toArray(new Integer[0]), new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        Assert.assertTrue(Arrays.equals(l.subList(9, 10).toArray(new Integer[0]), new Integer[]{9}));
        Assert.assertTrue(Arrays.equals(l.subList(0, 2).toArray(new Integer[0]), new Integer[]{0, 1}));
        Assert.assertTrue(Arrays.equals(l.subList(9, 9).toArray(new Integer[0]), new Integer[]{}));

        l.subList(0, 2).clear();
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{2, 3, 4, 5, 6, 7, 8, 9}));
        l.subList(7, 8).clear();
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{2, 3, 4, 5, 6, 7, 8}));
        l.subList(1, 2).clear();
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{2, 4, 5, 6, 7, 8}));

        Collections.reverse(l.subList(0, 6));
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{8, 7, 6, 5, 4, 2}));
        Collections.reverse(l.subList(1, 3));
        Assert.assertTrue(Arrays.equals(l.toArray(new Integer[0]), new Integer[]{8, 6, 7, 5, 4, 2}));
    }

    @Test
    public void testClear() {
        MyList<Integer> l = new MyList<Integer>();

        Assert.assertTrue(l.isEmpty());
        for (int i = 0; i < 1000; ++i) {
            l.add(i);
        }
        Assert.assertFalse(l.isEmpty());

        l.clear();

        Assert.assertTrue(l.isEmpty());
        Assert.assertEquals(0, l.size());
    }

    @Test
    public void testSize() {
        MyList<Integer> l = new MyList<Integer>();

        Assert.assertEquals(0, l.size());
        for (int i = 0; i < 1000; ++i) {
            l.add(i);
        }
        Assert.assertEquals(1000, l.size());
    }

    @Test
    public void testContains() {
        MyList<Integer> l = new MyList<Integer>();
        l.add(42);

        Assert.assertFalse(l.isEmpty());
        Assert.assertTrue(l.contains(42));

        l.add(666);

        Assert.assertTrue(l.contains(42));
        Assert.assertTrue(l.contains(666));
        Assert.assertFalse(l.contains(-1));

        l.remove(new Integer(42));

        Assert.assertFalse(l.contains(42));

        l.remove(l.indexOf(666));

        Assert.assertFalse(l.contains(666));
        Assert.assertEquals(0, l.size());
        Assert.assertTrue(l.isEmpty());
    }
}
