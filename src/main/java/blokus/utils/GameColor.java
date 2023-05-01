package blokus.utils;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.Serializable;

public enum GameColor implements Serializable {
    BLEU(Color.rgb(17, 0, 255, 0.8), new Vector2(19, 0), "src/main/resources/Images/ShapePixel_BLEU.png"),
    JAUNE(Color.rgb(251, 255, 0, 0.8), new Vector2(19, 19), "src/main/resources/Images/ShapePixel_JAUNE.png"),
    ROUGE(Color.rgb(255, 31, 31, 0.8), new Vector2(0, 19), "src/main/resources/Images/ShapePixel_ROUGE.png"),
    VERT(Color.rgb(13, 255, 0, 0.8), new Vector2(0, 0), "src/main/resources/Images/ShapePixel_VERT.png");

    private final Color color;
    private final Vector2 gridStartPos;
    private final File imageURL;

    GameColor(Color color, Vector2 gridStartPos, String imgURL) {
        this.color = color;
        this.gridStartPos = gridStartPos;
        this.imageURL = new File(imgURL);
    }

    public Color getColor() {
        return color;
    }
    public Vector2 getGridStartPos() {
        return gridStartPos;
    }
    public File getImageFile() {
        return imageURL;
    }
}
