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
│       │   └── ImageProcessingService.java # Servicio de procesamiento
│       └── util/
│           └── DefaultImageGenerator.java # Utilidades
│
├── vision-ui/                # Módulo UI (Interfaz de Usuario)
│   └── src/main/java/com/vision/
│       ├── controller/
│       │   └── MainController.java       # Controlador principal
│       ├── modules/                      # Módulos funcionales
│       │   ├── colorconversion/
│       │   │   ├── ColorConversionController.java
│       │   │   └── ColorConversionView.java
│       │   └── imageadjustment/
│       │       ├── ImageAdjustmentController.java
│       │       └── ImageAdjustmentView.java
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

**Responsabilidad**: Conversión entre diferentes espacios de color

**Componentes**:
- `ColorConversionController`: Orquesta las conversiones
- `ColorConversionView`: Vista con controles y visualización

**Funcionalidades**:
- Conversión RGB → CMY/CMYK/YIQ/HSI/HSV
- Visualización de canales individuales
- Generación de canales RGB

### Módulo: Image Adjustment
**Ubicación**: `vision-ui/src/main/java/com/vision/modules/imageadjustment/`

**Responsabilidad**: Ajustes de brillo y contraste

**Componentes**:
- `ImageAdjustmentController`: Controla los ajustes
- `ImageAdjustmentView`: Vista con sliders y previsualización

**Funcionalidades**:
- Ajuste de brillo (-1.0 a 1.0)
- Ajuste de contraste (0.1 a 3.0)
- Previsualización en tiempo real

## 🔌 Cómo Agregar un Nuevo Módulo

### Paso 1: Crear el Controlador
```java
package com.vision.modules.tumodulo;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;

public class TuModuloController {
    private final ColorSpaceModel model;
    private final TuServicio servicio;
    
    public TuModuloController(ColorSpaceModel model) {
        this.model = model;
        this.servicio = ServiceProvider.getInstance().getTuServicio();
    }
    
    public void tuMetodo() {
        // Implementa tu lógica
    }
}
```

### Paso 2: Crear la Vista
```java
package com.vision.modules.tumodulo;

import com.vision.model.ColorSpaceModel;
import javafx.scene.layout.VBox;

public class TuModuloView extends VBox {
    private final ColorSpaceModel model;
    private final TuModuloController controller;
    
    public TuModuloView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new TuModuloController(model);
        
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }
    
    private void initializeUI() {
        // Crea tus componentes
    }
    
    private void setupEventHandlers() {
        // Configura eventos
    }
    
    private void bindModelToView() {
        // Vincula el modelo a la vista
    }
}
```

### Paso 3: Registrar en MainController
```java
// En MainController.initializeModules()
TuModuloView tuModuloView = new TuModuloView(sharedModel);
Tab tuModuloTab = new Tab("Tu Módulo", tuModuloView);
tuModuloTab.setClosable(false);
moduleTabPane.getTabs().add(tuModuloTab);
```

## 🔄 Flujo de Datos

```
Usuario → MainController → Modelo Compartido
                              ↓
                    ┌─────────┴─────────┐
                    ↓                   ↓
         ColorConversionView    ImageAdjustmentView
                    ↓                   ↓
        ColorConversionCtrl    ImageAdjustmentCtrl
                    ↓                   ↓
              ServiceProvider
                    ↓
          ┌─────────┴─────────┐
          ↓                   ↓
  ColorSpaceService   ImageProcessingService
```

## 🎨 Componentes Reutilizables

### ImageDisplayPanel
Componente personalizado para mostrar imágenes:

```java
ImageDisplayPanel panel = new ImageDisplayPanel("Título", 600, 400);
panel.setImage(miImagen);
panel.setTitle("Nuevo Título");
```

**Ventajas**:
- Consistencia visual
- Menos código repetido
- Fácil mantenimiento

## 🚀 Escalabilidad

### Para agregar nuevas funcionalidades:
1. ✅ Crear nuevo módulo en `vision-ui/modules/`
2. ✅ Implementar Controller y View
3. ✅ Registrar en MainController
4. ✅ (Opcional) Agregar servicios en vision-core

### Para agregar nuevos servicios:
1. ✅ Crear servicio en `vision-core/service/`
2. ✅ Agregar getter en ServiceProvider
3. ✅ Usar desde cualquier módulo

### Para agregar componentes UI:
1. ✅ Crear en `vision-ui/components/`
2. ✅ Reutilizar en cualquier vista

## 📝 Buenas Prácticas

1. **Un módulo = Una responsabilidad**: Cada módulo debe tener una funcionalidad clara
2. **Modelo compartido**: Usa el modelo compartido para comunicación entre módulos
3. **Listener pattern**: Usa listeners del modelo para reaccionar a cambios
4. **Componentes reutilizables**: Extrae código común a componentes
5. **Servicios stateless**: Los servicios deben ser sin estado
6. **Documentación**: Documenta la responsabilidad de cada módulo

## 🔧 Compilación y Ejecución

```bash
# Compilar todo el proyecto
mvn clean install

# Ejecutar la aplicación
cd vision-app
mvn javafx:run
```

## 📚 Tecnologías Utilizadas

- **JavaFX 17**: Framework de UI
- **Maven**: Gestión de dependencias y módulos
- **Java 17**: Lenguaje de programación
- **Patrón MVC**: Arquitectura de presentación
- **Observer Pattern**: Comunicación modelo-vista

## 🎯 Beneficios de esta Arquitectura

✅ **Escalable**: Fácil agregar nuevos módulos
✅ **Mantenible**: Código organizado y separado por responsabilidades
✅ **Reutilizable**: Componentes y servicios compartidos
✅ **Testable**: Separación clara facilita testing
✅ **Extensible**: Nuevas funcionalidades sin afectar código existente
