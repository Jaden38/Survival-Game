package Game;

import Event.Event;
import Event.EventType;
import Item.Tool;
import Game.HUD;
import Player.Character;
import Item.Danger;
import Item.DataLists;
import Item.Resource;
import Map.MapGenerator;
import Player.Player;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    public static final int WINDOW_DIMENSION_WIDTH = 1080;
    public static final int WINDOW_DIMENSION_HEIGHT = 720;
    private final int WORLD_DIMENSION_WIDTH = 2560;
    private final int WORLD_DIMENSION_HEIGHT = 2048;
    private final int PERLIN_NOISE_SEED = 100;
    private final double EVENTS_COEFFICIENT = 0.00003;
    public Pane gamePane;
    private List<Event> eventList = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        gamePane = new Pane();
        // Add gamePane to the root
        root.getChildren().add(gamePane);
        Scene scene = new Scene(root, WINDOW_DIMENSION_WIDTH, WINDOW_DIMENSION_HEIGHT);
        // Create the character and HUD
        Character character = new Character("Player", 100, 50, 100);
        HUD hud = new HUD(character, scene);
//        hud.setPrefWidth(200); // Adjust width as needed
//        hud.setTranslateX(10); // Adjust X position as needed
//        hud.setTranslateY(10); // Adjust Y position as needed


        // Add HUD to the root, positioned at the top-left corner
        root.getChildren().add(hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);

        // Generate the world image
        MapGenerator world = new MapGenerator("file:resources/images.jpg",
                WORLD_DIMENSION_WIDTH, WORLD_DIMENSION_HEIGHT, PERLIN_NOISE_SEED);
        Image worldImage = world.generateWorld();
        ImageView worldImageView = new ImageView(worldImage);

        // Add the world image to the gamePane
        gamePane.getChildren().add(worldImageView);

        // Generate events
        Image eventImage = new Image("file:resources/mark.gif");
        generateEvents(gamePane, eventImage, EVENTS_COEFFICIENT);

        // Set up the scene with the root node

        Player player = new Player(gamePane, eventList, primaryStage, character);

        hud.setRoot(root);

        double playerStartX = WORLD_DIMENSION_WIDTH / 2;
        double playerStartY = WORLD_DIMENSION_HEIGHT / 2;
        player.getSpriteView().setX(playerStartX);
        player.getSpriteView().setY(playerStartY);
        primaryStage.setTitle("Alcia");
        primaryStage.setScene(scene);
        primaryStage.show();

        gamePane.requestFocus(); // Important for capturing keyboard events

        // Add listeners to update camera position when the scene is resized
        scene.widthProperty().addListener((observable, oldWidth, newWidth) -> player.updateCameraPosition());
        scene.heightProperty().addListener((observable, oldHeight, newHeight) -> player.updateCameraPosition());

        // Add listeners to handle stage size changes when going fullscreen
        primaryStage.widthProperty().addListener((observable, oldWidth, newWidth) -> player.updateCameraPosition());
        primaryStage.heightProperty().addListener((observable, oldHeight, newHeight) -> player.updateCameraPosition());

        hud.isRestartingProperty.addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                // Clean events
                cleanEvents();
                // Generate events again
                generateEvents(gamePane, eventImage, EVENTS_COEFFICIENT);
                // Reset the isRestartingProperty to false
                hud.isRestartingProperty.set(false);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void generateEvents(Pane gamePane, Image eventImage, double eventPercentage) {
        Random random = new Random();
        boolean isResource;
        // Calculate the number of events based on the eventPercentage
        int nEvents = (int) (WORLD_DIMENSION_WIDTH * WORLD_DIMENSION_HEIGHT * eventPercentage);


        // Define the coordinate limits
        double xmin = 250 + 0.1 * eventImage.getWidth();
        double ymin = 154 + 0.1 * eventImage.getHeight();
        double xmax = 5887 - 0.1 * eventImage.getWidth(); // Adjusted for event image width
        double ymax = 5792 - 0.1 * eventImage.getHeight(); // Adjusted for event image height

        for (int i = 0; i < nEvents; i++) {
            isResource = random.nextBoolean();
            // Generate random coordinates within the defined limits
            double x = xmin + random.nextDouble() * (xmax - xmin);
            double y = ymin + random.nextDouble() * (ymax - ymin);

            ImageView gifView = new ImageView(eventImage);

            // Set the position of the GIF image
            gifView.setX(x);
            gifView.setY(y);
            // Set the size of the GIF image
            double gifWidth = 100; // Adjust width as needed
            double gifHeight = 100; // Adjust height as needed
            gifView.setFitWidth(gifWidth);
            gifView.setFitHeight(gifHeight);

            if (isResource) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(0.5); // Adjust hue
                gifView.setEffect(colorAdjust);
            }
            String eventTypeTitle = isResource ? "Resource" : "Monster";
            EventType eventType = isResource ?
                    new Resource(DataLists.resourceNames.get(random.nextInt(DataLists.resourceNames.size())),
                            DataLists.resourceQuantities.get(random.nextInt(DataLists.resourceQuantities.size())))
                    : new Danger(DataLists.dangerNames.get(random.nextInt(DataLists.dangerNames.size())),
                    DataLists.dangerDescriptions.get(random.nextInt(DataLists.dangerDescriptions.size())),
                    DataLists.monsterQuantities.get(random.nextInt(DataLists.monsterQuantities.size())),
                    DataLists.dangerDamages.get(random.nextInt(DataLists.dangerDamages.size())));
            Event event = new Event(x, y, eventTypeTitle, gifView, eventType);

            eventList.add(event);
            gamePane.getChildren().add(gifView);
        }

    }
    public void cleanEvents() {
        // Remove all event-related ImageViews from the game pane
        for (Event event : eventList) {
            gamePane.getChildren().remove(event.getImageView());
        }
        // Clear the event list
        eventList.clear();
    }


}