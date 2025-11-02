package com.vision.view;

import com.vision.controller.ColorConverterController;
import com.vision.model.ColorSpaceModel;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Vista para la conversión de espacios de color y ajustes de imagen
 */
public class ColorConverterView {
    private final ColorSpaceModel model;
    private final ColorConverterController controller;

    // Componentes de la interfaz
    private VBox mainContainer;
    private Button loadDefaultButton;
    private Button generateRGBChannelsButton;
    private ComboBox<String> transformationComboBox;
    private Button applyTransformationButton;
    private Label statusLabel;

    // ImageViews
    private ImageView originalImageView;
    private ImageView transformedImageView;
    private ImageView channel1View, channel2View, channel3View, channel4View;

    // Labels para canales
    private Label channel1Label, channel2Label, channel3Label, channel4Label;

    // Componentes para ajustes
    private Slider brightnessSlider, contrastSlider;
    private Label brightnessLabel, contrastLabel;
    private ImageView brightnessAdjustedView, contrastAdjustedView;

    public ColorConverterView(ColorSpaceModel model, ColorConverterController controller) {
        this.model = model;
        this.controller = controller;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        if (model.getOriginalImage() != null) {
            updateViewForExistingImage();
        }
    }

    private void initializeComponents() {
        mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(15));

        loadDefaultButton = new Button("Imagen Predeterminada");
        loadDefaultButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        loadDefaultButton.setPrefWidth(150);
        
        generateRGBChannelsButton = new Button("Generar Canales RGB");
        generateRGBChannelsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        generateRGBChannelsButton.setPrefWidth(150);
        generateRGBChannelsButton.setDisable(true);

        transformationComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "RGB → CMY", "RGB → CMYK", "RGB → YIQ", "RGB → HSI", "RGB → HSV"));
        transformationComboBox.setPromptText("Selecciona una transformación...");
        transformationComboBox.setPrefWidth(250);

        applyTransformationButton = new Button("Aplicar Transformación");
        applyTransformationButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        applyTransformationButton.setPrefWidth(150);
        applyTransformationButton.setDisable(true);

        statusLabel = new Label("Selecciona una imagen para comenzar");
        statusLabel.setStyle("-fx-font-size: 12px;");

        originalImageView = createImageView("Imagen Original", 700, 500);
        transformedImageView = createImageView("Imagen Transformada", 700, 500);
        channel1View = createImageView("Canal 1", 360, 280);
        channel2View = createImageView("Canal 2", 360, 280);
        channel3View = createImageView("Canal 3", 360, 280);
        channel4View = createImageView("Canal 4", 360, 280);

        channel1Label = new Label("Canal 1");
        channel2Label = new Label("Canal 2");
        channel3Label = new Label("Canal 3");
        channel4Label = new Label("Canal 4");
        channel4View.setVisible(false);
        channel4Label.setVisible(false);

        // Inicializar componentes de ajuste
        brightnessSlider = new Slider(-1.0, 1.0, 0.0);
        contrastSlider = new Slider(0.1, 3.0, 1.0);
        brightnessLabel = new Label("Brillo: 0.0");
        contrastLabel = new Label("Contraste: 1.0");
        brightnessAdjustedView = createImageView("Ajuste de Brillo", 500, 360);
        contrastAdjustedView = createImageView("Ajuste de Contraste", 500, 360);
    }

    private ImageView createImageView(String tooltip, double width, double height) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1;");
        if (tooltip != null) Tooltip.install(imageView, new Tooltip(tooltip));
        return imageView;
    }

    private void setupLayout() {
        Label titleLabel = new Label("Conversor de Espacios de Color y Ajustes");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox loadSection = new HBox(10, loadDefaultButton, generateRGBChannelsButton);
        loadSection.setAlignment(Pos.CENTER);

        HBox transformationSection = new HBox(10, new Label("Transformación:"), transformationComboBox, applyTransformationButton);
        transformationSection.setAlignment(Pos.CENTER);

        HBox mainImagesSection = new HBox(20, 
            createImageSection("Imagen Original", originalImageView),
            createImageSection("Imagen Transformada", transformedImageView));
        mainImagesSection.setAlignment(Pos.CENTER);

        VBox channelsContainer = new VBox(10);
        channelsContainer.setAlignment(Pos.CENTER);
        Label channelsTitle = new Label("Canales Individuales:");
        channelsTitle.setStyle("-fx-font-weight: bold;");
        HBox channelsSection = new HBox(15, 
            createImageSection(channel1View, channel1Label),
            createImageSection(channel2View, channel2Label),
            createImageSection(channel3View, channel3Label),
            createImageSection(channel4View, channel4Label));
        channelsSection.setAlignment(Pos.CENTER);
        channelsContainer.getChildren().addAll(channelsTitle, channelsSection);

        // Layout para sección de ajustes
        VBox adjustmentsSection = new VBox(10);
        adjustmentsSection.setAlignment(Pos.CENTER);
        Label adjustmentsTitle = new Label("Ajustes de Brillo y Contraste");
        adjustmentsTitle.setStyle("-fx-font-weight: bold;");
        HBox brightnessSection = new HBox(10, brightnessLabel, brightnessSlider);
        brightnessSection.setAlignment(Pos.CENTER);
        HBox contrastSection = new HBox(10, contrastLabel, contrastSlider);
        contrastSection.setAlignment(Pos.CENTER);
        HBox adjustedImagesSection = new HBox(15, 
            createImageSection("Con Brillo Ajustado", brightnessAdjustedView),
            createImageSection("Con Contraste Ajustado", contrastAdjustedView));
        adjustedImagesSection.setAlignment(Pos.CENTER);
        adjustmentsSection.getChildren().addAll(adjustmentsTitle, brightnessSection, contrastSection, adjustedImagesSection);

        mainContainer.getChildren().addAll(
            createCenteredContainer(titleLabel), new Separator(),
            createCenteredContainer(loadSection), createCenteredContainer(transformationSection),
            createCenteredContainer(statusLabel), new Separator(),
            mainImagesSection, new Separator(),
            channelsContainer, new Separator(),
            adjustmentsSection
        );
    }

    private HBox createCenteredContainer(Node node) {
        HBox container = new HBox(node);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox createImageSection(String title, ImageView imageView) {
        VBox section = new VBox(5);
        section.setAlignment(Pos.CENTER);
        if (!title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-weight: bold;");
            section.getChildren().add(titleLabel);
        }
        section.getChildren().add(imageView);
        return section;
    }

    private VBox createImageSection(ImageView imageView, Label customLabel) {
        VBox section = new VBox(5, customLabel, imageView);
        section.setAlignment(Pos.CENTER);
        customLabel.setStyle("-fx-font-weight: bold;");
        return section;
    }

    private void setupEventHandlers() {
        loadDefaultButton.setOnAction(e -> controller.handleLoadDefault());
        generateRGBChannelsButton.setOnAction(e -> controller.handleRGBTransformation());
        applyTransformationButton.setOnAction(e -> controller.handleApplyTransformation());

        transformationComboBox.setOnAction(e -> {
            String selected = transformationComboBox.getValue();
            if (selected != null) {
                transformedImageView.setImage(null);
                clearChannelImages();
                controller.handleTransformationChange(selected);
                applyTransformationButton.setDisable(model.getOriginalImage() == null);
                updateChannelLabels(selected);
                statusLabel.setText("Transformación seleccionada: " + selected + " - Haz clic en 'Aplicar'");
            }
        });

        // Handlers para sliders
        brightnessSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            brightnessLabel.setText(String.format("Brillo: %.2f", newVal.doubleValue())));
        brightnessSlider.setOnMouseReleased(e -> controller.handleBrightnessChange(brightnessSlider.getValue()));

        contrastSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            contrastLabel.setText(String.format("Contraste: %.2f", newVal.doubleValue())));
        contrastSlider.setOnMouseReleased(e -> controller.handleContrastChange(contrastSlider.getValue()));

        model.addListener(new ColorSpaceModel.ColorSpaceModelListener() {
            @Override
            public void onImageLoaded() {
                originalImageView.setImage(model.getOriginalImage());
                applyTransformationButton.setDisable(transformationComboBox.getValue() == null);
                generateRGBChannelsButton.setDisable(false);
                statusLabel.setText("Imagen cargada. Genera canales RGB o aplica una transformación.");
                updateChannelLabels("RGB");
                updateChannelImages();
                resetAdjustmentsUI();
            }

            @Override
            public void onTransformationApplied() {
                transformedImageView.setImage(model.getTransformedImage());
                updateChannelImages();
                statusLabel.setText("Transformación aplicada: " + model.getCurrentTransformation());
            }

            @Override
            public void onTransformationChanged() {
                statusLabel.setText("Transformación seleccionada: " + model.getCurrentTransformation());
            }

            @Override
            public void onAdjustmentsChanged() {
                // Opcional: Lógica mientras se arrastra el slider
            }

            @Override
            public void onAdjustedImagesReady() {
                brightnessAdjustedView.setImage(model.getBrightnessImage());
                contrastAdjustedView.setImage(model.getContrastImage());
            }
        });
    }

    private void resetAdjustmentsUI() {
        brightnessSlider.setValue(0.0);
        contrastSlider.setValue(1.0);
        brightnessAdjustedView.setImage(null);
        contrastAdjustedView.setImage(null);
    }

    private void updateChannelLabels(String transformation) {
        String[] names = getChannelNames(transformation);
        channel1Label.setText(names[0]);
        channel2Label.setText(names[1]);
        channel3Label.setText(names[2]);
        boolean hasFour = names.length > 3 && !names[3].isEmpty();
        channel4View.setVisible(hasFour);
        channel4Label.setVisible(hasFour);
        channel4View.setManaged(hasFour);
        channel4Label.setManaged(hasFour);
        if (hasFour) channel4Label.setText(names[3]);
    }

    private String[] getChannelNames(String transformation) {
        return switch (transformation) {
            case "RGB" -> new String[]{"Canal Rojo", "Canal Verde", "Canal Azul", "Gris"};
            case "RGB → CMY" -> new String[]{"Cyan", "Magenta", "Yellow"};
            case "RGB → CMYK" -> new String[]{"Cyan", "Magenta", "Yellow", "Black"};
            case "RGB → YIQ" -> new String[]{"Y (Luminancia)", "I (Croma)", "Q (Croma)"};
            case "RGB → HSI" -> new String[]{"H (Matiz)", "S (Saturación)", "I (Intensidad)"};
            case "RGB → HSV" -> new String[]{"H (Matiz)", "S (Saturación)", "V (Valor)"};
            default -> new String[]{"Canal 1", "Canal 2", "Canal 3", "Canal 4"};
        };
    }

    private void updateChannelImages() {
        var images = model.getChannelImages();
        if (images != null) {
            channel1View.setImage(images.length > 0 ? images[0] : null);
            channel2View.setImage(images.length > 1 ? images[1] : null);
            channel3View.setImage(images.length > 2 ? images[2] : null);
            channel4View.setImage(images.length > 3 ? images[3] : null);
        }
    }

    private void clearChannelImages() {
        channel1View.setImage(null); channel2View.setImage(null);
        channel3View.setImage(null); channel4View.setImage(null);
    }

    public Node getView() {
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private void updateViewForExistingImage() {
        originalImageView.setImage(model.getOriginalImage());
        applyTransformationButton.setDisable(transformationComboBox.getValue() == null);
        generateRGBChannelsButton.setDisable(false);
        statusLabel.setText("Imagen cargada. Genera canales RGB o aplica una transformación.");
        updateChannelLabels("RGB");
        updateChannelImages();
        if (model.getTransformedImage() != null) {
            transformedImageView.setImage(model.getTransformedImage());
        }
        resetAdjustmentsUI();
    }
    
    public void onTabSelected() {
        if (model.getOriginalImage() == null) {
            statusLabel.setText("Selecciona una imagen para comenzar");
        }
    }
}
