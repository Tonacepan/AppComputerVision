package com.vision.modules.histogram;

import com.vision.model.ColorSpaceModel;
import com.vision.service.HistogramService;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HistogramView extends VBox {

    private final ColorSpaceModel model;
    private final HistogramController controller;

    private ImageDisplayPanel originalImageView, transformedImageView;
    private BarChart<String, Number> histogramChart, transformedHistogramChart;
    private LineChart<String, Number> cdfChart;
    private TextArea statsTextArea;
    private Button calculateButton, applyButton;
    private ComboBox<HistogramService.TransformationType> transformationComboBox;
    private TextField alphaField, potField;

    public HistogramView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new HistogramController(model);
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }

    private void initializeUI() {
        setSpacing(10);
        setPadding(new Insets(10));

        originalImageView = new ImageDisplayPanel("Imagen Original", 350, 250);
        transformedImageView = new ImageDisplayPanel("Imagen Transformada", 350, 250);
        HBox imageBox = new HBox(20, originalImageView, transformedImageView);

        histogramChart = createBarChart("Histograma Original (phi)");
        cdfChart = createLineChart("Densidad Acumulada (Dpi)");
        transformedHistogramChart = createBarChart("Histograma Transformado (phi)");
        HBox chartBox = new HBox(10, histogramChart, cdfChart, transformedHistogramChart);

        VBox statsBox = new VBox(5, new Label("Estadísticas:"), createStatsTextArea());
        VBox calculationBox = new VBox(10, createCalculateButton(), statsBox);

        VBox transformBox = createTransformControls();

        HBox bottomBox = new HBox(20, calculationBox, transformBox);
        getChildren().addAll(imageBox, chartBox, bottomBox);
    }

    private BarChart<String, Number> createBarChart(String title) {
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        chart.setTitle(title);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setBarGap(0);
        chart.setCategoryGap(0);
        chart.setPrefWidth(300);
        return chart;
    }

    private LineChart<String, Number> createLineChart(String title) {
        LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        chart.setTitle(title);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefWidth(300);
        return chart;
    }

    private TextArea createStatsTextArea() {
        statsTextArea = new TextArea();
        statsTextArea.setEditable(false);
        statsTextArea.setPrefHeight(120);
        return statsTextArea;
    }

    private Button createCalculateButton() {
        calculateButton = new Button("Calcular Histograma y Estadísticas");
        calculateButton.setMaxWidth(Double.MAX_VALUE);
        return calculateButton;
    }

    private VBox createTransformControls() {
        Label transformLabel = new Label("Transformación de Histograma:");
        transformationComboBox = new ComboBox<>(FXCollections.observableArrayList(HistogramService.TransformationType.values()));
        transformationComboBox.setValue(HistogramService.TransformationType.UNIFORM);

        alphaField = new TextField("1.0");
        potField = new TextField("2.0");

        GridPane paramsGrid = new GridPane();
        paramsGrid.setHgap(10);
        paramsGrid.setVgap(5);
        paramsGrid.add(new Label("Alpha (Exp/Rayleigh):"), 0, 0);
        paramsGrid.add(alphaField, 1, 0);
        paramsGrid.add(new Label("Pot (Hyperbolic):"), 0, 1);
        paramsGrid.add(potField, 1, 1);

        applyButton = new Button("Aplicar Transformación");
        applyButton.setMaxWidth(Double.MAX_VALUE);

        return new VBox(10, transformLabel, transformationComboBox, paramsGrid, applyButton);
    }

    private void setupEventHandlers() {
        calculateButton.setOnAction(e -> 
            controller.calculateAndDisplayHistogram(histogramChart, cdfChart, statsTextArea)
        );
        
        applyButton.setOnAction(e -> 
            controller.applyTransformation(
                transformationComboBox.getValue(),
                alphaField.getText(),
                potField.getText(),
                transformedImageView,
                transformedHistogramChart,
                statsTextArea
            )
        );
    }

    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                if (model.getOriginalImage() != null) {
                    originalImageView.setImage(model.getOriginalImage());
                    transformedImageView.setImage(null);
                    histogramChart.getData().clear();
                    cdfChart.getData().clear();
                    transformedHistogramChart.getData().clear();
                    statsTextArea.clear();
                }
            }
            @Override public void onTransformationApplied() {}
            @Override public void onTransformationChanged() {}
            @Override public void onAdjustmentsChanged() {}
            @Override public void onAdjustedImagesReady() {}
        });
    }
}
