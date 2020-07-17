package ru.dankoy.otus.generics;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class DIYArrayList<E> implements List<E> {

    private final static int INITIAL_ARRAY_SIZE = 30;
    private Object[] array;

    public DIYArrayList() {
        array = new Object[INITIAL_ARRAY_SIZE];
    }

    /**
     * Конструктор, создающий ArrayList определенного размера. Если размер отрицательный или нулевой - выбрасывается
     * исключение
     *
     * @param arraySize
     */
    public DIYArrayList(int arraySize) {
        array = new Object[arraySize];
        if (arraySize > 0) {
            array = new Object[arraySize];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + arraySize);
        }
    }

    // Непонятно откуда забирается длина массива. Видимо подтягивается какая-то реализация length из Dummy массива?
    @Override
    public int size() {
        return array.length;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    /**
     * Метод добавляющий элемент в массив. Ищет первое вхождение null в массив и вставляет туда указанный элемент.
     * Нет проверок, на indexOutOfBound. (Objects.checkIndex(index, size);)
     *
     * @param e
     * @return
     */
    @Override
    public boolean add(E e) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = e;
                break;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    // Непонятно что за компаратор. Из класса @Collections передается null.
    @Override
    public void sort(Comparator<? super E> c) {
        Arrays.sort((E[]) array, 0, array.length, c);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        return (E) array[index];
    }

    @Override
    public E set(int index, E element) {
        if (index > this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            E oldElement = (E) array[index];
            array[index] = element;
            return oldElement;
        }
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DIYArrayListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    // Итератор
    private class DIYArrayListIterator implements ListIterator<E> {

        private int currentPlace = 0;
        private int lastReturnedIndex = -1;

        @Override
        public boolean hasNext() {

            if (currentPlace < size()) {
                return true;
            } else {
                return false;
            }

        }

        @Override
        public E next() {
            if (hasNext()) {
                Object element = array[currentPlace];
                lastReturnedIndex = currentPlace;
                currentPlace += 1;
                return (E) element;
            } else {
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            // крайние случаи здесь могут быть?
            DIYArrayList.this.set(lastReturnedIndex, e);

        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            throw new UnsupportedOperationException();
        }
    }


}