package org.uade.structure.definition;


public interface GraphADT {

    
    SetADT getVertxs();

    
    void addVertx(int vertex);

    
    void removeVertx(int vertex);

    
    void addEdge(int vertxOne, int vertxTwo, int weight);

    
    void removeEdge(int vertxOne, int vertxTwo);

    
    boolean existsEdge(int vertxOne, int vertxTwo);

    
    int edgeWeight(int vertxOne, int vertxTwo);

    
    boolean isEmpty();
}
