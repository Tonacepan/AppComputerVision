package com.vision.modules.fourier;

import com.vision.core.ServiceProvider;
import com.vision.core.math.Complex;
import com.vision.model.ColorSpaceModel;
import com.vision.service.FourierService;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class FourierController {

    private final ColorSpaceModel model;
    private final FourierService fourierService;
    private Complex[][] frequencyDomain;

    public FourierController(ColorSpaceModel model) {
        this.model = model;
        this.fourierService = ServiceProvider.getInstance().getFourierService();
    }

    public WritableImage performFFT() {
        Image image = model.getOriginalImage();
        if (image == null) {
            showError("No hay imagen cargada.");
            return null;
        }

        if (!fourierService.isValidDimensions(image)) {
            showError("La imagen debe ser cuadrada y sus dimensiones una potencia de dos (ej. 256x256).");
            return null;
        }

        try {
            this.frequencyDomain = fourierService.fft(image);
            return fourierService.getSpectrumView(this.frequencyDomain);
        } catch (Exception e) {
            showError("Error al calcular la FFT: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public WritableImage performInverseFFT() {
        if (this.frequencyDomain == null) {
            showError("Primero debe aplicar la Transformada de Fourier (FFT).");
            return null;
        }

        try {
            return fourierService.inverseFft(this.frequencyDomain);
        } catch (Exception e) {
            showError("Error al calcular la IFFT: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Fourier");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
