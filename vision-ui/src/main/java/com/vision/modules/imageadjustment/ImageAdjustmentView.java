package com.vision.modules.imageadjustment;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Vista para el módulo de ajustes de imagen (brillo y contraste)
 */
public class ImageAdjustmentView extends VBox {
    
    private final ColorSpaceModel model;
    private final ImageAdjustmentController controller;
    
    // Componentes UI
    private Slider brightnessSlider;
    private Slider contrastSlider;
    private Label brightnessValueLabel;
    private Label contrastValueLabel;
    private Button resetButton;
    private Label statusLabel;
    
    // Paneles de imágenes
    private ImageDisplayPanel originalPanel;
    private ImageDisplayPanel brightnessPanel;
    private ImageDisplayPanel contrastPanel;
    
    public ImageAdjustmentView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new ImageAdjustmentController(model);
        
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }
    
    private void initializeUI() {
        setPadding(new Insets(15));
        setSpacing(15);
        
        // Título
        Label titleLabel = new Label("Ajustes de Brillo y Contraste");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Controles de brillo
        Label brightnessLabel = new Label("Brillo:");
        brightnessLabel.setStyle("-fx-font-weight: bold;");
        brightnessSlider = new Slider(-1.0, 1.0, 0.0);
        brightnessSlider.setShowTickLabels(true);
        brightnessSlider.setShowTickMarks(true);
        brightnessSlider.setMajorTickUnit(0.5);
        brightnessSlider.setMinorTickCount(5);
        brightnessSlider.setPrefWidth(400);
        brightnessValueLabel = new Label("0.00");
        brightnessValueLabel.setStyle("-fx-min-width: 50px;");
        
        HBox brightnessBox = new HBox(10, brightnessLabel, brightnessSlider, brightnessValueLabel);
        brightnessBox.setAlignment(Pos.CENTER);
        
        // Controles de contraste
        Label contrastLabel = new Label("Contraste:");
        contrastLabel.setStyle("-fx-font-weight: bold;");
        contrastSlider = new Slider(0.1, 3.0, 1.0);
        contrastSlider.setShowTickLabels(true);
        contrastSlider.setShowTickMarks(true);
        contrastSlider.setMajorTickUnit(0.5);
        contrastSlider.setMinorTickCount(5);
        contrastSlider.setPrefWidth(400);
        contrastValueLabel = new Label("1.00");
        contrastValueLabel.setStyle("-fx-min-width: 50px;");
        
        HBox contrastBox = new HBox(10, contrastLabel, contrastSlider, contrastValueLabel);
        contrastBox.setAlignment(Pos.CENTER);
        
        // Botón reset
        resetButton = new Button("Restablecer Valores");
        resetButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white;");
        
        statusLabel = new Label("Carga una imagen para comenzar");
        statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        
        VBox controlsContainer = new VBox(10, brightnessBox, contrastBox, resetButton, statusLabel);
        controlsContainer.setAlignment(Pos.CENTER);
        
        // Paneles de imágenes
        originalPanel = new ImageDisplayPanel("Imagen Original", 450, 350);
        brightnessPanel = new ImageDisplayPanel("Ajuste de Brillo", 450, 350);
        contrastPanel = new ImageDisplayPanel("Ajuste de Contraste", 450, 350);
        
        HBox imagesBox = new HBox(15, originalPanel, brightnessPanel, contrastPanel);
        imagesBox.setAlignment(Pos.CENTER);
        
        // Agregar todo
        getChildren().addAll(
            createCenteredBox(titleLabel),
            new Separator(),
            controlsContainer,
            new Separator(),
            imagesBox
        );
    }
    
    private void setupEventHandlers() {
        // Actualizar label mientras se arrastra el slider
        brightnessSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            brightnessValueLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });
        
        contrastSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            contrastValueLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });
        
        // Aplicar cambios cuando se suelta el slider
        brightnessSlider.setOnMouseReleased(e -> {
            controller.adjustBrightness(brightnessSlider.getValue());
        });
        
        contrastSlider.setOnMouseReleased(e -> {
            controller.adjustContrast(contrastSlider.getValue());
        });
        
        resetButton.setOnAction(e -> {
            brightnessSlider.setValue(0.0);
            contrastSlider.setValue(1.0);
            controller.resetAdjustments();
        });
    }
    
    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                originalPanel.setImage(model.getOriginalImage());
                brightnessPanel.setImage(null);
                contrastPanel.setImage(null);
                brightnessSlider.setValue(0.0);
                contrastSlider.setValue(1.0);
                statusLabel.setText("Imagen cargada - Ajusta brillo y contraste");
            }
            
            @Override
            public void onTransformationApplied() {
                // No aplica en este módulo
            }
            
            @Override
            public void onTransformationChanged() {
                // No aplica en este módulo
            }
            
            @Override
            public void onAdjustmentsChanged() {
                // Opcional: feedback visual mientras se arrastra
            }
            
            @Override
            public void onAdjustedImagesReady() {
                brightnessPanel.setImage(model.getBrightnessImage());
                contrastPanel.setImage(model.getContrastImage());
                statusLabel.setText(String.format("Brillo: %.2f | Contraste: %.2f", 
                    model.getBrightness(), model.getContrast()));
            }
        });
    }
    
    private HBox createCenteredBox(javafx.scene.Node node) {
        HBox box = new HBox(node);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
