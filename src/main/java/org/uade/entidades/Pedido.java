package org.uade.entidades;

import org.uade.enums.Estado;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;


public class Pedido {
    private int id;
    private Cliente cliente;
    private Tipo tipo;
    private Prioridad prioridad;
    private Estado estado;

    private int platosTotales;
    private int platosListos;
    private long tsLlegada;
    private Nodo destino;

    public Pedido(int id, Cliente cliente, Tipo tipo, Prioridad prioridad,
                  int platosTotales, Nodo destino) {
        this.id = id;
        this.cliente = cliente;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.estado = Estado.PENDIENTE;
        this.platosTotales = platosTotales;
        this.platosListos = 0;
        this.tsLlegada = System.nanoTime();
        this.destino = destino;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public Tipo getTipo() { return tipo; }
    public Prioridad getPrioridad() { return prioridad; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Nodo getDestino() { return destino; }

    public int getPlatosTotales() { return platosTotales; }
    public int getPlatosListos() { return platosListos; }
    public void marcarPlatoListo() { platosListos++; }
    public boolean estaCompleto() { return platosListos >= platosTotales; }
    public long getTsLlegada() { return tsLlegada; }

    @Override
    public String toString() {
        return "Pedido #" + id + " [" + prioridad + ", " + tipo + ", " + estado + "]";
    }
}
