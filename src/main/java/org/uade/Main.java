package org.uade;

import org.uade.datos.DataSeeder;
import org.uade.entidades.Nodo;
import org.uade.entidades.Cliente;
import org.uade.entidades.Pedido;
import org.uade.entidades.Plato;
import org.uade.entidades.Repartidor;
import org.uade.services.SistemaGestion;
import org.uade.enums.Prioridad;
import org.uade.enums.Tipo;
import org.uade.enums.Estado;
import org.uade.exception.EmptyADTException;
import org.uade.exception.FullADTException;

public class Main {
    private static SistemaGestion sistema;
    private static boolean running = true;

    // Platos disponibles
    private static final String[] PLATOS_MENU = {
        "Milanesa con puré",
        "Pizza muzzarella",
        "Empanadas",
        "Ensalada César",
        "Lasaña"
    };

    // Barrios disponibles para delivery
    private static final String[] BARRIOS = {
        "Palermo",
        "Recoleta",
        "Belgrano",
        "Caballito",
        "Flores"
    };

    // Repartidores disponibles
    private static final String[] NOMBRES_REPARTIDORES = {
        "Juan",
        "María",
        "Pedro"
    };

    public static void main(String[] args) {
        sistema = new SistemaGestion();
        Nodo restaurante = new Nodo("Restaurante");

        // Inicializar platos
        inicializarPlatos();

        // Inicializar repartidores
        inicializarRepartidores(restaurante);

        System.out.println("=================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE PEDIDOS - RESTAURANTE");
        System.out.println("=================================================");
        System.out.println("Sistema inicializado correctamente");
        System.out.println("=================================================\n");

        while (running) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            procesarOpcionPrincipal(opcion);
        }

