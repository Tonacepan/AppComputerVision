package com.vision.core;

import com.vision.service.ColorSpaceService;
import com.vision.service.ImageProcessingService;
import com.vision.service.HistogramService;

/**
 * Proveedor de servicios para inyección simple de dependencias
 * Implementa patrón Singleton para gestionar servicios compartidos
 */
public class ServiceProvider {
    
    private static ServiceProvider instance;
    
    private final ColorSpaceService colorSpaceService;
    private final ImageProcessingService imageProcessingService;
    private final HistogramService histogramService;
    
    private ServiceProvider() {
        this.colorSpaceService = new ColorSpaceService();
        this.imageProcessingService = new ImageProcessingService();
        this.histogramService = new HistogramService();
    }
    
    public static synchronized ServiceProvider getInstance() {
        if (instance == null) {
            instance = new ServiceProvider();
        }
        return instance;
    }
    
    public ColorSpaceService getColorSpaceService() {
        return colorSpaceService;
    }
    
    public ImageProcessingService getImageProcessingService() {
        return imageProcessingService;
    }

    public HistogramService getHistogramService() {
        return histogramService;
    }
    
    /**
     * Reinicia la instancia (útil para testing)
     */
    public static void reset() {
        instance = null;
    }
}
