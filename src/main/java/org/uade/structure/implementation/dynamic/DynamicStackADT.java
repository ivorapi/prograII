package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.StackADT;
import org.uade.exception.EmptyADTException;

public class DynamicStackADT implements StackADT {

    private static class Node {
        int value;
        Node next;
        Node(int v) { this.value = v; }
    }

    private Node top;
    private int size;
    private boolean initialized = true;

    @Override
    public int getElement() {
        if (isEmpty()) throw new EmptyADTException("La pila está vacía");
        return top.value;
    }

    @Override
    public void add(int value) {
        Node n = new Node(value);
        n.next = top;
        top = n;
        size++;
    }

    @Override
    public void remove() {
        if (isEmpty()) throw new EmptyADTException("La pila está vacía");
        top = top.next;
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
