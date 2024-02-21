package Item;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class LogoManager {
    private static final Map<String, ImageView> logoMap = new HashMap<>();

    static {
        // Load logo images and associate them with names
        logoMap.put("Gold", new ImageView(new Image("/resources/Logo/gold_logo.gif")));
//        logoMap.put("Iron", new ImageView(new Image("/resources/Logo/iron_logo.gif")));
        logoMap.put("Wood", new ImageView(new Image("/resources/Logo/wood_logo.gif")));
//        logoMap.put("Food", new ImageView(new Image("/resources/Logo/food_logo.gif")));
//        logoMap.put("Water", new ImageView(new Image("/resources/Logo/water_logo.gif")));
        // Add logos for danger names if needed
        logoMap.put("Dragon", new ImageView(new Image("/resources/Logo/dragon_logo.gif")));
        logoMap.put("Goblin", new ImageView(new Image("/resources/Logo/goblin_logo.gif")));
        logoMap.put("Gnoll", new ImageView(new Image("/resources/Logo/gnoll_logo.gif")));
        logoMap.put("Giant", new ImageView(new Image("/resources/Logo/giant_logo.gif")));
        logoMap.put("Dwarf", new ImageView(new Image("/resources/Logo/dwarf_logo.gif")));
        logoMap.put("Dreadnought", new ImageView(new Image("/resources/Logo/dreadnought_logo.gif")));
        logoMap.put("Hellhorse", new ImageView(new Image("/resources/Logo/hellhorse_logo.gif")));
        logoMap.put("Pheonix", new ImageView(new Image("/resources/Logo/pheonix_logo.gif")));

    }

    public static ImageView getLogo(String name) {
        return logoMap.get(name);
    }
}
