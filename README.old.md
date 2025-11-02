E# Vision Processor App

Aplicación completa de procesamiento de imágenes y conversión de espacios de color desarrollada con JavaFX 17.

## Características

### Pestaña 1: Procesamiento de Imágenes
- Carga de imágenes (PNG, JPG, JPEG, BMP, GIF)
- Conversión a escala de grises
- Separación de canales RGB
- Ajuste de brillo (-1.0 a 1.0)
- Ajuste de contraste (0.1 a 3.0)
- Vista en tiempo real de todos los procesamientos

### Pestaña 2: Conversión de Espacios de Color
- Carga de imágenes personalizadas o imagen predeterminada
- Conversiones disponibles:
  - RGB → CMY
  - RGB → CMYK
  - RGB → YIQ
  - RGB → HSI
  - RGB → HSV
- Visualización de canales individuales
- Interfaz intuitiva con tooltips

## Tecnologías

- **Java 17** - Versión estable y moderna
- **JavaFX 17** - Framework de interfaz gráfica
- **Maven** - Gestión de dependencias y construcción
- **Arquitectura MVC** - Separación clara de responsabilidades

## Estructura del Proyecto

```
src/main/java/com/vision/
├── VisionProcessorApp.java           # Aplicación principal
├── controller/
│   ├── MainController.java           # Controlador principal (pestañas)
│   ├── ImageProcessorController.java # Controlador procesamiento
│   └── ColorConverterController.java # Controlador espacios de color
├── model/
│   ├── ImageModel.java               # Modelo para procesamiento
│   └── ColorSpaceModel.java          # Modelo para espacios de color
├── service/
│   ├── ImageProcessingService.java   # Servicios procesamiento
│   └── ColorSpaceService.java        # Servicios espacios de color
├── view/
│   ├── ImageProcessorView.java       # Vista procesamiento (escalable)
│   └── ColorConverterView.java       # Vista espacios de color (escalable)
└── util/
    └── DefaultImageGenerator.java    # Generador imágenes prueba

src/main/resources/com/vision/view/
└── main.fxml                         # Interfaz principal
```

## Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior

### Comandos

1. **Compilar:**
```bash
mvn clean compile
```

2. **Ejecutar:**
```bash
mvn javafx:run
```

3. **Empaquetar:**
```bash
mvn clean package
```

## Uso de la Aplicación

### Procesamiento de Imágenes
1. Ir a la pestaña "Procesamiento de Imágenes"
2. Hacer clic en "Cargar Imagen" y seleccionar un archivo
3. La aplicación automáticamente:
   - Muestra la imagen original
   - Genera la versión en escala de grises
   - Separa los canales RGB
4. Usar los sliders para ajustar brillo y contraste en tiempo real

### Conversión de Espacios de Color
1. Ir a la pestaña "Conversión de Espacios de Color"
2. Cargar una imagen o usar "Imagen Predeterminada"
3. Seleccionar una transformación del menú desplegable
4. Hacer clic en "Aplicar Transformación"
5. Ver la imagen transformada y los canales individuales

## Características Técnicas

### Arquitectura MVC Escalable
- **Modelos:** Manejan datos y notifican cambios usando patrón Observer
- **Vistas:** Clases separadas para cada funcionalidad, fácil mantenimiento
- **Controladores:** Lógica de negocio separada de la presentación

### Servicios Especializados
- Algoritmos de procesamiento optimizados
- Conversiones matemáticas precisas entre espacios de color
- Manejo robusto de errores

### Interfaz Moderna
- Diseño responsivo con ScrollPane
- Tooltips informativos
- Controles intuitivos
- Separación clara por pestañas

## Extensibilidad

El proyecto está diseñado para ser fácilmente extensible:

1. **Nuevos espacios de color:** Agregar métodos en `ColorSpaceService`
2. **Nuevos filtros:** Extender `ImageProcessingService`
3. **Nuevas vistas:** Crear clases vista siguiendo el patrón establecido
4. **Nuevas pestañas:** Agregar en `MainController` y FXML

## Desarrollo

La aplicación combina las mejores prácticas de:
- Programación orientada a objetos
- Patrones de diseño (MVC, Observer)
- Arquitectura limpia
- Separación de responsabilidades
- Código mantenible y escalable
