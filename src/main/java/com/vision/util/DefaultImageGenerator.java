package com.vision.util;

import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

/**
 * Generador de imágenes predeterminadas para pruebas
 */
public class DefaultImageGenerator {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    /**
     * Crea una imagen predeterminada con gradientes de colores
     */
    public static WritableImage createDefaultImage() {
        WritableImage image = new WritableImage(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int x = 0; x < DEFAULT_WIDTH; x++) {
            for (int y = 0; y < DEFAULT_HEIGHT; y++) {
                // Crear un patrón con gradientes de colores
                double red = (double) x / DEFAULT_WIDTH;
                double green = (double) y / DEFAULT_HEIGHT;
                double blue = Math.sin((double) (x + y) / 50.0) * 0.5 + 0.5;

                Color color = new Color(red, green, blue, 1.0);
                pixelWriter.setColor(x, y, color);
            }
        }

        return image;
    }

    /**
     * Crea una imagen con bandas de colores RGB
     */
    public static WritableImage createRgbBandsImage() {
        WritableImage image = new WritableImage(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        PixelWriter pixelWriter = image.getPixelWriter();

        int bandHeight = DEFAULT_HEIGHT / 3;

        for (int x = 0; x < DEFAULT_WIDTH; x++) {
            for (int y = 0; y < DEFAULT_HEIGHT; y++) {
                Color color;
                double intensity = (double) x / DEFAULT_WIDTH;

                if (y < bandHeight) {
                    // Banda roja
                    color = new Color(intensity, 0, 0, 1.0);
                } else if (y < bandHeight * 2) {
                    // Banda verde
                    color = new Color(0, intensity, 0, 1.0);
                } else {
                    // Banda azul
                    color = new Color(0, 0, intensity, 1.0);
                }

                pixelWriter.setColor(x, y, color);
            }
        }

        return image;
    }
}
