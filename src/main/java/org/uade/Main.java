package org.uade;

import org.uade.entidades.Nodo;
import org.uade.entidades.Cliente;
import org.uade.entidades.Pedido;
import org.uade.entidades.Plato;
import org.uade.entidades.Repartidor;
import org.uade.services.SistemaGestion;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;
import org.uade.enums.Estado;
import org.uade.structure.definition.QueueADT;
import org.uade.structure.implementation.fixed.StaticQueueADT;
import org.uade.util.QueueADTUtil;

import java.util.Scanner;

public class Main {
    private static SistemaGestion sistema;
    private static boolean running = true;
    private final Scanner scanner = new Scanner(System.in);
    QueueADT pedido = new StaticQueueADT();

    public static void main(String[] args) {
        Main app = new Main();
        sistema = new SistemaGestion();
        Nodo restaurante = new Nodo("Restaurante");

        inicializarPlatos();
        inicializarRepartidores(restaurante);

        System.out.println("=================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE PEDIDOS - RESTAURANTE");
        System.out.println("=================================================");
        System.out.println("Sistema inicializado correctamente");
        System.out.println("=================================================\n");

        while (running) {
            mostrarMenuPrincipal();
            String opcion = app.readLine();
            app.procesarOpcionPrincipal(opcion);
        }

        System.out.println("\n👋 ¡Gracias por usar el sistema! Hasta pronto.");
    }

    /* =======================
       Helpers de entrada
       ======================= */

    private String readLine() {
        String s = scanner.nextLine();
        return (s == null) ? null : s.trim();
    }

    private int readInt() {
        String s = readLine();
        if (s == null || s.isEmpty()) return -1;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /* =======================
       Inicialización
       ======================= */

    private static void inicializarPlatos() {
        sistema.registrarPlato(new Plato("Milanesa con puré", 10));
        sistema.registrarPlato(new Plato("Pizza muzzarella", 15));
        sistema.registrarPlato(new Plato("Empanadas", 20));
        sistema.registrarPlato(new Plato("Ensalada César", 25));
        sistema.registrarPlato(new Plato("Lasaña", 30));
    }

    private static void inicializarRepartidores(Nodo restaurante) {
        sistema.registrarRepartidor(new Repartidor(0, "Juan", restaurante));
        sistema.registrarRepartidor(new Repartidor(0, "María", restaurante));
        sistema.registrarRepartidor(new Repartidor(0, "Pedro", restaurante));
    }

    /* =======================
       Menú principal
       ======================= */

    private static void mostrarMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           MENÚ PRINCIPAL                      ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ 1) Acciones                                   ║");
        System.out.println("║ 2) Estadísticas                               ║");
        System.out.println("║ 0) Salir                                      ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("Elegí una opción: ");
    }

