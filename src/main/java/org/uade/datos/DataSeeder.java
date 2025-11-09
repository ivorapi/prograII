package org.uade.datos;

import org.uade.entidades.*;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;
import org.uade.services.SistemaGestion;

public class DataSeeder {

    public static void seed(SistemaGestion sistema, Nodo restaurante) {
        // Clientes (si después querés set de clientes, podés mapearles IDs también)
        Cliente ana   = new Cliente("Ana");
        Cliente bruno = new Cliente("Bruno");

        // Repartidores (10)
        for (int i = 1; i <= 10; i++) {
            Repartidor r = new Repartidor(0, "Repartidor " + i, restaurante);
            sistema.registrarRepartidor(r);
        }

        // Pedidos (5) – usamos contadores de platos, no listas dentro del Pedido
        sistema.registrarPedido(new Pedido(0, ana,   Tipo.DOMICILIO, Prioridad.VIP,    2, new Nodo("B")));
        sistema.registrarPedido(new Pedido(0, bruno, Tipo.LLEVAR,    Prioridad.NORMAL, 1, null));
        sistema.registrarPedido(new Pedido(0, ana,   Tipo.DOMICILIO, Prioridad.NORMAL, 2, new Nodo("C")));
        sistema.registrarPedido(new Pedido(0, bruno, Tipo.DOMICILIO, Prioridad.VIP,    1, new Nodo("D")));
        sistema.registrarPedido(new Pedido(0, ana,   Tipo.DOMICILIO, Prioridad.NORMAL, 2, new Nodo("E")));
    }
}

