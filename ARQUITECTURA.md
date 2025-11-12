# Arquitectura de la Aplicación - Vision Processor

## 1. Visión General

La aplicación `Vision Processor` está diseñada como una herramienta de escritorio para el procesamiento de imágenes, siguiendo una **arquitectura modular** que separa claramente las responsabilidades. El objetivo es facilitar la extensibilidad y el mantenimiento, permitiendo que nuevas funcionalidades de procesamiento de imágenes se puedan agregar con un mínimo impacto en el resto del sistema.

El proyecto está gestionado con **Maven** y estructurado en un formato multi-módulo.

## 2. Estructura de Módulos Maven

El proyecto se divide en tres módulos principales bajo un POM padre (`vision-parent`):

```
/
├── vision-core/      # Módulo con la lógica de negocio y algoritmos.
├── vision-ui/        # Módulo con la interfaz de usuario (JavaFX).
└── vision-app/       # Módulo de ensamblaje y punto de entrada.
```

### 2.1. `vision-core`

*   **Responsabilidad**: Contiene toda la lógica de procesamiento de imágenes. Es el "cerebro" de la aplicación.
*   **Contenido Clave**:
    *   **Servicios (`com.vision.service.*`)**: Clases como `ImageProcessingService`, `ConvolutionService`, `CornerDetectionService`, etc. Cada servicio agrupa un conjunto de algoritmos relacionados.
    *   **Modelos (`com.vision.model.*`)**: Clases de datos, como `ColorSpaceModel`, que actúan como el modelo de datos compartido para la aplicación.
    *   **Utilidades (`com.vision.util.*`)**: Clases de apoyo como `KernelProvider` (que provee matrices para convolución) y `DefaultImageGenerator`.
*   **Independencia**: Este módulo no tiene dependencias de la interfaz de usuario (`vision-ui`), lo que significa que la lógica de negocio podría ser reutilizada en otras aplicaciones (ej. una aplicación web o de línea de comandos).

### 2.2. `vision-ui`

*   **Responsabilidad**: Contiene todos los componentes de la interfaz de usuario, construida con **JavaFX**.
*   **Contenido Clave**:
    *   **Controlador Principal (`MainController`)**: Orquesta la ventana principal, gestiona la carga de imágenes y la inicialización de los sub-módulos de la UI.
    *   **Vistas Modulares (`com.vision.modules.*`)**: La interfaz está dividida en pestañas, donde cada pestaña representa una categoría de operaciones (ej. `convolution`, `fourier`, `histogram`). Cada una de estas vistas es un componente autocontenido.
    *   **Archivos FXML (`resources/com/vision/view/*.fxml`)**: Definen la estructura de la interfaz de usuario de forma declarativa.
*   **Dependencias**: Depende de `vision-core` para acceder a los servicios de procesamiento de imágenes.

### 2.3. `vision-app`

*   **Responsabilidad**: Es el punto de entrada de la aplicación. Su única función es ensamblar los módulos `core` y `ui`.
*   **Contenido Clave**:
    *   **`VisionProcessorApp.java`**: La clase que contiene el método `main` y que extiende `javafx.application.Application`. Inicia la aplicación JavaFX y carga la vista principal.
*   **Dependencias**: Depende de `vision-core` y `vision-ui`.

## 3. Flujo de Datos y Patrones de Diseño

*   **Patrón Observer**: El `MainController` carga una imagen y la establece en una instancia compartida de `ColorSpaceModel`. Este modelo notifica a todas las vistas (pestañas) que se han suscrito a él. Al recibir la notificación, cada vista actualiza su contenido para mostrar la nueva imagen.
*   **Inyección de Dependencias Simple**: Las vistas reciben el modelo compartido a través de su constructor, asegurando que todos los componentes trabajen sobre el mismo estado.
*   **Fachada de Servicios**: Los controladores de la UI no implementan la lógica de procesamiento directamente. En su lugar, delegan esta tarea a los servicios del módulo `vision-core`.

## 4. Tecnologías Utilizadas

*   **Lenguaje**: Java 17
*   **Framework UI**: JavaFX 17
*   **Gestión de Proyecto**: Apache Maven
*   **Patrones Principales**: MVC (a nivel de módulo), Observer, Singleton.

## 5. Compilación y Ejecución

El proyecto se gestiona completamente a través de Maven.

*   **Para compilar el proyecto completo**:
    ```sh
    mvn clean install
    ```
*   **Para ejecutar la aplicación**:
    ```sh
    mvn javafx:run -pl vision-app
    ```