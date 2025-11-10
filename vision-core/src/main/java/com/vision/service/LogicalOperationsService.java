package com.vision.service;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Servicio para realizar operaciones lógicas y relacionales entre imágenes.
 * Las operaciones se realizan sobre versiones binarizadas de las imágenes.
 */
public class LogicalOperationsService {

    private final ImageProcessingService imageProcessingService;
    private static final double DEFAULT_THRESHOLD = 0.5;

    public LogicalOperationsService() {
        // Nota: Esto crea una nueva instancia. Si ImageProcessingService tuviera estado,
        // deberíamos obtenerla a través de ServiceProvider, pero es un servicio sin estado.
        this.imageProcessingService = new ImageProcessingService();
    }

    // --- Operaciones Lógicas ---

    public WritableImage and(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a && b), imageA, imageB);
    }

    public WritableImage or(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a || b), imageA, imageB);
    }

    public WritableImage xor(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a ^ b), imageA, imageB);
    }

    public WritableImage not(Image image) {
        WritableImage binaryImage = imageProcessingService.binarize(image, DEFAULT_THRESHOLD);
        int width = (int) binaryImage.getWidth();
        int height = (int) binaryImage.getHeight();
        WritableImage resultImage = new WritableImage(width, height);
        PixelReader reader = binaryImage.getPixelReader();
        PixelWriter writer = resultImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean pixel = reader.getColor(x, y).equals(Color.WHITE);
                writer.setColor(x, y, !pixel ? Color.WHITE : Color.BLACK);
            }
        }
        return resultImage;
    }

    // --- Operaciones Relacionales ---

    public WritableImage areEqual(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a == b), imageA, imageB);
    }

    public WritableImage areNotEqual(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a != b), imageA, imageB);
    }

    public WritableImage isGreater(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a && !b), imageA, imageB); // A > B (A=1, B=0)
    }

    public WritableImage isGreaterOrEqual(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (a || !b), imageA, imageB); // A >= B
    }

    public WritableImage isLess(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (!a && b), imageA, imageB); // A < B (A=0, B=1)
    }

    public WritableImage isLessOrEqual(Image imageA, Image imageB) {
        return applyLogic((a, b) -> (!a || b), imageA, imageB); // A <= B
    }


    /**
     * Función de ayuda para aplicar una operación lógica píxel a píxel.
     */
    private WritableImage applyLogic(LogicalOperation op, Image imageA, Image imageB) {
        WritableImage binaryA = imageProcessingService.binarize(imageA, DEFAULT_THRESHOLD);
        WritableImage binaryB = imageProcessingService.binarize(imageB, DEFAULT_THRESHOLD);

        int width = (int) Math.min(binaryA.getWidth(), binaryB.getWidth());
        int height = (int) Math.min(binaryA.getHeight(), binaryB.getHeight());

        WritableImage resultImage = new WritableImage(width, height);
        PixelReader readerA = binaryA.getPixelReader();
        PixelReader readerB = binaryB.getPixelReader();
        PixelWriter writer = resultImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean pixelA = readerA.getColor(x, y).equals(Color.WHITE);
                boolean pixelB = readerB.getColor(x, y).equals(Color.WHITE);
                boolean result = op.evaluate(pixelA, pixelB);
                writer.setColor(x, y, result ? Color.WHITE : Color.BLACK);
            }
        }
        return resultImage;
    }

    /**
     * Interfaz funcional para una operación booleana de dos variables.
     */
    @FunctionalInterface
    private interface LogicalOperation {
        boolean evaluate(boolean a, boolean b);
    }
}
