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
│       │   ├── ColorSpaceService.java    # Servicio de conversión de colores
│       │   ├── ImageProcessingService.java # Servicio de procesamiento
│       │   ├── GeometricTransformationService.java # Servicio de transformaciones geométricas
│       │   └── MorphologicalService.java # Servicio de operaciones morfológicas
│       └── util/
│           └── DefaultImageGenerator.java # Utilidades
│
├── vision-ui/                # Módulo UI (Interfaz de Usuario)
│   └── src/main/java/com/vision/
│       ├── controller/
│       │   └── MainController.java       # Controlador principal
│       ├── modules/                      # Módulos funcionales
│       │   ├── colorconversion/
│       │   ├── imageadjustment/
│       │   ├── geometrictransformation/
│       │   └── morphological/
│       └── ui/
│           └── components/               # Componentes reutilizables
│               └── ImageDisplayPanel.java
│
└── vision-app/               # Módulo de Aplicación (Punto de entrada)
    └── src/main/java/com/vision/
        └── VisionProcessorApp.java      # Clase principal
```

## 🎯 Principios de Diseño

### 1. **Separación por Capas**
- **Core**: Lógica de negocio, modelos y servicios
- **UI**: Presentación, vistas y controladores
- **App**: Punto de entrada y configuración inicial

### 2. **Arquitectura por Módulos**
Cada funcionalidad está encapsulada en su propio módulo:
- **colorconversion**: Conversión entre espacios de color (RGB, CMY, CMYK, YIQ, HSI, HSV)
- **imageadjustment**: Ajustes de brillo y contraste
- **geometrictransformation**: Transformaciones geométricas (traslación, rotación, escalamiento).
- **morphological**: Operaciones morfológicas (erosión, dilatación, apertura, clausura) y ruido.

### 3. **Patrón MVC Modular**
Cada módulo contiene:
- **Controller**: Lógica de control y orquestación
- **View**: Presentación y componentes visuales
- **Model**: Modelo compartido (ColorSpaceModel)

### 4. **Componentes Reutilizables**
- `ImageDisplayPanel`: Componente genérico para mostrar imágenes con título
- Fácil de extender con más componentes comunes

### 5. **Inyección de Dependencias Simple**
- `ServiceProvider`: Singleton que proporciona instancias de servicios
- Evita acoplamiento directo entre módulos

## 📦 Módulos Funcionales

### Módulo: Color Conversion
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/colorconversion/`
**Responsabilidad**: Conversión entre diferentes espacios de color.

### Módulo: Image Adjustment
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/imageadjustment/`
**Responsabilidad**: Ajustes de brillo y contraste.

### Módulo: Transformaciones Geométricas
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/geometrictransformation/`
**Responsabilidad**: Aplicar transformaciones geométricas a una imagen.
**Funcionalidades**:
- Traslación
- Rotación
- Escalamiento

### Módulo: Operaciones Morfológicas
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/morphological/`
**Responsabilidad**: Realizar operaciones morfológicas en imágenes binarias y manejo de ruido.
**Funcionalidades**:
- Añadir ruido de sal y pimienta.
- Erosión
- Dilatación
- Apertura (elimina ruido de sal)
- Clausura (elimina ruido de pimienta)

## 🔌 Cómo Agregar un Nuevo Módulo

(Esta sección permanece sin cambios como guía para futuros desarrollos)

## 🔄 Flujo de Datos

```
Usuario → MainController → Modelo Compartido
                              ↓
                    ┌─────────┴─────────┐
                    ↓                   ↓
         ColorConversionView    ImageAdjustmentView ...
                    ↓                   ↓
        ColorConversionCtrl    ImageAdjustmentCtrl ...
                    ↓                   ↓
              ServiceProvider
                    ↓
          ┌─────────┴─────────┐
          ↓                   ↓
  ColorSpaceService   ImageProcessingService ...
```
(El diagrama muestra una vista simplificada. Cada módulo de UI tiene su controlador que interactúa con los servicios correspondientes a través del `ServiceProvider`.)

## 🎨 Componentes Reutilizables

(Esta sección permanece sin cambios)

## 🚀 Escalabilidad

(Esta sección permanece sin cambios)

## 📝 Buenas Prácticas

(Esta sección permanece sin cambios)

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
