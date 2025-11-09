package org.uade;

import org.uade.datos.DataSeeder;
import org.uade.entidades.Nodo;
import org.uade.services.SistemaGestion;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaGestion sistema = new SistemaGestion();
        Nodo restaurante = new Nodo("Restaurante");
        DataSeeder.seed(sistema, restaurante);

        Scanner sc = new Scanner(System.in);
        int op;
        do {
            System.out.println("""
            === Sistema de Pedidos (Demo) ===
            1) Mandar 2 pedidos a cocina
            2) Procesar 1 plato listo
            3) Asignar pedidos listos a repartidores
            4) Entregar siguiente de un repartidor (por ID)
            5) Ver métricas
            0) Salir
            """);
            System.out.print("Elegí opción: ");
            op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1 -> { sistema.mandarPedidosACocina(2); System.out.println("➡ Enviados 2 pedidos a cocina."); }
                case 2 -> { sistema.procesarPlatoListo();    System.out.println("🍽️ Plato procesado."); }
                case 3 -> { sistema.asignarPedidosListos();  System.out.println("🛵 Asignados pedidos a repartidores."); }
                case 4 -> {
                    System.out.print("ID de repartidor: ");
                    int id = Integer.parseInt(sc.nextLine());
                    sistema.entregarSiguientePorId(id);
                    System.out.println("✅ Entrega registrada (si había pedido).");
                }
                case 5 -> {
                    System.out.println("📊 En cocina: " + sistema.pedidosEnCocina());
                    System.out.println("📊 Listos: " + sistema.pedidosListos());
                    System.out.println("📊 Finalizados: " + sistema.pedidosFinalizados());
                }
                case 0 -> System.out.println("Fin.");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }
}
