package org.uade.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.uade.entidades.Cliente;
import org.uade.entidades.Nodo;
import org.uade.entidades.Pedido;
import org.uade.entidades.Repartidor;
import org.uade.enums.Estado;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Tests para ServicioDespacho.despacharExistente()")
class ServicioDespachoTest {

    private ServicioDespacho servicioDespacho;
    private ServicioPedidos servicioPedidos;
    private ServicioMapaCiudad servicioMapaCiudad;

    @BeforeEach
    void setUp() {
        DynamicQueueADT colaPedidosPendientesVip = new DynamicQueueADT();
        DynamicQueueADT colaPedidosPendientesNoVip = new DynamicQueueADT();

        servicioMapaCiudad = new ServicioMapaCiudad();
        servicioPedidos = new ServicioPedidos(colaPedidosPendientesVip, colaPedidosPendientesNoVip);
        servicioDespacho = new ServicioDespacho(servicioPedidos, servicioMapaCiudad);

        servicioDespacho.registrarRepartidor(new Repartidor(0, "Juan", null));
        servicioDespacho.registrarRepartidor(new Repartidor(0, "María", null));
    }

    @Test
    @DisplayName("Debe asignar un pedido a un repartidor disponible")
    void debeAsignarPedidoARepartidorDisponible() {
        Cliente cliente = new Cliente("Carlos");
        Nodo destino = new Nodo("Palermo");
        Pedido pedido = new Pedido(0, cliente, Tipo.DOMICILIO, Prioridad.NORMAL, 2, destino);
        int idPedido = servicioPedidos.crearPedido(pedido);

        int idRepartidorAsignado = servicioDespacho.despacharExistente(idPedido);

        assertTrue(idRepartidorAsignado > 0, "Debe retornar un ID de repartidor válido");

        Pedido pedidoActualizado = servicioPedidos.obtener(idPedido);
        assertNotNull(pedidoActualizado, "El pedido debe existir");
        assertEquals(Estado.DESPACHADO, pedidoActualizado.getEstado(),
                "El pedido debe estar en estado DESPACHADO");
    }

    @Test
    @DisplayName("Debe reutilizar el mismo repartidor para múltiples pedidos hasta alcanzar capacidad")
    void debeReutilizarMismoRepartidorHastaCapacidadMaxima() {
        Cliente cliente = new Cliente("Ana");
        Nodo destino = new Nodo("Recoleta");

        Pedido pedido1 = new Pedido(0, cliente, Tipo.DOMICILIO, Prioridad.NORMAL, 1, destino);
        Pedido pedido2 = new Pedido(0, cliente, Tipo.DOMICILIO, Prioridad.NORMAL, 1, destino);
        Pedido pedido3 = new Pedido(0, cliente, Tipo.DOMICILIO, Prioridad.NORMAL, 1, destino);

        int idPedido1 = servicioPedidos.crearPedido(pedido1);
        int idPedido2 = servicioPedidos.crearPedido(pedido2);
        int idPedido3 = servicioPedidos.crearPedido(pedido3);

        int repartidor1 = servicioDespacho.despacharExistente(idPedido1);
        int repartidor2 = servicioDespacho.despacharExistente(idPedido2);
        int repartidor3 = servicioDespacho.despacharExistente(idPedido3);

        assertEquals(repartidor1, repartidor2,
                "El segundo pedido debe asignarse al mismo repartidor");
        assertEquals(repartidor1, repartidor3,
                "El tercer pedido debe asignarse al mismo repartidor");

        assertEquals(Estado.DESPACHADO, servicioPedidos.obtener(idPedido1).getEstado());
        assertEquals(Estado.DESPACHADO, servicioPedidos.obtener(idPedido2).getEstado());
        assertEquals(Estado.DESPACHADO, servicioPedidos.obtener(idPedido3).getEstado());
    }

    @Test
    @DisplayName("Debe retornar -1 cuando no hay repartidores disponibles")
    void debeRetornarMenosUnoSinRepartidoresDisponibles() {
        DynamicQueueADT colaVip = new DynamicQueueADT();
        DynamicQueueADT colaNoVip = new DynamicQueueADT();
        ServicioPedidos servicioSinRepartidores = new ServicioPedidos(colaVip, colaNoVip);
        ServicioDespacho despachoSinRepartidores = new ServicioDespacho(
                servicioSinRepartidores, servicioMapaCiudad);

        Cliente cliente = new Cliente("Roberto");
        Nodo destino = new Nodo("Nuñez");
        Pedido pedido = new Pedido(0, cliente, Tipo.DOMICILIO, Prioridad.NORMAL, 1, destino);
        int idPedido = servicioSinRepartidores.crearPedido(pedido);

        int resultado = despachoSinRepartidores.despacharExistente(idPedido);

        assertEquals(-1, resultado,
                "Debe retornar -1 cuando no hay repartidores disponibles");
    }

    @Test
    @DisplayName("Debe retornar -1 cuando el pedido no existe")
    void debeRetornarMenosUnoCuandoPedidoNoExiste() {

        int idPedidoInexistente = 9999;

        int resultado = servicioDespacho.despacharExistente(idPedidoInexistente);

        assertEquals(-1, resultado,
                "Debe retornar -1 cuando el pedido no existe");
    }

    @Test
    @DisplayName("Debe manejar correctamente pedidos VIP y NORMAL en secuencia")
    void debeManejarPedidosVIPyNormalEnSecuencia() {
        Cliente cliente1 = new Cliente("VIP Cliente");
        Cliente cliente2 = new Cliente("Normal Cliente");
        Nodo destino = new Nodo("Colegiales");

        Pedido pedidoNormal = new Pedido(0, cliente2, Tipo.DOMICILIO, Prioridad.NORMAL, 1, destino);
        Pedido pedidoVIP = new Pedido(0, cliente1, Tipo.DOMICILIO, Prioridad.VIP, 1, destino);

        int idPedidoNormal = servicioPedidos.crearPedido(pedidoNormal);
        int idPedidoVIP = servicioPedidos.crearPedido(pedidoVIP);
        int repartidorNormal = servicioDespacho.despacharExistente(idPedidoNormal);
        int repartidorVIP = servicioDespacho.despacharExistente(idPedidoVIP);

        assertTrue(repartidorNormal > 0, "Pedido NORMAL debe ser despachado");
        assertTrue(repartidorVIP > 0, "Pedido VIP debe ser despachado");
        assertEquals(Estado.DESPACHADO, servicioPedidos.obtener(idPedidoNormal).getEstado());
        assertEquals(Estado.DESPACHADO, servicioPedidos.obtener(idPedidoVIP).getEstado());
    }
}

