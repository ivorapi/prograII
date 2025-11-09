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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente) o;
        return nombre.equals(c.nombre);
    }

    @Override
    public int hashCode() { return nombre.hashCode(); }

    @Override
    public String toString() {
        return nombre + " (" + pedidosRealizados + " pedidos)";
    }
}
