package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.LinkedListADT;
import org.uade.exception.EmptyADTException;
import org.uade.exception.GenericADTException;

public class DynamicLinkedListADT implements LinkedListADT {

    private static class Node {
        int value;
        Node next;
        Node(int v) { this.value = v; }
    }

    private Node head;
    private Node tail;
    private int size;
    private boolean initialized = true;

    @Override
    public void add(int value) {
        Node n = new Node(value);
        if (tail == null) { // lista vacía
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    @Override
    public void insert(int index, int value) {
        if (index <= 0) {
            Node n = new Node(value);
            n.next = head;
            head = n;
            if (tail == null) tail = n;
            size++;
            return;
        }
        if (index >= size) {
            add(value); // insertar al final
            return;
        }
        Node prev = head;
        for (int i = 0; i < index - 1; i++) prev = prev.next;
        Node n = new Node(value);
        n.next = prev.next;
        prev.next = n;
        size++;
    }

    @Override
    public void remove(int index) {
        if (isEmpty()) throw new EmptyADTException("La lista está vacía");
        if (index < 0 || index >= size) throw new GenericADTException("Índice fuera de rango: " + index);

        if (index == 0) {
            head = head.next;
            if (head == null) tail = null;
            size--;
            return;
        }
        Node prev = head;
        for (int i = 0; i < index - 1; i++) prev = prev.next;
        Node target = prev.next;
        prev.next = target.next;
        if (target == tail) tail = prev;
        size--;
    }

    @Override
    public int get(int index) {
        if (isEmpty()) throw new EmptyADTException("La lista está vacía");
        if (index < 0 || index >= size) throw new GenericADTException("Índice fuera de rango: " + index);
        Node c = head;
        for (int i = 0; i < index; i++) c = c.next;
        return c.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
