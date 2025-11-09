package org.uade.structure.definition;


import org.uade.exception.ElementNotFoundADTException;
import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;

public interface SetADT {
    boolean exist(int value);
    int choose() throws EmptyADTException;
    void add(int value) throws FullADTException;
    void remove(int element) throws ElementNotFoundADTException;
    boolean isEmpty();
}
