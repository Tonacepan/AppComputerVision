package com.vision.controller;

import com.vision.model.ColorSpaceModel;
import com.vision.service.ColorSpaceService;
import com.vision.service.ImageProcessingService;
import com.vision.view.ColorConverterView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador principal que maneja la carga de la vista principal
 */
public class MainController implements Initializable {
    @FXML
    private BorderPane mainPane;

    @FXML
    private Button loadImageButton;

    private final ColorSpaceModel sharedColorSpaceModel = new ColorSpaceModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Crear servicios
        ColorSpaceService colorSpaceService = new ColorSpaceService();
        ImageProcessingService imageProcessingService = new ImageProcessingService();

        // 2. Crear el controlador con el modelo y los servicios
        ColorConverterController colorConverterController = new ColorConverterController(
            sharedColorSpaceModel, 
            colorSpaceService, 
            imageProcessingService
        );

        // 3. Crear la vista con el modelo y el controlador
        ColorConverterView colorConverterView = new ColorConverterView(sharedColorSpaceModel, colorConverterController);

        // 4. Establecer la vista en la UI
        mainPane.setCenter(colorConverterView.getView());

        setupLoadImageButton();
    }

    private void setupLoadImageButton() {
        loadImageButton.setOnAction(event -> handleLoadImage());
    }

    private void handleLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        Stage stage = (Stage) loadImageButton.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            if (!image.isError()) {
                sharedColorSpaceModel.setOriginalImage(image);
            }
        }
    }
}
