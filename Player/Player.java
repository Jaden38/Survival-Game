package Player;

import Event.Event;
import Event.EventHandler;
import Game.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Game.Main.WINDOW_DIMENSION_HEIGHT;
import static Game.Main.WINDOW_DIMENSION_WIDTH;

public class Player {
    private Pane gamePane; // Reference to the gamePane for camera movement

    private final double SPEED_COEFFICIENT = 10;
    private final int spriteWidth = 64;
    private final int spriteHeight = 64;

    private Boolean isMoving = false;
    private int lastOrientation = 0;

    private final String FRONT_MOVEMENT = "file:resources/CharacterSprite/Front MovementInverted.png";
    private final String BACK_MOVEMENT = "file:resources/CharacterSprite/Back MovementInverted.png";
    private final String SIDE_MOVEMENT = "file:resources/CharacterSprite/Side MovementInverted.png";
    private final String FRONT_ATTACK = "file:resources/CharacterSprite/Front ConsecutiveSlashInverted.png";
    private final String SIDE_ATTACK = "file:resources/CharacterSprite/Side ConsecutiveSlashInverted.png";
    private final String BACK_ATTACK = "file:resources/CharacterSprite/Back ConsecutiveSlashInverted.png";
    private String currentSpritesheet;

    private boolean isAttacking = false;
    private int currentFrame = 0; // Keep track of the current frame
    private int numRows = 2; // Number of rows in your spritesheet
    private int numCols = 6; // Number of columns in your spritesheet
    private Image spritesheet;
    private ImageView spriteView;
    private final Set<KeyCode> keysPressed = new HashSet<>();

    private EventHandler eventHandler; // Single instance of EventHandler
    private List<Event> eventList;
    private Stage primaryStage;

