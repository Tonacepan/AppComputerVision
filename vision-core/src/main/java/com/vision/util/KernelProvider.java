package com.vision.util;

import java.util.HashMap;
import java.util.Map;

public class KernelProvider {

    public enum LowPass {
        MEAN_7x7,
        MEAN_11x11,
        MEAN_15x15
    }

    public enum HighPass {
        SHARPEN_SOFT,
        SHARPEN_MEDIUM,
        SHARPEN_STRONG
    }

    private static final Map<LowPass, double[][]> lowPassKernels = new HashMap<>();
    private static final Map<HighPass, double[][]> highPassKernels = new HashMap<>();

    static {
        // Low-pass (Averaging)
        lowPassKernels.put(LowPass.MEAN_7x7, createMeanKernel(7));
        lowPassKernels.put(LowPass.MEAN_11x11, createMeanKernel(11));
        lowPassKernels.put(LowPass.MEAN_15x15, createMeanKernel(15));

        // High-pass (Sharpening)
        highPassKernels.put(HighPass.SHARPEN_SOFT, new double[][]{
                {-2, -2, -2},
                {-2, 17, -2},
                {-2, -2, -2}
        });
        highPassKernels.put(HighPass.SHARPEN_MEDIUM, new double[][]{
                {-1, -1, -1},
                {-1, 9, -1},
                {-1, -1, -1}
        });
        highPassKernels.put(HighPass.SHARPEN_STRONG, new double[][]{
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}
        });
    }

    public static double[][] getKernel(LowPass type) {
        return lowPassKernels.get(type);
    }

    public static double[][] getKernel(HighPass type) {
        return highPassKernels.get(type);
    }

    private static double[][] createMeanKernel(int size) {
        if (size % 2 == 0) {
            throw new IllegalArgumentException("Kernel size must be odd.");
        }
        double[][] kernel = new double[size][size];
        double value = 1.0 / (size * size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] = value;
            }
        }
        return kernel;
    }
    
    public static final double[][] SOBEL_X = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    public static final double[][] SOBEL_Y = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
    };
    
    public static double[][] createGaussianKernel(int size, double sigma) {
        if (size % 2 == 0) {
            throw new IllegalArgumentException("Kernel size must be odd.");
        }
        double[][] kernel = new double[size][size];
        double sum = 0;
        int half = size / 2;
        for (int y = -half; y <= half; y++) {
            for (int x = -half; x <= half; x++) {
                double value = (1.0 / (2 * Math.PI * sigma * sigma)) * Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                kernel[y + half][x + half] = value;
                sum += value;
            }
        }
        // Normalize the kernel
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= sum;
            }
        }
        return kernel;
    }
}
