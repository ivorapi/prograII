package org.uade.entidades;

public class Cliente {
    private String nombre;
    private int pedidosRealizados;

    public Cliente(String nombre) {
        this.nombre = nombre;
        this.pedidosRealizados = 0;
    }

    public String getNombre() { return nombre; }
    public int getPedidosRealizados() { return pedidosRealizados; }
    public void incrementarPedidos() { pedidosRealizados++; }

}
