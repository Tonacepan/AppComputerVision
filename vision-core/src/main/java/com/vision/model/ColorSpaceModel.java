package com.vision.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para manejo de espacios de color y ajustes de imagen
 */
public class ColorSpaceModel {

    private Image originalImage;
    private WritableImage transformedImage;
    private WritableImage[] channelImages;
    private String currentTransformation;
    private Object transformedData;

    // Nuevos campos para ajustes
    private double brightness = 0.0;
    private double contrast = 1.0;
    private WritableImage brightnessImage;
    private WritableImage contrastImage;

    // Lista de observadores
    private List<ColorSpaceModelListener> listeners = new ArrayList<>();

    /**
     * Interface para observadores del modelo
     */
    public interface ColorSpaceModelListener {
        void onImageLoaded();
        void onTransformationApplied();
        void onTransformationChanged();
        void onAdjustmentsChanged();
        void onAdjustedImagesReady();
    }

    /**
     * Agrega un listener al modelo
     */
    public void addListener(ColorSpaceModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Remueve un listener del modelo
     */
    public void removeListener(ColorSpaceModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica a todos los listeners que se cargó una imagen
     */
    private void notifyImageLoaded() {
        if (originalImage != null) {
            applyRGBTransformation();
        }
        Platform.runLater(() -> {
            for (ColorSpaceModelListener listener : listeners) {
                listener.onImageLoaded();
            }
        });
    }

    /**
     * Notifica a todos los listeners que se aplicó una transformación
     */
    private void notifyTransformationApplied() {
        Platform.runLater(() -> {
            for (ColorSpaceModelListener listener : listeners) {
                listener.onTransformationApplied();
            }
        });
    }

    /**
     * Notifica a todos los listeners que cambió la transformación
     */
    private void notifyTransformationChanged() {
        Platform.runLater(() -> {
            for (ColorSpaceModelListener listener : listeners) {
                listener.onTransformationChanged();
            }
        });
    }

    /**
     * Notifica a los listeners que los valores de ajuste han cambiado
     */
    private void notifyAdjustmentsChanged() {
        Platform.runLater(() -> {
            for (ColorSpaceModelListener listener : listeners) {
                listener.onAdjustmentsChanged();
            }
        });
    }

    /**
     * Notifica a los listeners que las imágenes ajustadas están listas
     */
    private void notifyAdjustedImagesReady() {
        Platform.runLater(() -> {
            for (ColorSpaceModelListener listener : listeners) {
                listener.onAdjustedImagesReady();
            }
        });
    }

    // Constructores
    public ColorSpaceModel() {
        // Constructor por defecto
    }

    public void applyRGBTransformation() {
        if (originalImage != null) {
            int width = (int) originalImage.getWidth();
            int height = (int) originalImage.getHeight();
            transformedImage = new WritableImage(width, height);
            WritableImage redChannel = new WritableImage(width, height);
            WritableImage greenChannel = new WritableImage(width, height);
            WritableImage blueChannel = new WritableImage(width, height);
            WritableImage grayChannel = new WritableImage(width, height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = originalImage.getPixelReader().getArgb(x, y);
                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;
                    int rgb = (0xFF << 24) | (r << 16) | (g << 8) | b;
                    transformedImage.getPixelWriter().setArgb(x, y, rgb);
                    int redPixel = (0xFF << 24) | (r << 16) | (r << 8) | r;
                    redChannel.getPixelWriter().setArgb(x, y, redPixel);
                    int greenPixel = (0xFF << 24) | (g << 16) | (g << 8) | g;
                    greenChannel.getPixelWriter().setArgb(x, y, greenPixel);
                    int bluePixel = (0xFF << 24) | (b << 16) | (b << 8) | b;
                    blueChannel.getPixelWriter().setArgb(x, y, bluePixel);
                    int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    int grayPixel = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;
                    grayChannel.getPixelWriter().setArgb(x, y, grayPixel);
                }
            }
            channelImages = new WritableImage[] { redChannel, greenChannel, blueChannel, grayChannel };
            currentTransformation = "RGB";
            notifyTransformationApplied();
        }
    }

    // Getters y Setters
    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
        resetAdjustments();
        notifyImageLoaded();
    }

    public WritableImage getTransformedImage() {
        return transformedImage;
    }

    public void setTransformedImage(WritableImage transformedImage) {
        this.transformedImage = transformedImage;
        notifyTransformationApplied();
    }

    public WritableImage[] getChannelImages() {
        return channelImages;
    }

    public void setChannelImages(WritableImage[] channelImages) {
        this.channelImages = channelImages;
    }

    public String getCurrentTransformation() {
        return currentTransformation;
    }

    public void setCurrentTransformation(String currentTransformation) {
        this.currentTransformation = currentTransformation;
        notifyTransformationChanged();
    }

    public Object getTransformedData() {
        return transformedData;
    }

    public void setTransformedData(Object transformedData) {
        this.transformedData = transformedData;
    }

    // Getters y Setters para ajustes
    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
        notifyAdjustmentsChanged();
    }

    public double getContrast() {
        return contrast;
    }

    public void setContrast(double contrast) {
        this.contrast = contrast;
        notifyAdjustmentsChanged();
    }

    public WritableImage getBrightnessImage() {
        return brightnessImage;
    }

    public void setBrightnessImage(WritableImage brightnessImage) {
        this.brightnessImage = brightnessImage;
    }

    public WritableImage getContrastImage() {
        return contrastImage;
    }

    public void setContrastImage(WritableImage contrastImage) {
        this.contrastImage = contrastImage;
        notifyAdjustedImagesReady();
    }

    private void resetAdjustments() {
        this.brightness = 0.0;
        this.contrast = 1.0;
    }
}
