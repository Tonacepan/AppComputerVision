package com.vision.modules.geometrictransformation;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GeometricTransformationView extends VBox {

    private final ColorSpaceModel model;
    private final GeometricTransformationController controller;

    private final ImageDisplayPanel originalImagePanel = new ImageDisplayPanel("Original", 300, 300);
    private final ImageDisplayPanel transformedImagePanel = new ImageDisplayPanel("Transformada", 300, 300);

    private Slider translateXSlider, translateYSlider, angleSlider, scaleXSlider, scaleYSlider;
    private Label translateXValue, translateYValue, angleValue, scaleXValue, scaleYValue;

    public GeometricTransformationView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new GeometricTransformationController(model);
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }

    private void initializeUI() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);

        HBox imageRow = new HBox(20, originalImagePanel, transformedImagePanel);
        imageRow.setAlignment(Pos.CENTER);

        GridPane controls = createControlsGrid();

        getChildren().addAll(imageRow, controls);
    }

    private GridPane createControlsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Translation
        translateXSlider = new Slider(-100, 100, 0);
        translateYSlider = new Slider(-100, 100, 0);
        translateXValue = new Label("0");
        translateYValue = new Label("0");
        Button translateButton = new Button("Trasladar");
        grid.add(new Label("Traslación X:"), 0, 0);
        grid.add(translateXSlider, 1, 0);
        grid.add(translateXValue, 2, 0);
        grid.add(new Label("Traslación Y:"), 0, 1);
        grid.add(translateYSlider, 1, 1);
        grid.add(translateYValue, 2, 1);
        grid.add(translateButton, 3, 0, 1, 2);


        // Rotation
        angleSlider = new Slider(0, 360, 0);
        angleValue = new Label("0°");
        Button rotateButton = new Button("Rotar");
        grid.add(new Label("Rotación:"), 0, 2);
        grid.add(angleSlider, 1, 2);
        grid.add(angleValue, 2, 2);
        grid.add(rotateButton, 3, 2);

        // Scaling
        scaleXSlider = new Slider(0.1, 2, 1);
        scaleYSlider = new Slider(0.1, 2, 1);
        scaleXValue = new Label("1.0");
        scaleYValue = new Label("1.0");
        Button scaleButton = new Button("Escalar");
        grid.add(new Label("Escala X:"), 0, 3);
        grid.add(scaleXSlider, 1, 3);
        grid.add(scaleXValue, 2, 3);
        grid.add(new Label("Escala Y:"), 0, 4);
        grid.add(scaleYSlider, 1, 4);
        grid.add(scaleYValue, 2, 4);
        grid.add(scaleButton, 3, 3, 1, 2);
        
        // Reset
        Button resetButton = new Button("Resetear Imagen");
        grid.add(resetButton, 0, 5, 4, 1);
        resetButton.setMaxWidth(Double.MAX_VALUE);


        // Event Handlers for buttons
        translateButton.setOnAction(e -> controller.translateImage(translateXSlider.getValue(), translateYSlider.getValue()));
        rotateButton.setOnAction(e -> controller.rotateImage(angleSlider.getValue()));
        scaleButton.setOnAction(e -> controller.scaleImage(scaleXSlider.getValue(), scaleYSlider.getValue()));
        resetButton.setOnAction(e -> controller.resetImage());

        return grid;
    }

    private void setupEventHandlers() {
        // Bind slider values to labels
        translateXSlider.valueProperty().addListener((obs, old, n) -> translateXValue.setText(String.format("%.0f", n)));
        translateYSlider.valueProperty().addListener((obs, old, n) -> translateYValue.setText(String.format("%.0f", n)));
        angleSlider.valueProperty().addListener((obs, old, n) -> angleValue.setText(String.format("%.0f°", n)));
        scaleXSlider.valueProperty().addListener((obs, old, n) -> scaleXValue.setText(String.format("%.2f", n)));
        scaleYSlider.valueProperty().addListener((obs, old, n) -> scaleYValue.setText(String.format("%.2f", n)));
    }

    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                originalImagePanel.setImage(model.getOriginalImage());
                transformedImagePanel.setImage(model.getOriginalImage());
            }

            @Override
            public void onTransformationApplied() {
                transformedImagePanel.setImage(model.getTransformedImage());
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
