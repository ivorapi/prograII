package org.uade.structure.implementation.fixed;

import org.uade.structure.definition.BinaryTreeADT;

public class StaticBinaryTreeADT implements BinaryTreeADT {

    private Integer root; // null si vacío
    private StaticBinaryTreeADT left;
    private StaticBinaryTreeADT right;
    private boolean initialized;

    public StaticBinaryTreeADT() {
        this.root = null;
        this.left = null;
        this.right = null;
        this.initialized = true;
    }

    @Override
    public int getRoot() {
        if (isEmpty()) {
            throw new IllegalStateException("El árbol está vacío");
        }
        return root;
    }

    @Override
    public BinaryTreeADT getLeft() {
        return left; // puede ser null si hoja o si está vacío
    }

    @Override
    public BinaryTreeADT getRight() {
        return right; // puede ser null si hoja o si está vacío
    }

    @Override
    public void add(int value) {
        if (root == null) {
            root = value;
            return;
        }
        if (value < root) {
            if (left == null) left = new StaticBinaryTreeADT();
            left.add(value);
        } else { // duplicados a la derecha
            if (right == null) right = new StaticBinaryTreeADT();
            right.add(value);
        }
    }

    @Override
    public void remove(int value) {
        // Borrar en BST: reemplazo por sucesor (mínimo del subárbol derecho) cuando tiene dos hijos
        delete(value);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    private StaticBinaryTreeADT delete(int value) {
        if (root == null) return this;

        if (value < root) {
            if (left != null) left = left.delete(value);
        } else if (value > root) {
            if (right != null) right = right.delete(value);
        } else {
            if (left == null && right == null) {
                clearNode();
                return this;
            } else if (left == null) {
                copyFrom(right);
                return this;
            } else if (right == null) {
                copyFrom(left);
                return this;
            } else {
                int succ = right.minValue();
                this.root = succ;
                right = right.delete(succ);
                return this;
            }
        }
        return this;
    }

    private int minValue() {
        StaticBinaryTreeADT curr = this;
        while (curr.left != null && curr.left.root != null) {
            curr = curr.left;
        }
        if (curr.root == null) {
            throw new IllegalStateException("Árbol inconsistente");
        }
        return curr.root;
    }

    private void clearNode() {
        this.root = null;
        this.left = null;
        this.right = null;
    }

    private void copyFrom(StaticBinaryTreeADT other) {
        if (other == null || other.root == null) {
            clearNode();
        } else {
            this.root = other.root;
            this.left = other.left;
            this.right = other.right;
        }
    }
}
