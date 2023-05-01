package blokus.game;

import blokus.utils.GameColor;
import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.scene.CacheHint;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;

public class Pixel {
    private final ShapeType type;
    private final GameColor color;
    private final ImageView img;

    Pixel(ShapeType type, GameColor color, Vector2 size) {
        this.type = type;
        this.color = color;

        img = new ImageView(new Image(color.getImageFile().toURI().toString()));
        img.setFitWidth(size.GetX());
        img.setFitHeight(size.GetY());
    }

    public ShapeType getType() {
        return type;
    }
    public GameColor getColor() {
        return color;
    }
    public ImageView getImg() {
        return img;
    }
}
