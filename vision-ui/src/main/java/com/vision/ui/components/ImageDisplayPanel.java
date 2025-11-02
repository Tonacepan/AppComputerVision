package com.vision.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Componente reutilizable para mostrar imágenes con título
 */
public class ImageDisplayPanel extends VBox {
    
    private final Label titleLabel;
    private final ImageView imageView;
    
    public ImageDisplayPanel(String title, double width, double height) {
        this(title, width, height, true);
    }
    
    public ImageDisplayPanel(String title, double width, double height, boolean preserveRatio) {
        super(5);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5));
        
        titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        
        imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setSmooth(true);
        imageView.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #F5F5F5;");
        
        Tooltip.install(imageView, new Tooltip(title));
        
        getChildren().addAll(titleLabel, imageView);
    }
    
    public void setImage(Image image) {
        imageView.setImage(image);
    }
    
    public Image getImage() {
        return imageView.getImage();
    }
    
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public ImageView getImageView() {
        return imageView;
    }
}
