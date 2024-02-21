package Event;

import Game.Main;
import Item.Danger;
import Item.DataLists;
import Item.LogoManager;
import Item.Resource;
import Player.Player;
import Player.Character;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventHandler {
    private VBox eventInfoPane; // Declare a VBox to hold event information

    private ImageView player;
    private Character character;
    private List<Event> eventList;
    private double playerX;
    private double playerY;
    private Event lastEventEncountered; // Store information about the last event encountered
    private long lastEventTime; // Store the timestamp when the last event was encountered
    private static final long EVENT_COOLDOWN = 5000; // Cooldown time in milliseconds (5 seconds)
    public static boolean isAlertDisplayed = false;
    private Pane gamePane;


    // Constructor

    public EventHandler(List<Event> eventList, Pane gamePane, Character character, ImageView player) {
        this.eventList = eventList;
        this.gamePane = gamePane;
        this.character = character;
        this.player = player;
        eventInfoPane = new VBox();
        eventInfoPane.setStyle("-fx-background-color: #ffffff;"); // Set background color for visibility

    }

    public void handleEvent() {
        // Define a threshold for closeness
        double threshold = 30; // Adjust as needed

        // Get the current time
        long currentTime = System.currentTimeMillis();

        // Check if the player is close to any event
        for (Event event : eventList) {
            double eventX = event.getX();
            double eventY = event.getY();

            // Calculate the distance between the player and the event
            double distance = Math.sqrt(Math.pow(playerX - eventX + 55, 2) + Math.pow(playerY - eventY + 45, 2));

            // Check if the distance is less than the threshold
            if (distance < threshold) {
                // Check if this event is different from the last one encountered
                if (!event.equals(lastEventEncountered)) {
                    // Show a message window containing the event information
                    showEventMessage(event);
                    lastEventEncountered = event; // Update the last event encountered
                    lastEventTime = currentTime; // Update the timestamp
                } else {
                    // Check if enough time has passed since the last event encounter
                    if (currentTime - lastEventTime >= EVENT_COOLDOWN) {
                        // Show a message window containing the event information
                        showEventMessage(event);
                        lastEventTime = currentTime; // Update the timestamp
                    }
                }
                return; // Exit the loop after encountering an event
            }
        }
    }

    private void showEventMessage(Event event) {
        isAlertDisplayed = true;
        Platform.runLater(() -> {
            Alert alert = createAlert(event);
            configureAlertButtons(alert, event);
            setAlertCloseCallback(alert, event);
            Optional<ButtonType> result = alert.showAndWait();
            handleUserResponse(result, event);
        });
    }

    private Alert createAlert(Event event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Event Information");
        Label nameLabel = createNameLabel(event);
        VBox content = createContent(event);
        alert.setHeaderText(nameLabel.getText());
        alert.getDialogPane().setContent(content);
        String cssPath = "file:resources/alert.css";
        alert.getDialogPane().getStylesheets().add(cssPath);
        return alert;
    }

    private Label createNameLabel(Event event) {
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("file:lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        Label nameLabel = new Label(event.getEventType().getName());
        nameLabel.setFont(gameFont);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        return nameLabel;
    }

    private VBox createContent(Event event) {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.setPadding(new Insets(10));
        if (event.getEventType() instanceof Resource) {
            createResourceContent(event, content);
        } else if (event.getEventType() instanceof Danger) {
            createDangerContent(event, content);
        }
        return content;
    }

    private void createResourceContent(Event event, VBox content) {
        Resource resource = (Resource) event.getEventType();
        ImageView logo = LogoManager.getLogo(resource.getName());
        Label logoLabel = new Label();
        if (logo != null) {
            logoLabel.setGraphic(new ImageView(logo.getImage()));
        }
        Label quantityLabel = new Label("Quantity: " + resource.getQuantity());
        quantityLabel.setStyle("-fx-font-size: 14;");
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("file:lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        quantityLabel.setFont(gameFont);
        content.getChildren().addAll(logoLabel, quantityLabel);
    }

    private void createDangerContent(Event event, VBox content) {
        Danger danger = (Danger) event.getEventType();
        ImageView logo = LogoManager.getLogo(danger.getName());
        Label logoLabel = new Label();
        if (logo != null) {
            logoLabel.setGraphic(new ImageView(logo.getImage()));
        }
        Label quantityLabel = new Label("Number: " + danger.getQuantity());
        quantityLabel.setStyle("-fx-font-size: 14;");
        Label damageLabel = new Label("Damage: " + danger.getDamage());
        damageLabel.setStyle("-fx-font-size: 14;");
        Label experienceLabel = new Label("XP: " + danger.getExperience());
        experienceLabel.setStyle("-fx-font-size: 14;");
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("file:lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        damageLabel.setFont(gameFont);
        content.getChildren().addAll(logoLabel, quantityLabel, damageLabel, experienceLabel);
    }

    private void configureAlertButtons(Alert alert, Event event) {
        boolean isMonster = event.getEventType() instanceof Danger;
        String okButtonText = isMonster ? "Fight" : "Collect";
        String cancelButtonText = isMonster ? "Run" : "Leave";
        ButtonType deleteButtonType = new ButtonType(okButtonText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButtonText, ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);
    }

    private void setAlertCloseCallback(Alert alert, Event event) {
        alert.setOnCloseRequest(closeEvent -> {
            handleAlertClose(event);
        });
    }

    private void handleAlertClose(Event event) {
        ImageView logoView = LogoManager.getLogo(event.getEventType().getName());
        if (logoView != null && !event.isHasAlreadyVisited()) {
            ImageView newImageView = createNewImageView(event, logoView);
            updateEventAndView(event, newImageView);
        }
        isAlertDisplayed = false;
    }

    private ImageView createNewImageView(Event event, ImageView logoView) {
        ImageView newImageView = new ImageView(logoView.getImage());
        double imageWidth = newImageView.getImage().getWidth();
        double imageHeight = newImageView.getImage().getHeight();
        double newX;
        double newY;
        switch (event.getEventType().getName()) {
            case "Wood" -> {
                newX = event.getX() - imageWidth / 2.5;
                newY = event.getY() - imageHeight / 1.5;
            }
            case "Gold" -> {
                newImageView.setFitHeight(48);
                newImageView.setFitWidth(125);
                newX = event.getX() - imageWidth / 15;
                newY = event.getY() - imageHeight / 8;
            }
            default -> {
                newX = event.getX() - imageWidth / 5;
                newY = event.getY() - imageHeight / 2;
            }
        }
        newImageView.setX(newX);
        newImageView.setY(newY);
        gamePane.getChildren().remove(event.imageView);
        gamePane.getChildren().add(newImageView);
        return newImageView;
    }

    private void updateEventAndView(Event event, ImageView newImageView) {
        event.setImageView(newImageView);
        eventList.remove(event);
        eventList.add(event);
        event.setHasAlreadyVisited(true);
        player.toFront();
    }

    private void handleUserResponse(Optional<ButtonType> result, Event event) {
        result.ifPresent(buttonType -> {
            if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                handleDeleteButton(event);
            }
        });
    }

    private void handleDeleteButton(Event event) {
        if (event.getEventType() instanceof Danger) {
            handleMonsterFight((Danger) event.getEventType());
        }
        gamePane.getChildren().remove(event.getImageView());
        eventList.remove(event);
    }

    private void handleMonsterFight(Danger danger) {
        double playerXp = character.getExperience();
        double monsterXp = danger.getExperience();
        character.setExperience(playerXp + monsterXp);
    }

    public double getPlayerX() {
        return playerX;
    }

    public void setPlayerX(double playerX) {
        this.playerX = playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public void setPlayerY(double playerY) {
        this.playerY = playerY;
    }

    public void setPlayerPosition(double x, double y) {
        setPlayerX(x);
        setPlayerY(y);
    }
}
