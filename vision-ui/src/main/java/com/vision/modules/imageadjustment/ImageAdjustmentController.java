package com.vision.modules.imageadjustment;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.ImageProcessingService;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Controlador para el módulo de ajustes de imagen (brillo y contraste)
 */
public class ImageAdjustmentController {
    
    private final ColorSpaceModel model;
    private final ImageProcessingService imageProcessingService;
    
    public ImageAdjustmentController(ColorSpaceModel model) {
        this.model = model;
        this.imageProcessingService = ServiceProvider.getInstance().getImageProcessingService();
    }
    
    public void adjustBrightness(double brightness) {
        Image originalImage = model.getOriginalImage();
        
        if (originalImage == null) {
            showError("No hay imagen cargada", "Por favor, carga una imagen primero.");
            return;
        }
        
        try {
            model.setBrightness(brightness);
            WritableImage adjustedImage = imageProcessingService.adjustBrightness(originalImage, brightness);
            model.setBrightnessImage(adjustedImage);
            
            // Actualizar también la imagen de contraste con el valor actual
            WritableImage contrastImage = imageProcessingService.adjustContrast(originalImage, model.getContrast());
            model.setContrastImage(contrastImage);
        } catch (Exception e) {
            showError("Error al ajustar brillo", e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void adjustContrast(double contrast) {
        Image originalImage = model.getOriginalImage();
        
        if (originalImage == null) {
            showError("No hay imagen cargada", "Por favor, carga una imagen primero.");
            return;
        }
        
        try {
            model.setContrast(contrast);
            WritableImage adjustedImage = imageProcessingService.adjustContrast(originalImage, contrast);
            model.setContrastImage(adjustedImage);
            
            // Actualizar también la imagen de brillo con el valor actual
            WritableImage brightnessImage = imageProcessingService.adjustBrightness(originalImage, model.getBrightness());
            model.setBrightnessImage(brightnessImage);
        } catch (Exception e) {
            showError("Error al ajustar contraste", e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void resetAdjustments() {
        adjustBrightness(0.0);
        adjustContrast(1.0);
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public ColorSpaceModel getModel() {
        return model;
    }
}
