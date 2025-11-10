package org.uade.datos;

import org.uade.entidades.*;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;
import org.uade.services.SistemaGestion;

public class DataSeeder {

    public static void seed(SistemaGestion sistema, Nodo restaurante) {

        Plato pizza = new Plato("Pizza Margherita", 15);
        Plato burger = new Plato("Hamburguesa Completa", 12);
        Plato pasta = new Plato("Pasta Carbonara", 18);
        Plato ensalada = new Plato("Ensalada César", 8);
        Plato milanesa = new Plato("Milanesa Napolitana", 20);

        sistema.registrarPlato(pizza);
        sistema.registrarPlato(burger);
        sistema.registrarPlato(pasta);
        sistema.registrarPlato(ensalada);
        sistema.registrarPlato(milanesa);


        Cliente ana   = new Cliente("Ana García");
        Cliente bruno = new Cliente("Bruno López");
        Cliente carla = new Cliente("Carla Martínez");


        int i = 1;
        while (i <= 10) {
            Repartidor r = new Repartidor(0, "Repartidor " + i, restaurante);
            sistema.registrarRepartidor(r);
            i++;
        }


        sistema.registrarPedido(new Pedido(0, ana,   Tipo.DOMICILIO, Prioridad.VIP,    2, new Nodo("B")));
        sistema.registrarPedido(new Pedido(0, bruno, Tipo.LLEVAR,    Prioridad.NORMAL, 1, null));
        sistema.registrarPedido(new Pedido(0, carla, Tipo.DOMICILIO, Prioridad.NORMAL, 2, new Nodo("C")));
        sistema.registrarPedido(new Pedido(0, bruno, Tipo.DOMICILIO, Prioridad.VIP,    3, new Nodo("D")));
        sistema.registrarPedido(new Pedido(0, ana,   Tipo.DOMICILIO, Prioridad.NORMAL, 2, new Nodo("E")));


        pizza.incrementarVecesPedido();
        pizza.incrementarVecesPedido();
        pizza.incrementarVecesPedido();
        burger.incrementarVecesPedido();
        burger.incrementarVecesPedido();
        pasta.incrementarVecesPedido();
        ensalada.incrementarVecesPedido();
        milanesa.incrementarVecesPedido();
        milanesa.incrementarVecesPedido();
        milanesa.incrementarVecesPedido();
        milanesa.incrementarVecesPedido();
    }
}

