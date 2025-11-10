package org.uade.util;

import org.uade.structure.definition.PriorityQueueADT;
import org.uade.structure.definition.QueueADT;
import org.uade.structure.implementation.dynamic.DynamicPriorityQueueADT;
import org.uade.structure.implementation.fixed.StaticPriorityQueueADT;

public class PriorityQueueADTUtil extends BaseUtil {


    public static void print(PriorityQueueADT q) {
        while (!q.isEmpty()) {
            System.out.println(q.getElement() + " (p=" + q.getPriority() + ")");
            q.remove();
        }
    }

    public static PriorityQueueADT copy(PriorityQueueADT src) {
        PriorityQueueADT dst = new StaticPriorityQueueADT();
        while (!src.isEmpty()) {
            dst.add(src.getElement(), src.getPriority());
            src.remove();
        }
        return dst;
    }

    public static boolean areEqual(PriorityQueueADT queueOne, PriorityQueueADT queueTwo) {
        if (queueOne.isEmpty() && queueTwo.isEmpty()) {
            return true;
        }

        PriorityQueueADT aux1 = copy(queueOne);
        PriorityQueueADT aux2 = copy(queueTwo);

        boolean areEqual = true;

        while (!aux1.isEmpty() && !aux2.isEmpty()) {
            int element1 = aux1.getElement();
            int priority1 = aux1.getPriority();
            int element2 = aux2.getElement();
            int priority2 = aux2.getPriority();

            if (element1 != element2 || priority1 != priority2) {
                areEqual = false;
            }

            aux1.remove();
            aux2.remove();
        }

        if (!aux1.isEmpty() || !aux2.isEmpty()) {
            areEqual = false;
        }

        return areEqual;
    }

    public static void populateWithRandomValues(PriorityQueueADT queue) {
        int i = 0;
        while (i < TOTAL) {
            queue.add(randomInteger(), randomInteger());
            i++;
        }
    }

    private static PriorityQueueADT getNewQueue(PriorityQueueADT queue) {
        if (queue instanceof QueueADT) {
            return new StaticPriorityQueueADT();
        } else {
            return new DynamicPriorityQueueADT();
        }
    }
}
