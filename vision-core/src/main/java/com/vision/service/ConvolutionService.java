package com.vision.service;

import com.vision.util.KernelProvider;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ConvolutionService {

    private final ImageProcessingService imageProcessingService = new ImageProcessingService();

    public WritableImage convolve(Image image, double[][] kernel) {
        WritableImage grayImage = imageProcessingService.convertToGrayscale(image);
        int width = (int) grayImage.getWidth();
        int height = (int) grayImage.getHeight();
        int kernelSize = kernel.length;
        int kernelHalf = kernelSize / 2;

        WritableImage resultImage = new WritableImage(width, height);
        PixelReader reader = grayImage.getPixelReader();
        PixelWriter writer = resultImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double sum = 0.0;
                for (int ky = 0; ky < kernelSize; ky++) {
                    for (int kx = 0; kx < kernelSize; kx++) {
                        int pixelX = x + (kx - kernelHalf);
                        int pixelY = y + (ky - kernelHalf);

                        pixelX = Math.max(0, Math.min(width - 1, pixelX));
                        pixelY = Math.max(0, Math.min(height - 1, pixelY));

                        double pixelValue = reader.getColor(pixelX, pixelY).getRed();
                        sum += pixelValue * kernel[ky][kx];
                    }
                }
                double finalValue = Math.max(0.0, Math.min(1.0, sum));
                writer.setColor(x, y, new Color(finalValue, finalValue, finalValue, 1.0));
            }
        }
        return resultImage;
    }

    public WritableImage applyLowPassFilter(Image image, KernelProvider.LowPass type) {
        return convolve(image, KernelProvider.getKernel(type));
    }

    public WritableImage applyHighPassFilter(Image image, KernelProvider.HighPass type) {
        return convolve(image, KernelProvider.getKernel(type));
    }

    public WritableImage applyCannyEdgeDetector(Image image, double lowThreshold, double highThreshold) {
        double[][] gaussianKernel = KernelProvider.createGaussianKernel(5, 1.4);
        WritableImage blurredImage = convolve(image, gaussianKernel);

        int width = (int) blurredImage.getWidth();
        int height = (int) blurredImage.getHeight();
        double[][] magnitude = new double[height][width];
        double[][] direction = new double[height][width];
        
        WritableImage gxImage = convolve(blurredImage, KernelProvider.SOBEL_X);
        WritableImage gyImage = convolve(blurredImage, KernelProvider.SOBEL_Y);
        
        PixelReader readerGx = gxImage.getPixelReader();
        PixelReader readerGy = gyImage.getPixelReader();
        double maxMag = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double gx = (readerGx.getColor(x, y).getRed() - 0.5) * 2;
                double gy = (readerGy.getColor(x, y).getRed() - 0.5) * 2;
                magnitude[y][x] = Math.sqrt(gx * gx + gy * gy);
                if (magnitude[y][x] > maxMag) maxMag = magnitude[y][x];
                direction[y][x] = Math.atan2(gy, gx);
            }
        }
        
        if (maxMag > 0) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    magnitude[y][x] /= maxMag;
                }
            }
        }

        double[][] suppressed = nonMaximumSuppression(magnitude, direction);
        WritableImage thresholded = doubleThreshold(suppressed, lowThreshold / 255.0, highThreshold / 255.0);
        return hysteresis(thresholded);
    }

    private double[][] nonMaximumSuppression(double[][] magnitude, double[][] direction) {
        int height = magnitude.length;
        int width = magnitude[0].length;
        double[][] result = new double[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double angle = direction[y][x] * 180.0 / Math.PI;
                if (angle < 0) angle += 180;

                double q = 255, r = 255;

                if ((0 <= angle && angle < 22.5) || (157.5 <= angle && angle <= 180)) {
                    q = magnitude[y][x+1]; r = magnitude[y][x-1];
                } else if (22.5 <= angle && angle < 67.5) {
                    q = magnitude[y+1][x-1]; r = magnitude[y-1][x+1];
                } else if (67.5 <= angle && angle < 112.5) {
                    q = magnitude[y+1][x]; r = magnitude[y-1][x];
                } else if (112.5 <= angle && angle < 157.5) {
                    q = magnitude[y-1][x-1]; r = magnitude[y+1][x+1];
                }

                if (magnitude[y][x] >= q && magnitude[y][x] >= r) {
                    result[y][x] = magnitude[y][x];
                } else {
                    result[y][x] = 0;
                }
            }
        }
        return result;
    }

    private WritableImage doubleThreshold(double[][] image, double low, double high) {
        int height = image.length;
        int width = image[0].length;
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        double weak = 0.5;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double val = image[y][x];
                if (val >= high) writer.setColor(x, y, Color.WHITE);
                else if (val >= low) writer.setColor(x, y, Color.gray(weak));
                else writer.setColor(x, y, Color.BLACK);
            }
        }
        return result;
    }

    private WritableImage hysteresis(WritableImage image) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        PixelReader reader = image.getPixelReader();
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        double weak = 0.5;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (reader.getColor(x, y).equals(Color.WHITE)) {
                    writer.setColor(x, y, Color.WHITE);
                } else {
                    writer.setColor(x, y, Color.BLACK);
                }
            }
        }

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (reader.getColor(x, y).equals(Color.gray(weak))) {
                    for (int ky = -1; ky <= 1; ky++) {
                        for (int kx = -1; kx <= 1; kx++) {
                            if (reader.getColor(x + kx, y + ky).equals(Color.WHITE)) {
                                writer.setColor(x, y, Color.WHITE);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
