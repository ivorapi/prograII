package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.BinaryTreeADT;

public class DynamicBinaryTreeADT implements BinaryTreeADT {

    private static class Node {
        int value;
        Node left, right;
        Node(int v) { this.value = v; }
    }

    private Node root;
    private boolean initialized;

    public DynamicBinaryTreeADT() {
        this.root = null;
        this.initialized = true;
    }

    // Constructor privado para envolver un subárbol existente
    private DynamicBinaryTreeADT(Node node, boolean _unused) {
        this.root = node;
        this.initialized = true;
    }

    @Override
    public int getRoot() {
        if (isEmpty()) throw new IllegalStateException("El árbol está vacío");
        return root.value;
    }

    @Override
    public BinaryTreeADT getLeft() {
        // Nunca null: si no hay hijo, devuelvo un árbol vacío
        return new DynamicBinaryTreeADT(root == null ? null : root.left, true);
    }

    @Override
    public BinaryTreeADT getRight() {
        return new DynamicBinaryTreeADT(root == null ? null : root.right, true);
    }

    @Override
    public void add(int value) {
        root = insert(root, value);
    }

    private Node insert(Node n, int v) {
        if (n == null) return new Node(v);
        if (v < n.value) n.left = insert(n.left, v);
        else n.right = insert(n.right, v); // duplicados a la derecha
        return n;
    }

    @Override
    public void remove(int value) {
        root = delete(root, value);
    }

    private Node delete(Node n, int v) {
        if (n == null) return null;
        if (v < n.value) {
            n.left = delete(n.left, v);
        } else if (v > n.value) {
            n.right = delete(n.right, v);
        } else {
            // encontrado
            if (n.left == null && n.right == null) return null;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            // dos hijos: sucesor (min del derecho)
            Node s = n.right;
            while (s.left != null) s = s.left;
            n.value = s.value;
            n.right = delete(n.right, s.value);
        }
        return n;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }
}

