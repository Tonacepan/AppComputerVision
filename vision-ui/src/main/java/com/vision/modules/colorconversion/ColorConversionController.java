package com.vision.modules.colorconversion;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.ColorSpaceService;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Controlador para el módulo de conversión de espacios de color
 * Maneja la lógica de conversión entre diferentes espacios de color
 */
public class ColorConversionController {
    
    private final ColorSpaceModel model;
    private final ColorSpaceService colorSpaceService;
    
    public ColorConversionController(ColorSpaceModel model) {
        this.model = model;
        this.colorSpaceService = ServiceProvider.getInstance().getColorSpaceService();
    }
    
    public void applyTransformation(String transformation) {
        Image originalImage = model.getOriginalImage();
        
        if (originalImage == null) {
            showError("No hay imagen cargada", "Por favor, carga una imagen primero.");
            return;
        }
        
        if (transformation == null || transformation.isEmpty()) {
            showError("No hay transformación seleccionada", "Por favor, selecciona una transformación.");
            return;
        }
        
        try {
            switch (transformation) {
                case "RGB → CMY" -> applyCmyTransformation(originalImage);
                case "RGB → CMYK" -> applyCmykTransformation(originalImage);
                case "RGB → YIQ" -> applyYiqTransformation(originalImage);
                case "RGB → HSI" -> applyHsiTransformation(originalImage);
                case "RGB → HSV" -> applyHsvTransformation(originalImage);
                case "RGB" -> model.applyRGBTransformation();
                default -> showError("Transformación no soportada", "La transformación " + transformation + " no está implementada.");
            }
        } catch (Exception e) {
            showError("Error al aplicar transformación", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void applyCmyTransformation(Image image) {
        double[][][] cmyData = colorSpaceService.convertRgbToCmy(image);
        model.setTransformedData(cmyData);
        WritableImage transformedImage = colorSpaceService.createCmyImage(cmyData);
        model.setTransformedImage(transformedImage);
        
        WritableImage[] channelImages = {
            colorSpaceService.createChannelImage(cmyData, 0), // Cyan
            colorSpaceService.createChannelImage(cmyData, 1), // Magenta
            colorSpaceService.createChannelImage(cmyData, 2)  // Yellow
        };
        model.setChannelImages(channelImages);
    }
    
    private void applyCmykTransformation(Image image) {
        double[][][] cmykData = colorSpaceService.convertRgbToCmyk(image);
        model.setTransformedData(cmykData);
        WritableImage transformedImage = colorSpaceService.createCmykImage(cmykData);
        model.setTransformedImage(transformedImage);
        
        WritableImage[] channelImages = {
            colorSpaceService.createChannelImage(cmykData, 0), // Cyan
            colorSpaceService.createChannelImage(cmykData, 1), // Magenta
            colorSpaceService.createChannelImage(cmykData, 2), // Yellow
            colorSpaceService.createChannelImage(cmykData, 3)  // Black
        };
        model.setChannelImages(channelImages);
    }
    
    private void applyYiqTransformation(Image image) {
        double[][][] yiqData = colorSpaceService.convertRgbToYiq(image);
        model.setTransformedData(yiqData);
        model.setTransformedImage(colorSpaceService.createChannelImage(yiqData, 0)); // Y channel
        
        WritableImage[] channelImages = {
            colorSpaceService.createChannelImage(yiqData, 0), // Y
            colorSpaceService.createChannelImage(yiqData, 1), // I
            colorSpaceService.createChannelImage(yiqData, 2)  // Q
        };
        model.setChannelImages(channelImages);
    }
    
    private void applyHsiTransformation(Image image) {
        double[][][] hsiData = colorSpaceService.convertRgbToHsi(image);
        model.setTransformedData(hsiData);
        model.setTransformedImage(colorSpaceService.createChannelImage(hsiData, 2)); // I channel
        
        WritableImage[] channelImages = {
            colorSpaceService.createChannelImage(hsiData, 0), // H
            colorSpaceService.createChannelImage(hsiData, 1), // S
            colorSpaceService.createChannelImage(hsiData, 2)  // I
        };
        model.setChannelImages(channelImages);
    }
    
    private void applyHsvTransformation(Image image) {
        double[][][] hsvData = colorSpaceService.convertRgbToHsv(image);
        model.setTransformedData(hsvData);
        model.setTransformedImage(colorSpaceService.createChannelImage(hsvData, 2)); // V channel
        
        WritableImage[] channelImages = {
            colorSpaceService.createChannelImage(hsvData, 0), // H
            colorSpaceService.createChannelImage(hsvData, 1), // S
            colorSpaceService.createChannelImage(hsvData, 2)  // V
        };
        model.setChannelImages(channelImages);
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
