package com.vision.controller;

import com.vision.model.ColorSpaceModel;
import com.vision.modules.colorconversion.ColorConversionView;
import com.vision.modules.imageadjustment.ImageAdjustmentView;
import com.vision.modules.histogram.HistogramView;
import com.vision.modules.logicaloperations.LogicalOperationsView;
import com.vision.modules.geometrictransformation.GeometricTransformationView;
import com.vision.modules.morphological.MorphologicalView;
import com.vision.util.DefaultImageGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador principal que maneja la navegación entre módulos
 * Arquitectura modular con separación de vistas y controladores
 */
public class MainController implements Initializable {

    @FXML
    private TabPane moduleTabPane;

    @FXML
    private Button loadImageButton;

    @FXML
    private Button loadDefaultImageButton;

    // Modelo compartido entre todos los módulos
    private final ColorSpaceModel sharedModel = new ColorSpaceModel();

    // Vistas de módulos
    private ColorConversionView colorConversionView;
    private ImageAdjustmentView imageAdjustmentView;
    private HistogramView histogramView;
    private LogicalOperationsView logicalOperationsView;
    private GeometricTransformationView geometricTransformationView;
    private MorphologicalView morphologicalView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeModules();
        setupEventHandlers();
    }

    /**
     * Inicializa todos los módulos de la aplicación
     */
    private void initializeModules() {
        // Módulo 1: Conversión de Espacios de Color
        colorConversionView = new ColorConversionView(sharedModel);
        Tab colorConversionTab = new Tab("Conversión de Color", colorConversionView);
        colorConversionTab.setClosable(false);

        // Módulo 2: Ajustes de Imagen
        imageAdjustmentView = new ImageAdjustmentView(sharedModel);
        Tab imageAdjustmentTab = new Tab("Ajustes de Imagen", imageAdjustmentView);
        imageAdjustmentTab.setClosable(false);

        // Módulo 3: Análisis de Histogramas
        histogramView = new HistogramView(sharedModel);
        Tab histogramTab = new Tab("Histograma", histogramView);
        histogramTab.setClosable(false);

        // Módulo 4: Operaciones Lógicas
        logicalOperationsView = new LogicalOperationsView(sharedModel);
        Tab logicalOperationsTab = new Tab("Operaciones Lógicas", logicalOperationsView);
        logicalOperationsTab.setClosable(false);

        // Módulo 5: Transformaciones Geométricas
        geometricTransformationView = new GeometricTransformationView(sharedModel);
        Tab geometricTransformationTab = new Tab("Transformaciones Geométricas", geometricTransformationView);
        geometricTransformationTab.setClosable(false);

        // Módulo 6: Operaciones Morfológicas
        morphologicalView = new MorphologicalView(sharedModel);
        Tab morphologicalTab = new Tab("Operaciones Morfológicas", morphologicalView);
        morphologicalTab.setClosable(false);

        // Agregar tabs al TabPane
        moduleTabPane.getTabs().addAll(colorConversionTab, imageAdjustmentTab, histogramTab, logicalOperationsTab, geometricTransformationTab, morphologicalTab);
    }

    /**
     * Configura los event handlers para los botones principales
     */
    private void setupEventHandlers() {
        loadImageButton.setOnAction(event -> handleLoadImage());
        loadDefaultImageButton.setOnAction(event -> handleLoadDefaultImage());
    }
    
    /**
     * Maneja la carga de una imagen desde el sistema de archivos
     */
    private void handleLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        
        Stage stage = (Stage) loadImageButton.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                if (!image.isError()) {
                    sharedModel.setOriginalImage(image);
                } else {
                    showError("Error al cargar la imagen");
                }
            } catch (Exception e) {
                showError("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Maneja la carga de una imagen predeterminada
     */
    private void handleLoadDefaultImage() {
        try {
            Image defaultImage = DefaultImageGenerator.createDefaultImage();
            sharedModel.setOriginalImage(defaultImage);
        } catch (Exception e) {
            showError("Error al generar imagen predeterminada: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
