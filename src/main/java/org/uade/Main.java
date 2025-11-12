package org.uade;

import org.uade.entidades.Cliente;
import org.uade.entidades.Nodo;
import org.uade.entidades.Pedido;
import org.uade.enums.Estado;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;
import org.uade.services.SistemaGestion;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

import java.util.Scanner;

public class Main {

    private static SistemaGestion sistemaGestion;
    private static boolean aplicacionEjecutandose = true;

    private static final Scanner lectorConsola = new Scanner(System.in);


    public static void main(String[] args) {
        Main aplicacion = new Main();
        sistemaGestion = new SistemaGestion();

        System.out.println("=================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE PEDIDOS - RESTAURANTE");
        System.out.println("=================================================");
        System.out.println("Sistema inicializado correctamente");
        System.out.println("=================================================\n");
        precargarPedidos();
        while (aplicacionEjecutandose) {
            aplicacion.mostrarMenuPrincipal();
            String opcionElegida = lectorConsola.nextLine();
            System.out.println();
            aplicacion.procesarOpcionMenuPrincipal(opcionElegida);
        }

        System.out.println("\n👋 ¡Gracias por usar el sistema! Hasta pronto.");
    }
    private static void precargarPedidos() {
        System.out.println("Precargando 5 pedidos de ejemplo...");

        // Pedido 1: VIP
        Cliente cliente1 = new Cliente("Maria Rodriguez");
        DynamicQueueADT platos1 = new DynamicQueueADT();
        platos1.add(1); // Milanesa
        platos1.add(2); // Pizza
        Nodo destino1 = new Nodo("Palermo");
        Pedido pedido1 = new Pedido(0, cliente1, Tipo.DOMICILIO, Prioridad.VIP, 2, destino1);
        sistemaGestion.agregarPedido(pedido1);
        sistemaGestion.agregarPedidoACocina(platos1, Prioridad.VIP);

        // Pedido 2: Normal
        Cliente cliente2 = new Cliente("Juan Perez");
        DynamicQueueADT platos2 = new DynamicQueueADT();
        platos2.add(3); // Empanadas
        Pedido pedido2 = new Pedido(0, cliente2, Tipo.LLEVAR, Prioridad.NORMAL, 1, null);
        sistemaGestion.agregarPedido(pedido2);
        sistemaGestion.agregarPedidoACocina(platos2, Prioridad.NORMAL);

        // Pedido 3: Normal
        Cliente cliente3 = new Cliente("Ana Lopez");
        DynamicQueueADT platos3 = new DynamicQueueADT();
        platos3.add(4); // Ensalada
        platos3.add(5); // Lasaña
        platos3.add(2); // Pizza
        Nodo destino3 = new Nodo("Recoleta");
        Pedido pedido3 = new Pedido(0, cliente3, Tipo.DOMICILIO, Prioridad.NORMAL, 3, destino3);
        sistemaGestion.agregarPedido(pedido3);
        sistemaGestion.agregarPedidoACocina(platos3, Prioridad.NORMAL);

        // Pedido 4: VIP
        Cliente cliente4 = new Cliente("Carlos Gomez");
        DynamicQueueADT platos4 = new DynamicQueueADT();
        platos4.add(1); // Milanesa
        Pedido pedido4 = new Pedido(0, cliente4, Tipo.LLEVAR, Prioridad.VIP, 1, null);
        sistemaGestion.agregarPedido(pedido4);
        sistemaGestion.agregarPedidoACocina(platos4, Prioridad.VIP);

        // Pedido 5: Normal
        Cliente cliente5 = new Cliente("Sofia Martinez");
        DynamicQueueADT platos5 = new DynamicQueueADT();
        platos5.add(2); // Pizza
        platos5.add(3); // Empanadas
        Nodo destino5 = new Nodo("Belgrano");
        Pedido pedido5 = new Pedido(0, cliente5, Tipo.DOMICILIO, Prioridad.NORMAL, 2, destino5);
        sistemaGestion.agregarPedido(pedido5);
        sistemaGestion.agregarPedidoACocina(platos5, Prioridad.NORMAL);

        System.out.println("✅ 5 pedidos precargados exitosamente");
    }


