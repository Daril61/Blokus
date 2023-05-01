package blokus.utils;

import javafx.scene.paint.Color;

import java.io.Serializable;

public enum GameColor implements Serializable {
    BLEU(Color.rgb(17, 0, 255, 0.8), new Vector2(19, 0)),
    JAUNE(Color.rgb(251, 255, 0, 0.8), new Vector2(19, 19)),
    ROUGE(Color.rgb(255, 31, 31, 0.8), new Vector2(0, 19)),
    VERT(Color.rgb(13, 255, 0, 0.8), new Vector2(0, 0));

    private final Color color;
    private final Vector2 gridStartPos;

    GameColor(Color color, Vector2 gridStartPos) {
        this.color = color;
        this.gridStartPos = gridStartPos;
    }

    public Color getColor() {
        return color;
    }
    public Vector2 getGridStartPos() {
        return gridStartPos;
    }
}
