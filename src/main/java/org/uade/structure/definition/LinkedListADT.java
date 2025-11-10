package org.uade.structure.definition;


public interface LinkedListADT {

    
    void add(int value);

    
    void insert(int index, int value);

    
    void remove(int index);

    
    int get(int index);

    
    int size();

    
    boolean isEmpty();
}
