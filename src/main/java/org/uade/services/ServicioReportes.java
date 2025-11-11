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
        NombreNodo impresos = null;

        cur = servicioPedidos.obtenerCabezaPedidos();
        while (cur != null) {
            if (cur.pedido != null && cur.pedido.getCliente() != null) {
                if (cur.pedido.getCantidadPlatos() == maxPlatos) {
                    String nombre = trimSeguro(cur.pedido.getCliente().getNombre());
                    if (nombre != null && nombre.length() > 0 && !yaImpreso(impresos, nombre)) {
                        impresos = agregarNombre(impresos, nombre);
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
            int v = q.getElement(); tmp.add(v); q.remove(); count++;
        }
        while (!tmp.isEmpty()) { q.add(tmp.getElement()); tmp.remove(); }
        return count;
    }

    private static class NombreNodo {
        String nombre; NombreNodo sig;
        NombreNodo(String n){ this.nombre = n; }
    }

    private boolean yaImpreso(NombreNodo head, String n) {
        NombreNodo c = head;
        while (c != null) {
            if (equalsIgnoreCase(c.nombre, n)) return true;
            c = c.sig;
        }
        return false;
    }

    private NombreNodo agregarNombre(NombreNodo head, String n) {
        NombreNodo nn = new NombreNodo(n);
        nn.sig = head;
        return nn;
    }

    private String trimSeguro(String s) {
        if (s == null) return null;
        int i = 0, j = s.length() - 1;
        while (i <= j && esEspacio(s.charAt(i))) i++;
        while (j >= i && esEspacio(s.charAt(j))) j--;
        if (i > j) return "";
        String r = "";
        int k = i;
        while (k <= j) { r += s.charAt(k); k++; }
        return r;
    }

    private boolean esEspacio(char c) { return c==' ' || c=='\t' || c=='\n' || c=='\r'; }

    private boolean equalsIgnoreCase(String a, String b) {
        if (a == null || b == null) return false;
        int la = a.length(), lb = b.length(); if (la != lb) return false;
        int i = 0;
        while (i < la) {
            char ca = a.charAt(i), cb = b.charAt(i);
            if (ca >= 'A' && ca <= 'Z') ca = (char)(ca - 'A' + 'a');
            if (cb >= 'A' && cb <= 'Z') cb = (char)(cb - 'A' + 'a');
            if (ca != cb) return false; i++;
        }
        return true;
    }
}
