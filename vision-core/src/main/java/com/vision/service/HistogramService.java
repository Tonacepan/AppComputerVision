package com.vision.service;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Service for histogram-related calculations and manipulations.
 */
public class HistogramService {

    public enum TransformationType {
        UNIFORM, EXPONENTIAL, RAYLEIGH, HYPERBOLIC_ROOTS, HYPERBOLIC_LOG
    }

    /**
     * Calculates the histogram of a grayscale image.
     *
     * @param grayImage The input grayscale image.
     * @return An array of 256 integers representing the histogram.
     */
    public int[] getHistogram(Image grayImage) {
        int[] histogram = new int[256];
        PixelReader pixelReader = grayImage.getPixelReader();
        int width = (int) grayImage.getWidth();
        int height = (int) grayImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayLevel = (int) (pixelReader.getColor(x, y).getRed() * 255);
                histogram[grayLevel]++;
            }
        }
        return histogram;
    }

    public double[] getHistogramProbability(int[] histogram, int totalPixels) {
        double[] probability = new double[256];
        for (int i = 0; i < 256; i++) {
            probability[i] = (double) histogram[i] / totalPixels;
        }
        return probability;
    }

    public double[] getCumulativeDistribution(double[] probability) {
        double[] cdf = new double[256];
        cdf[0] = probability[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + probability[i];
        }
        return cdf;
    }

    public record HistogramStats(double mean, double variance, double standardDeviation, double skewness, double energy, double entropy) {}

    public HistogramStats calculateStats(double[] probability) {
        double mean = 0; for (int i = 0; i < 256; i++) mean += i * probability[i];
        double variance = 0; for (int i = 0; i < 256; i++) variance += Math.pow(i - mean, 2) * probability[i];
        double skewness = 0; for (int i = 0; i < 256; i++) skewness += Math.pow(i - mean, 3) * probability[i];
        double energy = 0; for (int i = 0; i < 256; i++) energy += Math.pow(probability[i], 2);
        double entropy = 0; for (int i = 0; i < 256; i++) { if (probability[i] > 0) entropy -= probability[i] * (Math.log(probability[i]) / Math.log(2)); }
        return new HistogramStats(mean, variance, Math.sqrt(variance), skewness, energy, entropy);
    }

    public WritableImage transformImage(Image grayImage, TransformationType type, double... params) {
        int width = (int) grayImage.getWidth();
        int height = (int) grayImage.getHeight();
        int[] histogram = getHistogram(grayImage);
        double[] probability = getHistogramProbability(histogram, width * height);
        double[] cdf = getCumulativeDistribution(probability);

        int[] lut = createLut(cdf, type, params);

        WritableImage transformedImage = new WritableImage(width, height);
        PixelReader pixelReader = grayImage.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int oldGray = (int) (pixelReader.getColor(x, y).getRed() * 255);
                int newGray = lut[oldGray];
                transformedImage.getPixelWriter().setColor(x, y, Color.grayRgb(newGray));
            }
        }
        return transformedImage;
    }

    private int[] createLut(double[] cdf, TransformationType type, double... params) {
        int[] lut = new int[256];
        double fmin = 0, fmax = 255;

        for (int i = 0; i < 256; i++) {
            double cdf_i = cdf[i];
            double newVal = 0;
            switch (type) {
                case UNIFORM:
                    newVal = fmin + (fmax - fmin) * cdf_i;
                    break;
                case EXPONENTIAL: {
                    double alphaExp = params.length > 0 ? params[0] : 1.0;
                    // Ensure alphaExp is positive to avoid division by zero
                    if (alphaExp <= 0) alphaExp = 1.0;
                    // Clamp cdf_i to [0, 1 - eps] to avoid log(0)
                    if (cdf_i >= 1.0) cdf_i = 1.0 - 1e-12;
                    newVal = fmin - (1.0 / alphaExp) * Math.log(1 - cdf_i);
                    // Map to [fmin, fmax] roughly (clamp afterwards)
                    break;
                }
                case RAYLEIGH: {
                    double alphaRay = params.length > 0 ? params[0] : 1.0;
                    if (cdf_i >= 1.0) cdf_i = 1.0 - 1e-6; // avoid log(0)
                    newVal = fmin + Math.sqrt(2 * Math.pow(alphaRay, 2) * Math.log(1 / (1 - cdf_i)));
                    break;
                }
                case HYPERBOLIC_ROOTS: {
                    double pot = params.length > 0 ? params[0] : 2.0;
                    if (pot <= 0) pot = 2.0;
                    double fmin_pot = Math.pow(fmin, 1.0 / pot);
                    double fmax_pot = Math.pow(fmax, 1.0 / pot);
                    newVal = Math.pow((fmax_pot - fmin_pot) * cdf_i + fmin_pot, pot);
                    break;
                }
                case HYPERBOLIC_LOG: {
                    // Avoid division by zero (fmin == 0). Use a smooth, monotonic mapping
                    // that maps cdf_i in [0,1] -> [fmin,fmax]. We use an exponential-like
                    // normalization: newVal = fmin + (exp(cdf_i)-1)/(exp(1)-1) * (fmax-fmin)
                    double denom = Math.exp(1.0) - 1.0;
                    double numer = Math.exp(cdf_i) - 1.0;
                    newVal = fmin + (denom == 0 ? cdf_i : (numer / denom)) * (fmax - fmin);
                    break;
                }
            }
            lut[i] = (int) Math.round(Math.max(0, Math.min(255, newVal)));
        }
        return lut;
    }
}
