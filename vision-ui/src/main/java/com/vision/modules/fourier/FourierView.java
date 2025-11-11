package com.vision.modules.fourier;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.FourierService;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FourierView extends VBox {

    private final ColorSpaceModel model;
    private final FourierController controller;
    private final FourierService fourierService;

    private ImageDisplayPanel originalPanel;
    private ImageDisplayPanel spectrumPanel;
    private ImageDisplayPanel reconstructedPanel;
    private Label statusLabel;
    private Button fftButton;
    private Button ifftButton;

    public FourierView(ColorSpaceModel model) {
        this.model = model;
        this.controller = new FourierController(model);
        this.fourierService = ServiceProvider.getInstance().getFourierService();
        initializeUI();
        setupEventHandlers();
        bindModelToView();
    }

    private void initializeUI() {
        setPadding(new Insets(15));
        setSpacing(15);
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Transformada de Fourier");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        statusLabel = new Label("Cargue una imagen cuadrada con dimensiones de potencia de dos (ej. 256x256).");
        statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

        fftButton = new Button("Aplicar FFT");
        ifftButton = new Button("Aplicar FFT Inversa");
        fftButton.setDisable(true);
        ifftButton.setDisable(true);

        HBox controlsBox = new HBox(20, fftButton, ifftButton);
        controlsBox.setAlignment(Pos.CENTER);

        originalPanel = new ImageDisplayPanel("Original", 400, 400);
        spectrumPanel = new ImageDisplayPanel("Espectro de Fourier", 400, 400);
        reconstructedPanel = new ImageDisplayPanel("Imagen Reconstruida (IFFT)", 400, 400);

        HBox imagesBox = new HBox(15, originalPanel, spectrumPanel, reconstructedPanel);
        imagesBox.setAlignment(Pos.CENTER);

        getChildren().addAll(
            titleLabel,
            new Separator(),
            controlsBox,
            statusLabel,
            new Separator(),
            imagesBox
        );
    }

    private void setupEventHandlers() {
        fftButton.setOnAction(e -> {
            reconstructedPanel.setImage(null);
            WritableImage spectrum = controller.performFFT();
            if (spectrum != null) {
                spectrumPanel.setImage(spectrum);
                ifftButton.setDisable(false);
            }
        });

        ifftButton.setOnAction(e -> {
            WritableImage reconstructed = controller.performInverseFFT();
            if (reconstructed != null) {
                reconstructedPanel.setImage(reconstructed);
            }
        });
    }

    private void bindModelToView() {
        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                Image image = model.getOriginalImage();
                originalPanel.setImage(image);
                spectrumPanel.setImage(null);
                reconstructedPanel.setImage(null);

                boolean isValid = fourierService.isValidDimensions(image);
                fftButton.setDisable(!isValid);
                ifftButton.setDisable(true);
                
                if (image == null) {
                    statusLabel.setText("Cargue una imagen para comenzar.");
                } else if (isValid) {
                    statusLabel.setText("Imagen válida. Puede aplicar la FFT.");
                } else {
                    statusLabel.setText("Imagen no válida. Cargue una imagen cuadrada con dimensiones de potencia de dos.");
                }
            }

            @Override
            public void onTransformationApplied() { /* No-op for this view */ }
            @Override
            public void onTransformationChanged() { /* No-op for this view */ }
            @Override
            public void onAdjustmentsChanged() { /* No-op for this view */ }
            @Override
            public void onAdjustedImagesReady() { /* No-op for this view */ }
        });
    }
}
