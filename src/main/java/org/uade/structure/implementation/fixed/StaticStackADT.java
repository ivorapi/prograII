package org.uade.structure.implementation.fixed;

import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;
import org.uade.structure.definition.StackADT;
public class StaticStackADT implements StackADT {

    private static final int CAPACITY = 100;
    private int[] elements;
    private int size;
    private boolean initialized;

    public StaticStackADT() {
        elements = new int[CAPACITY];
        size = 0;
        this.initialized = true;
    }

    @Override
    public int getElement() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La pila está vacía");
        }
        return elements[size - 1];
    }

    @Override
    public void add(int value) throws FullADTException {
        if (size == CAPACITY) {
            throw new FullADTException("La pila está llena");
        }
        elements[size++] = value;
    }

    @Override
    public void remove() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La pila está vacía");
        }
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
