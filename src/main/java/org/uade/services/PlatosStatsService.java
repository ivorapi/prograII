package org.uade.services;

import org.uade.structure.definition.SimpleDictionaryADT;
import org.uade.structure.implementation.dynamic.DynamicSimpleDictionaryADT;


public class PlatosStatsService {

    private final SimpleDictionaryADT dict = new DynamicSimpleDictionaryADT();

    private static class KeyNode {
        int key;
        KeyNode next;
        KeyNode(int clavePlato){ this.key = clavePlato; }
    }
    private KeyNode head = null;

    private void ensureKeyTracked(int key) {
        KeyNode nodoActual = head;
        while (nodoActual != null) {
            if (nodoActual.key == key) return;
            nodoActual = nodoActual.next;
        }
        KeyNode nuevoNodo = new KeyNode(key);
        nuevoNodo.next = head;
        head = nuevoNodo;
    }

    private int safeGet(SimpleDictionaryADT d, int key) {
        try { return d.get(key); } catch (Exception e) { return 0; }
    }

    private void safePut(SimpleDictionaryADT d, int key, int value) {
        d.add(key, value);
    }

    public void inc(int platoId) {
        ensureKeyTracked(platoId);
        int actual = safeGet(dict, platoId);
        safePut(dict, platoId, actual + 1);
    }


    public void printTopPlatos() {
        if (head == null) {
            System.out.println("🍽️ No hay registros de platos aún.");
            return;
        }

        int cantidadMaxima = -1;
        KeyNode nodoActual = head;
        while (nodoActual != null) {
            int cantidadPedidos = safeGet(dict, nodoActual.key);
            if (cantidadPedidos > cantidadMaxima) cantidadMaxima = cantidadPedidos;
            nodoActual = nodoActual.next;
        }

        if (cantidadMaxima <= 0) {
            System.out.println("🍽️ No hay registros de platos aún.");
            return;
        }

        System.out.println("🍽️ Platos más pedidos (cantidad = " + cantidadMaxima + "):");
        nodoActual = head;
        while (nodoActual != null) {
            int cantidadPedidos = safeGet(dict, nodoActual.key);
            if (cantidadPedidos == cantidadMaxima) {
                System.out.println("  Plato #" + nodoActual.key + " (x" + cantidadPedidos + ") " + nombrePlato(nodoActual.key));
            }
            nodoActual = nodoActual.next;
        }
    }

    private String nombrePlato(int cod) {
        if (cod == 1) return "→ Milanesa con puré";
        if (cod == 2) return "→ Pizza muzzarella";
        if (cod == 3) return "→ Empanadas";
        if (cod == 4) return "→ Ensalada César";
        if (cod == 5) return "→ Lasaña";
        return "";
    }
}
