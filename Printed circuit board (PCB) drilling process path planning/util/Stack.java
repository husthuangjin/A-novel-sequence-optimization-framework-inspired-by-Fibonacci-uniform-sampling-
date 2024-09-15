package util;

import java.util.function.Consumer;

public class Stack<T> {

    private Object[] array = new Object[16];

    private int index = 0;

    public int getSize() {
        return index;
    }

    public boolean isEmpty() {
        return index == 0;
    }

    @SuppressWarnings("unchecked")
    public T top() {
        return (T) array[index - 1];
    }

    public void push(T elem) {
        if (index == array.length) {
            growStack();
        }
        array[index] = (Object) elem;
        index++;
    }

    public void clear() {
        while (index > 0) {
            index--;
            array[index] = null;
        }
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        return (T) array[--index];
    }

    @SuppressWarnings("unchecked")
    public void forEach(Consumer<T> c) {
        for (int i = 0; i < index; i++) {
            c.accept((T) array[i]);
        }
    }

    private void growStack() {
        Object[] newArray = new Object[index * 2];
        System.arraycopy(array, 0, newArray, 0, index);
        array = newArray;
    }

    @Override
    public String toString() {
        if (index == 0) {
            return "[]";
        }
        StringBuffer bf = new StringBuffer("[");
        for (int i = 0; i < index - 1; i++) {
            bf.append(array[i]);
            bf.append(", ");
        }
        bf.append(array[index-1]);
        bf.append("]");
        return bf.toString();
    }
}