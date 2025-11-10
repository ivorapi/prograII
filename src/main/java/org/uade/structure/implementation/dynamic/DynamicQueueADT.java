package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.QueueADT;
import org.uade.exception.EmptyADTException;

public class DynamicQueueADT implements QueueADT {

    private static class Node {
        int value;
        Node next;
        Node(int v) { this.value = v; }
    }

    private Node head; // sale
    private Node tail; // entra
    private int size;
    private boolean initialized = true;

    @Override
    public int getElement() {
        if (isEmpty()) throw new EmptyADTException("La cola está vacía");
        return head.value;
    }

    @Override
    public void add(int value) {
        Node n = new Node(value);
        if (tail == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    @Override
    public void remove() {
        if (isEmpty()) throw new EmptyADTException("La cola está vacía");
        head = head.next;
        if (head == null) tail = null;
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
