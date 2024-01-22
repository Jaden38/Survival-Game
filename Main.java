import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import Player.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane gamePane = new Pane();
        Player player = new Player(gamePane);

        Scene scene = new Scene(gamePane, 300, 300);
        primaryStage.setTitle("Déplacement fluide du Joueur");
        scene.setFill(Color.DARKGRAY);
        primaryStage.setScene(scene);
        primaryStage.show();

        gamePane.requestFocus(); // Important pour capturer les événements clavier
    }

    public static void main(String[] args) {
        launch(args);
    }
}
