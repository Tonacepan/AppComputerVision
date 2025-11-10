package com.vision.modules.morphological;

import com.vision.core.ServiceProvider;
import com.vision.model.ColorSpaceModel;
import com.vision.service.MorphologicalService;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class MorphologicalController {

    private final ColorSpaceModel model;
    private final MorphologicalService morphologicalService;
    private WritableImage noisyImage;

    public MorphologicalController(ColorSpaceModel model) {
        this.model = model;
        this.morphologicalService = ServiceProvider.getInstance().getMorphologicalService();
    }

    public void addSaltNoise(double percentage) {
        Image originalImage = model.getOriginalImage();
        if (originalImage != null) {
            noisyImage = morphologicalService.addSaltNoise(originalImage, percentage);
            model.setTransformedImage(noisyImage);
        }
    }

    public void addPepperNoise(double percentage) {
        Image originalImage = model.getOriginalImage();
        if (originalImage != null) {
            noisyImage = morphologicalService.addPepperNoise(originalImage, percentage);
            model.setTransformedImage(noisyImage);
        }
    }

    public void erode() {
        if (noisyImage != null) {
            WritableImage filteredImage = morphologicalService.erode(noisyImage, 3);
            model.setTransformedImage(filteredImage);
        }
    }

    public void dilate() {
        if (noisyImage != null) {
            WritableImage filteredImage = morphologicalService.dilate(noisyImage, 3);
            model.setTransformedImage(filteredImage);
        }
    }

    public void open() {
        if (noisyImage != null) {
            WritableImage filteredImage = morphologicalService.open(noisyImage, 3);
            model.setTransformedImage(filteredImage);
        }
    }

    public void close() {
        if (noisyImage != null) {
            WritableImage filteredImage = morphologicalService.close(noisyImage, 3);
            model.setTransformedImage(filteredImage);
        }
    }
    
    public WritableImage getNoisyImage() {
        return noisyImage;
    }
}
