package org.uade.services;

import org.uade.entidades.Pedido;
import org.uade.entidades.Cliente;
import org.uade.enums.*;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

public class ServicioPedidos {

    public static class NodoPedido {
        int idPedido;
        Pedido pedido;
        NodoPedido siguiente;
        NodoPedido(int id, Pedido p) { this.idPedido = id; this.pedido = p; this.siguiente = null; }
    }

    private NodoPedido inicioListaPedidos = null;
    private int proximoIdPedido = 1;

    private final DynamicQueueADT colaPedidosPendientesVip;
    private final DynamicQueueADT colaPedidosPendientesNoVip;

    private int contadorPedidosFinalizados = 0;

    public ServicioPedidos(DynamicQueueADT colaVip, DynamicQueueADT colaNoVip) {
        this.colaPedidosPendientesVip = colaVip;
        this.colaPedidosPendientesNoVip = colaNoVip;
    }


    private void insertarPedidoEnLista(int id, Pedido pedido) {
        NodoPedido nuevo = new NodoPedido(id, pedido);
        nuevo.siguiente = inicioListaPedidos;
        inicioListaPedidos = nuevo;
    }


    public int crearPedido(Pedido pedido) {
        int idGenerado = proximoIdPedido++;
        pedido.setId(idGenerado);
        pedido.setEstado(Estado.PENDIENTE);

        insertarPedidoEnLista(idGenerado, pedido);

        if (pedido.getPrioridad() == Prioridad.VIP) {
            colaPedidosPendientesVip.add(idGenerado);
        } else {
            colaPedidosPendientesNoVip.add(idGenerado);
        }

        Cliente cliente = pedido.getCliente();
        if (cliente != null) {
            cliente.incrementarPedidos();
        }
        return idGenerado;
    }

    public Pedido obtener(int idPedido) {
        NodoPedido actual = inicioListaPedidos;
        while (actual != null) {
            if (actual.idPedido == idPedido) return actual.pedido;
            actual = actual.siguiente;
        }
        return null;
    }

    public void actualizar(Pedido pedidoActualizado) {
        NodoPedido actual = inicioListaPedidos;
        while (actual != null) {
            if (actual.idPedido == pedidoActualizado.getId()) {
                actual.pedido = pedidoActualizado;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public int tomarSiguienteIdSegunPrioridad() {
        if (!colaPedidosPendientesVip.isEmpty()) {
            int id = colaPedidosPendientesVip.getElement(); colaPedidosPendientesVip.remove(); return id;
        }
        if (!colaPedidosPendientesNoVip.isEmpty()) {
            int id = colaPedidosPendientesNoVip.getElement(); colaPedidosPendientesNoVip.remove(); return id;
        }
        return -1;
    }

    public void incrementarFinalizados() { contadorPedidosFinalizados++; }
    public int obtenerCantidadFinalizados() { return contadorPedidosFinalizados; }
    public NodoPedido obtenerCabezaPedidos() { return inicioListaPedidos; }

    public int cocinarYCumplirSiguiente(ServicioDespacho servicioDespacho, ServicioCocina servicioCocina) {
        int idSiguiente = tomarSiguienteIdSegunPrioridad();
        if (idSiguiente == -1) return -1;

        Pedido pedido = obtener(idSiguiente);
        if (pedido == null) return -1;

        int platosPendientes = pedido.getCantidadPlatos();
        DynamicQueueADT colaCocina = (pedido.getPrioridad() == Prioridad.VIP)
                ? servicioCocina.getColaPlatosVip()
                : servicioCocina.getColaPlatosNoVip();

        while (platosPendientes > 0 && !colaCocina.isEmpty()) {
            colaCocina.remove();
            platosPendientes--;
        }

        if (pedido.getTipo() == Tipo.LLEVAR) {
            pedido.setEstado(Estado.FINALIZADO);
            actualizar(pedido);
            incrementarFinalizados();
            return idSiguiente;
        } else {
            int idRepartidor = servicioDespacho.despacharExistente(idSiguiente);
            if (idRepartidor <= 0) return -2;
            return idSiguiente;
        }
    }
}
