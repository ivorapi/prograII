package org.uade.entidades;

import org.uade.enums.Estado;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;

public class Pedido {
    private int id;
    private Cliente cliente;
    private Tipo tipo;
    private Prioridad prioridad;
    private int cantidadPlatos;
    private Nodo destino;
    private Estado estado;

    public Pedido(int id,
                  Cliente cliente,
                  Tipo tipo,
                  Prioridad prioridad,
                  int cantidadPlatos,
                  Nodo destino) {
        this.id = id;
        this.cliente = cliente;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.cantidadPlatos = cantidadPlatos;
        this.destino = destino;
        this.estado = Estado.PENDIENTE;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }

    public Tipo getTipo() { return tipo; }

    public Prioridad getPrioridad() { return prioridad; }

    public int getCantidadPlatos() { return cantidadPlatos; }
    public void setCantidadPlatos(int n) { this.cantidadPlatos = n; }

    public Nodo getDestino() { return destino; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
