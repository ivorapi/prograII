package org.uade.entidades;

public class Nodo {
    private String id;

    public Nodo(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nodo)) return false;
        Nodo n = (Nodo) o;
        return id.equals(n.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public String toString() { return id; }
}
