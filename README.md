Pontificia Universidad Católica Madre y Maestra
Nombre y Matrícula:

Ernesto David del Villar Reyes – 1014-3598
Erick Daniel de la Rosa – 1014-4197

Profesor:

Freddy Peña

Asignación:

Avance Proyecto Final

Descripción del Proyecto
Este proyecto es una aplicación de simulación de tráfico desarrollada con JavaFX, diseñada para modelar el comportamiento de vehículos en una intersección de cuatro vías. La simulación permite crear vehículos regulares y de emergencia, que pueden realizar movimientos rectos, giros en U, giros a la izquierda y giros a la derecha. Utiliza técnicas de programación concurrente para garantizar que los vehículos se muevan de manera segura, evitando colisiones y respetando la prioridad de los vehículos de emergencia. La interfaz gráfica, implementada con JavaFX, muestra visualmente los vehículos en un fondo de intersección, con controles interactivos para gestionar la simulación.
Objetivo
El objetivo principal es simular un cruce de cuatro intersecciones con señales de alto, donde:

Los vehículos respetan el orden de llegada (FIFO) para cruzar la intersección.
Los vehículos de emergencia tienen prioridad absoluta, deteniendo otras colas hasta que se procesen los vehículos en la cola con la emergencia.
Se evitan colisiones mediante la sincronización de hilos y el uso de banderas atómicas (AtomicBoolean).
La interfaz gráfica permite a los usuarios interactuar con la simulación, seleccionando direcciones y tipos de movimiento.

Características Principales

Creación de Vehículos: Los usuarios pueden añadir vehículos regulares o de emergencia desde las cuatro direcciones (Norte, Sur, Este, Oeste).
Movimientos de Vehículos: Soporta movimientos rectos, giros en U, giros a la izquierda y giros a la derecha, con animaciones fluidas usando TranslateTransition.
Prioridad de Emergencia: Los vehículos de emergencia se procesan primero, utilizando una PriorityBlockingQueue separada.
Prevención de Colisiones: Cuatro banderas AtomicBoolean (crossingNorthOccupied, crossingSouthOccupied, crossingEastOccupied, crossingWestOccupied) aseguran que solo un vehículo cruce la intersección en un momento dado.
Interfaz Gráfica: Implementada con JavaFX, incluye un fondo de intersección (Fondo.png), imágenes de vehículos (Auto.png, AutoEmergencia.png), y controles interactivos como botones y toggles.
Programación Concurrente: Utiliza hilos (Thread), colas de prioridad (PriorityBlockingQueue), y sincronización para manejar múltiples vehículos simultáneamente.

Tecnologías Utilizadas

Java 21: Lenguaje principal, compatible con las últimas características de concurrencia.
JavaFX 21: Framework para la interfaz gráfica y animaciones.
Gradle: Herramienta de automatización para gestionar dependencias y compilación.
Dependencias Externas:
ControlsFX: Para controles avanzados de JavaFX.
FormsFX: Para formularios dinámicos.
ValidatorFX: Para validación de entradas.
Ikonli JavaFX: Para iconos en la interfaz.
BootstrapFX: Para estilos adicionales.
TilesFX: Para componentes visuales avanzados.
FXGL: Para soporte de animaciones y gráficos.


Concurrencia:
PriorityBlockingQueue: Gestiona la prioridad de los vehículos.
AtomicBoolean: Garantiza operaciones atómicas para evitar condiciones de carrera.
Thread: Maneja la lógica de procesamiento de vehículos en segundo plano.



Estructura del Proyecto
El proyecto está organizado en dos paquetes principales y una estructura de recursos:

com.example:
HelloApplication.java: Punto de entrada de la aplicación, inicializa la ventana principal y carga el archivo FXML inicial (hello-view.fxml).
HelloController.java: Controlador para la interfaz principal, gestiona la selección de escenarios y crea botones dinámicos con imágenes.


org.example:
Intersection.java: Representa una intersección con una cola de vehículos y soporte para giros a la derecha.
TrafficController.java: Controla el flujo de vehículos, prioriza los de emergencia y evita colisiones usando sincronización.
Vehicle.java: Define un vehículo con propiedades como dirección, posición, estado de emergencia y su representación gráfica (ImageView).
TrafficSimulation.java: Implementa la lógica principal de la simulación, gestiona la creación de vehículos, animaciones y actualizaciones de posición.


Recursos:
/hello-view.fxml: Define la interfaz principal con un botón para seleccionar el "Escenario 1".
/acción1/hello-view.fxml: Define la interfaz del escenario de simulación, con controles para seleccionar dirección, tipo de vehículo y movimiento.
/escenario1/Fondo.png: Imagen de fondo de la intersección.
/escenario1/Auto.png: Imagen para vehículos regulares.
/escenario1/AutoEmergencia.png: Imagen para vehículos de emergencia.
module-info.java: Configura los módulos de JavaFX y dependencias externas.
build.gradle: Define la configuración de Gradle, incluyendo dependencias y tareas de compilación.



Uso de la Aplicación

Iniciar la Aplicación:
Al ejecutar, se abre una ventana principal con un botón para "Escenario 1".


Seleccionar Escenario 1:
Haz clic en el botón "Escenario 1" para abrir la ventana de simulación.


Configurar Vehículos:
Usa los botones de toggle (Norte, Sur, Este, Oeste) para seleccionar la dirección de origen del vehículo.
Marca la casilla "Emergencia" si deseas crear un vehículo de emergencia.
Selecciona el tipo de movimiento: "Recto", "Giro en U", "Giro a la Izquierda" o "Giro a la Derecha".


Observar la Simulación:
Los vehículos aparecen en la dirección seleccionada y se mueven hacia la intersección.
Los vehículos de emergencia tienen prioridad y detienen otras colas hasta que se procesan.
Las animaciones muestran los movimientos de los vehículos, con rotaciones y traslaciones según el tipo de maniobra.


Límites:
Cada dirección admite un máximo de dos vehículos en la cola para evitar saturación.
Los vehículos se eliminan de la interfaz al completar su movimiento.



