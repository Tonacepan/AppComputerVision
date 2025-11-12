package com.vision.modules.cornerdetection;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.CornerDetectionService;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class CornerDetectionController {

    private final ColorSpaceModel model;
    private final CornerDetectionService cornerDetectionService;

    public CornerDetectionController(ColorSpaceModel model) {
        this.model = model;
        this.cornerDetectionService = ServiceProvider.getInstance().getCornerDetectionService();
    }

    private Image getCurrentImage() {
        Image image = model.getOriginalImage();
        if (image == null) {
            showError("No hay imagen cargada.");
        }
        return image;
    }

    public WritableImage applyKirsch() {
        Image image = getCurrentImage();
        return (image != null) ? cornerDetectionService.applyKirsch(image) : null;
    }

    public WritableImage applyFreiChen() {
        Image image = getCurrentImage();
        return (image != null) ? cornerDetectionService.applyFreiChen(image) : null;
    }

    public WritableImage applyHarris(double k, double threshold) {
        Image image = getCurrentImage();
        if (image == null) return null;
        if (k <= 0 || threshold <= 0) {
            showError("Los parámetros de Harris (k y umbral) deben ser mayores que cero.");
            return null;
        }
        return cornerDetectionService.applyHarris(image, k, threshold);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en Detección de Esquinas");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
