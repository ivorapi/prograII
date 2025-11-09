package org.uade.structure.implementation.fixed;

import org.uade.exception.ElementNotFoundADTException;
import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;
import org.uade.structure.definition.SetADT;

import java.util.Random;

public class StaticSetADT implements SetADT {

    private static final int CAPACITY = 100;
    private int[] elements;
    private int size;
    private boolean initialized;

    public StaticSetADT() {
        elements = new int[CAPACITY];
        size = 0;
        this.initialized = true;
    }

    @Override
    public boolean exist(int value) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == value) return true;
        }
        return false;
    }

    @Override
    public int choose() throws EmptyADTException {
        if (isEmpty()) {
            throw new EmptyADTException("El conjunto está vacío");
        }
        Random rand = new Random();
        return elements[rand.nextInt(size)];
    }

    @Override
    public void add(int value) throws FullADTException {
        if (exist(value)) return;
        if (size == CAPACITY) {
            throw new FullADTException("El conjunto está lleno");
        }
        elements[size++] = value;
    }

    @Override
    public void remove(int element) throws ElementNotFoundADTException {
        for (int i = 0; i < size; i++) {
            if (elements[i] == element) {
                elements[i] = elements[size - 1];
                size--;
                return;
            }
        }
        throw new ElementNotFoundADTException("Elemento no encontrado: " + element);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


}
