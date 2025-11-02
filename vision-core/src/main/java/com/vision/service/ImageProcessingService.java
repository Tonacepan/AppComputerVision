package com.vision.service;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

/**
 * Servicio para procesamiento básico de imágenes
 */
public class ImageProcessingService {

    /**
     * Convierte una imagen a escala de grises usando el promedio de los canales RGB
     */
    public WritableImage convertToGrayscale(Image originalImage) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage grayscaleImage = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = grayscaleImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                Color grayColor = new Color(gray, gray, gray, color.getOpacity());
                pixelWriter.setColor(x, y, grayColor);
            }
        }

        return grayscaleImage;
    }

    /**
     * Extrae el canal rojo de una imagen
     */
    public WritableImage extractRedChannel(Image originalImage) {
        return extractChannel(originalImage, ChannelType.RED);
    }

    /**
     * Extrae el canal verde de una imagen
     */
    public WritableImage extractGreenChannel(Image originalImage) {
        return extractChannel(originalImage, ChannelType.GREEN);
    }

    /**
     * Extrae el canal azul de una imagen
     */
    public WritableImage extractBlueChannel(Image originalImage) {
        return extractChannel(originalImage, ChannelType.BLUE);
    }

    /**
     * Extrae un canal específico de la imagen y lo muestra en su color correspondiente
     */
    private WritableImage extractChannel(Image originalImage, ChannelType channelType) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage channelImage = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = channelImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                double channelValue = switch (channelType) {
                    case RED -> color.getRed();
                    case GREEN -> color.getGreen();
                    case BLUE -> color.getBlue();
                };

                // Mostrar el canal en su color correspondiente
                Color channelColor = switch (channelType) {
                    case RED -> new Color(channelValue, 0, 0, color.getOpacity());
                    case GREEN -> new Color(0, channelValue, 0, color.getOpacity());
                    case BLUE -> new Color(0, 0, channelValue, color.getOpacity());
                };

                pixelWriter.setColor(x, y, channelColor);
            }
        }

        return channelImage;
    }

    /**
     * Ajusta el brillo de una imagen
     */
    public WritableImage adjustBrightness(Image originalImage, double brightness) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage adjustedImage = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = adjustedImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);

                double red = Math.max(0, Math.min(1, color.getRed() + brightness));
                double green = Math.max(0, Math.min(1, color.getGreen() + brightness));
                double blue = Math.max(0, Math.min(1, color.getBlue() + brightness));

                Color adjustedColor = new Color(red, green, blue, color.getOpacity());
                pixelWriter.setColor(x, y, adjustedColor);
            }
        }

        return adjustedImage;
    }

    /**
     * Ajusta el contraste de una imagen
     */
    public WritableImage adjustContrast(Image originalImage, double contrast) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage adjustedImage = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = adjustedImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);

                double red = Math.max(0, Math.min(1, (color.getRed() - 0.5) * contrast + 0.5));
                double green = Math.max(0, Math.min(1, (color.getGreen() - 0.5) * contrast + 0.5));
                double blue = Math.max(0, Math.min(1, (color.getBlue() - 0.5) * contrast + 0.5));

                Color adjustedColor = new Color(red, green, blue, color.getOpacity());
                pixelWriter.setColor(x, y, adjustedColor);
            }
        }

        return adjustedImage;
    }

    /**
     * Enum para tipos de canal
     */
    private enum ChannelType {
        RED, GREEN, BLUE
    }
}
