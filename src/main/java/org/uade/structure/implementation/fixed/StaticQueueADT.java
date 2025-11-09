package org.uade.structure.implementation.fixed;

import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;
import org.uade.structure.definition.QueueADT;

public class StaticQueueADT implements QueueADT {

    private static final int CAPACITY = 100;
    private int[] elements;
    private int head, tail, size;
    private boolean initialized;

    public StaticQueueADT() {
        elements = new int[CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
        this.initialized = true;
    }

    @Override
    public int getElement() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La cola está vacía");
        }
        return elements[head];
    }

    @Override
    public void add(int value) throws FullADTException {
        if (size == CAPACITY) {
            throw new FullADTException("La cola está llena");
        }
        elements[tail] = value;
        tail = (tail + 1) % CAPACITY;
        size++;
    }

    @Override
    public void remove() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La cola está vacía");
        }
        head = (head + 1) % CAPACITY;
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
