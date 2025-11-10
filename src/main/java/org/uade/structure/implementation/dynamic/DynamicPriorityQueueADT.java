package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.PriorityQueueADT;
import org.uade.exception.EmptyADTException;

public class DynamicPriorityQueueADT implements PriorityQueueADT {

    private static class Node {
        int value;
        int priority;
        Node next;
        Node(int v, int p) { this.value = v; this.priority = p; }
    }

    private Node head;
    private int size;
    private boolean initialized = true;

    @Override
    public int getElement() {
        if (isEmpty()) throw new EmptyADTException("La prioridad está vacía");
        return head.value;
    }

    @Override
    public int getPriority() {
        if (isEmpty()) throw new EmptyADTException("La prioridad está vacía");
        return head.priority;
    }

    @Override
    public void add(int value, int priority) {
        Node n = new Node(value, priority);
        if (head == null || priority > head.priority) {
            n.next = head;
            head = n;
        } else {
            Node prev = head, curr = head.next;
            while (curr != null && curr.priority >= priority) {
                prev = curr;
                curr = curr.next;
            }
            n.next = curr;
            prev.next = n;
        }
        size++;
    }

    @Override
    public void remove() {
        if (isEmpty()) throw new EmptyADTException("La prioridad está vacía");
        head = head.next;
        size--;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
