package ru.ipccenter.dborody;

import java.lang.reflect.Array;
import java.util.*;

/**
 * My implementation of List interface: a double-linked list.
 */
public class MyList<E> implements List<E> {

    public MyList() {
        this.tail = new ListElement();
        this.head = new ListElement();

        this.tail.prev = this.head;
        this.head.next = this.tail;

        this.sizeModified = false;

        this.size = 0;
    }

    @Override
    public int size() {
        if (this.sizeModified) {
            int newSize = 0;
            Iterator<E> iterator = this.iterator();

            while (iterator.hasNext()) {
                newSize++;
                iterator.next();
            }

            this.size = newSize;
            this.sizeModified = false;
        }

        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        Iterator<E> iterator = this.iterator();

        while (iterator.hasNext()) {
            E element = iterator.next();
            if (o == null? element == null : o.equals(element))
                return true;
        }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyListIterator();
    }

    @Override
    public Object[] toArray() {
        Object array[] = new Object[this.size()];

        int index = 0;
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext())
            array[index++] = iterator.next();

        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] array = a;

        if (a.length < this.size())
            array = (T[]) Array.newInstance(a.getClass().getComponentType(), this.size);

        int index = 0;
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext())
            array[index++] = (T) iterator.next();

        if (index < array.length)
            array[index] = null;

        return array;
    }

    @Override
    public boolean add(E e) {
        ListElement newElement = new ListElement(e);

        newElement.next = this.tail;
        newElement.prev = this.tail.prev;
        this.tail.prev.next = newElement;
        this.tail.prev = newElement;

        this.checkAndSetSizeChanged();
        this.size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> iterator = this.iterator();

        while (iterator.hasNext()) {
            E e = iterator.next();
            if (o == null? e == null : o.equals(e)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> iterator = c.iterator();

        while (iterator.hasNext()) {
            if (!this.contains(iterator.next()))
                return false;
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty())
            return false;

        Iterator<?> iterator = c.iterator();

        final int n = c.size();
        for (int i = 0; i < n; i++) {
            this .add((E) iterator.next());
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException();

        if (c.isEmpty())
            return false;

        Iterator<?> iterator = c.iterator();

        ListElement currentElement = this.getElement(index).prev;

        final int n = c.size();
        for (int i = 0; i < n; i++) {
            ListElement newElement = new ListElement();

            newElement.data = (E) iterator.next();

            newElement.prev = currentElement;
            newElement.next = currentElement.next;
            currentElement.next.prev = newElement;
            currentElement.next = newElement;

            currentElement = newElement;

            this.size++;
            this.checkAndSetSizeChanged();
        }

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean wasModified = false;

        if (c == this) {
            wasModified = !this.isEmpty();
            this.clear();
            return wasModified;
        }

        Iterator<?> iterator = c.iterator();

        while (iterator.hasNext()) {
            if (this.remove((E) iterator.next()))
                wasModified = true;
        }

        return wasModified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.tail.prev = this.head;
        this.head.next = this.tail;
        this.size = 0;
        this.checkAndSetSizeChanged();
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException();

        ListElement element = this.getElement(index);

        return element.data;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException();

        ListElement listElement = this.getElement(index);

        E oldElement = listElement.data;
        listElement.data = element;
        return oldElement;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException();

        ListElement listElement = this.getElement(index).prev;

        ListElement newListElement = new ListElement(element);
        newListElement.prev = listElement;
        newListElement.next = listElement.next;
        listElement.next.prev = newListElement;
        listElement.next = newListElement;

        this.size++;
        this.checkAndSetSizeChanged();
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException();

        ListElement currentElement = this.head.next;

        int currentIndex = 0;
        while (currentIndex < index) {
            currentElement = currentElement.next;
            currentIndex++;
        }

        E oldElement = currentElement.data;

        currentElement.prev.next = currentElement.next;
        currentElement.next.prev = currentElement.prev;

        this.size--;
        this.checkAndSetSizeChanged();

        return oldElement;
    }

    @Override
    public int indexOf(Object o) {
        Iterator<E> iterator = this.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            E e = iterator.next();
            if ((o == null)? e == null : o.equals(e)) {
                return index;
            }

            index++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        ListIterator<E> iterator = this.listIterator(this.size() - 1);

        int index = this.size() - 1;
        while (iterator.hasPrevious()) {
            E e = iterator.previous();
            if ((o == null)? e == null : o.equals(e)) {
                return index;
            }

            index--;
        }

        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new MyListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new MyListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new MySubList(fromIndex, toIndex);
    }

    protected ListElement getElement(int index) {
        ListElement currentElement = null;

        if (index > (this.size() >> 1)) {
            currentElement = this.tail.prev;
            index = this.size() - 1 - index;

            while (index > 0) {
                currentElement = currentElement.prev;
                index--;
            }
        } else {
            currentElement = this.head.next;

            while (index > 0) {
                currentElement = currentElement.next;
                index--;
            }
        }

        return currentElement;
    }

    protected void checkAndSetSizeChanged() {

    }

    private class ListElement {

        private E data = null;
        private ListElement next = null;
        private ListElement prev = null;

        public ListElement() { }

        public ListElement(E data) {
            this.data = data;
        }
    }

    private class MyListIterator implements Iterator<E>, ListIterator<E> {

        private MyListIterator() {
            this.currentElement = MyList.this.head.next;
            this.previousElement = null;
            this.index = 0;
        }

        private MyListIterator(int index) {
            if (index < 0 || index >= MyList.this.size())
                throw new IndexOutOfBoundsException();

            this.currentElement = MyList.this.getElement(index);
            this.previousElement = null;
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return this.currentElement != MyList.this.tail;
        }

        @Override
        public E next() {
            E data = this.currentElement.data;
            this.previousElement = this.currentElement;
            this.currentElement = this.currentElement.next;
            this.index++;
            return data;
        }

        @Override
        public boolean hasPrevious() {
            return this.currentElement != MyList.this.head;
        }

        @Override
        public E previous() {
            E data = this.currentElement.data;
            this.previousElement = this.currentElement;
            this.currentElement = this.currentElement.prev;
            this.index--;
            return data;
        }

        @Override
        public int nextIndex() {
            return this.index + 1;
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
        public void remove() {
            if (this.previousElement == null)
                throw new IllegalStateException();

            this.previousElement.prev.next = this.previousElement.next;
            this.previousElement.next.prev = this.previousElement.prev;

            MyList.this.size--;
            MyList.this.checkAndSetSizeChanged();
        }

        @Override
        public void set(E e) {
            if (this.previousElement == null)
                throw new IllegalStateException();

            this.previousElement.data = e;
        }

        @Override
        public void add(E e) {
            ListElement newElement = new ListElement(e);
            newElement.prev = currentElement.prev;
            newElement.next = currentElement;
            currentElement.prev.next = newElement;
            currentElement.prev = newElement;
            MyList.this.size++;
            MyList.this.checkAndSetSizeChanged();
        }

        private int index;
        private ListElement currentElement;
        private ListElement previousElement;
    }

    private class MySubList extends MyList {

        @Override
        protected void checkAndSetSizeChanged() {
            MyList.this.sizeModified = true;
        }

        public MySubList(int fromIndex, int toIndex) {
            this.head = MyList.this.getElement(fromIndex).prev;
            this.tail = MyList.this.getElement(toIndex - 1).next;
            this.size = toIndex - fromIndex;
            this.sizeModified = false;
        }
    }

    protected ListElement tail;
    protected ListElement head;
    protected boolean sizeModified;
    protected int size;
}
