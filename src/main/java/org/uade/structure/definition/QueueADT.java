package org.uade.structure.definition;

import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;

    public interface QueueADT {
        int getElement() throws EmptyADTException;
        void add(int value) throws FullADTException;
        void remove() throws EmptyADTException;
        boolean isEmpty();
    }

