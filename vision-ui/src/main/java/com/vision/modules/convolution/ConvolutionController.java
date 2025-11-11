package com.vision.modules.convolution;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.ConvolutionService;
import com.vision.util.KernelProvider;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ConvolutionController {

    private final ColorSpaceModel model;
    private final ConvolutionService convolutionService;

    public ConvolutionController(ColorSpaceModel model) {
        this.model = model;
        this.convolutionService = ServiceProvider.getInstance().getConvolutionService();
    }

    public WritableImage applyLowPassFilter(KernelProvider.LowPass type) {
        Image image = model.getOriginalImage();
        if (image == null) {
            showError("No hay imagen cargada.");
            return null;
        }
        return convolutionService.applyLowPassFilter(image, type);
    }

    public WritableImage applyHighPassFilter(KernelProvider.HighPass type) {
        Image image = model.getOriginalImage();
        if (image == null) {
            showError("No hay imagen cargada.");
            return null;
        }
        return convolutionService.applyHighPassFilter(image, type);
    }

    public WritableImage applyCanny(double lowThreshold, double highThreshold) {
        Image image = model.getOriginalImage();
        if (image == null) {
            showError("No hay imagen cargada.");
            return null;
        }
        if (lowThreshold >= highThreshold) {
            showError("El umbral bajo debe ser menor que el umbral alto.");
            return null;
        }
        return convolutionService.applyCannyEdgeDetector(image, lowThreshold, highThreshold);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Convolución");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
