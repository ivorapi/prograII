package org.uade.services;

import org.uade.entidades.Pedido;
import org.uade.entidades.Repartidor;
import org.uade.enums.Estado;
import org.uade.structure.implementation.dynamic.DynamicLinkedListADT;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

public class ServicioDespacho {

    private final ServicioPedidos servicioPedidos;
    private final ServicioMapaCiudad servicioMapa;

    public static class NodoRepartidor {
        public int id;
        public Repartidor repartidor;
        public int capacidadMaxima;
        public int cantidadPendientes;
        public DynamicLinkedListADT mochilaIds;
        public NodoRepartidor siguiente;

        NodoRepartidor(int id, Repartidor r, int cap) {
            this.id = id;
            this.repartidor = r;
            this.capacidadMaxima = cap;
            this.cantidadPendientes = 0;
            this.mochilaIds = new DynamicLinkedListADT();
            this.siguiente = null;
        }
    }

    private NodoRepartidor inicioListaRepartidores = null;
    private int proximoIdRepartidor = 1;

    private final DynamicQueueADT colaRepartidoresDisponibles = new DynamicQueueADT();
    private int idRepartidorEnCarga = -1;

    public ServicioDespacho(ServicioPedidos servicioPedidos, ServicioMapaCiudad servicioMapa) {
        this.servicioPedidos = servicioPedidos;
        this.servicioMapa = servicioMapa;
    }


    public void registrarRepartidor(Repartidor repartidor) {
        int id = proximoIdRepartidor++;
        repartidor.setId(id);
        NodoRepartidor nodo = new NodoRepartidor(id, repartidor, 5);
        nodo.siguiente = inicioListaRepartidores;
        inicioListaRepartidores = nodo;
        colaRepartidoresDisponibles.add(id);
    }

    public NodoRepartidor getRepartidoresHead() { return inicioListaRepartidores; }

    private NodoRepartidor buscarRepartidor(int id) {
        NodoRepartidor c = inicioListaRepartidores;
        while (c != null) {
            if (c.id == id) return c;
            c = c.siguiente;
        }
        return null;
    }

    private int tomarSiguienteDisponible() {
        if (colaRepartidoresDisponibles.isEmpty()) return -1;
        int id = colaRepartidoresDisponibles.getElement();
        colaRepartidoresDisponibles.remove();
        return id;
    }

    private void devolverDisponible(int idRep) {
        colaRepartidoresDisponibles.add(idRep);
    }


    public int despacharExistente(int idPedido) {
        NodoRepartidor repartidorElegido = null;

        if (idRepartidorEnCarga > 0) {
            NodoRepartidor cand = buscarRepartidor(idRepartidorEnCarga);
            if (cand != null && cand.cantidadPendientes < cand.capacidadMaxima) {
                repartidorElegido = cand;
            } else {
                idRepartidorEnCarga = -1;
            }
        }

        if (repartidorElegido == null) {
            int nuevo = tomarSiguienteDisponible();
            if (nuevo <= 0) return -1;
            repartidorElegido = buscarRepartidor(nuevo);
            if (repartidorElegido == null) return -1;
            idRepartidorEnCarga = repartidorElegido.id;
        }

        Pedido pedido = servicioPedidos.obtener(idPedido);
        if (pedido == null) return -1;

        pedido.setEstado(Estado.DESPACHADO);
        servicioPedidos.actualizar(pedido);

        repartidorElegido.mochilaIds.add(idPedido);
        repartidorElegido.cantidadPendientes++;

        if (repartidorElegido.cantidadPendientes >= repartidorElegido.capacidadMaxima) {
            realizarRecorridoDeEntrega(repartidorElegido);
            idRepartidorEnCarga = -1;
            devolverDisponible(repartidorElegido.id);
        }

        return repartidorElegido.id;
    }

    private void realizarRecorridoDeEntrega(NodoRepartidor repNode) {
        System.out.println("\n🚴 Repartidor " + repNode.repartidor.getNombre() +
                " sale con " + repNode.cantidadPendientes + " pedidos.");
        int verticeActual = servicioMapa.obtenerIdBarrio("Restaurante");
        int distanciaAcumulada = 0;

        System.out.print("Ruta: " + servicioMapa.obtenerNombreBarrio(verticeActual));

        while (!repNode.mochilaIds.isEmpty()) {
            int mejorPedidoId = -1;
            int mejorDistancia = 1_000_000_000;
            DynamicLinkedListADT temp = new DynamicLinkedListADT();

            while (!repNode.mochilaIds.isEmpty()) {
                int id = repNode.mochilaIds.get(0);
                repNode.mochilaIds.remove(0);

                Pedido p = servicioPedidos.obtener(id);
                int destino = servicioMapa.obtenerIdBarrio("Restaurante");
                if (p != null && p.getDestino() != null) {
                    int v = servicioMapa.obtenerIdBarrio(p.getDestino().getNombre());
                    if (v >= 0) destino = v;
                }

                int dist = servicioMapa.calcularDistanciaMinima(verticeActual, destino);
                if (dist >= 0 && dist < mejorDistancia) {
                    mejorDistancia = dist;
                    mejorPedidoId = id;
                }

                temp.add(id);
            }

            boolean saltado = false;
            while (!temp.isEmpty()) {
                int id = temp.get(0);
                temp.remove(0);
                if (!saltado && id == mejorPedidoId) {
                    saltado = true;
                } else {
                    repNode.mochilaIds.add(id);
                }
            }

            if (mejorPedidoId == -1) break;

            Pedido entregado = servicioPedidos.obtener(mejorPedidoId);
            int destinoVert = servicioMapa.obtenerIdBarrio("Restaurante");
            if (entregado != null && entregado.getDestino() != null) {
                int v = servicioMapa.obtenerIdBarrio(entregado.getDestino().getNombre());
                if (v >= 0) destinoVert = v;
            }

            distanciaAcumulada += mejorDistancia;
            System.out.print(" -> " + servicioMapa.obtenerNombreBarrio(destinoVert) + " (" + mejorDistancia + ")");

            if (entregado != null) {
                entregado.setEstado(Estado.FINALIZADO);
                servicioPedidos.actualizar(entregado);
                servicioPedidos.incrementarFinalizados();
            }
            repNode.cantidadPendientes--;
            repNode.repartidor.incrementarEntregas();

            verticeActual = destinoVert;
        }

        System.out.println("\nDistancia total estimada: " + distanciaAcumulada + "\n");
    }

    public boolean forzarEntregaPendientes() {
        if (idRepartidorEnCarga <= 0) {
            return false;
        }

        NodoRepartidor repartidorConPedidos = buscarRepartidor(idRepartidorEnCarga);
        if (repartidorConPedidos == null || repartidorConPedidos.cantidadPendientes == 0) {
            return false;
        }

        realizarRecorridoDeEntrega(repartidorConPedidos);
        idRepartidorEnCarga = -1;
        devolverDisponible(repartidorConPedidos.id);
        return true;
    }
}
