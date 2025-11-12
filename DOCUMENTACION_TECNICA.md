# Documentación Técnica - Vision Processor

## 1. Introducción

Este documento proporciona una descripción técnica detallada de la aplicación **Vision Processor**. El objetivo es servir como una guía de referencia sobre las funcionalidades implementadas, los algoritmos utilizados y las instrucciones para compilar y ejecutar el proyecto.

## 2. Tecnologías Principales

*   **Lenguaje**: Java 17
*   **Framework de UI**: JavaFX 17
*   **Gestión de Proyecto y Dependencias**: Apache Maven

## 3. Guía de Funcionalidades

La aplicación está organizada en módulos funcionales, cada uno presentado como una pestaña en la interfaz de usuario. A continuación se detallan las operaciones disponibles en cada módulo.

### 3.1. Módulo: Conversión de Color

*   **Responsable**: `ColorSpaceService.java`
*   **Funcionalidades**:
    *   **Conversión RGB a CMY**: Transforma el modelo de color de aditivo (Rojo, Verde, Azul) a sustractivo (Cian, Magenta, Amarillo).
    *   **Conversión RGB a HSI/HSV**: Transforma el modelo de color a modelos basados en la percepción humana (Tono, Saturación, Intensidad/Valor).
    *   **Extracción de Canales**: Permite visualizar individualmente los canales de los diferentes modelos de color (R, G, B, C, M, Y, H, S, I).

### 3.2. Módulo: Ajustes de Imagen

*   **Responsable**: `ImageProcessingService.java`
*   **Funcionalidades**:
    *   **Conversión a Escala de Grises**: Simplifica la imagen a un solo canal de intensidad.
    *   **Ajuste de Brillo**: Aumenta o disminuye la luminosidad general de la imagen.
    *   **Ajuste de Contraste**: Aumenta o disminuye la diferencia entre las áreas claras y oscuras.
    *   **Binarización**: Convierte la imagen a blanco y negro puro basándose en un umbral de luminosidad.

### 3.3. Módulo: Histograma

*   **Responsable**: `HistogramService.java`
*   **Funcionalidades**:
    *   **Cálculo y Visualización**: Genera y muestra los histogramas para los canales de color (RGB) y la escala de grises.
    *   **Ecualización del Histograma**: Redistribuye las intensidades para mejorar el contraste global.
    *   **Expansión del Histograma (Normalización)**: Estira el rango de intensidades para que ocupe todo el espectro posible.
    *   **Contracción del Histograma**: Reduce el rango de intensidades.

### 3.4. Módulo: Operaciones Lógicas

*   **Responsable**: `LogicalOperationsService.java`
*   **Funcionalidades**:
    *   Aplica operaciones a nivel de bits (`AND`, `OR`, `XOR`, `NOT`) entre dos imágenes o entre una imagen y una constante.

### 3.5. Módulo: Transformaciones Geométricas

*   **Responsable**: `GeometricTransformationService.java`
*   **Funcionalidades**:
    *   **Traslación**: Mueve la imagen a una nueva posición.
    *   **Escalado**: Cambia el tamaño de la imagen.
    *   **Rotación**: Gira la imagen un ángulo determinado.
    *   **Deformación (Shearing)**: Inclina la imagen horizontal o verticalmente.

### 3.6. Módulo: Operaciones Morfológicas

*   **Responsable**: `MorphologicalService.java`
*   **Funcionalidades**:
    *   **Erosión**: Reduce el tamaño de las áreas de primer plano (objetos blancos).
    *   **Dilatación**: Aumenta el tamaño de las áreas de primer plano.
    *   **Apertura**: Realiza una erosión seguida de una dilatación (elimina ruido tipo "sal").
    *   **Cierre**: Realiza una dilatación seguida de una erosión (rellena huecos y elimina ruido tipo "pimienta").

### 3.7. Módulo: Transformada de Fourier

*   **Responsable**: `FourierService.java`
*   **Funcionalidades**:
    *   **Transformada Discreta de Fourier (DFT)**: Convierte la imagen del dominio espacial al dominio de la frecuencia.
    *   **Visualización del Espectro**: Muestra la magnitud del espectro de frecuencias.
    *   **Transformada Inversa de Fourier**: Reconstruye la imagen desde el dominio de la frecuencia.
    *   **Filtrado en Frecuencia**: Permite aplicar filtros pasa-bajas y pasa-altas en el espectro.

### 3.8. Módulo: Convolución

*   **Responsable**: `ConvolutionService.java`
*   **Funcionalidades**:
    *   **Aplicación de Kernels**: Permite aplicar matrices de convolución (kernels) para efectos como:
        *   Desenfoque (Blur).
        *   Realce de Bordes (Edge Enhancement).
        *   Repujado (Emboss).
        *   Detección de Bordes con kernels Prewitt, Sobel y Roberts.

### 3.9. Módulo: Detección de Esquinas

*   **Responsable**: `CornerDetectionService.java`
*   **Funcionalidades**:
    *   **Detector de Kirsch**: Algoritmo para la detección de bordes usando 8 máscaras orientadas.
    *   **Detector de Frei-Chen**: Método para la detección de bordes y líneas usando una base de vectores ortogonales.
    *   **Detector de Esquinas de Harris**: Algoritmo para identificar puntos de interés (esquinas) en la imagen.

## 4. Compilación y Ejecución

El proyecto se gestiona con Maven, lo que simplifica su compilación y ejecución.

1.  **Requisitos Previos**:
    *   JDK 17 o superior.
    *   Apache Maven 3.6 o superior.

2.  **Compilar el Proyecto**:
    Abre una terminal en la raíz del proyecto y ejecuta el siguiente comando. Este comando limpiará, compilará y empaquetará todos los módulos.
    ```sh
    mvn clean install
    ```

3.  **Ejecutar la Aplicación**:
    Una vez compilado el proyecto, utiliza el siguiente comando para iniciar la aplicación.
    ```sh
    mvn javafx:run -pl vision-app
    ```
    El flag `-pl vision-app` le indica a Maven que ejecute el plugin de JavaFX específicamente en el módulo `vision-app`.