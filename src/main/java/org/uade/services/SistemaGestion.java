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
    private final Cliente[]    clientesStore = new Cliente[100];
    private final Plato[]      platosStore   = new Plato[50];

    private int nextPedidoSlot = 1;
    private int nextRepSlot    = 1;
    private int nextClienteSlot = 0;
    private int nextPlatoSlot = 0;

    private final SimpleDictionaryADT diccPedidos      = new StaticSimpleDictionaryADT();
    private final SimpleDictionaryADT diccRepartidores = new StaticSimpleDictionaryADT();
    private final SimpleDictionaryADT diccClientes     = new StaticSimpleDictionaryADT(); // nombre -> slot
    private final SimpleDictionaryADT diccPlatos       = new StaticSimpleDictionaryADT(); // nombre hash -> slot

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

        // Registrar cliente si no existe
        registrarCliente(p.getCliente());
        p.getCliente().incrementarPedidos();

        return id;
    }

    public void registrarCliente(Cliente c) {
        int hash = hashNombre(c.getNombre());
        if (!setClientes.exist(hash)) {
            int slot = nextClienteSlot++;
            clientesStore[slot] = c;
            diccClientes.add(hash, slot);
            setClientes.add(hash);
        }
    }

    public void registrarPlato(Plato p) {
        int hash = hashNombre(p.getNombre());
        if (!setPlatos.exist(hash)) {
            int slot = nextPlatoSlot++;
            platosStore[slot] = p;
            diccPlatos.add(hash, slot);
            setPlatos.add(hash);
        }
    }

    private int hashNombre(String nombre) {
        // Hash simple para convertir nombre en int
        int hash = 0;
        int i = 0;
        while (i < nombre.length()) {
            hash = hash * 31 + nombre.charAt(i);
            i++;
        }
        return hash < 0 ? -hash : hash;
    }

    public int mandarPedidosACocina(int cantidad) {
        int enviados = 0;
        int i = 0;
        while (i < cantidad && !pqPedidos.isEmpty()) {
            int idPedido = pqPedidos.getElement();
            pqPedidos.remove();

            if (!pedidoExiste(idPedido)) {
                i++;
                continue;
            }
            Pedido p = pedidoById(idPedido);
            p.setEstado(Estado.EN_PREPARACION);

            int j = 0;
            while (j < p.getPlatosTotales()) {
                colaCocina.add(idPedido);
                j++;
            }
            enviados++;
            i++;
        }
        return enviados;
    }

    public boolean procesarPlatoListo() {
        if (colaCocina.isEmpty()) return false;
        int idPedido = colaCocina.getElement();
        colaCocina.remove();

        if (!pedidoExiste(idPedido)) return false;
        Pedido p = pedidoById(idPedido);
        p.marcarPlatoListo();
        if (p.estaCompleto()) {
            p.setEstado(Estado.LISTO);
            colaListos.add(idPedido);
        }
        return true;
    }

    public int asignarPedidosListos() {
        int asignados = 0;
        while (!colaListos.isEmpty() && !poolRepartidores.isEmpty()) {
            int idPedido = colaListos.getElement();     colaListos.remove();
            int idRep    = poolRepartidores.getElement(); poolRepartidores.remove();

            if (!pedidoExiste(idPedido) || !repExiste(idRep)) continue;

            Repartidor r = repById(idRep);
            r.getPedidosAsignados().add(idPedido);
            pedidoById(idPedido).setEstado(Estado.DESPACHADO);
            asignados++;
        }
        return asignados;
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

    public boolean entregarSiguientePorId(int idRepartidor) {
        if (!repExiste(idRepartidor)) return false;
        Repartidor r = repById(idRepartidor);
        if (r.getPedidosAsignados().isEmpty()) return false;
        entregarSiguiente(r);
        return true;
    }

    public int pedidosPendientes()  { return contarPorEstado(Estado.PENDIENTE); }
    public int pedidosEnCocina()    { return contarPorEstado(Estado.EN_PREPARACION); }
    public int pedidosListos()      { return contarPorEstado(Estado.LISTO); }
    public int pedidosDespachados() { return contarPorEstado(Estado.DESPACHADO); }
    public int pedidosFinalizados() { return contarPorEstado(Estado.FINALIZADO); }

    public int totalPedidos() {
        return nextPedidoId - 1;
    }

    public void reiniciarSistema() {
        // Limpiar todos los pedidos
        int i = 0;
        while (i < 256) {
            pedidosStore[i] = null;
            i++;
        }

        // Limpiar y reiniciar repartidores (mantener los repartidores pero limpiar sus colas)
        int slot = 1;
        while (slot < nextRepSlot) {
            Repartidor r = repsStore[slot];
            if (r != null) {
                // Vaciar la cola de pedidos asignados
                while (!r.getPedidosAsignados().isEmpty()) {
                    r.getPedidosAsignados().remove();
                }
            }
            slot++;
        }

        // Limpiar diccionarios
        SetADT keysPedidos = diccPedidos.getKeys();
        while (!keysPedidos.isEmpty()) {
            int key = keysPedidos.choose();
            diccPedidos.remove(key);
            keysPedidos.remove(key);
        }

        // Limpiar colas
        while (!pqPedidos.isEmpty()) {
            pqPedidos.remove();
        }
        while (!colaCocina.isEmpty()) {
            colaCocina.remove();
        }
        while (!colaListos.isEmpty()) {
            colaListos.remove();
        }

        // Reiniciar pool de repartidores disponibles
        while (!poolRepartidores.isEmpty()) {
            poolRepartidores.remove();
        }
        slot = 1;
        while (slot < nextRepSlot) {
            Repartidor r = repsStore[slot];
            if (r != null) {
                poolRepartidores.add(r.getId());
            }
            slot++;
        }

        // Reiniciar contadores
        nextPedidoSlot = 1;
        nextPedidoId = 1;
    }

    public int cantidadRepartidores() {
        return nextRepId - 1;
    }

    public Pedido[] obtenerPedidosPorEstado(Estado estado) {
        // Contar cuántos pedidos hay en ese estado
        int count = 0;
        int slot = 1;
        while (slot < nextPedidoSlot) {
            if (pedidosStore[slot] != null && pedidosStore[slot].getEstado() == estado) {
                count++;
            }
            slot++;
        }

        if (count == 0) return new Pedido[0];

        // Crear array con los pedidos encontrados
        Pedido[] resultado = new Pedido[count];
        int idx = 0;
        slot = 1;
        while (slot < nextPedidoSlot) {
            if (pedidosStore[slot] != null && pedidosStore[slot].getEstado() == estado) {
                resultado[idx] = pedidosStore[slot];
                idx++;
            }
            slot++;
        }

        return resultado;
    }

    public Pedido obtenerPedidoPorId(int id) {
        if (!pedidoExiste(id)) return null;
        return pedidoById(id);
    }

    public Repartidor[] obtenerTodosRepartidores() {
        Repartidor[] resultado = new Repartidor[nextRepSlot - 1];
        int idx = 0;
        int slot = 1;
        while (slot < nextRepSlot) {
            if (repsStore[slot] != null) {
                resultado[idx] = repsStore[slot];
                idx++;
            }
            slot++;
        }
        return resultado;
    }

    public Repartidor obtenerRepartidorPorId(int id) {
        if (!repExiste(id)) return null;
        return repById(id);
    }

    public boolean despacharPedido(int idPedido, int idRepartidor) {
        if (!pedidoExiste(idPedido) || !repExiste(idRepartidor)) return false;

        Pedido pedido = pedidoById(idPedido);
        if (pedido.getEstado() != Estado.PENDIENTE) return false;

        Repartidor repartidor = repById(idRepartidor);

        // Cambiar estado a DESPACHADO
        pedido.setEstado(Estado.DESPACHADO);

        // Asignar repartidor (guardar en alguna estructura si es necesario)
        // Por ahora solo cambiamos el estado

        return true;
    }

    public boolean finalizarPedido(int idPedido) {
        if (!pedidoExiste(idPedido)) return false;

        Pedido pedido = pedidoById(idPedido);
        if (pedido.getEstado() != Estado.DESPACHADO) return false;

        // Cambiar estado a FINALIZADO
        pedido.setEstado(Estado.FINALIZADO);

        return true;
    }

    public int contarPedidosPorRepartidorYEstado(int idRepartidor, Estado estado) {
        // Por simplicidad, contamos todos los pedidos en ese estado
        // En una implementación más completa, deberíamos guardar la asignación repartidor-pedido
        return contarPorEstado(estado);
    }

    public Cliente obtenerClienteConMasPedidos() {
        Cliente clienteTop = null;
        int maxPedidos = 0;

        int i = 0;
        while (i < nextClienteSlot) {
            Cliente c = clientesStore[i];
            if (c != null && c.getPedidosRealizados() > maxPedidos) {
                maxPedidos = c.getPedidosRealizados();
                clienteTop = c;
            }
            i++;
        }

        return clienteTop;
    }

    public String[] obtenerPlatosDisponibles() {
        String[] platos = new String[nextPlatoSlot];
        int i = 0;
        while (i < nextPlatoSlot) {
            if (platosStore[i] != null) {
                platos[i] = platosStore[i].getNombre();
            }
            i++;
        }
        return platos;
    }

    public void mostrarRepartidores() {
        int slot = 1;
        while (slot < nextRepSlot) {
            Repartidor r = repsStore[slot];
            if (r != null) {
                int pedidosAsignados = contarPedidosEnCola(r.getPedidosAsignados());
                String estado = pedidosAsignados > 0 ? "🚴 Ocupado" : "✅ Disponible";
                System.out.println("║ Repartidor #" + formatearId(r.getId()) + " - " +
                    formatearNombre(r.getNombre()) + " " + estado + " ║");
                System.out.println("║   Entregas realizadas: " + formatearNumero(r.getEntregas()) +
                    " | Pedidos pendientes: " + pedidosAsignados + "  ║");
            }
            slot++;
        }
    }

    public void mostrarReportesAvanzados() {
        System.out.println("║                                                ║");
        System.out.println("║ 📊 CLIENTE CON MAYOR NÚMERO DE PEDIDOS:       ║");
        Cliente clienteTop = obtenerClienteConMasPedidos();
        if (clienteTop != null) {
            System.out.println("║   👤 " + formatearNombre(clienteTop.getNombre()) + "          ║");
            System.out.println("║   📦 Total de pedidos: " + clienteTop.getPedidosRealizados() + "                   ║");
        } else {
            System.out.println("║   ⚠️  No hay clientes registrados             ║");
        }

        System.out.println("║                                                ║");
        System.out.println("║ 🍽️  TOP 3 PLATOS MÁS PEDIDOS:                 ║");
        mostrarTopPlatos(3);

        System.out.println("║                                                ║");
        System.out.println("║ 📈 ESTADÍSTICAS GENERALES:                    ║");
        System.out.println("║   Total de clientes: " + nextClienteSlot + "                      ║");
        System.out.println("║   Total de platos disponibles: " + nextPlatoSlot + "             ║");
    }


    private void mostrarTopPlatos(int top) {
        // Crear un array temporal para ordenar platos por veces pedido
        Plato[] platosOrdenados = new Plato[nextPlatoSlot];
        int idx = 0;
        while (idx < nextPlatoSlot) {
            platosOrdenados[idx] = platosStore[idx];
            idx++;
        }

        // Ordenar por veces pedido (bubble sort simple)
        int i = 0;
        while (i < nextPlatoSlot - 1) {
            int j = 0;
            while (j < nextPlatoSlot - i - 1) {
                if (platosOrdenados[j] != null && platosOrdenados[j + 1] != null &&
                    platosOrdenados[j].getVecesPedido() < platosOrdenados[j + 1].getVecesPedido()) {
                    Plato temp = platosOrdenados[j];
                    platosOrdenados[j] = platosOrdenados[j + 1];
                    platosOrdenados[j + 1] = temp;
                }
                j++;
            }
            i++;
        }

        // Mostrar top N platos
        int contador = 0;
        int k = 0;
        while (k < nextPlatoSlot && contador < top) {
            if (platosOrdenados[k] != null && platosOrdenados[k].getVecesPedido() > 0) {
                contador++;
                System.out.println("║   " + contador + ". " + formatearNombre(platosOrdenados[k].getNombre()) +
                    " (" + platosOrdenados[k].getVecesPedido() + " veces) ║");
            }
            k++;
        }

        if (contador == 0) {
            System.out.println("║   ⚠️  No hay platos pedidos aún               ║");
        }
    }

    private int contarPedidosEnCola(QueueADT cola) {
        int count = 0;
        // No podemos iterar sin destruir la cola, así que usamos un enfoque diferente
        // Creamos una cola temporal para contar
        QueueADT temp = new StaticQueueADT();
        while (!cola.isEmpty()) {
            int val = cola.getElement();
            cola.remove();
            temp.add(val);
            count++;
        }
        // Restaurar la cola original
        while (!temp.isEmpty()) {
            cola.add(temp.getElement());
            temp.remove();
        }
        return count;
    }

    private String formatearId(int id) {
        if (id < 10) return " " + id;
        return "" + id;
    }

    private String formatearNombre(String nombre) {
        if (nombre.length() > 20) return nombre.substring(0, 20);
        while (nombre.length() < 20) nombre = nombre + " ";
        return nombre;
    }

    private String formatearNumero(int num) {
        String s = "" + num;
        while (s.length() < 2) s = " " + s;
        return s;
    }

    private int contarPorEstado(Estado e) {
        int c = 0;
        int slot = 1;
        while (slot < nextPedidoSlot) {
            Pedido p = pedidosStore[slot];
            if (p != null && p.getEstado() == e) c++;
            slot++;
        }
        return c;
    }
}
