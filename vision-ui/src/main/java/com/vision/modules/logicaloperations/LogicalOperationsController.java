package com.vision.modules.logicaloperations;

import com.vision.core.ServiceProvider;
import com.vision.service.LogicalOperationsService;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LogicalOperationsController {

    private final LogicalOperationsService logicalOperationsService;
    private final LogicalOperationsModel model;

    public LogicalOperationsController(LogicalOperationsModel model) {
        this.model = model;
        this.logicalOperationsService = ServiceProvider.getInstance().getLogicalOperationsService();
    }

    public void performOperation(String operation) {
        Image imageA = model.getImageA();
        Image imageB = model.getImageB();

        if (imageA == null) {
            // Handle error: Image A is missing
            System.err.println("Image A is not loaded.");
            return;
        }

        WritableImage result = null;
        try {
            if (isUnaryOperation(operation)) {
                if ("NOT".equals(operation)) {
                    result = logicalOperationsService.not(imageA);
                }
            } else {
                if (imageB == null) {
                    // Handle error: Image B is missing
                    System.err.println("Image B is not loaded for a binary operation.");
                    return;
                }
                result = switch (operation) {
                    case "AND" -> logicalOperationsService.and(imageA, imageB);
                    case "OR" -> logicalOperationsService.or(imageA, imageB);
                    case "XOR" -> logicalOperationsService.xor(imageA, imageB);
                    case "==" -> logicalOperationsService.areEqual(imageA, imageB);
                    case "!=" -> logicalOperationsService.areNotEqual(imageA, imageB);
                    case ">" -> logicalOperationsService.isGreater(imageA, imageB);
                    case ">=" -> logicalOperationsService.isGreaterOrEqual(imageA, imageB);
                    case "<" -> logicalOperationsService.isLess(imageA, imageB);
                    case "<=" -> logicalOperationsService.isLessOrEqual(imageA, imageB);
                    default -> throw new IllegalArgumentException("Unknown operation: " + operation);
                };
            }
            if (result != null) {
                model.setResultImage(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, update the model to show an error state
        }
    }

    private boolean isUnaryOperation(String operation) {
        return "NOT".equals(operation);
    }
}
