package org.uade.entidades;

public class LineaPedido {
    private Plato plato;
    private int cantidad;

    public LineaPedido(Plato plato, int cantidad) {
        this.plato = plato;
        this.cantidad = cantidad;
    }

    public Plato getPlato() { return plato; }
    public int getCantidad() { return cantidad; }

    @Override
    public String toString() {
        return cantidad + "x " + plato.getNombre();
    }
}
