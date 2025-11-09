package org.uade.entidades;

public class Plato {
    private String nombre;
    private int tiempoPreparacion;
    private int vecesPedido;

    public Plato(String nombre, int tiempoPreparacion) {
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.vecesPedido = 0;
    }

    public String getNombre() { return nombre; }
    public int getTiempoPreparacion() { return tiempoPreparacion; }
    public int getVecesPedido() { return vecesPedido; }
    public void incrementarVecesPedido() { vecesPedido++; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plato)) return false;
        Plato p = (Plato) o;
        return nombre.equals(p.nombre);
    }

    @Override
    public int hashCode() { return nombre.hashCode(); }

    @Override
    public String toString() { return nombre; }
}
