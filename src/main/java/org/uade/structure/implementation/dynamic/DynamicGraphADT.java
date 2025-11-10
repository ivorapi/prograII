package org.uade.structure.implementation.dynamic;

import org.uade.structure.definition.GraphADT;
import org.uade.structure.definition.SetADT;

public class DynamicGraphADT implements GraphADT {

    private static class Edge {
        int to;
        int weight;
        Edge next;
        Edge(int t, int w) { this.to = t; this.weight = w; }
    }

    private static class Vertex {
        int id;
        Edge adj;
        Vertex next;
        Vertex(int id) { this.id = id; }
    }

    private Vertex vertices;
    private int verticesCount;
    private boolean initialized;

    public DynamicGraphADT() {
        this.vertices = null;
        this.verticesCount = 0;
        this.initialized = true;
    }

    @Override
    public SetADT getVertxs() {
        SetADT set = new DynamicSetADT();
        Vertex v = vertices;
        while (v != null) {
            set.add(v.id);
            v = v.next;
        }
        return set;
    }

    @Override
    public void addVertx(int vertex) {
        if (findVertex(vertex) != null) return;
        Vertex nv = new Vertex(vertex);
        nv.next = vertices;
        vertices = nv;
        verticesCount++;
    }

    @Override
    public void removeVertx(int vertex) {

        Vertex it = vertices;
        while (it != null) {
            if (it.id != vertex) removeEdgeFromAdj(it, vertex);
            it = it.next;
        }

        Vertex prev = null, curr = vertices;
        while (curr != null && curr.id != vertex) { prev = curr; curr = curr.next; }
        if (curr == null) return;
        if (prev == null) vertices = curr.next;
        else prev.next = curr.next;
        verticesCount--;
    }

    @Override
    public void addEdge(int vertxOne, int vertxTwo, int w) {
        if (vertxOne == vertxTwo) return;
        Vertex a = findVertex(vertxOne);
        Vertex b = findVertex(vertxTwo);
        if (a == null) { addVertx(vertxOne); a = findVertex(vertxOne); }
        if (b == null) { addVertx(vertxTwo); b = findVertex(vertxTwo); }

        Edge eab = findEdge(a.adj, vertxTwo);
        if (eab != null) eab.weight = w;
        else { Edge ne = new Edge(vertxTwo, w); ne.next = a.adj; a.adj = ne; }

        Edge eba = findEdge(b.adj, vertxOne);
        if (eba != null) eba.weight = w;
        else { Edge ne = new Edge(vertxOne, w); ne.next = b.adj; b.adj = ne; }
    }

    @Override
    public void removeEdge(int vertxOne, int vertxTwo) {
        Vertex a = findVertex(vertxOne);
        Vertex b = findVertex(vertxTwo);
        if (a == null || b == null) return;
        removeEdgeFromAdj(a, vertxTwo);
        removeEdgeFromAdj(b, vertxOne);
    }

    @Override
    public boolean existsEdge(int vertxOne, int vertxTwo) {
        Vertex a = findVertex(vertxOne);
        return a != null && findEdge(a.adj, vertxTwo) != null;
    }

    @Override
    public int edgeWeight(int vertxOne, int vertxTwo) {
        Vertex a = findVertex(vertxOne);
        if (a == null) return 0;
        Edge e = findEdge(a.adj, vertxTwo);
        return (e == null) ? 0 : e.weight;
    }

    @Override
    public boolean isEmpty() {
        return verticesCount == 0;
    }


    private Vertex findVertex(int id) {
        Vertex v = vertices;
        while (v != null) {
            if (v.id == id) return v;
            v = v.next;
        }
        return null;
    }

    private Edge findEdge(Edge head, int to) {
        Edge e = head;
        while (e != null) {
            if (e.to == to) return e;
            e = e.next;
        }
        return null;
    }

    private void removeEdgeFromAdj(Vertex v, int toRemove) {
        Edge prev = null, curr = v.adj;
        while (curr != null && curr.to != toRemove) { prev = curr; curr = curr.next; }
        if (curr == null) return;
        if (prev == null) v.adj = curr.next;
        else prev.next = curr.next;
    }
}
