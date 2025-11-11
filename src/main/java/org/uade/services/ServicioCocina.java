package org.uade.services;

import org.uade.enums.Prioridad;
import org.uade.structure.implementation.dynamic.DynamicQueueADT;

public class ServicioCocina {

    private final DynamicQueueADT colaPlatosVip;
    private final DynamicQueueADT colaPlatosNoVip;

    public ServicioCocina(DynamicQueueADT colaVip, DynamicQueueADT colaNoVip) {
        this.colaPlatosVip = colaVip;
        this.colaPlatosNoVip = colaNoVip;
    }

    public DynamicQueueADT getColaPlatosVip() { return colaPlatosVip; }
    public DynamicQueueADT getColaPlatosNoVip() { return colaPlatosNoVip; }

    public void encolarPlatos(DynamicQueueADT seleccion, Prioridad prioridad) {
        DynamicQueueADT destino = (prioridad == Prioridad.VIP) ? colaPlatosVip : colaPlatosNoVip;
        while (!seleccion.isEmpty()) {
            int codigo = seleccion.getElement();
            destino.add(codigo);
            seleccion.remove();
        }
    }
}
