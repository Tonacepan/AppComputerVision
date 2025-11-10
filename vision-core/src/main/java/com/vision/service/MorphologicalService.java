package com.vision.service;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Random;

public class MorphologicalService {

    private final ImageProcessingService imageProcessingService = new ImageProcessingService();

    public WritableImage addSaltNoise(Image image, double percentage) {
        return addNoise(image, percentage, Color.WHITE);
    }

    public WritableImage addPepperNoise(Image image, double percentage) {
        return addNoise(image, percentage, Color.BLACK);
    }

    private WritableImage addNoise(Image image, double percentage, Color noiseColor) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage noisyImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = noisyImage.getPixelWriter();
        Random random = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (random.nextDouble() < percentage) {
                    pixelWriter.setColor(x, y, noiseColor);
                } else {
                    pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                }
            }
        }
        return noisyImage;
    }

    public WritableImage erode(Image image, int kernelSize) {
        WritableImage binaryImage = imageProcessingService.binarize(image, 0.5);
        return applyMorphologicalOperation(binaryImage, kernelSize, true); // true for erosion
    }

    public WritableImage dilate(Image image, int kernelSize) {
        WritableImage binaryImage = imageProcessingService.binarize(image, 0.5);
        return applyMorphologicalOperation(binaryImage, kernelSize, false); // false for dilation
    }

    public WritableImage open(Image image, int kernelSize) {
        WritableImage eroded = erode(image, kernelSize);
        return applyMorphologicalOperation(eroded, kernelSize, false); // dilate
    }

    public WritableImage close(Image image, int kernelSize) {
        WritableImage dilated = dilate(image, kernelSize);
        return applyMorphologicalOperation(dilated, kernelSize, true); // erode
    }

    private WritableImage applyMorphologicalOperation(WritableImage binaryImage, int kernelSize, boolean isErosion) {
        int width = (int) binaryImage.getWidth();
        int height = (int) binaryImage.getHeight();
        WritableImage resultImage = new WritableImage(width, height);
        PixelReader reader = binaryImage.getPixelReader();
        PixelWriter writer = resultImage.getPixelWriter();
        int halfKernel = kernelSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean applyOperation = isErosion; // For erosion, start with true, for dilation, start with false
                for (int ky = -halfKernel; ky <= halfKernel; ky++) {
                    for (int kx = -halfKernel; kx <= halfKernel; kx++) {
                        int newY = y + ky;
                        int newX = x + kx;
                        if (newY >= 0 && newY < height && newX >= 0 && newX < width) {
                            boolean isWhite = reader.getColor(newX, newY).equals(Color.WHITE);
                            if (isErosion) {
                                if (!isWhite) { // If any pixel is black, the result is black
                                    applyOperation = false;
                                    break;
                                }
                            } else { // Dilation
                                if (isWhite) { // If any pixel is white, the result is white
                                    applyOperation = true;
                                    break;
                                }
                            }
                        }
                    }
                    if ((isErosion && !applyOperation) || (!isErosion && applyOperation)) {
                        break;
                    }
                }
                writer.setColor(x, y, applyOperation ? Color.WHITE : Color.BLACK);
            }
        }
        return resultImage;
    }
}
