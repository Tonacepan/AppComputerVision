# Documentación Técnica - Vision Processor

## 1. Introducción

Este documento detalla la arquitectura técnica y la implementación de los diferentes módulos de la aplicación **Vision Processor**. El objetivo de la aplicación es proporcionar un entorno modular para realizar operaciones de procesamiento de imágenes, desde conversiones de color básicas hasta algoritmos complejos de detección de bordes y esquinas.

## 2. Arquitectura General

El proyecto sigue una **arquitectura modular** basada en Maven, diseñada para maximizar la cohesión y minimizar el acoplamiento entre las diferentes áreas de la aplicación.

### 2.1. Estructura de Módulos

-   **`vision-core`**: El corazón de la aplicación. Contiene toda la lógica de negocio, los algoritmos de procesamiento de imágenes y los modelos de datos. Es completamente independiente de la interfaz de usuario.
-   **`vision-ui`**: La capa de presentación. Contiene todos los componentes de la interfaz de usuario, construidos con JavaFX. Se organiza en sub-módulos que corresponden a las funcionalidades ofrecidas.
-   **`vision-app`**: El punto de entrada de la aplicación. Su única responsabilidad es iniciar la aplicación JavaFX y ensamblar los módulos `core` y `ui`.

### 2.2. Patrones de Diseño

-   **Singleton**: Utilizado en `ServiceProvider` para garantizar una única instancia de los servicios de la aplicación, facilitando una forma simple de inyección de dependencias.
-   **Modelo-Vista-Controlador (MVC)**: Cada módulo de la UI sigue una variación de este patrón, donde la `View` (Vista) es responsable de la presentación, el `Controller` (Controlador) maneja la interacción del usuario y el `Model` (Modelo) (compartido) mantiene el estado.
-   **Observer**: El `ColorSpaceModel` notifica a las vistas de cualquier cambio en la imagen cargada, permitiendo que la UI se actualice automáticamente.

---

## 3. Módulo `vision-core`: Servicios y Algoritmos

Este módulo contiene la implementación de todos los algoritmos de procesamiento de imágenes.

### 3.1. `ServiceProvider`

Actúa como un registro central para todos los servicios. Proporciona un punto de acceso único (`getInstance()`) para que los controladores de la UI obtengan las instancias de los servicios que necesitan.

### 3.2. `ImageProcessingService`

Contiene algoritmos básicos de manipulación de píxeles.
-   `convertToGrayscale(Image)`: Convierte una imagen a escala de grises.
-   `adjustBrightness(Image, double)`: Ajusta el brillo de la imagen.
-   `adjustContrast(Image, double)`: Ajusta el contraste de la imagen.
-   `binarize(Image, double)`: Convierte una imagen a blanco y negro basado en un umbral.

### 3.3. `ColorSpaceService`

Realiza conversiones entre diferentes modelos de color.
-   `convertRgbToCmy(Image)`
-   `convertRgbToCmyk(Image)`
-   `convertRgbToYiq(Image)`
-   `convertRgbToHsi(Image)`
-   `convertRgbToHsv(Image)`

### 3.4. `FourierService`

Implementa la Transformada de Fourier para análisis en el dominio de la frecuencia.
-   `fft(Image)`: Calcula la Transformada Rápida de Fourier (FFT) 2D de una imagen. Requiere que la imagen sea cuadrada y con dimensiones de potencia de dos.
-   `inverseFft(Complex[][])`: Calcula la transformada inversa para reconstruir la imagen.
-   `getSpectrumView(Complex[][])`: Genera una imagen visible del espectro de Fourier (magnitud en escala logarítmica).

### 3.5. `ConvolutionService`

Proporciona la funcionalidad de convolución, que es la base para los filtros espaciales.
-   `convolve(Image, double[][])`: Aplica un kernel (matriz) de convolución a una imagen en escala de grises.
-   `applyLowPassFilter(Image, LowPass)`: Aplica filtros de promediado para suavizar la imagen.
-   `applyHighPassFilter(Image, HighPass)`: Aplica filtros de realce para enfocar los detalles.
-   `applyCannyEdgeDetector(Image, double, double)`: Implementa el algoritmo de Canny para detectar bordes, que internamente utiliza convoluciones para el suavizado y el cálculo de gradientes.

### 3.6. `CornerDetectionService`

Implementa algoritmos para la detección de bordes y esquinas.
-   `applyKirsch(Image)`: Aplica los 8 kernels de Kirsch para detectar bordes en diferentes orientaciones, mostrando la máxima respuesta.
-   `applyFreiChen(Image)`: Utiliza los 9 kernels de la base ortogonal de Frei-Chen para detectar bordes.
-   `applyHarris(Image, double, double)`: Implementa el detector de esquinas de Harris-Stephens para encontrar puntos de interés (esquinas) en la imagen.

### 3.7. Utilidades (`KernelProvider`, `DefaultImageGenerator`)

-   **`KernelProvider`**: Almacena y provee las matrices (kernels) estándar utilizadas en `ConvolutionService` y `CornerDetectionService`, como Sobel, Kirsch, Frei-Chen, etc.
-   **`DefaultImageGenerator`**: Crea imágenes de prueba con patrones predefinidos para facilitar la depuración y el uso de la aplicación.

---

## 4. Módulo `vision-ui`: Interfaz de Usuario

La interfaz está organizada en pestañas, donde cada una representa un módulo funcional.

-   **`MainController`**: Orquesta la aplicación, maneja la carga de imágenes y la inicialización de los módulos.
-   **Modelo Compartido (`ColorSpaceModel`)**: Una instancia de este modelo se comparte entre todas las vistas. Contiene la imagen original cargada por el usuario. Las vistas se suscriben a los cambios de este modelo para actualizarse cuando se carga una nueva imagen.
-   **Módulos de Vista/Controlador**: Cada funcionalidad (ej. `fourier`, `convolution`) tiene su propio paquete con una `View` y un `Controller`. La `View` define la interfaz y los controles, mientras que el `Controller` responde a las acciones del usuario, llama al servicio correspondiente en el `core` y actualiza la vista con el resultado.

---

## 5. Cómo Compilar y Ejecutar

El proyecto se gestiona con Maven.

1.  **Compilar e Instalar**:
    Este comando limpia el proyecto, compila todo el código y empaqueta los módulos en archivos `.jar`.

    ```sh
    mvn clean install
    ```

2.  **Ejecutar la Aplicación**:
    Este comando ejecuta la aplicación desde el módulo `vision-app`.

    ```sh
    mvn javafx:run -pl vision-app
    ```