    private void mostrarMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           MENÚ PRINCIPAL                      ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ 1) Acciones                                   ║");
        System.out.println("║ 2) Estadísticas                               ║");
        System.out.println("║ 0) Salir                                      ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("Elegí una opción: ");
    }

    private void procesarOpcionMenuPrincipal(String opcionElegida) {
        System.out.println();
        try {
            if ("1".equals(opcionElegida)) {
                mostrarMenuAcciones();
            } else if ("2".equals(opcionElegida)) {
                mostrarMenuEstadisticas();
            } else if ("0".equals(opcionElegida)) {
                aplicacionEjecutandose = false;
            } else {
                System.out.println("❌ Opción inválida. Por favor elegí 1, 2 o 0.");
            }
        } catch (Exception ex) {
            System.out.println("❌ Error inesperado: " + ex.getMessage());
        }
    }


    private void mostrarMenuAcciones() {
        boolean continuarEnAcciones = true;

        while (continuarEnAcciones) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║              MENÚ ACCIONES                    ║");
            System.out.println("╠════════════════════════════════════════════════╣");
            System.out.println("║ 1) Crear un nuevo pedido                      ║");
            System.out.println("║ 2) Cocinar y cumplir el siguiente pedido      ║");
            System.out.println("║ 3) Ver estado de repartidores                 ║");
            System.out.println("║ 4) Forzar entrega de pedidos despachados      ║");
            System.out.println("║ 9) Volver al menú principal                   ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.print("Elegí una opción: ");

            int opcionAccion = lectorConsola.nextInt();
            lectorConsola.nextLine();
            System.out.println();

            if (opcionAccion == 1) {
                accionCrearNuevoPedido();
            } else if (opcionAccion == 2) {
                accionCocinarYCumplirSiguiente();
            } else if (opcionAccion == 3) {
                accionVerRepartidores();
            } else if (opcionAccion == 4) {
                accionForzarEntregaDespachados();
            } else if (opcionAccion == 9) {
                continuarEnAcciones = false;
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void accionCrearNuevoPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║           CREAR NUEVO PEDIDO                  ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        System.out.print("📝 Ingresá el nombre del cliente: ");
        String nombreDelCliente = lectorConsola.nextLine();
        System.out.println();
        if (nombreDelCliente == null || nombreDelCliente.length() == 0) {
            System.out.println("❌ Nombre inválido. Operación cancelada.");
            return;
        }
        Cliente cliente = new Cliente(nombreDelCliente);

        System.out.print("⭐ ¿Es cliente VIP? (S/N): ");
        String respuestaVip = lectorConsola.nextLine();
        System.out.println();
        Prioridad prioridadDelPedido = (respuestaVip != null && (respuestaVip.equalsIgnoreCase("S") || respuestaVip.equalsIgnoreCase("SI")))
                ? Prioridad.VIP : Prioridad.NORMAL;
        System.out.println(prioridadDelPedido == Prioridad.VIP ? "✅ Cliente VIP registrado" : "✅ Cliente NORMAL registrado");

        DynamicQueueADT colaDePlatosSeleccionados = new DynamicQueueADT();
        int contadorDePlatos = 0;
        boolean seguirSeleccionandoPlatos = true;

        System.out.println("\n🍽️  PLATOS DISPONIBLES:");
        System.out.println("  1) Milanesa con puré");
        System.out.println("  2) Pizza muzzarella");
        System.out.println("  3) Empanadas");
        System.out.println("  4) Ensalada César");
        System.out.println("  5) Lasaña");
        System.out.println("  0) Terminar selección");

        while (seguirSeleccionandoPlatos) {
            System.out.print("\nSeleccioná un plato (0 para terminar): ");
            int opcionPlato = lectorConsola.nextInt();
            lectorConsola.nextLine();
            System.out.println();

            if (opcionPlato == 0) {
                seguirSeleccionandoPlatos = false;
            } else if (opcionPlato >= 1 && opcionPlato <= 5) {
                colaDePlatosSeleccionados.add(opcionPlato);
                contadorDePlatos++;

                String nombrePlato;
                if (opcionPlato == 1)      nombrePlato = "Milanesa con puré";
                else if (opcionPlato == 2) nombrePlato = "Pizza muzzarella";
                else if (opcionPlato == 3) nombrePlato = "Empanadas";
                else if (opcionPlato == 4) nombrePlato = "Ensalada César";
                else                        nombrePlato = "Lasaña";

                System.out.println("✅ Agregado: " + nombrePlato + " (Total: " + contadorDePlatos + ")");
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }

        if (contadorDePlatos == 0) {
            System.out.println("❌ No seleccionaste platos. Operación cancelada.");
            return;
        }

        System.out.println("\n🚚 Tipo de pedido:");
        System.out.println("  1) TAKEAWAY (para llevar)");
        System.out.println("  2) DELIVERY (envío a domicilio)");
        System.out.print("Opción: ");
        int opcionTipoPedido = lectorConsola.nextInt();
        lectorConsola.nextLine();
        System.out.println();

        Tipo tipoDePedido;
        Nodo nodoDestino = null;

        if (opcionTipoPedido == 2) {
            tipoDePedido = Tipo.DOMICILIO;

            System.out.println("\n📍 Barrios disponibles:");
            System.out.println("  1) Palermo");
            System.out.println("  2) Recoleta");
            System.out.println("  3) Belgrano");
            System.out.println("  4) Caballito");
            System.out.println("  5) Flores");
            System.out.print("Elegí el barrio (1-5): ");
            int opcionBarrio = lectorConsola.nextInt();
            lectorConsola.nextLine();
            System.out.println();

            String nombreBarrio;
            if      (opcionBarrio == 1) nombreBarrio = "Palermo";
            else if (opcionBarrio == 2) nombreBarrio = "Recoleta";
            else if (opcionBarrio == 3) nombreBarrio = "Belgrano";
            else if (opcionBarrio == 4) nombreBarrio = "Caballito";
            else if (opcionBarrio == 5) nombreBarrio = "Flores";
            else                         nombreBarrio = null;

            if (nombreBarrio == null) {
                System.out.println("❌ Barrio inválido. Operación cancelada.");
                return;
            }

            nodoDestino = new Nodo(nombreBarrio);
            System.out.println("✅ Destino: " + nombreBarrio);
        } else {
            tipoDePedido = Tipo.LLEVAR;
            System.out.println("✅ Pedido para LLEVAR");
        }

        Pedido nuevoPedido = new Pedido(0, cliente, tipoDePedido, prioridadDelPedido, contadorDePlatos, nodoDestino);
        int idGenerado = sistemaGestion.agregarPedido(nuevoPedido);
        sistemaGestion.agregarPedidoACocina(colaDePlatosSeleccionados, prioridadDelPedido);

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║         ✅ PEDIDO CREADO EXITOSAMENTE          ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ ID Pedido: #" + idGenerado);
        System.out.println("║ Cliente: " + nombreDelCliente);
        System.out.println("║ Prioridad: " + prioridadDelPedido);
        System.out.println("║ Tipo: " + tipoDePedido);
        System.out.println("║ Cantidad de platos: " + contadorDePlatos);
        System.out.println("║ Estado: PENDIENTE");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    private void accionCocinarYCumplirSiguiente() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   COCINAR Y CUMPLIR (VIP→NoVIP, 1 por vez)    ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        int idProcesado = sistemaGestion.cocinarYCumplirSiguientePedido();
        if (idProcesado == -1) {
            System.out.println("⚠️  No hay pedidos pendientes por prioridad.");
            return;
        }
        if (idProcesado == -2) {
            System.out.println("❌ No hay repartidores disponibles para DELIVERY.");
            return;
        }

        Pedido pedidoProcesado = sistemaGestion.obtenerPedidoPorId(idProcesado);
        if (pedidoProcesado != null) {
            System.out.println("✅ Pedido #" + idProcesado + " procesado:");
            System.out.println("   Tipo: " + pedidoProcesado.getTipo() + " | Prioridad: " + pedidoProcesado.getPrioridad() + " | Estado: " + pedidoProcesado.getEstado());
            if (pedidoProcesado.getTipo() == Tipo.LLEVAR && pedidoProcesado.getEstado() == Estado.FINALIZADO) {
                System.out.println("   → Takeaway finalizado.");
            }
        }
    }

    private void accionVerRepartidores() {
        System.out.println("== Repartidores ==");
        org.uade.services.ServicioDespacho.NodoRepartidor nodoRepartidor = sistemaGestion.getRepartidoresHead();
        if (nodoRepartidor == null) {
            System.out.println("  (no hay repartidores)");
            return;
        }
        while (nodoRepartidor != null) {
            System.out.println("  ID " + nodoRepartidor.id + " | " + nodoRepartidor.repartidor.getNombre()
                    + " | entregas: " + nodoRepartidor.repartidor.getEntregasRealizadas());
            nodoRepartidor = nodoRepartidor.siguiente;
        }
    }

    private void accionForzarEntregaDespachados() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   FORZAR ENTREGA DE PEDIDOS DESPACHADOS       ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        boolean seRealizoEntrega = sistemaGestion.forzarEntregaDespachados();

        if (seRealizoEntrega) {
            System.out.println("✅ Se realizó la entrega de los pedidos despachados");
        } else {
            System.out.println("⚠️  No hay pedidos despachados pendientes de entregar");
        }
    }

    private void mostrarMenuEstadisticas() {
        boolean continuarEnEstadisticas = true;

        while (continuarEnEstadisticas) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║           MENÚ ESTADÍSTICAS                   ║");
            System.out.println("╠════════════════════════════════════════════════╣");
            System.out.println("║ 1) Nº de pedidos pendientes de ser despachados║");
            System.out.println("║ 2) Nº de pedidos finalizados                  ║");
            System.out.println("║ 3) Pedidos entregados por repartidor          ║");
            System.out.println("║ 4) Cliente con mayor número de pedidos        ║");
            System.out.println("║    (se mide por el pedido con más platos)     ║");
            System.out.println("║ 9) Volver                                     ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.print("Elegí una opción: ");

            int opcionEstadistica = lectorConsola.nextInt();
            lectorConsola.nextLine();
            System.out.println();

            if (opcionEstadistica == 1) {
                sistemaGestion.mostrarPendientesPorDespachar();
            } else if (opcionEstadistica == 2) {
                sistemaGestion.mostrarFinalizados();
            } else if (opcionEstadistica == 3) {
                sistemaGestion.mostrarEntregasPorRepartidor();
            } else if (opcionEstadistica == 4) {
                sistemaGestion.mostrarClienteTop();
            } else if (opcionEstadistica == 9) {
                continuarEnEstadisticas = false;
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }
    }
}
