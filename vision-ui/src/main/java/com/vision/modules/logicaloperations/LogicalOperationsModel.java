package com.vision.modules.logicaloperations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class LogicalOperationsModel {

    private final ObjectProperty<Image> imageA = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> imageB = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> resultImage = new SimpleObjectProperty<>();

    public ObjectProperty<Image> imageAProperty() {
        return imageA;
    }

    public Image getImageA() {
        return imageA.get();
    }

    public void setImageA(Image image) {
        this.imageA.set(image);
    }

    public ObjectProperty<Image> imageBProperty() {
        return imageB;
    }

    public Image getImageB() {
        return imageB.get();
    }

    public void setImageB(Image image) {
        this.imageB.set(image);
    }

    public ObjectProperty<Image> resultImageProperty() {
        return resultImage;
    }

    public Image getResultImage() {
        return resultImage.get();
    }

    public void setResultImage(Image image) {
        this.resultImage.set(image);
    }
}
