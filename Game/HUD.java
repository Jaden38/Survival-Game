package Game;

import Player.Character;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HUD extends BorderPane {
    private Character character;
    private GameTime gameTime;

    private Label nameLabelPrefix;
    private Label healthLabelPrefix;
    private Label hungerLabelPrefix;
    private Label thirstLabelPrefix;
    private Label nameLabelValue;
    private Label healthLabelValue;
    private Label hungerLabelValue;
    private Label thirstLabelValue;
    private Label timeLabel;
    private ScheduledExecutorService scheduler;


    public HUD(Character character) {
        this.character = character;
        this.gameTime = new GameTime();
        initializeLabels();
        setLabelsStyle();
        setLayout();
        startTimerAsync();

    }

    private void initializeLabels() {
        nameLabelPrefix = new Label();
        healthLabelPrefix = new Label();
        hungerLabelPrefix = new Label();
        thirstLabelPrefix = new Label();
        timeLabel = new Label();

        nameLabelValue = new Label();
        healthLabelValue = new Label();
        hungerLabelValue = new Label();
        thirstLabelValue = new Label();
        updateLabels();
    }

    private void updateLabels() {
        nameLabelPrefix.setText("Name: ");
        healthLabelPrefix.setText("Health: ");
        hungerLabelPrefix.setText("Hunger: ");
        thirstLabelPrefix.setText("Thirst: ");
        timeLabel.setText(gameTime.getTimeString());

        nameLabelValue.setText(character.getName());
        healthLabelValue.setText(String.valueOf(character.getHealth()));
        hungerLabelValue.setText(String.valueOf(character.getHunger()));
        thirstLabelValue.setText(String.valueOf(character.getThirst()));

    }

    private void setLabelsStyle() {
        Font gameFont = Font.loadFont(getClass().getResourceAsStream("/lib/Fonts/SuperLegendBoy-4w8Y.ttf"), 16);
        // Apply the font to labels
        nameLabelPrefix.setFont(gameFont);
        healthLabelPrefix.setFont(gameFont);
        hungerLabelPrefix.setFont(gameFont);
        thirstLabelPrefix.setFont(gameFont);
        nameLabelValue.setFont(gameFont);
        healthLabelValue.setFont(gameFont);
        hungerLabelValue.setFont(gameFont);
        thirstLabelValue.setFont(gameFont);
        timeLabel.setFont(gameFont);


        // Set font color to white
        nameLabelPrefix.setTextFill(Color.WHITE);
        healthLabelPrefix.setTextFill(Color.WHITE);
        hungerLabelPrefix.setTextFill(Color.WHITE);
        thirstLabelPrefix.setTextFill(Color.WHITE);
        timeLabel.setTextFill(Color.WHITE);


        nameLabelValue.setTextFill(Color.WHITE);
        if(Integer.parseInt(healthLabelValue.getText()) > 50){
            healthLabelValue.setTextFill(Color.GREEN);
        }
        else{
            healthLabelValue.setTextFill(Color.RED);
        }
        if(Integer.parseInt(hungerLabelValue.getText()) > 50){
            hungerLabelValue.setTextFill(Color.GREEN);
        }
        else{
            hungerLabelValue.setTextFill(Color.RED);
        }

        if(Integer.parseInt(thirstLabelValue.getText()) > 50){
            thirstLabelValue.setTextFill(Color.GREEN);
        }
        else{
            thirstLabelValue.setTextFill(Color.RED);
        }


    }

    private void setLayout() {
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

        HBox nameAndDataBox = new HBox(nameBox, new Pane(), healthBox, new Pane(), hungerBox, new Pane(), thirstBox);
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

    }

    private void startTimerAsync() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            // This runs in a separate thread asynchronously
            Platform.runLater(() -> {
                gameTime.updateTime();
                updateLabels();
            });
        }, 0, 300, TimeUnit.MILLISECONDS); // Update timer every 60 seconds
    }
}
