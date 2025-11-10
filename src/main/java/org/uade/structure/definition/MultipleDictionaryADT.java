package org.uade.structure.definition;


public interface MultipleDictionaryADT {

    
    void add(int key, int value);

    
    void remove(int key);

    
    int[] get(int key);

    
    SetADT getKeys();

    
    boolean isEmpty();

    
    void remove(int key, int value);
}
