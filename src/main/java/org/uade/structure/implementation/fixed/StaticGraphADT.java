package org.uade.structure.implementation.fixed;

import org.uade.structure.definition.GraphADT;
import org.uade.structure.definition.SetADT;
import org.uade.exception.GenericADTException;

public class StaticGraphADT implements GraphADT {

    private static final int CAPACITY = 100;

    private final boolean[] present;
    private final int[][] weight;
    private int verticesCount;
    private boolean initialized;

    public StaticGraphADT() {
        this.present = new boolean[CAPACITY];
        this.weight = new int[CAPACITY][CAPACITY];
        this.verticesCount = 0;
        this.initialized = true;
    }

    @Override
    public SetADT getVertxs() {
        SetADT set = new StaticSetADT();
        int v = 0;
        while (v < CAPACITY) {
            if (present[v]) set.add(v);
            v++;
        }
        return set;
    }

    @Override
    public void addVertx(int vertex) {
        checkVertexRange(vertex);
        if (!present[vertex]) {
            present[vertex] = true;
            verticesCount++;
        }
    }

    @Override
    public void removeVertx(int vertex) {
        checkVertexRange(vertex);
        if (!present[vertex]) return;

        int u = 0;
        while (u < CAPACITY) {
            weight[vertex][u] = 0;
            weight[u][vertex] = 0;
            u++;
        }
        present[vertex] = false;
        verticesCount--;
    }

    @Override
    public void addEdge(int vertxOne, int vertxTwo, int w) {
        checkVertexRange(vertxOne);
        checkVertexRange(vertxTwo);
        if (!present[vertxOne]) addVertx(vertxOne);
        if (!present[vertxTwo]) addVertx(vertxTwo);
        if (vertxOne == vertxTwo) return;
        weight[vertxOne][vertxTwo] = w;
        weight[vertxTwo][vertxOne] = w;
    }

    @Override
    public void removeEdge(int vertxOne, int vertxTwo) {
        checkVertexRange(vertxOne);
        checkVertexRange(vertxTwo);
        weight[vertxOne][vertxTwo] = 0;
        weight[vertxTwo][vertxOne] = 0;
    }

    @Override
    public boolean existsEdge(int vertxOne, int vertxTwo) {
        checkVertexRange(vertxOne);
        checkVertexRange(vertxTwo);
        return weight[vertxOne][vertxTwo] != 0;
    }

    @Override
    public int edgeWeight(int vertxOne, int vertxTwo) {
        checkVertexRange(vertxOne);
        checkVertexRange(vertxTwo);
        return weight[vertxOne][vertxTwo];
    }

    @Override
    public boolean isEmpty() {
        return verticesCount == 0;
    }

    private void checkVertexRange(int v) {
        if (v < 0 || v >= CAPACITY) {
            throw new GenericADTException("Vértice fuera de rango: " + v);
        }
    }
}
