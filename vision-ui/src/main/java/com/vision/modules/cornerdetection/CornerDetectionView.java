package com.vision.modules.cornerdetection;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CornerDetectionView extends VBox {

    private final ColorSpaceModel model;
    private final CornerDetectionController controller;

    private ImageDisplayPanel originalPanel;
    private ImageDisplayPanel processedPanel;

    private Button kirschButton, freiChenButton, harrisButton;
    private Slider harrisKSlider, harrisThresholdSlider;
    private Label harrisKLabel, harrisThresholdLabel;

    public CornerDetectionView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new CornerDetectionController(model);
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }

    private void initializeUI() {
        setPadding(new Insets(15));
        setSpacing(15);
        setAlignment(Pos.TOP_CENTER);

        originalPanel = new ImageDisplayPanel("Original", 600, 500);
        processedPanel = new ImageDisplayPanel("Procesada", 600, 500);
        HBox imagesBox = new HBox(20, originalPanel, processedPanel);
        imagesBox.setAlignment(Pos.CENTER);

        GridPane controlsGrid = new GridPane();
        controlsGrid.setHgap(20);
        controlsGrid.setVgap(15);
        controlsGrid.setAlignment(Pos.CENTER);

        VBox edgeBox = new VBox(10);
        edgeBox.setAlignment(Pos.CENTER_LEFT);
        kirschButton = new Button("Aplicar Kirsch");
        freiChenButton = new Button("Aplicar Frei-Chen");
        edgeBox.getChildren().addAll(new Label("Detección de Bordes:"), kirschButton, freiChenButton);
        controlsGrid.add(edgeBox, 0, 0);

        VBox harrisBox = new VBox(10);
        harrisBox.setAlignment(Pos.CENTER_LEFT);
        
        harrisKSlider = new Slider(0.01, 0.2, 0.04);
        harrisKLabel = new Label("k: 0.04");
        HBox kBox = new HBox(10, harrisKLabel, harrisKSlider);
        kBox.setAlignment(Pos.CENTER_LEFT);

        harrisThresholdSlider = new Slider(0.01, 1.0, 0.1);
        harrisThresholdLabel = new Label("Umbral: 0.10");
        HBox thresholdBox = new HBox(10, harrisThresholdLabel, harrisThresholdSlider);
        thresholdBox.setAlignment(Pos.CENTER_LEFT);

        harrisButton = new Button("Aplicar Harris");
        harrisBox.getChildren().addAll(new Label("Detector de Esquinas (Harris):"), kBox, thresholdBox, harrisButton);
        controlsGrid.add(harrisBox, 1, 0);

        getChildren().addAll(imagesBox, new Separator(), controlsGrid);
    }

    private void setupEventHandlers() {
        kirschButton.setOnAction(e -> {
            WritableImage result = controller.applyKirsch();
            if (result != null) processedPanel.setImage(result);
        });

        freiChenButton.setOnAction(e -> {
            WritableImage result = controller.applyFreiChen();
            if (result != null) processedPanel.setImage(result);
        });

        harrisButton.setOnAction(e -> {
            WritableImage result = controller.applyHarris(harrisKSlider.getValue(), harrisThresholdSlider.getValue());
            if (result != null) processedPanel.setImage(result);
        });

        harrisKSlider.valueProperty().addListener((obs, o, n) -> harrisKLabel.setText(String.format("k: %.2f", n)));
        harrisThresholdSlider.valueProperty().addListener((obs, o, n) -> harrisThresholdLabel.setText(String.format("Umbral: %.2f", n)));
    }

    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                Image image = model.getOriginalImage();
                originalPanel.setImage(image);
                processedPanel.setImage(null);
                boolean isImageLoaded = image != null;
                kirschButton.setDisable(!isImageLoaded);
                freiChenButton.setDisable(!isImageLoaded);
                harrisButton.setDisable(!isImageLoaded);
                harrisKSlider.setDisable(!isImageLoaded);
                harrisThresholdSlider.setDisable(!isImageLoaded);
            }

            @Override public void onTransformationApplied() {}
            @Override public void onTransformationChanged() {}
            @Override public void onAdjustmentsChanged() {}
            @Override public void onAdjustedImagesReady() {}
        });
    }
}
