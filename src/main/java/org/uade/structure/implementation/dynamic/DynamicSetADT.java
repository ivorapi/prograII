package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.SetADT;

import java.util.Random;

/**
 * Implementación dinámica de SetADT con lista enlazada simple.
 * IMPORTANTE: Esta clase debe ser CONCRETA (no abstract) para poder instanciarse
 * desde otras TDAs como el MultipleDictionary.
 */
public class DynamicSetADT implements SetADT {

    private static class Node {
        int value;
        Node next;
        Node(int v) { this.value = v; }
    }

    private Node head;
    private int size;
    private final Random rng = new Random();

    @Override
    public boolean exist(int value) {
        Node c = head;
        while (c != null) {
            if (c.value == value) return true;
            c = c.next;
        }
        return false;
    }

    @Override
    public int choose() {
        if (isEmpty()) throw new IllegalStateException("El conjunto está vacío");
        int idx = rng.nextInt(size); // 0..size-1
        Node c = head;
        for (int i = 0; i < idx; i++) c = c.next;
        return c.value;
    }

    @Override
    public void add(int value) {
        if (exist(value)) return; // evita duplicados
        Node n = new Node(value);
        n.next = head;
        head = n;
        size++;
    }

    @Override
    public void remove(int element) {
        if (isEmpty()) return;
        if (head.value == element) {
            head = head.next;
            size--;
            return;
        }
        Node prev = head, curr = head.next;
        while (curr != null) {
            if (curr.value == element) {
                prev.next = curr.next;
                size--;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
