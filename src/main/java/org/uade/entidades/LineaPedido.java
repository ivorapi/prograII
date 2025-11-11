package org.uade.entidades;

public class LineaPedido {
    private int codigoPlato;
    private int cantidad;

    public LineaPedido(int codigoPlato, int cantidad) {
        this.codigoPlato = codigoPlato;
        this.cantidad = cantidad;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public int getCantidad() {
        return cantidad;
    }
}
