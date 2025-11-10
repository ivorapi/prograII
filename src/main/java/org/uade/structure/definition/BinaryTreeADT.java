package org.uade.structure.definition;


public interface BinaryTreeADT {

    

    int getRoot();

    
    BinaryTreeADT getLeft();

    
    BinaryTreeADT getRight();

    
    void add(int value);

    
    void remove(int value);

    
    boolean isEmpty();
}
