package com.vision.service;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

/**
 * Servicio para conversión entre espacios de color
 */
public class ColorSpaceService {

    /**
     * Convierte RGB a CMY
     */
    public double[][][] convertRgbToCmy(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        double[][][] cmyData = new double[height][width][3];

        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);

                cmyData[y][x][0] = 1.0 - color.getRed();   // Cyan
                cmyData[y][x][1] = 1.0 - color.getGreen(); // Magenta
                cmyData[y][x][2] = 1.0 - color.getBlue();  // Yellow
            }
        }

        return cmyData;
    }

    /**
     * Convierte RGB a CMYK
     */
    public double[][][] convertRgbToCmyk(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        double[][][] cmykData = new double[height][width][4];

        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);

                double k = 1.0 - Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());

                if (k < 1.0) {
                    cmykData[y][x][0] = (1.0 - color.getRed() - k) / (1.0 - k);   // Cyan
                    cmykData[y][x][1] = (1.0 - color.getGreen() - k) / (1.0 - k); // Magenta
                    cmykData[y][x][2] = (1.0 - color.getBlue() - k) / (1.0 - k);  // Yellow
                } else {
                    cmykData[y][x][0] = 0;
                    cmykData[y][x][1] = 0;
                    cmykData[y][x][2] = 0;
                }
                cmykData[y][x][3] = k; // Black
            }
        }

        return cmykData;
    }

    /**
     * Convierte RGB a YIQ
     */
    public double[][][] convertRgbToYiq(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        double[][][] yiqData = new double[height][width][3];

        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);

                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();

                yiqData[y][x][0] = 0.299 * r + 0.587 * g + 0.114 * b;           // Y
                yiqData[y][x][1] = Math.max(0, Math.min(1, 0.596 * r - 0.274 * g - 0.322 * b + 0.5));     // I
                yiqData[y][x][2] = Math.max(0, Math.min(1, 0.211 * r - 0.523 * g + 0.312 * b + 0.5));     // Q
            }
        }

        return yiqData;
    }

    /**
     * Convierte RGB a HSI
     */
    public double[][][] convertRgbToHsi(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        double[][][] hsiData = new double[height][width][3];

        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);

                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();

                double intensity = (r + g + b) / 3.0;
                double min = Math.min(Math.min(r, g), b);
                double saturation = (intensity == 0) ? 0 : 1 - (min / intensity);

                double hue = 0;
                if (saturation != 0) {
                    double numerator = 0.5 * ((r - g) + (r - b));
                    double denominator = Math.sqrt((r - g) * (r - g) + (r - b) * (g - b));
                    if (denominator != 0) {
                        hue = Math.acos(numerator / denominator);
                        if (b > g) {
                            hue = 2 * Math.PI - hue;
                        }
                    }
                }

                hsiData[y][x][0] = hue / (2 * Math.PI);  // H (normalizado 0-1)
                hsiData[y][x][1] = saturation;           // S
                hsiData[y][x][2] = intensity;            // I
            }
        }

        return hsiData;
    }

    /**
     * Convierte RGB a HSV
     */
    public double[][][] convertRgbToHsv(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        double[][][] hsvData = new double[height][width][3];

        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);

                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();

                double max = Math.max(Math.max(r, g), b);
                double min = Math.min(Math.min(r, g), b);
                double delta = max - min;

                double hue = 0;
                if (delta != 0) {
                    if (max == r) {
                        hue = ((g - b) / delta) % 6;
                    } else if (max == g) {
                        hue = (b - r) / delta + 2;
                    } else {
                        hue = (r - g) / delta + 4;
                    }
                    hue *= 60;
                    if (hue < 0) hue += 360;
                }

                double saturation = (max == 0) ? 0 : delta / max;
                double value = max;

                hsvData[y][x][0] = hue / 360.0;  // H (normalizado 0-1)
                hsvData[y][x][1] = saturation;   // S
                hsvData[y][x][2] = value;        // V
            }
        }

        return hsvData;
    }

    /**
     * Crea una imagen a partir de datos CMY
     */
    public WritableImage createCmyImage(double[][][] cmyData) {
        int height = cmyData.length;
        int width = cmyData[0].length;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = 1.0 - cmyData[y][x][0];
                double g = 1.0 - cmyData[y][x][1];
                double b = 1.0 - cmyData[y][x][2];

                Color color = new Color(
                    Math.max(0, Math.min(1, r)),
                    Math.max(0, Math.min(1, g)),
                    Math.max(0, Math.min(1, b)),
                    1.0
                );
                pixelWriter.setColor(x, y, color);
            }
        }

        return image;
    }

    /**
     * Crea una imagen a partir de datos CMYK
     */
    public WritableImage createCmykImage(double[][][] cmykData) {
        int height = cmykData.length;
        int width = cmykData[0].length;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double c = cmykData[y][x][0];
                double m = cmykData[y][x][1];
                double yellow = cmykData[y][x][2];
                double k = cmykData[y][x][3];

                double r = (1.0 - c) * (1.0 - k);
                double g = (1.0 - m) * (1.0 - k);
                double b = (1.0 - yellow) * (1.0 - k);

                Color color = new Color(
                    Math.max(0, Math.min(1, r)),
                    Math.max(0, Math.min(1, g)),
                    Math.max(0, Math.min(1, b)),
                    1.0
                );
                pixelWriter.setColor(x, y, color);
            }
        }

        return image;
    }

    /**
     * Crea una imagen de canal en escala de grises
     */
    public WritableImage createChannelImage(double[][][] colorData, int channelIndex) {
        int height = colorData.length;
        int width = colorData[0].length;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = colorData[y][x][channelIndex];
                Color color = new Color(value, value, value, 1.0);
                pixelWriter.setColor(x, y, color);
            }
        }

        return image;
    }
}
