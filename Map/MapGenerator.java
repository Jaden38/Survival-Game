package Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import mikera.math.PerlinNoise;

public class MapGenerator {
    private final int tileWidth = 8;
    private final int tileHeight = 8;
    private final int worldWidth;
    private final int worldHeight;
    private final Image tilesImage;
    private final PerlinNoise perlinNoise;

    // Coordinates for specific tiles on the sprite sheet
    private final int sandTileX = 6 * tileWidth; // x6
    private final int sandTileY = 2 * tileHeight; // y2
    private final int grassTileX = 2 * tileWidth; // x2
    private final int grassTileY = 1 * tileHeight; // y1

    public MapGenerator(String tilesImagePath, int worldWidth, int worldHeight, long seed ) {
        this.worldWidth = worldWidth / tileWidth;
        this.worldHeight = worldHeight / tileHeight;
        this.tilesImage = new Image(tilesImagePath);
        if (this.tilesImage.isError()) {
            throw new IllegalArgumentException("Image could not be loaded: " + tilesImagePath);
        }
        this.perlinNoise = new PerlinNoise((int) seed);
    }

    public Image generateWorld() {
        // Ajuster la taille de l'image
        WritableImage scaledWorldImage = new WritableImage(worldWidth * tileWidth * 3, worldHeight * tileHeight * 3);
        PixelWriter writer = scaledWorldImage.getPixelWriter();

        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                double noiseValue = perlinNoise.noise2((float) (x * 0.1), (float) (y * 0.1)); // scale factor may need to be adjusted
                Image tile = chooseTile(noiseValue);

                // Placer la tuile sur l'image mise à l'échelle
                placeScaledTile(writer, tile, x, y);
            }
        }

        return scaledWorldImage;
    }

    private Image chooseTile(double noiseValue) {
        // Check if the image has been loaded correctly
        if (tilesImage.getPixelReader() == null) {
            throw new IllegalStateException("PixelReader not available for the image.");
        }

//        System.out.println(noiseValue);

        // Choose the appropriate tile based on the noise value
        if (noiseValue > 0.06) { // Threshold value for choosing sand
            return new WritableImage(tilesImage.getPixelReader(), sandTileX, sandTileY, tileWidth, tileHeight);
        } else {
            return new WritableImage(tilesImage.getPixelReader(), grassTileX, grassTileY, tileWidth, tileHeight);
        }
    }

    private void placeScaledTile(PixelWriter writer, Image tile, int x, int y) {
        int scale = 3;
        for (int i = 0; i < tileWidth; i++) {
            for (int j = 0; j < tileHeight; j++) {
                Color color = tile.getPixelReader().getColor(i, j);
                // Dessiner chaque pixel de la tuile à une taille plus grande
                for (int dx = 0; dx < scale; dx++) {
                    for (int dy = 0; dy < scale; dy++) {
                        writer.setColor(x * tileWidth * scale + i * scale + dx, y * tileHeight * scale + j * scale + dy, color);
                    }
                }
            }
        }
    }


}