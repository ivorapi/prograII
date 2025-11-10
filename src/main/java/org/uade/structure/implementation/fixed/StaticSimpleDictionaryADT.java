package org.uade.structure.implementation.fixed;

import org.uade.structure.definition.SimpleDictionaryADT;
import org.uade.structure.definition.SetADT;
import org.uade.exception.FullADTException;
import org.uade.exception.EmptyADTException;
import org.uade.exception.ElementNotFoundADTException;

public class StaticSimpleDictionaryADT implements SimpleDictionaryADT {

    private static final int CAPACITY = 100;
    private final int[] keys;
    private final int[] values;
    private int size;
    private boolean initialized;

    public StaticSimpleDictionaryADT() {
        this.keys = new int[CAPACITY];
        this.values = new int[CAPACITY];
        this.size = 0;
        this.initialized = true;
    }

    @Override
    public void add(int key, int value) {
        int idx = indexOfKey(key);
        if (idx != -1) {
            values[idx] = value;
            return;
        }
        if (size == CAPACITY) {
            throw new FullADTException("Diccionario lleno");
        }
        keys[size] = key;
        values[size] = value;
        size++;
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) return;
        int idx = indexOfKey(key);
        if (idx == -1) return;
        for (int i = idx; i < size - 1; i++) {
            keys[i] = keys[i + 1];
            values[i] = values[i + 1];
        }
        size--;
    }

    @Override
    public int get(int key) {
        if (isEmpty()) {
            throw new EmptyADTException("El diccionario está vacío");
        }
        int idx = indexOfKey(key);
        if (idx == -1) {
            throw new ElementNotFoundADTException("La clave no existe: " + key);
        }
        return values[idx];
    }

    @Override
    public SetADT getKeys() {
        SetADT set = new StaticSetADT();
        for (int i = 0; i < size; i++) {
            set.add(keys[i]);
        }
        return set;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int indexOfKey(int key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) return i;
        }
        return -1;
    }
}
