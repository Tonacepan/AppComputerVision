# Vision Processor

![Java CI with Maven](https://github.com/tonacepan/AppComputerVision/actions/workflows/maven.yml/badge.svg)

Una aplicación de escritorio desarrollada con **JavaFX** para el procesamiento de imágenes y la experimentación con algoritmos de visión por computadora.

La aplicación proporciona una interfaz gráfica modular que permite cargar imágenes y aplicar una amplia variedad de operaciones en tiempo real, desde ajustes básicos de color hasta transformaciones complejas en el dominio de la frecuencia y detección de características.

*(Aquí se podría agregar una captura de pantalla de la aplicación en funcionamiento)*

---

## ✨ Funcionalidades Implementadas

La interfaz se organiza en módulos, cada uno con un conjunto específico de herramientas:

*   **Conversión de Espacios de Color**:
    *   RGB a CMY (Cian, Magenta, Amarillo).
    *   RGB a HSI/HSV (Tono, Saturación, Intensidad/Valor).
    *   Extracción de canales individuales (R, G, B, H, S, I, etc.).

*   **Ajustes de Imagen**:
    *   Conversión a escala de grises.
    *   Ajuste de brillo y contraste.
    *   Binarización de imagen mediante un umbral.

*   **Análisis de Histograma**:
    *   Visualización de histogramas para canales RGB y escala de grises.
    *   Ecualización y expansión del histograma para mejorar el contraste.

*   **Operaciones Lógicas**:
    *   Operaciones a nivel de bit: `AND`, `OR`, `XOR`, `NOT`.

*   **Transformaciones Geométricas**:
    *   Traslación, escalado, rotación y deformación (*shearing*).

*   **Operaciones Morfológicas**:
    *   Erosión, dilatación, apertura y cierre para manipulación de formas y eliminación de ruido.

*   **Transformada de Fourier**:
    *   Cálculo de la Transformada Discreta de Fourier (DFT).
    *   Visualización del espectro de magnitud.
    *   Filtrado pasa-altas y pasa-bajas en el dominio de la frecuencia.

*   **Convolución**:
    *   Aplicación de filtros (kernels) para desenfoque, realce de bordes, repujado, etc.
    *   Detección de bordes mediante los operadores de Prewitt, Sobel y Roberts.

*   **Detección de Bordes y Esquinas**:
    *   Algoritmos de Kirsch y Frei-Chen.
    *   Detector de esquinas de Harris.

---

## 🛠️ Tecnologías Utilizadas

*   **Java 17**: Lenguaje de programación principal.
*   **JavaFX 17**: Framework para la construcción de la interfaz gráfica de usuario.
*   **Maven**: Herramienta para la gestión del proyecto, dependencias y compilación.

---

## 🚀 Cómo Empezar

Sigue estos pasos para compilar y ejecutar el proyecto en tu máquina local.

### Prerrequisitos

*   Tener instalado el **JDK 17** (o una versión superior).
*   Tener instalado **Apache Maven**.

### Compilación

1.  Clona o descarga este repositorio.
2.  Abre una terminal en el directorio raíz del proyecto.
3.  Ejecuta el siguiente comando de Maven para compilar todos los módulos y empaquetarlos:

    ```sh
    mvn clean install
    ```

### Ejecución

Una vez que el proyecto ha sido compilado exitosamente, puedes ejecutar la aplicación con el siguiente comando:

```sh
mvn javafx:run -pl vision-app
```

Este comando utiliza el plugin de JavaFX para iniciar la aplicación desde el módulo `vision-app`.

---

## 🏗️ Estructura del Proyecto

El código está organizado en una arquitectura multi-módulo para separar responsabilidades:

*   **`vision-core`**: Contiene toda la lógica de negocio y los algoritmos de procesamiento de imágenes. No depende de la interfaz de usuario.
*   **`vision-ui`**: Contiene todos los componentes de la interfaz de usuario (vistas y controladores de JavaFX).
*   **`vision-app`**: Es el módulo de entrada que une `vision-core` y `vision-ui` para lanzar la aplicación.
