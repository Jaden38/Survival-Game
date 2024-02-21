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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventHandler {

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
        // Set the flag to true since an alert window is being displayed
        isAlertDisplayed = true;

        // Create and show the event message on the JavaFX Application Thread
        Platform.runLater(() -> {
            boolean isMonster = false;
            Font gameFont = Font.loadFont(getClass().getResourceAsStream("file:lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
            // Create a custom alert window to display the event information
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Event Information");
            Label nameLabel = new Label(event.getEventType().getName());
            nameLabel.setFont(gameFont);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            alert.setHeaderText(nameLabel.getText());

            VBox content = new VBox();
            content.setAlignment(Pos.CENTER); // Center align content
            content.setSpacing(10); // Add spacing between labels
            content.setPadding(new Insets(10)); // Add padding around the VBox

            if (event.getEventType() instanceof Resource resource) {
                ImageView logo = LogoManager.getLogo(resource.getName());
                Label logoLabel = new Label();
                if (logo != null) {
                    logoLabel.setGraphic(new ImageView(logo.getImage())); // Create a new ImageView instance
                }
                Label quantityLabel = new Label("Quantity: " + resource.getQuantity());
                quantityLabel.setStyle("-fx-font-size: 14;");
                quantityLabel.setFont(gameFont);
                content.getChildren().addAll(logoLabel, quantityLabel);

            } else if (event.getEventType() instanceof Danger danger) {
                isMonster = true;

                ImageView logo = LogoManager.getLogo(danger.getName());
                Label logoLabel = new Label();
                if (logo != null) {
                    logoLabel.setGraphic(new ImageView(logo.getImage())); // Create a new ImageView instance
                }
                Label quantityLabel = new Label("Number: " + danger.getQuantity());
                quantityLabel.setStyle("-fx-font-size: 14;");

                Label damageLabel = new Label("Damage: " + danger.getDamage());
                damageLabel.setStyle("-fx-font-size: 14;");

                Label experienceLabel = new Label("XP: " + danger.getExperience());
                experienceLabel.setStyle("-fx-font-size: 14;");

                damageLabel.setFont(gameFont);
                content.getChildren().addAll(logoLabel, quantityLabel, damageLabel, experienceLabel);
            }

            // Set background color and padding for the VBox
            content.setStyle("-fx-background-color: #333333; -fx-border-color: #CCCCCC; -fx-border-width: 1px;");

            alert.getDialogPane().setContent(content);
            // Create buttons for deleting the event/GIF and for closing without deleting
            String okButtonText = isMonster ? "Fight" : "Collect";
            String cancelButtonText = isMonster ? "Run" : "Leave";
            ButtonType deleteButtonType = new ButtonType(okButtonText, ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType(cancelButtonText, ButtonBar.ButtonData.CANCEL_CLOSE);

            // Set the buttons
            alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);

            // Apply custom CSS to style the alert window
            String cssPath = "file:resources/alert.css";
            alert.getDialogPane().getStylesheets().add(cssPath);

            // Set a callback for when the alert window is closed
            alert.setOnCloseRequest(closeEvent -> {
                ImageView logoView = LogoManager.getLogo(event.getEventType().getName());
                // Set the flag back to false since the alert window is closed
                if (logoView != null && !event.isHasAlreadyVisited()) {
                    // Create a new ImageView instance
                    ImageView newImageView = new ImageView(logoView.getImage());

                    // Calculate the position to center the ImageView
                    double imageWidth = newImageView.getImage().getWidth();
                    double imageHeight = newImageView.getImage().getHeight();
                    double newX;
                    double newY;
                    System.out.println(event.getEventType().getName());
                    switch (event.getEventType().getName()) {
                        case "Wood" -> {
                            newX = event.getX() - imageWidth / 2.5; // Center on x-axis
                            newY = event.getY() - imageHeight / 1.5; // Center on y-axis
                        }
                        case "Gold" -> {
                            newImageView.setFitHeight(48);
                            newImageView.setFitWidth(125);
                            newX = event.getX() - imageWidth / 10; // Center on x-axis
                            newY = event.getY() - imageHeight / 3; // Center on y-axis
                        }
                        default -> {
                            newX = event.getX() - imageWidth / 5; // Center on x-axis
                            newY = event.getY() - imageHeight / 2; // Center on y-axis
                        }
                    }

                    // Set the position of the new logoView
                    newImageView.setX(newX);
                    newImageView.setY(newY);
                    gamePane.getChildren().remove(event.imageView);
                    // Add the new logoView to gamePane
                    gamePane.getChildren().add(newImageView);

                    // Update the event's imageView
                    event.setImageView(newImageView);

                    eventList.remove(event);
                    eventList.add(event);
                    event.setHasAlreadyVisited(true);
                    player.toFront();
                }
                isAlertDisplayed = false;
            });


            // Show the alert window and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();

            // Handle the user's response
            result.ifPresent(buttonType -> {
                if (buttonType == deleteButtonType) {
                    if (event.getEventType() instanceof Danger danger) {
                        double playerXp = character.getExperience();
                        double monsterXp = danger.getExperience();
                        character.setExperience(playerXp + monsterXp);
                    }

                    // Remove the event's GIF from the gamePane
                    gamePane.getChildren().remove(event.getImageView());
                    // Remove the event from the event list
                    eventList.remove(event);
                }
            });
        });
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
