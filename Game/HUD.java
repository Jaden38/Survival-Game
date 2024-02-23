package Game;

import Interface.CharacterObserver;
import Player.Character;
import Player.Player;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HUD extends BorderPane implements CharacterObserver {

    private int MAX_EXPERIENCE = 300;
    private Character character;
    private Scene scene;
    private GameTime gameTime;
    private ProgressBar xpBar;

    private Label levelLabelPrefix;
    private Label nameLabelPrefix;
    private Label healthLabelPrefix;
    private Label hungerLabelPrefix;
    private Label thirstLabelPrefix;
    private Label levelLabelValue;

    private Label nameLabelValue;
    private Label healthLabelValue;
    private Label hungerLabelValue;
    private Label thirstLabelValue;
    private Label timeLabel;
    private ScheduledExecutorService scheduler;
    private StackPane root;
   private AtomicInteger counter;
    public BooleanProperty isRestartingProperty = new SimpleBooleanProperty(false);

    public HUD(Character character, Scene scene) {
        this.character = character;
        this.scene = scene;
        this.gameTime = new GameTime();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Game/bar.css")).toExternalForm());
        initializeLabels();
        setLabelsStyle();
        setLayout();
        startTimerAsync();
        character.addObserver(this);

    }

    private void initializeLabels() {
        levelLabelPrefix = new Label();
        nameLabelPrefix = new Label();
        healthLabelPrefix = new Label();
        hungerLabelPrefix = new Label();
        thirstLabelPrefix = new Label();
        timeLabel = new Label();

        levelLabelValue = new Label();
        nameLabelValue = new Label();
        healthLabelValue = new Label();
        hungerLabelValue = new Label();
        thirstLabelValue = new Label();

        xpBar = new ProgressBar();
        xpBar.setProgress(character.getExperience() / MAX_EXPERIENCE); // Set the progress based on character's experience
        updateLabels();
    }

    private void updateLabels() {
        levelLabelPrefix.setText("Lv: ");
        nameLabelPrefix.setText("Name: ");
        healthLabelPrefix.setText("Health: ");
        hungerLabelPrefix.setText("Hunger: ");
        thirstLabelPrefix.setText("Thirst: ");
        timeLabel.setText(gameTime.getTimeString());

        levelLabelValue.setText(String.valueOf(character.getLevel()));
        nameLabelValue.setText(character.getName());
        healthLabelValue.setText(String.valueOf(character.getHealth()));
        hungerLabelValue.setText(String.valueOf(character.getHunger()));
        thirstLabelValue.setText(String.valueOf(character.getThirst()));
        xpBar.setProgress(character.getExperience() / MAX_EXPERIENCE);
        handleXPBar();


    }

    private void setLabelsStyle() {
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        // Apply the font to labels
        levelLabelPrefix.setFont(gameFont);
        nameLabelPrefix.setFont(gameFont);
        healthLabelPrefix.setFont(gameFont);
        hungerLabelPrefix.setFont(gameFont);
        thirstLabelPrefix.setFont(gameFont);

        levelLabelValue.setFont(gameFont);
        nameLabelValue.setFont(gameFont);
        healthLabelValue.setFont(gameFont);
        hungerLabelValue.setFont(gameFont);
        thirstLabelValue.setFont(gameFont);
        timeLabel.setFont(gameFont);


        // Set font color to white
        levelLabelPrefix.setTextFill(Color.WHITE);
        nameLabelPrefix.setTextFill(Color.WHITE);
        healthLabelPrefix.setTextFill(Color.WHITE);
        hungerLabelPrefix.setTextFill(Color.WHITE);
        thirstLabelPrefix.setTextFill(Color.WHITE);
        timeLabel.setTextFill(Color.WHITE);


        levelLabelValue.setTextFill(Color.WHITE);
        nameLabelValue.setTextFill(Color.WHITE);
        if (Integer.parseInt(healthLabelValue.getText()) > 50) {
            healthLabelValue.setTextFill(Color.GREEN);
        } else {
            healthLabelValue.setTextFill(Color.RED);
        }
        if (Integer.parseInt(hungerLabelValue.getText()) > 50) {
            hungerLabelValue.setTextFill(Color.GREEN);
        } else {
            hungerLabelValue.setTextFill(Color.RED);
        }

        if (Integer.parseInt(thirstLabelValue.getText()) > 50) {
            thirstLabelValue.setTextFill(Color.GREEN);
        } else {
            thirstLabelValue.setTextFill(Color.RED);
        }


    }

    private void setLayout() {
        HBox levelBox = new HBox();
        levelBox.getChildren().addAll(levelLabelPrefix, levelLabelValue);

        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(nameLabelPrefix, nameLabelValue);

        HBox healthBox = new HBox();
        healthBox.getChildren().addAll(healthLabelPrefix, healthLabelValue);

        HBox hungerBox = new HBox();
        hungerBox.getChildren().addAll(hungerLabelPrefix, hungerLabelValue);

        HBox thirstBox = new HBox();
        thirstBox.getChildren().addAll(thirstLabelPrefix, thirstLabelValue);

        // Add spacing between the groups
        int groupSpacing = 20;

        HBox nameAndDataBox = new HBox(levelBox, new Pane(), nameBox, new Pane(), healthBox, new Pane(), hungerBox, new Pane(), thirstBox);
        nameAndDataBox.setPadding(new Insets(10));
        nameAndDataBox.setSpacing(groupSpacing);

        HBox timerBox = new HBox(timeLabel);
        timerBox.setPadding(new Insets(10));
        timeLabel.setStyle("-fx-text-alignment: right;");

        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        HBox topBox = new HBox(nameAndDataBox, filler, timerBox);
        topBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-border-color: white; -fx-border-width: 0 0 2 0;");
        topBox.setPadding(new Insets(10));
        topBox.setSpacing(groupSpacing);
        topBox.setAlignment(Pos.CENTER_LEFT);

        setTop(topBox);

        xpBar.getStyleClass().add("xp-bar");
        xpBar.getStyleClass().add("progress-bar");
        xpBar.prefWidthProperty().bind(scene.widthProperty());

        // Add the XP bar to the layout
        HBox xpBarBox = new HBox(xpBar);
        xpBarBox.setAlignment(Pos.CENTER);
//        xpBarBox.setPadding(new Insets(10));

        xpBarBox.setMaxWidth(Double.MAX_VALUE);
        xpBarBox.setStyle("-fx-background-color: #333333;"); // Set background color

        setBottom(xpBarBox);

    }

    private void startTimerAsync() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        counter = new AtomicInteger(0);
        scheduler.scheduleAtFixedRate(() -> {
            // This runs in a separate thread asynchronously
            Platform.runLater(() -> {
                gameTime.updateTime();
                updateLabels();

                // Increment the counter
                counter.incrementAndGet();

                // Check if the counter reaches a threshold (e.g., every 1 hours)
                if (counter.get() == 60) {
                    character.decreaseHunger(1); // Decrease hunger by 1
                    character.decreaseThirst(1); // Decrease thirst by 1
                    updateLabels();

                    // Reset the counter
                    counter.set(0);
                }
            });
        }, 0, 300, TimeUnit.MILLISECONDS);
    }


    private void handleXPBar() {
        // Check if XP bar is full
        if (xpBar.getProgress() >= 1.0) {
            // Level up the character
            character.setLevel(character.getLevel() + 1);
            // Calculate additional XP beyond the maximum
            double additionalXP = character.getExperience() - MAX_EXPERIENCE;
            // Reset XP to the additional XP
            character.setExperience(additionalXP);
            // Update labels
            updateLabels();
        }
    }

    @Override
    public void onHealthZero() {
        character.setHealth(0);
        updateLabels();
        character.isDead = true;
        scheduler.shutdown();

        // Create a custom pane for displaying death information
        VBox deathPane = new VBox();
        deathPane.setAlignment(Pos.CENTER);
        deathPane.setSpacing(10);
        deathPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");

        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setStyle("-fx-font-size: 36px;");

        Label deathLabel = new Label("You survived for " + gameTime.getTimeString());
        deathLabel.setTextFill(Color.WHITE);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER); // Aligning the buttonBox to the center
        buttonBox.setSpacing(10); // Adding spacing between buttons
        buttonBox.setPadding(new Insets(10, 0, 0, 0)); // Adding padding to move the buttonBox a bit lower
        Button quitButton = new Button("Quit Game");
        Button restartButton = new Button("Restart Game");
        quitButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px;");
        restartButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px;");

        buttonBox.getChildren().addAll(quitButton, restartButton);

        // Add actions to buttons
        quitButton.setOnAction(event -> Platform.exit());
        restartButton.setOnAction(event -> {
            isRestartingProperty.set(true);
            character.isDead=false;
            restart();
            root.getChildren().remove(deathPane);
        });

        deathPane.getChildren().addAll(gameOverLabel, deathLabel, buttonBox);

        // Add the death pane to the root StackPane
        root.getChildren().add(deathPane);
    }



    @Override
    public void onThirstZero() {
        // Implement behavior when thirst reaches zero
    }

    @Override
    public void onHungerZero() {
        // Implement behavior when hunger reaches zero
    }
    void restart() {
        cleanup();
    }
    void cleanup() {
        character.setHealth(100);
        character.setHunger(50);
        character.setThirst(100);
        character.setLevel(1);
        character.setExperience(0);
        character.playerInventory.cleanInventory();

        updateLabels();

        // Shutdown the existing scheduler if it's running
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }

        // Start a new timer
        gameTime=new GameTime();
        startTimerAsync();
    }

    public void setRoot(StackPane root) {
        this.root = root;
    }

}
