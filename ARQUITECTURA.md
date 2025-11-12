# Arquitectura Modular - Vision Processor

## 📋 Descripción General

La aplicación Vision Processor ha sido reestructurada siguiendo una **arquitectura modular escalable** que separa claramente las responsabilidades y facilita el mantenimiento y extensión del código.

## 🏗️ Estructura del Proyecto

```
vision/App/
├── vision-core/              # Módulo Core (Lógica de Negocio)
│   └── src/main/java/com/vision/
│       ├── core/
│       │   └── ServiceProvider.java      # Proveedor de servicios (DI)
│       ├── model/
│       │   └── ColorSpaceModel.java      # Modelo de datos
│       ├── service/
│       │   ├── ColorSpaceService.java    # ...
│       │   ├── ImageProcessingService.java # ...
│       │   ├── GeometricTransformationService.java # ...
│       │   ├── MorphologicalService.java # ...
│       │   ├── FourierService.java       # Servicio para Transformada de Fourier
│       │   ├── ConvolutionService.java   # Servicio para convoluciones y filtros
│       │   └── CornerDetectionService.java # Servicio para detección de esquinas
│       └── util/
│           ├── DefaultImageGenerator.java # ...
│           └── KernelProvider.java       # Proveedor de kernels para convolución
│
├── vision-ui/                # Módulo UI (Interfaz de Usuario)
│   └── src/main/java/com/vision/
│       ├── controller/
│       │   └── MainController.java       # ...
│       ├── modules/                      # Módulos funcionales
│       │   ├── ...
│       │   ├── fourier/
│       │   ├── convolution/
│       │   └── cornerdetection/
│       └── ui/
│           └── components/               # ...
│
└── vision-app/               # ...
```

## 🎯 Principios de Diseño

(Esta sección permanece sin cambios)

## 📦 Módulos Funcionales

### Módulo: Transformada de Fourier
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/fourier/`
**Responsabilidad**: Aplicar la Transformada de Fourier (directa e inversa) a imágenes cuadradas con dimensiones de potencia de dos.

### Módulo: Convolución
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/convolution/`
**Responsabilidad**: Aplicar filtros de convolución para suavizado, realce de bordes y detección de bordes con Canny.
**Funcionalidades**:
- **Filtros Pasa-Bajas**: Desenfoque con filtro de promediado (7x7, 11x11, 15x15).
- **Filtros Pasa-Altas**: Realce/definición de imagen (suave, medio, fuerte).
- **Detector de Canny**: Algoritmo de detección de bordes multi-paso.

### Módulo: Detección de Esquinas
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/cornerdetection/`
**Responsabilidad**: Detectar bordes y esquinas en una imagen.
**Funcionalidades**:
- **Operador de Kirsch**: Detección de bordes usando 8 máscaras de compás.
- **Operador de Frei-Chen**: Detección de bordes usando 9 vectores base ortogonales.
- **Detector de Harris-Stephens**: Detección de esquinas.

(Se conservan las descripciones de los módulos anteriores)

### Módulo: Color Conversion
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/colorconversion/`
**Responsabilidad**: Conversión entre diferentes espacios de color.

### Módulo: Image Adjustment
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/imageadjustment/`
**Responsabilidad**: Ajustes de brillo y contraste.

### Módulo: Transformaciones Geométricas
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/geometrictransformation/`
**Responsabilidad**: Aplicar transformaciones geométricas a una imagen.

### Módulo: Operaciones Morfológicas
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/morphological/`
**Responsabilidad**: Realizar operaciones morfológicas en imágenes binarias y manejo de ruido.

## 🔌 Cómo Agregar un Nuevo Módulo

(Esta sección permanece sin cambios)

## 🔄 Flujo de Datos

(El diagrama general sigue siendo válido, pero ahora con más servicios y módulos)

## 🔧 Compilación y Ejecución

```bash
# Compilar todo el proyecto
mvn clean install

# Ejecutar la aplicación
mvn javafx:run -pl vision-app
```

## 📚 Tecnologías Utilizadas

- **JavaFX 17**: Framework de UI
- **Maven**: Gestión de dependencias y módulos
- **Java 17**: Lenguaje de programación
- **Patrón MVC**: Arquitectura de presentación
- **Observer Pattern**: Comunicación modelo-vista

## 🎯 Beneficios de esta Arquitectura

(Esta sección permanece sin cambios)
