package com.vision.service;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Servicio para realizar transformaciones geométricas en imágenes.
 */
public class GeometricTransformationService {

    /**
     * Traslada una imagen.
     *
     * @param image La imagen original.
     * @param tx    Desplazamiento en el eje X.
     * @param ty    Desplazamiento en el eje Y.
     * @return La imagen trasladada.
     */
    public WritableImage translate(Image image, double tx, double ty) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage translatedImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = translatedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newX = (int) (x - tx);
                int newY = (int) (y - ty);

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    Color color = pixelReader.getColor(newX, newY);
                    pixelWriter.setColor(x, y, color);
                } else {
                    pixelWriter.setColor(x, y, Color.TRANSPARENT); // O un color de fondo
                }
            }
        }
        return translatedImage;
    }

    /**
     * Rota una imagen alrededor de su centro.
     *
     * @param image La imagen original.
     * @param angle El ángulo de rotación en grados.
     * @return La imagen rotada.
     */
    public WritableImage rotate(Image image, double angle) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        double angleRad = Math.toRadians(angle);
        double sin = Math.sin(angleRad);
        double cos = Math.cos(angleRad);
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        WritableImage rotatedImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = rotatedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double x0 = x - centerX;
                double y0 = y - centerY;

                double newX = x0 * cos - y0 * sin + centerX;
                double newY = x0 * sin + y0 * cos + centerY;

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    Color color = pixelReader.getColor((int) newX, (int) newY);
                    pixelWriter.setColor(x, y, color);
                } else {
                    pixelWriter.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }
        return rotatedImage;
    }

    /**
     * Escala una imagen desde su centro.
     *
     * @param image La imagen original.
     * @param sx    Factor de escala en X.
     * @param sy    Factor de escala en Y.
     * @return La imagen escalada.
     */
    public WritableImage scale(Image image, double sx, double sy) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage scaledImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = scaledImage.getPixelWriter();

        double centerX = width / 2.0;
        double centerY = height / 2.0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double newX = (x - centerX) / sx + centerX;
                double newY = (y - centerY) / sy + centerY;

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    Color color = pixelReader.getColor((int) newX, (int) newY);
                    pixelWriter.setColor(x, y, color);
                } else {
                    pixelWriter.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }
        return scaledImage;
    }
}
