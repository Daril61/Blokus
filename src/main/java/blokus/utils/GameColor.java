package blokus.utils;

import javafx.scene.paint.Color;

public enum GameColor {
    ROUGE(Color.rgb(255, 31, 31)),
    VERT(Color.rgb(13, 255, 0)),
    BLEU(Color.rgb(17, 0, 255)),
    JAUNE(Color.rgb(251, 255, 0));

    private final Color color;

    GameColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
