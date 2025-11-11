package com.vision.service;

import com.vision.core.math.Complex;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FourierService {

    private final ImageProcessingService imageProcessingService = new ImageProcessingService();

    public boolean isValidDimensions(Image image) {
        if (image == null) return false;
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        if (width != height) return false;
        // Check if width is a power of two
        return (width > 0) && ((width & (width - 1)) == 0);
    }

    public Complex[][] fft(Image image) {
        if (!isValidDimensions(image)) {
            throw new IllegalArgumentException("La imagen debe ser cuadrada y sus dimensiones una potencia de dos.");
        }
        WritableImage grayImage = imageProcessingService.convertToGrayscale(image);
        int n = (int) grayImage.getWidth();
        Complex[][] data = new Complex[n][n];
        PixelReader reader = grayImage.getPixelReader();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                data[y][x] = new Complex(reader.getColor(x, y).getRed() * 255, 0);
            }
        }

        // 2D FFT (FFT on rows, then FFT on columns)
        for (int y = 0; y < n; y++) {
            data[y] = fft1D(data[y]);
        }

        for (int x = 0; x < n; x++) {
            Complex[] column = new Complex[n];
            for (int y = 0; y < n; y++) {
                column[y] = data[y][x];
            }
            column = fft1D(column);
            for (int y = 0; y < n; y++) {
                data[y][x] = column[y];
            }
        }
        return data;
    }

    public WritableImage inverseFft(Complex[][] frequencyDomain) {
        int n = frequencyDomain.length;
        Complex[][] data = new Complex[n][n];

        // 2D Inverse FFT (IFFT on rows, then IFFT on columns)
        for (int y = 0; y < n; y++) {
            data[y] = ifft1D(frequencyDomain[y]);
        }

        for (int x = 0; x < n; x++) {
            Complex[] column = new Complex[n];
            for (int y = 0; y < n; y++) {
                column[y] = data[y][x];
            }
            column = ifft1D(column);
            for (int y = 0; y < n; y++) {
                data[y][x] = column[y];
            }
        }

        WritableImage resultImage = new WritableImage(n, n);
        PixelWriter writer = resultImage.getPixelWriter();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                double realVal = data[y][x].getReal();
                int gray = (int) Math.round(Math.max(0, Math.min(255, realVal)));
                writer.setColor(x, y, Color.grayRgb(gray));
            }
        }
        return resultImage;
    }

    public WritableImage getSpectrumView(Complex[][] frequencyDomain) {
        int n = frequencyDomain.length;
        WritableImage spectrumImage = new WritableImage(n, n);
        PixelWriter writer = spectrumImage.getPixelWriter();

        Complex[][] shifted = fftShift(frequencyDomain);
        double[][] magnitudes = new double[n][n];
        double maxMag = 0;

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                magnitudes[y][x] = Math.log(1 + shifted[y][x].magnitude());
                if (magnitudes[y][x] > maxMag) {
                    maxMag = magnitudes[y][x];
                }
            }
        }

        if (maxMag == 0) maxMag = 1; // Avoid division by zero

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int gray = (int) (255 * (magnitudes[y][x] / maxMag));
                writer.setColor(x, y, Color.grayRgb(gray));
            }
        }
        return spectrumImage;
    }

    private Complex[][] fftShift(Complex[][] data) {
        int n = data.length;
        int half = n / 2;
        Complex[][] shifted = new Complex[n][n];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                shifted[y][x] = data[(y + half) % n][(x + half) % n];
            }
        }
        return shifted;
    }

    private Complex[] fft1D(Complex[] x) {
        int n = x.length;
        if (n == 1) return new Complex[]{x[0]};

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        Complex[] q = fft1D(even);
        Complex[] r = fft1D(odd);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + n / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    private Complex[] ifft1D(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];

        for (int i = 0; i < n; i++) {
            y[i] = new Complex(x[i].getReal(), -x[i].getImag());
        }

        y = fft1D(y);

        for (int i = 0; i < n; i++) {
            y[i] = new Complex(y[i].getReal() / n, -y[i].getImag() / n);
        }
        return y;
    }
}
