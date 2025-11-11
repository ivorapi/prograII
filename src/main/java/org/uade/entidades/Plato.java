package org.uade.entidades;

public class Plato {
    private String nombre;
    private int vecesPedido;

    public Plato(String nombre, int tiempoPreparacion) {
        this.nombre = nombre;
        this.vecesPedido = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVecesPedido() {
        return vecesPedido;
    }

    public void incrementarVecesPedido() {
        vecesPedido++;
    }
}

