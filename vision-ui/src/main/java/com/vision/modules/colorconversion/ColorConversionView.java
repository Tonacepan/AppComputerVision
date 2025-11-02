package com.vision.modules.colorconversion;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Vista para el módulo de conversión de espacios de color
 */
public class ColorConversionView extends VBox {
    
    private final ColorSpaceModel model;
    private final ColorConversionController controller;
    
    // Componentes UI
    private ComboBox<String> transformationComboBox;
    private Button applyButton;
    private Button generateRGBButton;
    private Label statusLabel;
    
    // Paneles de imágenes
    private ImageDisplayPanel originalPanel;
    private ImageDisplayPanel transformedPanel;
    private ImageDisplayPanel channel1Panel;
    private ImageDisplayPanel channel2Panel;
    private ImageDisplayPanel channel3Panel;
    private ImageDisplayPanel channel4Panel;
    
    public ColorConversionView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new ColorConversionController(model);
        
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }
    
    private void initializeUI() {
        setPadding(new Insets(15));
        setSpacing(15);
        
        // Título
        Label titleLabel = new Label("Conversión de Espacios de Color");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Controles
        transformationComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "RGB", "RGB → CMY", "RGB → CMYK", "RGB → YIQ", "RGB → HSI", "RGB → HSV"
        ));
        transformationComboBox.setPromptText("Selecciona transformación...");
        transformationComboBox.setPrefWidth(250);
        
        applyButton = new Button("Aplicar Transformación");
        applyButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        applyButton.setDisable(true);
        
        generateRGBButton = new Button("Generar Canales RGB");
        generateRGBButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        generateRGBButton.setDisable(true);
        
        statusLabel = new Label("Carga una imagen para comenzar");
        statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        
        HBox controlsBox = new HBox(10, transformationComboBox, applyButton, generateRGBButton);
        controlsBox.setAlignment(Pos.CENTER);
        
        // Paneles de imágenes principales
        originalPanel = new ImageDisplayPanel("Imagen Original", 600, 400);
        transformedPanel = new ImageDisplayPanel("Imagen Transformada", 600, 400);
        
        HBox mainImagesBox = new HBox(15, originalPanel, transformedPanel);
        mainImagesBox.setAlignment(Pos.CENTER);
        
        // Paneles de canales
        channel1Panel = new ImageDisplayPanel("Canal 1", 350, 260);
        channel2Panel = new ImageDisplayPanel("Canal 2", 350, 260);
        channel3Panel = new ImageDisplayPanel("Canal 3", 350, 260);
        channel4Panel = new ImageDisplayPanel("Canal 4", 350, 260);
        channel4Panel.setVisible(false);
        channel4Panel.setManaged(false);
        
        Label channelsTitle = new Label("Canales Individuales:");
        channelsTitle.setStyle("-fx-font-weight: bold;");
        
        HBox channelsBox = new HBox(10, channel1Panel, channel2Panel, channel3Panel, channel4Panel);
        channelsBox.setAlignment(Pos.CENTER);
        
        VBox channelsContainer = new VBox(10, channelsTitle, channelsBox);
        channelsContainer.setAlignment(Pos.CENTER);
        
        // Agregar todo al contenedor principal
        getChildren().addAll(
            createCenteredBox(titleLabel),
            new Separator(),
            createCenteredBox(controlsBox),
            createCenteredBox(statusLabel),
            new Separator(),
            mainImagesBox,
            new Separator(),
            channelsContainer
        );
    }
    
    private void setupEventHandlers() {
        applyButton.setOnAction(e -> {
            String transformation = transformationComboBox.getValue();
            if (transformation != null) {
                model.setCurrentTransformation(transformation);
                controller.applyTransformation(transformation);
            }
        });
        
        generateRGBButton.setOnAction(e -> {
            model.setCurrentTransformation("RGB");
            controller.applyTransformation("RGB");
        });
        
        transformationComboBox.setOnAction(e -> {
            String selected = transformationComboBox.getValue();
            if (selected != null && model.getOriginalImage() != null) {
                applyButton.setDisable(false);
                statusLabel.setText("Transformación seleccionada: " + selected + " - Haz clic en Aplicar");
            }
        });
    }
    
    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                originalPanel.setImage(model.getOriginalImage());
                applyButton.setDisable(transformationComboBox.getValue() == null);
                generateRGBButton.setDisable(false);
                statusLabel.setText("Imagen cargada correctamente");
            }
            
            @Override
            public void onTransformationApplied() {
                transformedPanel.setImage(model.getTransformedImage());
                updateChannelImages();
                updateChannelLabels(model.getCurrentTransformation());
                statusLabel.setText("Transformación aplicada: " + model.getCurrentTransformation());
            }
            
            @Override
            public void onTransformationChanged() {
                // Opcional
            }
            
            @Override
            public void onAdjustmentsChanged() {
                // No aplica en este módulo
            }
            
            @Override
            public void onAdjustedImagesReady() {
                // No aplica en este módulo
            }
        });
    }
    
    private void updateChannelImages() {
        var channels = model.getChannelImages();
        if (channels != null && channels.length > 0) {
            channel1Panel.setImage(channels.length > 0 ? channels[0] : null);
            channel2Panel.setImage(channels.length > 1 ? channels[1] : null);
            channel3Panel.setImage(channels.length > 2 ? channels[2] : null);
            channel4Panel.setImage(channels.length > 3 ? channels[3] : null);
        }
    }
    
    private void updateChannelLabels(String transformation) {
        String[] names = getChannelNames(transformation);
        channel1Panel.setTitle(names[0]);
        channel2Panel.setTitle(names[1]);
        channel3Panel.setTitle(names[2]);
        
        boolean hasFourChannels = names.length > 3 && !names[3].isEmpty();
        channel4Panel.setVisible(hasFourChannels);
        channel4Panel.setManaged(hasFourChannels);
        if (hasFourChannels) {
            channel4Panel.setTitle(names[3]);
        }
    }
    
    private String[] getChannelNames(String transformation) {
        if (transformation == null) transformation = "RGB";
        return switch (transformation) {
            case "RGB" -> new String[]{"Rojo", "Verde", "Azul", "Escala de Grises"};
            case "RGB → CMY" -> new String[]{"Cyan", "Magenta", "Yellow", ""};
            case "RGB → CMYK" -> new String[]{"Cyan", "Magenta", "Yellow", "Black"};
            case "RGB → YIQ" -> new String[]{"Y (Luminancia)", "I (Croma)", "Q (Croma)", ""};
            case "RGB → HSI" -> new String[]{"H (Matiz)", "S (Saturación)", "I (Intensidad)", ""};
            case "RGB → HSV" -> new String[]{"H (Matiz)", "S (Saturación)", "V (Valor)", ""};
            default -> new String[]{"Canal 1", "Canal 2", "Canal 3", ""};
        };
    }
    
    private HBox createCenteredBox(javafx.scene.Node node) {
        HBox box = new HBox(node);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
