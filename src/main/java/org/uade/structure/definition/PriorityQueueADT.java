package org.uade.structure.definition;

// Esta interfaz representa el TDA Cola con Prioridad.
public interface PriorityQueueADT {
    /**
     * Devuelve el primer elemento de la estructura.
     * Precondición: La estructura debe tener elementos.
     */
    int getElement();

    /**
     * Devuelve la prioridad del primer elemento de la estructura.
     * Precondición: La estructura debe tener elementos.
     */
    int getPriority();

    /**
     * Agrega un elemento a la estructura manteniendo orden por prioridad.
     * Precondición: La estructura no debe sobrepasar la capacidad.
     */
    void add(int value, int priority);

    /**
     * Elimina el primer elemento de la estructura.
     * Precondición: La estructura debe tener elementos.
     */
    void remove();

    /**
     * Comprueba si la estructura está vacía.
     */
    boolean isEmpty();
}
