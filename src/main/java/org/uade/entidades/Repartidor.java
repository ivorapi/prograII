package org.uade.entidades;

import org.uade.structure.definition.QueueADT;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

public class Repartidor {
    private int id;
    private String nombre;
    private Nodo ubicacionActual;
    private int entregas;

    private final QueueADT pedidosAsignados = new DynamicQueueADT();

    public Repartidor(int id, String nombre, Nodo ubicacionInicial) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacionActual = ubicacionInicial;
        this.entregas = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public Nodo getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(Nodo ubicacionActual) { this.ubicacionActual = ubicacionActual; }
    public QueueADT getPedidosAsignados() { return pedidosAsignados; }
    public void incrementarEntregas() { entregas++; }
    public int getEntregas() { return entregas; }

    @Override
    public String toString() {
        return "Repartidor #" + id + " - " + nombre + " (" + entregas + " entregas)";
    }
}
