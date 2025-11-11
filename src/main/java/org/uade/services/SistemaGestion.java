package org.uade.services;

import org.uade.entidades.Pedido;
import org.uade.enums.Prioridad;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

public class SistemaGestion {

    private final ServicioPedidos servicioPedidos;
    private final ServicioCocina servicioCocina;
    private final ServicioDespacho servicioDespacho;
    private final ServicioReportes servicioReportes;
    private final ServicioMapaCiudad servicioMapaCiudad;

    public SistemaGestion() {
        DynamicQueueADT colaPedidosPendientesVip = new DynamicQueueADT();
        DynamicQueueADT colaPedidosPendientesNoVip = new DynamicQueueADT();
        DynamicQueueADT colaPlatosCocinaVip = new DynamicQueueADT();
        DynamicQueueADT colaPlatosCocinaNoVip = new DynamicQueueADT();

        this.servicioMapaCiudad = new ServicioMapaCiudad();
        this.servicioPedidos = new ServicioPedidos(colaPedidosPendientesVip, colaPedidosPendientesNoVip);
        this.servicioCocina = new ServicioCocina(colaPlatosCocinaVip, colaPlatosCocinaNoVip);
        this.servicioDespacho = new ServicioDespacho(servicioPedidos, servicioMapaCiudad);
        this.servicioReportes = new ServicioReportes(servicioPedidos, servicioDespacho, colaPedidosPendientesVip, colaPedidosPendientesNoVip);

        registrarRepartidor(new org.uade.entidades.Repartidor(0, "Juan", null));
        registrarRepartidor(new org.uade.entidades.Repartidor(0, "María", null));
        registrarRepartidor(new org.uade.entidades.Repartidor(0, "Pedro", null));
    }

    public void registrarRepartidor(org.uade.entidades.Repartidor repartidor) {
        servicioDespacho.registrarRepartidor(repartidor);
    }
    public ServicioDespacho.NodoRepartidor getRepartidoresHead() { return servicioDespacho.getRepartidoresHead(); }

    public int agregarPedido(Pedido pedido) { return servicioPedidos.crearPedido(pedido); }
    public Pedido obtenerPedidoPorId(int idPedido) { return servicioPedidos.obtener(idPedido); }

    public int cocinarYCumplirSiguientePedido() {
        return servicioPedidos.cocinarYCumplirSiguiente(servicioDespacho, servicioCocina);
    }

    public void agregarPedidoACocina(DynamicQueueADT colaDePlatos, Prioridad prioridad) {
        servicioCocina.encolarPlatos(colaDePlatos, prioridad);
    }
    public void mostrarPendientesPorDespachar() { servicioReportes.imprimirCantidadPendientesPorDespachar(); }
    public void mostrarFinalizados()            { servicioReportes.imprimirCantidadFinalizados(); }
    public void mostrarEntregasPorRepartidor()  { servicioReportes.imprimirEntregasPorRepartidor(); }
    public void mostrarClienteTop()             { servicioReportes.imprimirClienteTopPorPlatosEnUnPedido(); }
}
