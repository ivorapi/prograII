package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.SimpleDictionaryADT;
import org.uade.structure.definition.SetADT;

public class DynamicSimpleDictionaryADT implements SimpleDictionaryADT {

    private static class Node {
        int key;
        int value;
        Node next;
        Node(int k, int v) { this.key = k; this.value = v; }
    }

    private Node head;
    private int size;
    private boolean initialized;

    public DynamicSimpleDictionaryADT() {
        this.head = null;
        this.size = 0;
        this.initialized = true;
    }

    @Override
    public void add(int key, int value) {
        Node n = head;
        while (n != null) {
            if (n.key == key) {
                n.value = value; // pisa si existe
                return;
            }
            n = n.next;
        }
        Node nuevo = new Node(key, value);
        nuevo.next = head;
        head = nuevo;
        size++;
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) return;
        if (head.key == key) {
            head = head.next;
            size--;
            return;
        }
        Node prev = head, curr = head.next;
        while (curr != null) {
            if (curr.key == key) {
                prev.next = curr.next;
                size--;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    @Override
    public int get(int key) {
        if (isEmpty()) throw new IllegalStateException("El diccionario está vacío");
        Node n = head;
        while (n != null) {
            if (n.key == key) return n.value;
            n = n.next;
        }
        throw new IllegalStateException("La clave no existe: " + key);
    }

    @Override
    public SetADT getKeys() {
        SetADT set = new DynamicSetADT();
        Node n = head;
        while (n != null) {
            set.add(n.key);
            n = n.next;
        }
        return set;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
