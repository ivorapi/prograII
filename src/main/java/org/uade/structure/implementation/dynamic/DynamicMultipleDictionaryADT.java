package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.MultipleDictionaryADT;
import org.uade.structure.definition.SetADT;

public class DynamicMultipleDictionaryADT implements MultipleDictionaryADT {

    private static class Node {
        int key;
        int value;
        Node next;
        Node(int k, int v) { this.key = k; this.value = v; }
    }

    private Node head;
    private int size;
    private boolean initialized;

    public DynamicMultipleDictionaryADT() {
        this.head = null;
        this.size = 0;
        this.initialized = true;
    }

    @Override
    public void add(int key, int value) {
        Node n = new Node(key, value);
        n.next = head;
        head = n;
        size++;
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) return;
        while (head != null && head.key == key) {
            head = head.next;
            size--;
        }
        Node prev = head, curr = (head == null) ? null : head.next;
        while (curr != null) {
            if (curr.key == key) {
                prev.next = curr.next;
                size--;
            } else {
                prev = curr;
            }
            curr = curr.next;
        }
    }

    @Override
    public void remove(int key, int value) {
        if (isEmpty()) return;
        while (head != null && head.key == key && head.value == value) {
            head = head.next;
            size--;
        }
        Node prev = head, curr = (head == null) ? null : head.next;
        while (curr != null) {
            if (curr.key == key && curr.value == value) {
                prev.next = curr.next;
                size--;
            } else {
                prev = curr;
            }
            curr = curr.next;
        }
    }

    @Override
    public int[] get(int key) {
        if (isEmpty()) throw new IllegalStateException("El diccionario está vacío");
        int count = 0;
        for (Node n = head; n != null; n = n.next) if (n.key == key) count++;
        if (count == 0) throw new IllegalStateException("La clave no existe: " + key);
        int[] out = new int[count];
        int i = 0;
        for (Node n = head; n != null; n = n.next) if (n.key == key) out[i++] = n.value;
        return out;
    }

    @Override
    public SetADT getKeys() {
        SetADT set = new DynamicSetADT();
        for (Node n = head; n != null; n = n.next) set.add(n.key);
        return set;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
