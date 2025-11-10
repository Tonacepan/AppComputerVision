package com.vision.modules.morphological;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MorphologicalView extends VBox {

    private final ColorSpaceModel model;
    private final MorphologicalController controller;

    private final ImageDisplayPanel originalImagePanel = new ImageDisplayPanel("Original", 300, 300);
    private final ImageDisplayPanel noisyImagePanel = new ImageDisplayPanel("Con Ruido", 300, 300);
    private final ImageDisplayPanel filteredImagePanel = new ImageDisplayPanel("Filtrada", 300, 300);

    private Slider noiseSlider;
    private Label noiseValue;

    public MorphologicalView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new MorphologicalController(model);
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }

    private void initializeUI() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);

        HBox imageRow = new HBox(20, originalImagePanel, noisyImagePanel, filteredImagePanel);
        imageRow.setAlignment(Pos.CENTER);

        GridPane controls = createControlsGrid();

        getChildren().addAll(imageRow, controls);
    }

    private GridPane createControlsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Noise controls
        noiseSlider = new Slider(0, 1, 0.1);
        noiseValue = new Label("10%");
        Button saltButton = new Button("Añadir Ruido Sal");
        Button pepperButton = new Button("Añadir Ruido Pimienta");

        grid.add(new Label("Porcentaje de Ruido:"), 0, 0);
        grid.add(noiseSlider, 1, 0);
        grid.add(noiseValue, 2, 0);
        grid.add(saltButton, 0, 1);
        grid.add(pepperButton, 1, 1);

        // Morphological operations
        Button erodeButton = new Button("Erosión");
        Button dilateButton = new Button("Dilatación");
        Button openButton = new Button("Apertura");
        Button closeButton = new Button("Clausura");

        grid.add(erodeButton, 0, 2);
        grid.add(dilateButton, 1, 2);
        grid.add(openButton, 0, 3);
        grid.add(closeButton, 1, 3);

        // Event Handlers for buttons
        saltButton.setOnAction(e -> controller.addSaltNoise(noiseSlider.getValue()));
        pepperButton.setOnAction(e -> controller.addPepperNoise(noiseSlider.getValue()));
        erodeButton.setOnAction(e -> controller.erode());
        dilateButton.setOnAction(e -> controller.dilate());
        openButton.setOnAction(e -> controller.open());
        closeButton.setOnAction(e -> controller.close());

        return grid;
    }

    private void setupEventHandlers() {
        noiseSlider.valueProperty().addListener((obs, old, n) -> noiseValue.setText(String.format("%.0f%%", n.doubleValue() * 100)));
    }

    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                originalImagePanel.setImage(model.getOriginalImage());
                noisyImagePanel.setImage(null);
                filteredImagePanel.setImage(null);
            }

            @Override
            public void onTransformationApplied() {
                if (controller.getNoisyImage() != null) {
                    noisyImagePanel.setImage(controller.getNoisyImage());
                }
                filteredImagePanel.setImage(model.getTransformedImage());
            }

            @Override
            public void onTransformationChanged() {}

            @Override
            public void onAdjustmentsChanged() {}

            @Override
            public void onAdjustedImagesReady() {}
        });
    }
}
