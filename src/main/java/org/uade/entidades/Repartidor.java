package org.uade.entidades;

public class Repartidor {
    private int id;
    private String nombre;
    private Nodo nodoActual;
    private int entregasRealizadas;

    public Repartidor(int id, String nombre, Nodo nodoActual) {
        this.id = id;
        this.nombre = nombre;
        this.nodoActual = nodoActual;
        this.entregasRealizadas = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }

    public Nodo getNodoActual() {
        return nodoActual;
    }

    public int getEntregasRealizadas() {
        return entregasRealizadas;
    }

    public void incrementarEntregas() {
        entregasRealizadas++;
    }
}