    private void procesarOpcionPrincipal(String opcion) {
        System.out.println();

        try {
            if ("1".equals(opcion)) {
                menuAcciones();
            } else if ("2".equals(opcion)) {
                menuEstadisticas();
            } else if ("0".equals(opcion)) {
                running = false;
            } else {
                System.out.println("❌ Opción inválida. Por favor elegí 1, 2 o 0.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    /* =======================
       Menú Acciones
       ======================= */

    private void menuAcciones() {
        boolean enAcciones = true;

        while (enAcciones) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║              MENÚ ACCIONES                    ║");
            System.out.println("╠════════════════════════════════════════════════╣");
            System.out.println("║ 1) Crear pedido                               ║");
            System.out.println("║ 2) Mandar pedido (despachar)                  ║");
            System.out.println("║ 3) Terminar pedido                            ║");
            System.out.println("║ 9) Volver                                     ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.print("Elegí una opción: ");

            int opcion = readInt();
            System.out.println();

            if (opcion == 1) {
                accionCrearPedido();
            } else if (opcion == 2) {
                accionMandarPedido();
            } else if (opcion == 3) {
                accionTerminarPedido();
            } else if (opcion == 9) {
                enAcciones = false;
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void accionCrearPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║           CREAR NUEVO PEDIDO                  ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        System.out.print("📝 Ingresá el nombre del cliente: ");
        String nombreCliente = readLine();
        if (nombreCliente == null || nombreCliente.length() == 0) {
            System.out.println("❌ Nombre inválido. Operación cancelada.");
            return;
        }

        Cliente cliente = new Cliente(nombreCliente);

        System.out.print("⭐ ¿Es cliente VIP? (S/N): ");
        String respuestaVIP = readLine();
        Prioridad prioridad = (respuestaVIP != null && (respuestaVIP.equalsIgnoreCase("S") || respuestaVIP.equalsIgnoreCase("SI")))
                ? Prioridad.VIP : Prioridad.NORMAL;

        System.out.println(prioridad == Prioridad.VIP ? "✅ Cliente VIP registrado" : "✅ Cliente NORMAL registrado");

        // Selección de platos en loop
        int cantidadPlatos = 0;
        boolean seguirSeleccionando = true;

        System.out.println("\n🍽️  PLATOS DISPONIBLES:");
        System.out.println("  1) Milanesa con puré");
        System.out.println("  2) Pizza muzzarella");
        System.out.println("  3) Empanadas");
        System.out.println("  4) Ensalada César");
        System.out.println("  5) Lasaña");
        System.out.println("  0) Terminar selección");


        while (seguirSeleccionando) {
            System.out.print("\nSeleccioná un plato (0 para terminar): ");
            int opcionPlato = readInt();

            if (opcionPlato == 0) {
                seguirSeleccionando = false;
            } else if (opcionPlato >= 1 && opcionPlato <= 5) {
                cantidadPlatos++;
                String nombrePlato = switch (opcionPlato) {
                    case 1 -> "Milanesa con puré";
                    case 2 -> "Pizza muzzarella";
                    case 3 -> "Empanadas";
                    case 4 -> "Ensalada César";
                    case 5 -> "Lasaña";
                    default -> "";
                };
                pedido.add(opcionPlato);
                System.out.println("✅ Agregado: " + nombrePlato + " (Total: " + cantidadPlatos + ")");
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }
        sistema.agregarPedidoAPreparacion(pedido, prioridad);




        if (cantidadPlatos == 0) {
            System.out.println("❌ No se seleccionaron platos. Operación cancelada.");
            return;
        }

        System.out.println("\n🚚 Tipo de pedido:");
        System.out.println("  1) TAKEAWAY (para llevar)");
        System.out.println("  2) DELIVERY (envío a domicilio)");
        System.out.print("Opción: ");
        int opcionTipo = readInt();

        Tipo tipo;
        Nodo destino = null;

        if (opcionTipo == 2) {
            tipo = Tipo.DOMICILIO;

            System.out.println("\n📍 Barrios disponibles para delivery:");
            System.out.println("  1) Palermo");
            System.out.println("  2) Recoleta");
            System.out.println("  3) Belgrano");
            System.out.println("  4) Caballito");
            System.out.println("  5) Flores");

            System.out.print("Elegí el barrio (1-5): ");
            int opcionBarrio = readInt();

            String barrioElegido;
            switch (opcionBarrio) {
                case 1 -> barrioElegido = "Palermo";
                case 2 -> barrioElegido = "Recoleta";
                case 3 -> barrioElegido = "Belgrano";
                case 4 -> barrioElegido = "Caballito";
                case 5 -> barrioElegido = "Flores";
                default -> {
                    System.out.println("❌ Barrio inválido. Operación cancelada.");
                    return;
                }
            }

            destino = new Nodo(barrioElegido);
            System.out.println("✅ Destino: " + barrioElegido);
        } else {
            tipo = Tipo.LLEVAR;
            System.out.println("✅ Pedido para LLEVAR");
        }

        Pedido nuevoPedido = new Pedido(0, cliente, tipo, prioridad, cantidadPlatos, destino);
        int idPedido = sistema.registrarPedido(nuevoPedido);

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║         ✅ PEDIDO CREADO EXITOSAMENTE          ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ ID Pedido: #" + idPedido);
        System.out.println("║ Cliente: " + nombreCliente);
        System.out.println("║ Prioridad: " + prioridad);
        System.out.println("║ Tipo: " + tipo);
        System.out.println("║ Cantidad de platos: " + cantidadPlatos);
        System.out.println("║ Estado: CREADO (pendiente de despacho)");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    /* =======================
       Mandar / Terminar pedidos
       ======================= */

    private void accionMandarPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║          MANDAR PEDIDO (DESPACHAR)            ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        Pedido[] pedidosPendientes = sistema.obtenerPedidosPorEstado(Estado.PENDIENTE);

        if (pedidosPendientes == null || pedidosPendientes.length == 0) {
            System.out.println("⚠️  No hay pedidos pendientes de despachar.");
            return;
        }

        for (Pedido p : pedidosPendientes) {
            if (p != null)
                System.out.println("  #" + p.getId() + " - Cliente: " + p.getCliente().getNombre() +
                        " | Tipo: " + p.getTipo() + " | Prioridad: " + p.getPrioridad());
        }

        System.out.print("\n📦 Ingresá el ID del pedido a despachar: ");
        int idPedido = readInt();

        Pedido pedido = sistema.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado.");
            return;
        }

        if (pedido.getEstado() != Estado.PENDIENTE) {
            System.out.println("❌ El pedido no está en estado PENDIENTE.");
            return;
        }

        System.out.println("\n🚴 REPARTIDORES DISPONIBLES:");
        System.out.println("  1) Juan");
        System.out.println("  2) María");
        System.out.println("  3) Pedro");

        System.out.print("\nElegí el repartidor (ID): ");
        int idRepartidor = readInt();

        boolean exito = sistema.despacharPedido(idPedido, idRepartidor);

        if (exito) {
            System.out.println("\n✅ Pedido #" + idPedido + " DESPACHADO exitosamente.");
            System.out.println("   Repartidor asignado: " + sistema.obtenerRepartidorPorId(idRepartidor).getNombre());
        } else {
            System.out.println("❌ Error al despachar el pedido.");
        }
    }

    private void accionTerminarPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║            TERMINAR PEDIDO                    ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        Pedido[] pedidosDespachados = sistema.obtenerPedidosPorEstado(Estado.DESPACHADO);

        if (pedidosDespachados == null || pedidosDespachados.length == 0) {
            System.out.println("⚠️  No hay pedidos despachados para terminar.");
            return;
        }

        for (Pedido p : pedidosDespachados) {
            if (p != null)
                System.out.println("  #" + p.getId() + " - Cliente: " + p.getCliente().getNombre() +
                        " | Tipo: " + p.getTipo());
        }

        System.out.print("\n✅ Ingresá el ID del pedido a terminar: ");
        int idPedido = readInt();

        Pedido pedido = sistema.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado.");
            return;
        }

        if (pedido.getEstado() != Estado.DESPACHADO) {
            System.out.println("❌ El pedido no está en estado DESPACHADO.");
            return;
        }

        boolean exito = sistema.finalizarPedido(idPedido);

        if (exito) {
            System.out.println("\n✅ Pedido #" + idPedido + " FINALIZADO exitosamente.");
        } else {
            System.out.println("❌ Error al finalizar el pedido.");
        }
    }

    /* =======================
       Menú Estadísticas
       ======================= */

    private void menuEstadisticas() {
        boolean enEstadisticas = true;

        while (enEstadisticas) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║           MENÚ ESTADÍSTICAS                   ║");
            System.out.println("╠════════════════════════════════════════════════╣");
            System.out.println("║ 1) Pedidos pendientes a despachar            ║");
            System.out.println("║ 2) Número de pedidos finalizados             ║");
            System.out.println("║ 3) Pedidos por repartidor                    ║");
            System.out.println("║ 4) Cliente con mayor número de pedidos       ║");
            System.out.println("║ 9) Volver                                     ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.print("Elegí una opción: ");

            int opcion = readInt();
            System.out.println();

            if (opcion == 1) {
                estadisticaPedidosPendientes();
            } else if (opcion == 2) {
                estadisticaPedidosFinalizados();
            } else if (opcion == 3) {
                estadisticaPedidosPorRepartidor();
            } else if (opcion == 4) {
                estadisticaClienteConMasPedidos();
            } else if (opcion == 9) {
                enEstadisticas = false;
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }
    }

    private static void estadisticaPedidosPendientes() {
        int cantidad = sistema.pedidosPendientes();
        System.out.println("\n📋 Pedidos pendientes: " + cantidad);
    }

    private static void estadisticaPedidosFinalizados() {
        int cantidad = sistema.pedidosFinalizados();
        System.out.println("\n✅ Pedidos finalizados: " + cantidad);
    }

    private static void estadisticaPedidosPorRepartidor() {
        Repartidor[] repartidores = sistema.obtenerTodosRepartidores();
        for (Repartidor r : repartidores) {
            if (r != null) {
                int despachados = sistema.contarPedidosPorRepartidorYEstado(r.getId(), Estado.DESPACHADO);
                int finalizados = sistema.contarPedidosPorRepartidorYEstado(r.getId(), Estado.FINALIZADO);
                System.out.println("🚴 " + r.getNombre() + " | Despachados: " + despachados + " | Finalizados: " + finalizados);
            }
        }
    }

    private static void estadisticaClienteConMasPedidos() {
        Cliente clienteTop = sistema.obtenerClienteConMasPedidos();
        if (clienteTop != null)
            System.out.println("👤 Cliente con más pedidos: " + clienteTop.getNombre() + " (" + clienteTop.getPedidosRealizados() + ")");
        else
            System.out.println("⚠️  No hay clientes registrados.");
    }
}