        System.out.println("\n👋 ¡Gracias por usar el sistema! Hasta pronto.");
    }

    private static void inicializarPlatos() {
        int i = 0;
        while (i < PLATOS_MENU.length) {
            Plato plato = new Plato(PLATOS_MENU[i], 10 + i * 5);
            sistema.registrarPlato(plato);
            i++;
        }
    }

    private static void inicializarRepartidores(Nodo restaurante) {
        int i = 0;
        while (i < NOMBRES_REPARTIDORES.length) {
            Repartidor r = new Repartidor(0, NOMBRES_REPARTIDORES[i], restaurante);
            sistema.registrarRepartidor(r);
            i++;
        }
    }

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

    private static void procesarOpcionPrincipal(int opcion) {
        System.out.println();

        try {
            if (opcion == 1) {
                menuAcciones();
            } else if (opcion == 2) {
                menuEstadisticas();
            } else if (opcion == 0) {
                running = false;
            } else {
                System.out.println("❌ Opción inválida. Por favor elegí 1, 2 o 0.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    // ==================== MENÚ ACCIONES ====================

    private static void menuAcciones() {
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

            int opcion = leerOpcion();
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

    private static void accionCrearPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║           CREAR NUEVO PEDIDO                  ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // 1. Nombre del cliente
        System.out.print("📝 Ingresá el nombre del cliente: ");
        String nombreCliente = leerTexto();
        if (nombreCliente == null || nombreCliente.length() == 0) {
            System.out.println("❌ Nombre inválido. Operación cancelada.");
            return;
        }

        Cliente cliente = new Cliente(nombreCliente);

        // 2. ¿Es VIP?
        System.out.print("⭐ ¿Es cliente VIP? (S/N): ");
        String respuestaVIP = leerTexto();
        Prioridad prioridad = Prioridad.NORMAL;
        if (respuestaVIP != null && (respuestaVIP.equalsIgnoreCase("S") || respuestaVIP.equalsIgnoreCase("SI"))) {
            prioridad = Prioridad.VIP;
            System.out.println("✅ Cliente VIP registrado");
        } else {
            System.out.println("✅ Cliente NORMAL registrado");
        }

        // 3. Selección de platos
        System.out.println("\n🍽️  PLATOS DISPONIBLES:");
        int i = 0;
        while (i < PLATOS_MENU.length) {
            System.out.println("  " + (i + 1) + ") " + PLATOS_MENU[i]);
            i++;
        }

        System.out.print("\n📦 ¿Cuántos platos en total querés agregar? (1-10): ");
        int cantidadPlatos = leerOpcion();

        if (cantidadPlatos < 1 || cantidadPlatos > 10) {
            System.out.println("❌ Cantidad inválida. Operación cancelada.");
            return;
        }

        // 4. Tipo de pedido
        System.out.println("\n🚚 Tipo de pedido:");
        System.out.println("  1) TAKEAWAY (para llevar)");
        System.out.println("  2) DELIVERY (envío a domicilio)");
        System.out.print("Opción: ");
        int opcionTipo = leerOpcion();

        Tipo tipo;
        Nodo destino = null;

        if (opcionTipo == 2) {
            tipo = Tipo.DOMICILIO;

            // 5. Selección de barrio
            System.out.println("\n📍 Barrios disponibles para delivery:");
            int j = 0;
            while (j < BARRIOS.length) {
                System.out.println("  " + (j + 1) + ") " + BARRIOS[j]);
                j++;
            }

            System.out.print("Elegí el barrio (1-" + BARRIOS.length + "): ");
            int opcionBarrio = leerOpcion();

            if (opcionBarrio < 1 || opcionBarrio > BARRIOS.length) {
                System.out.println("❌ Barrio inválido. Operación cancelada.");
                return;
            }

            destino = new Nodo(BARRIOS[opcionBarrio - 1]);
            System.out.println("✅ Destino: " + BARRIOS[opcionBarrio - 1]);
        } else {
            tipo = Tipo.LLEVAR;
            System.out.println("✅ Pedido para LLEVAR");
        }

        // Crear el pedido en estado CREADO (PENDIENTE)
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

    private static void accionMandarPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║          MANDAR PEDIDO (DESPACHAR)            ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Listar pedidos en estado PENDIENTE (CREADO)
        Pedido[] pedidosPendientes = sistema.obtenerPedidosPorEstado(Estado.PENDIENTE);

        if (pedidosPendientes == null || pedidosPendientes.length == 0) {
            System.out.println("⚠️  No hay pedidos pendientes de despachar.");
            return;
        }

        System.out.println("📋 PEDIDOS PENDIENTES DE DESPACHAR:");
        int i = 0;
        while (i < pedidosPendientes.length) {
            if (pedidosPendientes[i] != null) {
                Pedido p = pedidosPendientes[i];
                System.out.println("  #" + p.getId() + " - Cliente: " + p.getCliente().getNombre() +
                    " | Tipo: " + p.getTipo() + " | Prioridad: " + p.getPrioridad());
            }
            i++;
        }

        System.out.print("\n📦 Ingresá el ID del pedido a despachar: ");
        int idPedido = leerOpcion();

        // Verificar que el pedido existe y está pendiente
        Pedido pedido = sistema.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado.");
            return;
        }

        if (pedido.getEstado() != Estado.PENDIENTE) {
            System.out.println("❌ El pedido no está en estado PENDIENTE.");
            return;
        }

        // Mostrar lista de repartidores
        System.out.println("\n🚴 REPARTIDORES DISPONIBLES:");
        Repartidor[] repartidores = sistema.obtenerTodosRepartidores();
        int j = 0;
        while (j < repartidores.length) {
            if (repartidores[j] != null) {
                System.out.println("  " + repartidores[j].getId() + ") " + repartidores[j].getNombre());
            }
            j++;
        }

        System.out.print("\nElegí el repartidor (ID): ");
        int idRepartidor = leerOpcion();

        // Asignar repartidor y cambiar estado
        boolean exito = sistema.despacharPedido(idPedido, idRepartidor);

        if (exito) {
            System.out.println("\n✅ Pedido #" + idPedido + " DESPACHADO exitosamente.");
            System.out.println("   Repartidor asignado: " + sistema.obtenerRepartidorPorId(idRepartidor).getNombre());
        } else {
            System.out.println("❌ Error al despachar el pedido.");
        }
    }

    private static void accionTerminarPedido() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║            TERMINAR PEDIDO                    ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // Listar pedidos en estado DESPACHADO
        Pedido[] pedidosDespachados = sistema.obtenerPedidosPorEstado(Estado.DESPACHADO);

        if (pedidosDespachados == null || pedidosDespachados.length == 0) {
            System.out.println("⚠️  No hay pedidos despachados para terminar.");
            return;
        }

        System.out.println("📋 PEDIDOS DESPACHADOS:");
        int i = 0;
        while (i < pedidosDespachados.length) {
            if (pedidosDespachados[i] != null) {
                Pedido p = pedidosDespachados[i];
                System.out.println("  #" + p.getId() + " - Cliente: " + p.getCliente().getNombre() +
                    " | Tipo: " + p.getTipo());
            }
            i++;
        }

        System.out.print("\n✅ Ingresá el ID del pedido a terminar: ");
        int idPedido = leerOpcion();

        // Verificar que el pedido existe y está despachado
        Pedido pedido = sistema.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado.");
            return;
        }

        if (pedido.getEstado() != Estado.DESPACHADO) {
            System.out.println("❌ El pedido no está en estado DESPACHADO.");
            return;
        }

        // Cambiar estado a FINALIZADO
        boolean exito = sistema.finalizarPedido(idPedido);

        if (exito) {
            System.out.println("\n✅ Pedido #" + idPedido + " FINALIZADO exitosamente.");
        } else {
            System.out.println("❌ Error al finalizar el pedido.");
        }
    }

    // ==================== MENÚ ESTADÍSTICAS ====================

    private static void menuEstadisticas() {
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

            int opcion = leerOpcion();
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

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║     PEDIDOS PENDIENTES A DESPACHAR            ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║                                                ║");
        System.out.println("║  📋 Cantidad: " + cantidad + " pedido(s)");
        System.out.println("║                                                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    private static void estadisticaPedidosFinalizados() {
        int cantidad = sistema.pedidosFinalizados();

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║        PEDIDOS FINALIZADOS                    ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║                                                ║");
        System.out.println("║  ✅ Cantidad: " + cantidad + " pedido(s)");
        System.out.println("║                                                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    private static void estadisticaPedidosPorRepartidor() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║      PEDIDOS POR REPARTIDOR                   ║");
        System.out.println("╠════════════════════════════════════════════════╣");

        Repartidor[] repartidores = sistema.obtenerTodosRepartidores();

        int i = 0;
        while (i < repartidores.length) {
            if (repartidores[i] != null) {
                Repartidor r = repartidores[i];
                int despachados = sistema.contarPedidosPorRepartidorYEstado(r.getId(), Estado.DESPACHADO);
                int finalizados = sistema.contarPedidosPorRepartidorYEstado(r.getId(), Estado.FINALIZADO);

                System.out.println("║                                                ║");
                System.out.println("║ 🚴 " + r.getNombre());
                System.out.println("║    Despachados: " + despachados + " | Finalizados: " + finalizados);
            }
            i++;
        }

        System.out.println("║                                                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    private static void estadisticaClienteConMasPedidos() {
        Cliente clienteTop = sistema.obtenerClienteConMasPedidos();

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║    CLIENTE CON MÁS PEDIDOS                    ║");
        System.out.println("╠════════════════════════════════════════════════╣");

        if (clienteTop != null) {
            System.out.println("║                                                ║");
            System.out.println("║ 👤 Cliente: " + clienteTop.getNombre());
            System.out.println("║ 📦 Cantidad de pedidos: " + clienteTop.getPedidosRealizados());
            System.out.println("║                                                ║");
        } else {
            System.out.println("║                                                ║");
            System.out.println("║  ⚠️  No hay clientes registrados               ║");
            System.out.println("║                                                ║");
        }

        System.out.println("╚════════════════════════════════════════════════╝");
    }

    // ==================== UTILIDADES ====================

    private static int leerOpcion() {
        try {
            byte[] buffer = new byte[10];
            int bytesRead = System.in.read(buffer);
            if (bytesRead <= 0) return -1;

            int length = 0;
            while (length < bytesRead && buffer[length] != '\n' && buffer[length] != '\r') {
                length++;
            }

            int resultado = 0;
            boolean negativo = false;
            int inicio = 0;

            if (length > 0 && buffer[0] == '-') {
                negativo = true;
                inicio = 1;
            }

            int i = inicio;
            while (i < length) {
                if (buffer[i] >= '0' && buffer[i] <= '9') {
                    resultado = resultado * 10 + (buffer[i] - '0');
                } else {
                    return -1;
                }
                i++;
            }

            return negativo ? -resultado : resultado;
        } catch (Exception e) {
            return -1;
        }
    }

    private static String leerTexto() {
        try {
            byte[] buffer = new byte[100];
            int bytesRead = System.in.read(buffer);
            if (bytesRead <= 0) return null;

            int length = 0;
            while (length < bytesRead && buffer[length] != '\n' && buffer[length] != '\r') {
                length++;
            }

            String resultado = "";
            int i = 0;
            while (i < length) {
                resultado = resultado + (char) buffer[i];
                i++;
            }

            return resultado.trim();
        } catch (Exception e) {
            return null;
        }
    }
}

