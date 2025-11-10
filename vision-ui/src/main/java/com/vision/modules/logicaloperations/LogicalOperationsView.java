package com.vision.modules.logicaloperations;

import com.vision.model.ColorSpaceModel;
import com.vision.ui.components.ImageDisplayPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LogicalOperationsView extends VBox implements ColorSpaceModel.ColorSpaceModelListener {

    private final LogicalOperationsController controller;
    private final LogicalOperationsModel localModel;
    private final ColorSpaceModel sharedModel;

    private final ImageDisplayPanel imageAPanel = new ImageDisplayPanel("Imagen A (Global)", 200, 200);
    private final ImageDisplayPanel imageBPanel = new ImageDisplayPanel("Imagen B (Local)", 200, 200);
    private final ImageDisplayPanel resultPanel = new ImageDisplayPanel("Resultado", 200, 200);

    public LogicalOperationsView(ColorSpaceModel sharedModel) {
        this.sharedModel = sharedModel;
        this.localModel = new LogicalOperationsModel();
        this.controller = new LogicalOperationsController(localModel);

        // Listen to the shared model
        this.sharedModel.addListener(this);

        setupUI();
        bindModels();

        // Set initial image from shared model
        onImageLoaded();
    }

    private void setupUI() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);

        // Paneles de imágenes
        HBox imagePanels = new HBox(20,
                createImageContainer(imageAPanel),
                createImageContainer(imageBPanel),
                createImageContainer(resultPanel)
        );
        imagePanels.setAlignment(Pos.CENTER);

        // Botón para cargar Imagen B
        Button loadImageBButton = new Button("Cargar Imagen B");
        loadImageBButton.setOnAction(e -> handleLoadImageB());

        // Contenedor para botones de operaciones
        FlowPane operationsPane = new FlowPane(10, 10);
        operationsPane.setAlignment(Pos.CENTER);
        operationsPane.setPadding(new Insets(15, 0, 0, 0));

        String[] logicalOps = {"AND", "OR", "XOR", "NOT"};
        String[] relationalOps = {"==", "!=", ">", ">=", "<", "<="};

        for (String op : logicalOps) {
            Button btn = new Button(op);
            btn.setOnAction(e -> controller.performOperation(op));
            operationsPane.getChildren().add(btn);
        }
        for (String op : relationalOps) {
            Button btn = new Button(op);
            btn.setOnAction(e -> controller.performOperation(op));
            operationsPane.getChildren().add(btn);
        }

        VBox controls = new VBox(15, loadImageBButton, operationsPane);
        controls.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(new VBox(20, imagePanels, controls));
        scrollPane.setFitToWidth(true);
        getChildren().add(scrollPane);
    }

    private VBox createImageContainer(ImageDisplayPanel panel) {
        VBox container = new VBox(5, panel);
        container.setAlignment(Pos.CENTER);
        // Removed border to simplify
        return container;
    }

    private void bindModels() {
        // Listen to local model changes and update UI accordingly
        localModel.imageAProperty().addListener((obs, oldImg, newImg) -> imageAPanel.setImage(newImg));
        localModel.imageBProperty().addListener((obs, oldImg, newImg) -> imageBPanel.setImage(newImg));
        localModel.resultImageProperty().addListener((obs, oldImg, newImg) -> resultPanel.setImage(newImg));
    }

    private void handleLoadImageB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen B");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        Stage stage = (Stage) getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                if (!image.isError()) {
                    localModel.setImageB(image);
                } else {
                    // TODO: show error
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: show error
            }
        }
    }

    // --- ColorSpaceModelListener Implementation ---

    @Override
    public void onImageLoaded() {
        // When global image changes, update local model
        localModel.setImageA(sharedModel.getOriginalImage());
    }

    @Override
    public void onTransformationApplied() {
        // Not relevant for this view
    }

    @Override
    public void onTransformationChanged() {
        // Not relevant for this view
    }

    @Override
    public void onAdjustmentsChanged() {
        // Not relevant for this view
    }

    @Override
    public void onAdjustedImagesReady() {
        // Not relevant for this view
    }
}
