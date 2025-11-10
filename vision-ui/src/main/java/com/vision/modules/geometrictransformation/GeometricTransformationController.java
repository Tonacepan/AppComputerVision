package com.vision.modules.geometrictransformation;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.GeometricTransformationService;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class GeometricTransformationController {

    private final ColorSpaceModel model;
    private final GeometricTransformationService geometricTransformationService;

    public GeometricTransformationController(ColorSpaceModel model) {
        this.model = model;
        this.geometricTransformationService = ServiceProvider.getInstance().getGeometricTransformationService();
    }

    public void translateImage(double tx, double ty) {
        Image originalImage = model.getOriginalImage();
        if (originalImage != null) {
            WritableImage translatedImage = geometricTransformationService.translate(originalImage, tx, ty);
            model.setTransformedImage(translatedImage);
        }
    }

    public void rotateImage(double angle) {
        Image originalImage = model.getOriginalImage();
        if (originalImage != null) {
            WritableImage rotatedImage = geometricTransformationService.rotate(originalImage, angle);
            model.setTransformedImage(rotatedImage);
        }
    }

    public void scaleImage(double sx, double sy) {
        Image originalImage = model.getOriginalImage();
        if (originalImage != null) {
            WritableImage scaledImage = geometricTransformationService.scale(originalImage, sx, sy);
            model.setTransformedImage(scaledImage);
        }
    }
    
    public void resetImage() {
        model.applyRGBTransformation();
    }
}
