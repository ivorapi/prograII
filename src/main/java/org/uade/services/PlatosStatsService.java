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
        KeyNode cur = head;
        while (cur != null) {
            if (cur.key == key) return;
            cur = cur.next;
        }
        KeyNode n = new KeyNode(key);
        n.next = head;
        head = n;
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

        int maxCount = -1;
        KeyNode cur = head;
        while (cur != null) {
            int count = safeGet(dict, cur.key);
            if (count > maxCount) maxCount = count;
            cur = cur.next;
        }

        if (maxCount <= 0) {
            System.out.println("🍽️ No hay registros de platos aún.");
            return;
        }

        System.out.println("🍽️ Platos más pedidos (cantidad = " + maxCount + "):");
        cur = head;
        while (cur != null) {
            int count = safeGet(dict, cur.key);
            if (count == maxCount) {
                System.out.println("  Plato #" + cur.key + " (x" + count + ") " + nombrePlato(cur.key));
            }
            cur = cur.next;
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
