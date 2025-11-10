package org.uade.structure.definition;


public interface SimpleDictionaryADT {

    
    void add(int key, int value);

    
    void remove(int key);

    
    int get(int key);

    
    SetADT getKeys();

    
    boolean isEmpty();
}
