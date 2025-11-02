package com.vision.controller;

import com.vision.model.ColorSpaceModel;
import com.vision.service.ColorSpaceService;
import com.vision.service.ImageProcessingService;
import com.vision.util.DefaultImageGenerator;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Controlador para la conversión de espacios de color y ajustes de imagen
 */
public class ColorConverterController {

    private final ColorSpaceModel model;
    private final ColorSpaceService colorSpaceService;
    private final ImageProcessingService imageProcessingService;

    public ColorConverterController(ColorSpaceModel model, ColorSpaceService colorSpaceService, ImageProcessingService imageProcessingService) {
        this.model = model;
        this.colorSpaceService = colorSpaceService;
        this.imageProcessingService = imageProcessingService;
    }

    public void handleLoadDefault() {
        try {
            Image defaultImage = DefaultImageGenerator.createDefaultImage();
            model.setOriginalImage(defaultImage);
        } catch (Exception e) {
            showErrorMessage("Error al generar imagen predeterminada: " + e.getMessage());
        }
    }

    public void handleTransformationChange(String transformation) {
        model.setCurrentTransformation(transformation);
    }

    public void handleApplyTransformation() {
        Image originalImage = model.getOriginalImage();
        String transformation = model.getCurrentTransformation();

        if (originalImage == null || transformation == null) {
            showErrorMessage("Selecciona una imagen y una transformación primero");
            return;
        }

        try {
            applyTransformation(originalImage, transformation);
        } catch (Exception e) {
            showErrorMessage("Error al aplicar transformación: " + e.getMessage());
        }
    }

    private void applyTransformation(Image image, String transformation) {
        switch (transformation) {
            case "RGB → CMY" -> applyCmyTransformation(image);
            case "RGB → CMYK" -> applyCmykTransformation(image);
            case "RGB → YIQ" -> applyYiqTransformation(image);
            case "RGB → HSI" -> applyHsiTransformation(image);
            case "RGB → HSV" -> applyHsvTransformation(image);
            default -> showErrorMessage("Transformación no soportada: " + transformation);
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

    /**
     * Maneja el cambio de brillo
     */
    public void handleBrightnessChange(double brightness) {
        Image originalImage = model.getOriginalImage();
        if (originalImage == null) return;

        try {
            model.setBrightness(brightness);
            WritableImage adjustedImage = imageProcessingService.adjustBrightness(originalImage, brightness);
            model.setBrightnessImage(adjustedImage);
            // Para completitud, actualizamos también la de contraste con el valor actual
            WritableImage contrastImage = imageProcessingService.adjustContrast(originalImage, model.getContrast());
            model.setContrastImage(contrastImage);
        } catch (Exception e) {
            showErrorMessage("Error al ajustar brillo: " + e.getMessage());
        }
    }

    /**
     * Maneja el cambio de contraste
     */
    public void handleContrastChange(double contrast) {
        Image originalImage = model.getOriginalImage();
        if (originalImage == null) return;

        try {
            model.setContrast(contrast);
            WritableImage adjustedImage = imageProcessingService.adjustContrast(originalImage, contrast);
            model.setContrastImage(adjustedImage);
            // Para completitud, actualizamos también la de brillo con el valor actual
            WritableImage brightnessImage = imageProcessingService.adjustBrightness(originalImage, model.getBrightness());
            model.setBrightnessImage(brightnessImage);
        } catch (Exception e) {
            showErrorMessage("Error al ajustar contraste: " + e.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error en el procesamiento");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleRGBTransformation() {
        if (model.getOriginalImage() != null) {
            model.applyRGBTransformation();
        }
    }

    public void loadImage(Image image) {
        model.setOriginalImage(image);
    }

    public ColorSpaceModel getModel() {
        return model;
    }
}
