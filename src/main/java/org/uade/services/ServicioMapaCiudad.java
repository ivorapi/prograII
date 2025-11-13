package org.uade.services;

import org.uade.structure.definition.GraphADT;
import org.uade.structure.implementation.fixed.StaticGraphADT;

public class ServicioMapaCiudad {

    private final GraphADT grafo;
    private static final int MAXV = 6;
    private static final int INF = 1_000_000_000;

    public ServicioMapaCiudad() {
        this.grafo = new StaticGraphADT();
        inicializarGrafo();
    }

    private void inicializarGrafo() {
        int v = 0;
        while (v < MAXV) { grafo.addVertx(v); v++; }

        grafo.addEdge(0, 1, 4);
        grafo.addEdge(0, 2, 6);
        grafo.addEdge(1, 2, 2);
        grafo.addEdge(1, 3, 3);
        grafo.addEdge(2, 4, 5);
        grafo.addEdge(3, 4, 4);
        grafo.addEdge(4, 5, 3);
        grafo.addEdge(2, 5, 7);
    }

    public int obtenerIdBarrio(String nombre) {
        if (nombre == null) return -1;
        if (eqi(nombre, "Restaurante")) return 0;
        if (eqi(nombre, "Palermo"))     return 1;
        if (eqi(nombre, "Recoleta"))    return 2;
        if (eqi(nombre, "Belgrano"))    return 3;
        if (eqi(nombre, "Caballito"))   return 4;
        if (eqi(nombre, "Flores"))      return 5;
        return -1;
    }

    public String obtenerNombreBarrio(int id) {
        if (id == 0) return "Restaurante";
        if (id == 1) return "Palermo";
        if (id == 2) return "Recoleta";
        if (id == 3) return "Belgrano";
        if (id == 4) return "Caballito";
        if (id == 5) return "Flores";
        return "Desconocido";
    }

    private boolean eqi(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }

    public int calcularDistanciaMinima(int origen, int destino) {
        if (origen < 0 || destino < 0 || origen >= MAXV || destino >= MAXV) return -1;

        DNodo head = null;
        int v = 0;
        while (v < MAXV) {
            DNodo n = new DNodo(v, (v == origen) ? 0 : INF, false);
            n.sig = head; head = n;
            v++;
        }

        int visitados = 0;
        while (visitados < MAXV) {
            DNodo u = minNoVisitado(head);
            if (u == null || u.dist == INF) break;
            u.vis = true; visitados++;
            if (u.v == destino) break;

            int w = 0;
            while (w < MAXV) {
                if (w != u.v && grafo.existsEdge(u.v, w)) {
                    int peso = grafo.edgeWeight(u.v, w);
                    DNodo nw = buscar(head, w);
                    if (nw != null && !nw.vis) {
                        int alt = u.dist + peso;
                        if (alt < nw.dist) nw.dist = alt;
                    }
                }
                w++;
            }
        }

        DNodo d = buscar(head, destino);
        return (d == null || d.dist == INF) ? -1 : d.dist;
    }

    private static class DNodo {
        int v, dist; boolean vis; DNodo sig;
        DNodo(int v, int d, boolean vis) { this.v = v; this.dist = d; this.vis = vis; }
    }

    private DNodo minNoVisitado(DNodo head) {
        DNodo mejor = null, cur = head;
        while (cur != null) {
            if (!cur.vis && (mejor == null || cur.dist < mejor.dist)) mejor = cur;
            cur = cur.sig;
        }
        return mejor;
    }

    private DNodo buscar(DNodo head, int v) {
        DNodo c = head;
        while (c != null) { if (c.v == v) return c; c = c.sig; }
        return null;
    }
}
