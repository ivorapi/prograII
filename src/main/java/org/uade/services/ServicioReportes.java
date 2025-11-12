package org.uade.services;

import org.uade.entidades.Cliente;
import org.uade.entidades.Pedido;
import org.uade.entidades.Repartidor;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

public class ServicioReportes {

    private final ServicioPedidos servicioPedidos;
    private final ServicioDespacho servicioDespacho;
    private final DynamicQueueADT colaPendientesVip;
    private final DynamicQueueADT colaPendientesNoVip;

    public ServicioReportes(ServicioPedidos servicioPedidos,
                            ServicioDespacho servicioDespacho,
                            DynamicQueueADT pendientesVip,
                            DynamicQueueADT pendientesNoVip) {
        this.servicioPedidos = servicioPedidos;
        this.servicioDespacho = servicioDespacho;
        this.colaPendientesVip = pendientesVip;
        this.colaPendientesNoVip = pendientesNoVip;
    }

    public void imprimirCantidadPendientesPorDespachar() {
        int total = contarColaSinDestruir(colaPendientesVip) + contarColaSinDestruir(colaPendientesNoVip);
        System.out.println("• Número de pedidos pendientes de ser despachados: " + total);
    }

    public void imprimirCantidadFinalizados() {
        System.out.println("• Número de pedidos finalizados: " + servicioPedidos.obtenerCantidadFinalizados());
    }

    public void imprimirEntregasPorRepartidor() {
        System.out.println("• Cantidad de pedidos que entrega cada repartidor:");
        ServicioDespacho.NodoRepartidor cursor = servicioDespacho.getRepartidoresHead();
        if (cursor == null) {
            System.out.println("  (no hay repartidores)");
            return;
        }
        while (cursor != null) {
            Repartidor r = cursor.repartidor;
            System.out.println("  - " + r.getNombre() + " (ID " + r.getId() + "): " + r.getEntregasRealizadas());
            cursor = cursor.siguiente;
        }
    }


    public void imprimirClienteTopPorPlatosEnUnPedido() {
        ServicioPedidos.NodoPedido cur = servicioPedidos.obtenerCabezaPedidos();
        if (cur == null) {
            System.out.println("• Cliente con mayor número de pedidos: (no hay clientes)");
            return;
        }

        int maxPlatos = 0;
        while (cur != null) {
            if (cur.pedido != null) {
                int cp = cur.pedido.getCantidadPlatos();
                if (cp > maxPlatos) maxPlatos = cp;
            }
            cur = cur.siguiente;
        }

        if (maxPlatos <= 0) {
            System.out.println("• Cliente con mayor número de pedidos: (no hay pedidos)");
            return;
        }

        System.out.println("• Cliente con mayor número de pedidos: (medido por el pedido con más platos)");

        NombreNodo yaImpresos = null;

        cur = servicioPedidos.obtenerCabezaPedidos();
        while (cur != null) {
            if (cur.pedido != null && cur.pedido.getCliente() != null) {
                if (cur.pedido.getCantidadPlatos() == maxPlatos) {
                    String nombre = cur.pedido.getCliente().getNombre();
                    if (nombre != null) nombre = nombre.trim();
                    if (nombre != null && !nombre.isEmpty() && !yaImpreso(yaImpresos, nombre)) {
                        yaImpresos = agregarNombre(yaImpresos, nombre);
                        System.out.println("  - " + nombre + " (platos en su pedido más grande: " + maxPlatos + ")");
                    }
                }
            }
            cur = cur.siguiente;
        }
    }


    private int contarColaSinDestruir(DynamicQueueADT q) {
        int count = 0;
        DynamicQueueADT tmp = new DynamicQueueADT();
        while (!q.isEmpty()) {
            int v = q.getElement();
            tmp.add(v);
            q.remove();
            count++;
        }
        while (!tmp.isEmpty()) {
            q.add(tmp.getElement());
            tmp.remove();
        }
        return count;
    }

    private static class NombreNodo {
        String nombre; NombreNodo sig;
        NombreNodo(String n){ this.nombre = n; }
    }

    private boolean yaImpreso(NombreNodo head, String n) {
        NombreNodo nombreNodo = head;
        while (nombreNodo != null) {
            if (nombreNodo.nombre != null && nombreNodo.nombre.equalsIgnoreCase(n)) return true;
            nombreNodo = nombreNodo.sig;
        }
        return false;
    }

    private NombreNodo agregarNombre(NombreNodo head, String n) {
        NombreNodo nn = new NombreNodo(n);
        nn.sig = head;
        return nn;
    }
}