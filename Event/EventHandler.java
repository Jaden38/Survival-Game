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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private StackPane root;

    // Constructor

    public EventHandler(StackPane root, List<Event> eventList, Pane gamePane, Character character, ImageView player) {
        this.root = root;
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

        if (!character.isDead) {
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
                        if (currentTime - lastEventTime >= EVENT_COOLDOWN && !isAlertDisplayed) {
                            // Show a message window containing the event information
                            showEventMessage(event);
                            lastEventTime = currentTime; // Update the timestamp
                        }
                    }
                    return; // Exit the loop after encountering an event
                }
            }

        }
    }

    private void showEventMessage(Event event) {
        isAlertDisplayed = true;
        VBox eventPane = createContent(event);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        boolean isMonster = event.getEventType() instanceof Danger;

        // Define button text and colors based on event type
        String acceptButtonText = isMonster ? "Fight" : "Collect";
        String denyButtonText = isMonster ? "Run" : "Leave";

        // Create buttons with appropriate text
        Button acceptButton = new Button(acceptButtonText);
        Button denyButton = new Button(denyButtonText);

        // Load the font for the event message
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        // Apply the font to the buttons
        acceptButton.setFont(gameFont);
        denyButton.setFont(gameFont);

        // Apply button styles
        acceptButton.setStyle("-fx-text-fill:white;\n" +
                "-fx-font-size: 20px;\n" +
                "-fx-background-color: linear-gradient(#999999 0%, #000000 50%, #999999 100%);\n" +
                "-fx-padding: 5 30 5 30;");

        denyButton.setStyle("-fx-text-fill:white;\n" +
                "-fx-font-size: 20px;\n" +
                "-fx-background-color: linear-gradient(#999999 0%, #000000 50%, #999999 100%);\n" +
                "-fx-padding: 5 30 5 30;");

        // Add actions to buttons
        denyButton.setOnAction(eventKey -> {
            handleClose(event);
            root.getChildren().remove(eventPane);
            gamePane.requestFocus();
        });
        acceptButton.setOnAction(eventKey -> {
            handleAcceptButton(event);
            root.getChildren().remove(eventPane);
            gamePane.requestFocus();
        });

        buttonBox.getChildren().addAll(denyButton, acceptButton);
        eventPane.getChildren().add(buttonBox);

        // Apply the font to the labels in the event message
        eventPane.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setFont(gameFont);
            }
        });

        // Add the event pane to the root StackPane
        root.getChildren().add(eventPane);
    }

    private VBox createContent(Event event) {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: rgb(0,0,0,0.8);"); // Example color: light gray

        Label eventTypeName = new Label(event.getEventType().getName());
        eventTypeName.setStyle("-fx-font-size: 36px;");
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        eventTypeName.setFont(gameFont);

        if (event.getEventType() instanceof Resource) {
            createResourceContent(event, content, eventTypeName);
        } else if (event.getEventType() instanceof Danger) {
            createDangerContent(event, content, eventTypeName);
        }
        return content;
    }

    private void createResourceContent(Event event, VBox content, Label eventTypeName) {
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        eventTypeName.setTextFill(Color.GREEN);
        Resource resource = (Resource) event.getEventType();
        ImageView logo = LogoManager.getLogo(resource.getName());
        Label logoLabel = new Label();
        if (logo != null) {
            logoLabel.setGraphic(new ImageView(logo.getImage()));
        }
        Label quantityLabel = new Label("Quantity: " + resource.getQuantity());
        quantityLabel.setStyle("-fx-font-size: 14;");
        quantityLabel.setFont(gameFont);
        quantityLabel.setTextFill(Color.WHITE);
        content.getChildren().addAll(eventTypeName, logoLabel, quantityLabel);
    }

    private void createDangerContent(Event event, VBox content, Label eventTypeName) {
        eventTypeName.setTextFill(Color.RED);
        Danger danger = (Danger) event.getEventType();
        ImageView logo = LogoManager.getLogo(danger.getName());
        Label logoLabel = new Label();
        if (logo != null) {
            logoLabel.setGraphic(new ImageView(logo.getImage()));
        }
        Label quantityLabel = new Label("Number: " + danger.getQuantity());
        quantityLabel.setStyle("-fx-font-size: 14;");
        quantityLabel.setTextFill(Color.WHITE);

        Label damageLabel = new Label("Damage: " + danger.getDamage());
        damageLabel.setStyle("-fx-font-size: 14;");
        damageLabel.setTextFill(Color.WHITE);

        Label experienceLabel = new Label("XP: " + danger.getExperience());
        experienceLabel.setStyle("-fx-font-size: 14;");
        experienceLabel.setTextFill(Color.WHITE);


        Font gameFont = Font.loadFont(getClass().getResourceAsStream("file:lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        quantityLabel.setFont(gameFont);
        damageLabel.setFont(gameFont);
        experienceLabel.setFont(gameFont);
        content.getChildren().addAll(eventTypeName, logoLabel, quantityLabel, damageLabel, experienceLabel);
    }

    private void handleClose(Event event) {
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


    private void handleAcceptButton(Event event) {
        if (event.getEventType() instanceof Danger) {
            handleMonsterFight((Danger) event.getEventType());
        }
        if (event.getEventType() instanceof Resource) {
            handleResourceCollection((Resource) event.getEventType());
        }
        gamePane.getChildren().remove(event.getImageView());
        eventList.remove(event);
        isAlertDisplayed = false;
    }

    private void handleMonsterFight(Danger danger) {
        character.decreaseHealth(danger.getDamage());
        double playerXp = character.getExperience();
        double monsterXp = danger.getExperience();
        character.setExperience(playerXp + monsterXp);
    }

    private void handleResourceCollection(Resource resource) {
        character.playerInventory.addResource(resource);
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
