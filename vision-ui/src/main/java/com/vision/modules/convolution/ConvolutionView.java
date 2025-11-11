package com.vision.modules.convolution;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import com.vision.util.KernelProvider;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConvolutionView extends VBox {

    private final ColorSpaceModel model;
    private final ConvolutionController controller;

    private ImageDisplayPanel originalPanel;
    private ImageDisplayPanel processedPanel;

    private ComboBox<KernelProvider.LowPass> lowPassCombo;
    private ComboBox<KernelProvider.HighPass> highPassCombo;
    private Slider cannyLowSlider, cannyHighSlider;
    private Label cannyLowLabel, cannyHighLabel;
    private Button applyLowPassBtn, applyHighPassBtn, applyCannyBtn;

    public ConvolutionView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new ConvolutionController(model);
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
        controlsGrid.setHgap(15);
        controlsGrid.setVgap(10);
        controlsGrid.setAlignment(Pos.CENTER);

        lowPassCombo = new ComboBox<>(FXCollections.observableArrayList(KernelProvider.LowPass.values()));
        applyLowPassBtn = new Button("Aplicar Pasa-Bajas");
        controlsGrid.add(new Label("Filtros Pasa-Bajas (Desenfoque):"), 0, 0);
        controlsGrid.add(lowPassCombo, 1, 0);
        controlsGrid.add(applyLowPassBtn, 2, 0);

        highPassCombo = new ComboBox<>(FXCollections.observableArrayList(KernelProvider.HighPass.values()));
        applyHighPassBtn = new Button("Aplicar Pasa-Altas");
        controlsGrid.add(new Label("Filtros Pasa-Altas (Definición):"), 0, 1);
        controlsGrid.add(highPassCombo, 1, 1);
        controlsGrid.add(applyHighPassBtn, 2, 1);

        VBox cannyBox = new VBox(5);
        cannyLowSlider = new Slider(0, 255, 30);
        cannyHighSlider = new Slider(0, 255, 75);
        cannyLowLabel = new Label("Umbral Bajo: 30");
        cannyHighLabel = new Label("Umbral Alto: 75");
        applyCannyBtn = new Button("Aplicar Canny");
        
        HBox lowThreshBox = new HBox(10, cannyLowLabel, cannyLowSlider);
        HBox highThreshBox = new HBox(10, cannyHighLabel, cannyHighSlider);
        cannyBox.getChildren().addAll(new Label("Detector de Bordes Canny:"), lowThreshBox, highThreshBox, applyCannyBtn);
        
        controlsGrid.add(cannyBox, 0, 2, 3, 1);

        applyLowPassBtn.setOnAction(e -> {
            if (lowPassCombo.getValue() != null) {
                WritableImage result = controller.applyLowPassFilter(lowPassCombo.getValue());
                processedPanel.setImage(result);
            }
        });
        applyHighPassBtn.setOnAction(e -> {
            if (highPassCombo.getValue() != null) {
                WritableImage result = controller.applyHighPassFilter(highPassCombo.getValue());
                processedPanel.setImage(result);
            }
        });
        applyCannyBtn.setOnAction(e -> {
            WritableImage result = controller.applyCanny(cannyLowSlider.getValue(), cannyHighSlider.getValue());
            processedPanel.setImage(result);
        });

        getChildren().addAll(imagesBox, new Separator(), controlsGrid);
    }

    private void setupEventHandlers() {
        cannyLowSlider.valueProperty().addListener((obs, old, n) -> cannyLowLabel.setText(String.format("Umbral Bajo: %.0f", n)));
        cannyHighSlider.valueProperty().addListener((obs, old, n) -> cannyHighLabel.setText(String.format("Umbral Alto: %.0f", n)));
    }

    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                Image image = model.getOriginalImage();
                originalPanel.setImage(image);
                processedPanel.setImage(null);
                boolean isImageLoaded = image != null;
                lowPassCombo.setDisable(!isImageLoaded);
                highPassCombo.setDisable(!isImageLoaded);
                cannyLowSlider.setDisable(!isImageLoaded);
                cannyHighSlider.setDisable(!isImageLoaded);
                applyLowPassBtn.setDisable(!isImageLoaded);
                applyHighPassBtn.setDisable(!isImageLoaded);
                applyCannyBtn.setDisable(!isImageLoaded);
            }

            @Override public void onTransformationApplied() {}
            @Override public void onTransformationChanged() {}
            @Override public void onAdjustmentsChanged() {}
            @Override public void onAdjustedImagesReady() {}
        });
    }
}
