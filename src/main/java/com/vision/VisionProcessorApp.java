package com.vision;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Aplicación principal que combina procesamiento de imágenes y conversión de espacios de color
 */
public class VisionProcessorApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar el archivo FXML principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vision/view/main.fxml"));
        Parent root = loader.load();
        
        // Configurar la escena
        Scene scene = new Scene(root, 1400, 900);
        
        // Configurar la ventana principal
        primaryStage.setTitle("Vision Processor - Procesamiento de Imágenes y Espacios de Color");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        
        // Agregar icono (opcional)
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Ignorar si no se encuentra el icono
        }
        
        // Mostrar la ventana
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
