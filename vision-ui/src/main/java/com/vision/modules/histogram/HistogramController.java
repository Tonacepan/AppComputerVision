package com.vision.modules.histogram;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.HistogramService;
import com.vision.service.ImageProcessingService;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class HistogramController {

    private final ColorSpaceModel model;
    private final HistogramService histogramService;
    private final ImageProcessingService imageProcessingService;

    public HistogramController(ColorSpaceModel model) {
        this.model = model;
        this.histogramService = ServiceProvider.getInstance().getHistogramService();
        this.imageProcessingService = ServiceProvider.getInstance().getImageProcessingService();
    }

    public void calculateAndDisplayHistogram(BarChart<String, Number> histogramChart, LineChart<String, Number> cdfChart, TextArea statsTextArea) {
        Image originalImage = model.getOriginalImage();
        if (originalImage == null) {
            statsTextArea.setText("Por favor, cargue una imagen primero.");
            return;
        }

        WritableImage grayImage = imageProcessingService.convertToGrayscale(originalImage);
        updateChartsAndStats(grayImage, histogramChart, cdfChart, statsTextArea);
    }

    public void applyTransformation(
            HistogramService.TransformationType type, String alphaStr, String potStr, 
            ImageDisplayPanel transformedImageView, BarChart<String, Number> transformedHistogramChart, TextArea statsTextArea) {

        Image originalImage = model.getOriginalImage();
        if (originalImage == null || type == null) {
            statsTextArea.setText("Cargue una imagen y seleccione una transformación.");
            return;
        }

        WritableImage grayImage = imageProcessingService.convertToGrayscale(originalImage);
        double alpha = parseDouble(alphaStr, 1.0);
        double pot = parseDouble(potStr, 2.0);

        WritableImage transformedImage = histogramService.transformImage(grayImage, type, alpha, pot);
        transformedImageView.setImage(transformedImage);

        // Update charts and stats for the new transformed image
        updateChartsAndStats(transformedImage, transformedHistogramChart, null, null);
    }

    private void updateChartsAndStats(Image image, BarChart<String, Number> histogramChart, LineChart<String, Number> cdfChart, TextArea statsTextArea) {
        int totalPixels = (int) (image.getWidth() * image.getHeight());
        int[] histogram = histogramService.getHistogram(image);
        double[] probability = histogramService.getHistogramProbability(histogram, totalPixels);

        // Update Histogram Chart
        XYChart.Series<String, Number> probSeries = new XYChart.Series<>();
        probSeries.setName("p(hi)");
        for (int i = 0; i < 256; i++) {
            probSeries.getData().add(new XYChart.Data<>(String.valueOf(i), probability[i]));
        }
        histogramChart.getData().clear();
        histogramChart.getData().add(probSeries);

        // Update CDF Chart if provided
        if (cdfChart != null) {
            double[] cdf = histogramService.getCumulativeDistribution(probability);
            XYChart.Series<String, Number> cdfSeries = new XYChart.Series<>();
            cdfSeries.setName("D(p(hi))");
            for (int i = 0; i < 256; i++) {
                cdfSeries.getData().add(new XYChart.Data<>(String.valueOf(i), cdf[i]));
            }
            cdfChart.getData().clear();
            cdfChart.getData().add(cdfSeries);
        }

        // Update Stats TextArea if provided
        if (statsTextArea != null) {
            HistogramService.HistogramStats stats = histogramService.calculateStats(probability);
            String statsText = String.format(
                "Propiedades Estadísticas:\n" +
                "- Media: %.4f\n" + "- Varianza: %.4f\n" + "- Desviación Estándar: %.4f\n" +
                "- Asimetría (Skewness): %.4f\n" + "- Energía: %.4f\n" + "- Entropía: %.4f bits",
                stats.mean(), stats.variance(), stats.standardDeviation(), stats.skewness(), stats.energy(), stats.entropy()
            );
            statsTextArea.setText(statsText);
        }
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }
}
