package org.uade.structure.implementation.fixed;

import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;
import org.uade.structure.definition.PriorityQueueADT;

public class StaticPriorityQueueADT implements PriorityQueueADT {

    private static final int DEFAULT_CAPACITY = 50;
    private int[] elements;
    private int[] priorities;
    private int size;

    public StaticPriorityQueueADT() {
        this.elements = new int[DEFAULT_CAPACITY];
        this.priorities = new int[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public int getElement() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La cola con prioridad está vacía");
        }
        return elements[0];
    }

    @Override
    public int getPriority() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La cola con prioridad está vacía");
        }
        return priorities[0];
    }

    @Override
    public void add(int value, int priority) throws FullADTException {
        if (size == elements.length) {
            throw new FullADTException("La cola con prioridad está llena");
        }

        int i = size - 1;
        while (i >= 0 && priorities[i] < priority) {
            elements[i + 1] = elements[i];
            priorities[i + 1] = priorities[i];
            i--;
        }

        elements[i + 1] = value;
        priorities[i + 1] = priority;
        size++;
    }

    @Override
    public void remove() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("La cola con prioridad está vacía");
        }
        for (int i = 0; i < size - 1; i++) {
            elements[i] = elements[i + 1];
            priorities[i] = priorities[i + 1];
        }
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
