# Sistema de Gestión de Pedidos de Restaurante

## ¿Qué es esto?

Es un trabajo práctico de Programación II donde armamos un sistema para manejar pedidos de un restaurante. Básicamente simula cómo funciona una cocina y el delivery, pero usando todas las estructuras de datos que vimos en clase (colas, pilas, listas, etc.).

## ¿Qué hace el programa?

El sistema maneja todo el ciclo de vida de un pedido:

1. **Se crea un pedido**: Un cliente hace un pedido que puede ser para llevar o delivery
2. **Va a cocina**: Los platos van a una cola de cocina (respetando prioridades VIP)
3. **Se cocina**: Se van cocinando los platos en orden
4. **Se despacha**: Si es delivery, se le asigna a un repartidor disponible
5. **Se entrega**: El pedido llega al cliente y se marca como finalizado

### Características principales

- **Prioridades**: Los pedidos VIP se atienden antes que los normales
- **Tipos de pedido**: Puede ser para LLEVAR o DOMICILIO
- **Repartidores**: Hay 3 repartidores que se encargan de los deliverys
- **Estadísticas**: Podés ver cuántos pedidos hay pendientes, finalizados, qué repartidor hizo más entregas, etc.

## Estructuras de datos que usamos

Usamos las estructuras de datos de la materia para organizar todo:

- **Colas (Queue)**: Para la cola de cocina y pedidos pendientes. Hay una cola VIP y una normal
- **Listas enlazadas**: Para mantener el registro de pedidos y repartidores
- **Grafos**: Para el mapa de la ciudad (aunque es medio básico por ahora)
- **Diccionarios**: Para buscar pedidos por ID rápidamente

Todo está implementado desde cero, sin usar las Collections de Java. Por eso tenemos carpetas como `structure/definition` e `structure/implementation`.

## Estructura del proyecto

```
src/main/java/org/uade/
├── Main.java                    # Acá arranca todo
├── entidades/                   # Las clases básicas (Pedido, Cliente, etc.)
├── enums/                       # Estados, prioridades y tipos
├── services/                    # La lógica del negocio
│   ├── SistemaGestion.java     # El cerebro del sistema
│   ├── ServicioPedidos.java    # Maneja los pedidos
│   ├── ServicioCocina.java     # Maneja la cocina
│   ├── ServicioDespacho.java   # Maneja los repartidores
│   └── ServicioReportes.java   # Para las estadísticas
└── structure/                   # Nuestras implementaciones de estructuras de datos
    ├── definition/              # Interfaces (ADTs)
    └── implementation/          # Las implementaciones reales
```

## ¿Qué podés hacer en el menú?

Cuando ejecutás el programa, sale un menú con opciones:

### Acciones:
- Cocinar y cumplir el siguiente pedido
- Ver un pedido específico
- Forzar entrega de pedidos despachados
- Ver repartidores disponibles

### Estadísticas:
- Cuántos pedidos hay pendientes por despachar
- Cuántos pedidos ya se finalizaron
- Qué repartidor hizo más entregas
- Quién es el cliente con el pedido más grande

## Datos precargados

El programa arranca con 5 pedidos ya cargados para que puedas probarlo sin tener que crear todo desde cero. Hay pedidos VIP y normales, algunos para llevar y otros para delivery.

## Tests

Hay tests en `src/test/java` que prueban que los servicios funcionan bien. Por ahora solo hay tests para el ServicioDespacho pero se pueden agregar más.

## Tecnologías

- Java 21
- Maven (para manejar dependencias y compilar)
- JUnit 5 (para los tests)

---

*TP de Programación II - UADE 2024 - Ergas - Rapisarda*

