package org.uade.structure.implementation.fixed;

import org.uade.structure.definition.MultipleDictionaryADT;
import org.uade.structure.definition.SetADT;
import org.uade.exception.FullADTException;
import org.uade.exception.EmptyADTException;
import org.uade.exception.ElementNotFoundADTException;

public class StaticMultipleDictionaryADT implements MultipleDictionaryADT {

    private static final int CAPACITY = 100;

    // Representación simple como “lista de pares (key, value)”, permite duplicados
    private final int[] keys;
    private final int[] values;
    private int size;
    private boolean initialized;

    public StaticMultipleDictionaryADT() {
        this.keys = new int[CAPACITY];
        this.values = new int[CAPACITY];
        this.size = 0;
        this.initialized = true;
    }

    @Override
    public void add(int key, int value) {
        if (size == CAPACITY) {
            throw new FullADTException("Diccionario múltiple lleno");
        }
        keys[size] = key;
        values[size] = value;
        size++;
    }

    @Override
    public void remove(int key) {
        if (isEmpty()) return;
        int i = 0;
        while (i < size) {
            if (keys[i] == key) {
                removeAt(i);
            } else {
                i++;
            }
        }
    }

    @Override
    public int[] get(int key) {
        if (isEmpty()) {
            throw new EmptyADTException("El diccionario está vacío");
        }
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) count++;
        }
        if (count == 0) {
            throw new ElementNotFoundADTException("La clave no existe: " + key);
        }
        int[] result = new int[count];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) {
                result[j++] = values[i];
            }
        }
        return result;
    }

    @Override
    public SetADT getKeys() {
        SetADT set = new StaticSetADT();
        for (int i = 0; i < size; i++) {
            set.add(keys[i]); // Set filtra duplicados
        }
        return set;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void remove(int key, int value) {
        if (isEmpty()) return;
        int i = 0;
        while (i < size) {
            if (keys[i] == key && values[i] == value) {
                removeAt(i);
            } else {
                i++;
            }
        }
    }

    private void removeAt(int idx) {
        for (int k = idx; k < size - 1; k++) {
            keys[k] = keys[k + 1];
            values[k] = values[k + 1];
        }
        size--;
    }
}
