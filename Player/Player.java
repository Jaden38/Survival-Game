package Player;

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
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private Pane gamePane; // Reference to the gamePane for camera movement

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

    public Player(Pane gamePane) {
        this.gamePane = gamePane;
        setupPlayer();
        gamePane.getChildren().add(spriteView);

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
        double dx = 0;
        double dy = 0;
        double speed = 1;

        boolean wasMoving = isMoving;
        int previousOrientation = lastOrientation;

        // Movement logic
        if (keysPressed.contains(KeyCode.UP)) {
            dy -= speed;
            lastOrientation = 0; // Up
        }
        if (keysPressed.contains(KeyCode.DOWN)) {
            dy += speed;
            lastOrientation = 2; // Down
        }
        if (keysPressed.contains(KeyCode.LEFT)) {
            dx -= speed;
            lastOrientation = -1; // Left
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            dx += speed;
            lastOrientation = 1; // Right
        }

        // Normalize the movement vector if diagonal movement is detected
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length * speed;
            dy = dy / length * speed;
        }

        isMoving = dx != 0 || dy != 0;

        // Change sprite sheet if necessary
        if (wasMoving != isMoving || previousOrientation != lastOrientation) {
            updateSpriteSheet();
        }

        spriteView.setX(spriteView.getX() + dx);
        spriteView.setY(spriteView.getY() + dy);
    }

    private void updateCameraPosition() {
        double offsetX = gamePane.getWidth() / 2 - spriteView.getX() - spriteWidth / 2;
        double offsetY = gamePane.getHeight() / 2 - spriteView.getY() - spriteHeight / 2;

        gamePane.setTranslateX(offsetX);
        gamePane.setTranslateY(offsetY);
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

}