    public Player(Pane gamePane, List<Event> eventList, Stage primaryStage) {
        this.gamePane = gamePane;
        this.eventList = eventList;
        this.primaryStage = primaryStage;
        setupPlayer();
        gamePane.getChildren().add(spriteView);

        // Initialize the EventHandler
        eventHandler = new EventHandler(eventList, gamePane);

        gamePane.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
        gamePane.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
        gamePane.setOnKeyPressed(e -> {
            keysPressed.add(e.getCode());

            // Check if the space bar is pressed to initiate the attack
            if (e.getCode() == KeyCode.SPACE) {
                startAttack();
            }
        });

        gamePane.setOnKeyReleased(e -> {
            keysPressed.remove(e.getCode());

            // Check if the space bar is released to stop the attack
            if (e.getCode() == KeyCode.SPACE) {
                stopAttack();
            }
        });
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
                updateCameraPosition();
            }
        }.start();
    }

    private void setupPlayer() {

        spritesheet = new Image(FRONT_MOVEMENT);

        int scale = 3;
        int scaledWidth = (int) (spritesheet.getWidth() * scale);
        int scaledHeight = (int) (spritesheet.getHeight() * scale);

        WritableImage scaledImage = setCharacterSize(scale, scaledWidth, scaledHeight);

        spriteView = new ImageView(scaledImage);
        spriteView.setViewport(new Rectangle2D(0, 0, spriteWidth * scale, spriteHeight * scale));

        spriteView.setSmooth(false);

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            currentFrame = (currentFrame + 1) % (numRows * numCols); // Cycle through all frames
            int frameX = (currentFrame % numCols) * spriteWidth * scale; // Calculate the frame's x position
            int frameY;
            if (!isMoving) {
                frameY = 0;
            } else {
                frameY = spriteHeight * scale;
            }

            spriteView.setViewport(new Rectangle2D(frameX, frameY, spriteWidth * scale, spriteHeight * scale));
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void move() {
        if(EventHandler.isAlertDisplayed){
            keysPressed.clear();
            return;
        }

        double dx = 0;
        double dy = 0;

        boolean wasMoving = isMoving;
        int previousOrientation = lastOrientation;

        // Movement logic
        if (keysPressed.contains(KeyCode.UP)) {
            dy -= SPEED_COEFFICIENT;
            lastOrientation = 0; // Up
        }
        if (keysPressed.contains(KeyCode.DOWN)) {
            dy += SPEED_COEFFICIENT;
            lastOrientation = 2; // Down
        }
        if (keysPressed.contains(KeyCode.LEFT)) {
            dx -= SPEED_COEFFICIENT;
            lastOrientation = -1; // Left
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            dx += SPEED_COEFFICIENT;
            lastOrientation = 1; // Right
        }

        // Normalize the movement vector if diagonal movement is detected
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length * SPEED_COEFFICIENT;
            dy = dy / length * SPEED_COEFFICIENT;
        }

        isMoving = dx != 0 || dy != 0;

        // Calculate new player position
        double newX = spriteView.getX() + dx;
        double newY = spriteView.getY() + dy;

        // Enforce boundary limits
        double xmin = 200;
        double ymin = 154;
        double xmax = 5792;
        double ymax = 5792;

        // Clamp the new position within the limits
        newX = Math.max(xmin, Math.min(xmax, newX));
        newY = Math.max(ymin, Math.min(ymax, newY));

        // Update player's position
        spriteView.setX(newX);
        spriteView.setY(newY);

        // Change sprite sheet if necessary
        if (wasMoving != isMoving || previousOrientation != lastOrientation) {
            updateSpriteSheet();
        }

        // Update the EventHandler's player position
        eventHandler.setPlayerPosition(newX, newY);

        // Handle events
        eventHandler.handleEvent();

    }

    public void updateCameraPosition() {
        double xStartRangeMax;
        double xEndRangeMax;
        double yStartRangeMax;
        double yEndRangeMax;

        if (primaryStage.getWidth() >1100 && primaryStage.getHeight() > 760) {
            // Adjust max values for full-screen mode
            xStartRangeMax = 950;
            xEndRangeMax = 5100;
            yStartRangeMax = 500;
            yEndRangeMax = 5500;
        } else {
            // Default max values for non-full-screen mode
            xStartRangeMax = 750;
            xEndRangeMax = 5320;
            yStartRangeMax = 500;
            yEndRangeMax = 5500;
        }

        double offsetX = gamePane.getWidth() / 2 - spriteView.getX() - spriteWidth;
        double offsetY = gamePane.getHeight() / 2 - spriteView.getY() - spriteHeight;

        if (spriteView.getX() > xStartRangeMax && spriteView.getX() < xEndRangeMax) {
            gamePane.setTranslateX(offsetX);
        }

        if (spriteView.getY() > yStartRangeMax && spriteView.getY() < yEndRangeMax) {
            gamePane.setTranslateY(offsetY);
        }
    }

    private void updateSpriteSheet() {
        switch (lastOrientation) { // Left
            case -1, 1 ->  // Right
                    changeSpriteSheet(SIDE_MOVEMENT);
            case 0 ->  // Up
                    changeSpriteSheet(BACK_MOVEMENT);
            case 2 ->  // Down
                    changeSpriteSheet(FRONT_MOVEMENT);
        }
    }

    private void changeSpriteSheet(String imagePath) {
        if (!imagePath.equals(currentSpritesheet)) {
            currentSpritesheet = imagePath;
            spritesheet = new Image(imagePath);
        }
        setupSpriteView();
    }

    private void setupSpriteView() {
        int scale = 3;
        int scaledWidth = spriteWidth * scale * numCols;
        int scaledHeight = spriteHeight * scale * numRows;


        WritableImage scaledImage = setCharacterSize(scale, scaledWidth, scaledHeight);

        spriteView.setImage(scaledImage);
        spriteView.setViewport(new Rectangle2D(0, 0, spriteWidth * scale, spriteHeight * scale));
        spriteView.setSmooth(false);

        // Gère le flip horizontal pour la direction gauche
        if (lastOrientation == -1 && currentSpritesheet.equals(SIDE_MOVEMENT)) {
            spriteView.setScaleX(-1);
        } else {
            spriteView.setScaleX(1);
        }
    }

    private WritableImage setCharacterSize(int scale, int scaledWidth, int scaledHeight) {
        WritableImage scaledImage = new WritableImage(scaledWidth, scaledHeight);
        PixelWriter pixelWriter = scaledImage.getPixelWriter();

        for (int x = 0; x < scaledWidth; x++) {
            for (int y = 0; y < scaledHeight; y++) {
                int originalX = x / scale;
                int originalY = y / scale;
                Color pixelColor = spritesheet.getPixelReader().getColor(originalX, originalY);
                pixelWriter.setColor(x, y, pixelColor);
            }
        }
        return scaledImage;
    }


    public ImageView getSpriteView() {
        return spriteView;
    }

    private void startAttack() {
        if (!isAttacking) {
            isAttacking = true;
            updateSpriteSheetForAttack();
        }
    }

    private void stopAttack() {
        if (isAttacking) {
            isAttacking = false;
            updateSpriteSheet(); // Revert to the regular movement spritesheet
        }
    }

    private void updateSpriteSheetForAttack() {
        switch (lastOrientation) {
            case -1 -> { // Left
                changeSpriteSheet(SIDE_ATTACK);
                spriteView.setScaleX(-1); // Invert the sprite for left-facing attack
            }
            case 1 -> {  // Right
                changeSpriteSheet(SIDE_ATTACK);
                spriteView.setScaleX(1);
            }
            case 0 -> {  // Up
                changeSpriteSheet(BACK_ATTACK);
                spriteView.setScaleX(1);
            }
            case 2 -> {  // Down
                changeSpriteSheet(FRONT_ATTACK);
                spriteView.setScaleX(1);
            }
        }
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

}