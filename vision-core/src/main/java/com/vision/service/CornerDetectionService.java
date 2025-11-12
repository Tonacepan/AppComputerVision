package com.vision.service;

import com.vision.util.KernelProvider;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CornerDetectionService {

    private final ImageProcessingService imageProcessingService = new ImageProcessingService();
    private final ConvolutionService convolutionService = new ConvolutionService();

    public WritableImage applyKirsch(Image image) {
        WritableImage grayImage = imageProcessingService.convertToGrayscale(image);
        int width = (int) grayImage.getWidth();
        int height = (int) grayImage.getHeight();
        WritableImage resultImage = new WritableImage(width, height);
        PixelWriter writer = resultImage.getPixelWriter();

        List<WritableImage> convolutions = new ArrayList<>();
        for (double[][] kernel : KernelProvider.KIRSCH_ALL) {
            convolutions.add(convolutionService.convolve(grayImage, kernel));
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double maxResponse = 0;
                for (WritableImage convImage : convolutions) {
                    double response = convImage.getPixelReader().getColor(x, y).getRed();
                    if (response > maxResponse) {
                        maxResponse = response;
                    }
                }
                writer.setColor(x, y, new Color(maxResponse, maxResponse, maxResponse, 1.0));
            }
        }
        return resultImage;
    }

    public WritableImage applyFreiChen(Image image) {
        WritableImage grayImage = imageProcessingService.convertToGrayscale(image);
        int width = (int) grayImage.getWidth();
        int height = (int) grayImage.getHeight();
        WritableImage resultImage = new WritableImage(width, height);
        PixelWriter writer = resultImage.getPixelWriter();

        List<WritableImage> convolutions = new ArrayList<>();
        for (double[][] kernel : KernelProvider.FREI_CHEN_ALL) {
            convolutions.add(convolutionService.convolve(grayImage, kernel));
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double edgeSubspaceSum = 0;
                for (int i = 0; i < 4; i++) {
                    double g = convolutions.get(i).getPixelReader().getColor(x, y).getRed();
                    edgeSubspaceSum += g * g;
                }

                double totalSubspaceSum = 0;
                for (WritableImage convImage : convolutions) {
                    double g = convImage.getPixelReader().getColor(x, y).getRed();
                    totalSubspaceSum += g * g;
                }

                double response = (totalSubspaceSum > 0) ? Math.sqrt(edgeSubspaceSum / totalSubspaceSum) : 0;
                writer.setColor(x, y, new Color(response, response, response, 1.0));
            }
        }
        return resultImage;
    }

    public WritableImage applyHarris(Image image, double k, double threshold) {
        WritableImage grayImage = imageProcessingService.convertToGrayscale(image);
        int width = (int) grayImage.getWidth();
        int height = (int) grayImage.getHeight();

        WritableImage ixImage = convolutionService.convolve(grayImage, KernelProvider.SOBEL_X);
        WritableImage iyImage = convolutionService.convolve(grayImage, KernelProvider.SOBEL_Y);

        double[][] ix2 = new double[height][width];
        double[][] iy2 = new double[height][width];
        double[][] ixy = new double[height][width];

        PixelReader readerX = ixImage.getPixelReader();
        PixelReader readerY = iyImage.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double ix = readerX.getColor(x, y).getRed() - 0.5;
                double iy = readerY.getColor(x, y).getRed() - 0.5;
                ix2[y][x] = ix * ix;
                iy2[y][x] = iy * iy;
                ixy[y][x] = ix * iy;
            }
        }

        double[][] s_ix2 = applyGaussianWindow(ix2, 5, 1.5);
        double[][] s_iy2 = applyGaussianWindow(iy2, 5, 1.5);
        double[][] s_ixy = applyGaussianWindow(ixy, 5, 1.5);

        double[][] harrisResponse = new double[height][width];
        double maxR = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double a = s_ix2[y][x], b = s_ixy[y][x], c = s_iy2[y][x];
                double det = (a * c) - (b * b);
                double trace = a + c;
                double r = det - k * (trace * trace);
                harrisResponse[y][x] = r;
                if (r > maxR) maxR = r;
            }
        }
        
        WritableImage resultImage = new WritableImage(width, height);
        PixelWriter writer = resultImage.getPixelWriter();
        PixelReader originalReader = image.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, originalReader.getColor(x, y));
            }
        }

        if (maxR > 0) {
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    if (harrisResponse[y][x] / maxR > threshold && isLocalMax(harrisResponse, x, y)) {
                        writer.setColor(x, y, Color.RED);
                    }
                }
            }
        }
        return resultImage;
    }

    private double[][] applyGaussianWindow(double[][] data, int size, double sigma) {
        double[][] kernel = KernelProvider.createGaussianKernel(size, sigma);
        int width = data[0].length;
        int height = data.length;
        int kernelHalf = size / 2;
        double[][] result = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double sum = 0.0;
                for (int ky = 0; ky < size; ky++) {
                    for (int kx = 0; kx < size; kx++) {
                        int pixelX = Math.max(0, Math.min(width - 1, x + (kx - kernelHalf)));
                        int pixelY = Math.max(0, Math.min(height - 1, y + (ky - kernelHalf)));
                        sum += data[pixelY][pixelX] * kernel[ky][kx];
                    }
                }
                result[y][x] = sum;
            }
        }
        return result;
    }
    
    private boolean isLocalMax(double[][] data, int x, int y) {
        double center = data[y][x];
        for (int ky = -1; ky <= 1; ky++) {
            for (int kx = -1; kx <= 1; kx++) {
                if (data[y + ky][x + kx] > center) return false;
            }
        }
        return true;
    }
}
