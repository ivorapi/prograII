package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.SetADT;
import org.uade.exception.EmptyADTException;


public class DynamicSetADT implements SetADT {

    private static class Node {
        int value;
        Node next;
        Node(int v) { this.value = v; }
    }

    private Node head;
    private int size;
    private long seed;

    public DynamicSetADT() {
        this.seed = (long) this.hashCode() * 31;
    }


    private int nextRandomInt(int bound) {
        seed = (seed * 1103515245 + 12345) & 0x7fffffffL;
        return (int) (seed % bound);
    }

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
        if (isEmpty()) throw new EmptyADTException("El conjunto está vacío");
        int idx = nextRandomInt(size);
        Node c = head;
        int i = 0;
        while (i < idx) {
            c = c.next;
            i++;
        }
        return c.value;
    }

    @Override
    public void add(int value) {
        if (exist(value)) return;
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
