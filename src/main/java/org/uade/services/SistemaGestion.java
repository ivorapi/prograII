package org.uade.services;

import org.uade.entidades.*;
import org.uade.enums.Estado;
import org.uade.enums.Prioridad;

import org.uade.structure.definition.PriorityQueueADT;
import org.uade.structure.definition.QueueADT;
import org.uade.structure.definition.SetADT;
import org.uade.structure.definition.SimpleDictionaryADT;

import org.uade.structure.implementation.fixed.StaticPriorityQueueADT;
import org.uade.structure.implementation.fixed.StaticQueueADT;
import org.uade.structure.implementation.fixed.StaticSetADT;
import org.uade.structure.implementation.fixed.StaticSimpleDictionaryADT;

public class SistemaGestion {

    private final Pedido[]     pedidosStore = new Pedido[256];
    private final Repartidor[] repsStore    = new Repartidor[128];
    private int nextPedidoSlot = 1;
    private int nextRepSlot    = 1;

    private final SimpleDictionaryADT diccPedidos      = new StaticSimpleDictionaryADT();
    private final SimpleDictionaryADT diccRepartidores = new StaticSimpleDictionaryADT();
    private final SetADT setClientes     = new StaticSetADT();
    private final SetADT setPlatos       = new StaticSetADT();
    private final SetADT setRepartidores = new StaticSetADT();

    private final PriorityQueueADT pqPedidos        = new StaticPriorityQueueADT(); // add(value, priority)
    private final QueueADT         colaCocina       = new StaticQueueADT();         // ids de pedido (1 plato = 1 tarea)
    private final QueueADT         colaListos       = new StaticQueueADT();         // ids de pedido listos
    private final QueueADT         poolRepartidores = new StaticQueueADT();         // ids de repartidores

    private int nextPedidoId = 1;
    private int nextRepId    = 1;

    private boolean pedidoExiste(int id) {
        return diccPedidos.getKeys().exist(id);
    }

    private boolean repExiste(int id) {
        return diccRepartidores.getKeys().exist(id);
    }

    private Pedido pedidoById(int id) {
        int slot = diccPedidos.get(id);
        return pedidosStore[slot];
    }

    private Repartidor repById(int id) {
        int slot = diccRepartidores.get(id);
        return repsStore[slot];
    }

    public int registrarRepartidor(Repartidor r) {
        int id   = nextRepId++;
        int slot = nextRepSlot++;
        repsStore[slot] = r;
        r.setId(id);

        diccRepartidores.add(id, slot);
        setRepartidores.add(id);
        poolRepartidores.add(id);
        return id;
    }

    public int registrarPedido(Pedido p) {
        int id   = nextPedidoId++;
        int slot = nextPedidoSlot++;
        pedidosStore[slot] = p;
        p.setId(id);

        diccPedidos.add(id, slot);
        int prio = (p.getPrioridad() == Prioridad.VIP) ? 2 : 1;
        pqPedidos.add(id, prio);
        return id;
    }

    public void mandarPedidosACocina(int cantidad) {
        for (int i = 0; i < cantidad && !pqPedidos.isEmpty(); i++) {
            int idPedido = pqPedidos.getElement();
            pqPedidos.remove();

            if (!pedidoExiste(idPedido)) continue;
            Pedido p = pedidoById(idPedido);
            p.setEstado(Estado.EN_PREPARACION);

            for (int j = 0; j < p.getPlatosTotales(); j++) {
                colaCocina.add(idPedido);
            }
        }
    }

    public void procesarPlatoListo() {
        if (colaCocina.isEmpty()) return;
        int idPedido = colaCocina.getElement();
        colaCocina.remove();

        if (!pedidoExiste(idPedido)) return;
        Pedido p = pedidoById(idPedido);
        p.marcarPlatoListo();
        if (p.estaCompleto()) {
            p.setEstado(Estado.LISTO);
            colaListos.add(idPedido);
        }
    }

    public void asignarPedidosListos() {
        while (!colaListos.isEmpty() && !poolRepartidores.isEmpty()) {
            int idPedido = colaListos.getElement();     colaListos.remove();
            int idRep    = poolRepartidores.getElement(); poolRepartidores.remove();

            if (!pedidoExiste(idPedido) || !repExiste(idRep)) continue;

            Repartidor r = repById(idRep);
            r.getPedidosAsignados().add(idPedido); // cola de IDs (QueueADT)
            pedidoById(idPedido).setEstado(Estado.DESPACHADO);
        }
    }

    public void entregarSiguiente(Repartidor r) {
        if (r.getPedidosAsignados().isEmpty()) return;
        int idPedido = r.getPedidosAsignados().getElement();
        r.getPedidosAsignados().remove();

        if (pedidoExiste(idPedido)) {
            Pedido p = pedidoById(idPedido);
            p.setEstado(Estado.FINALIZADO);
            r.incrementarEntregas();
        }
        if (r.getPedidosAsignados().isEmpty()) {
            poolRepartidores.add(r.getId());
        }
    }

    public void entregarSiguientePorId(int idRepartidor) {
        if (!repExiste(idRepartidor)) return;
        Repartidor r = repById(idRepartidor);
        entregarSiguiente(r);
    }

    public int pedidosEnCocina()    { return contarPorEstado(Estado.EN_PREPARACION); }
    public int pedidosListos()      { return contarPorEstado(Estado.LISTO); }
    public int pedidosFinalizados() { return contarPorEstado(Estado.FINALIZADO); }

    private int contarPorEstado(Estado e) {
        int c = 0;
        for (int slot = 1; slot < nextPedidoSlot; slot++) {
            Pedido p = pedidosStore[slot];
            if (p != null && p.getEstado() == e) c++;
        }
        return c;
    }
}
