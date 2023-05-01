package blokus.game;

import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.scene.CacheHint;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;

public class Pixel {

    private final static File shapePixelURL = new File("src/main/resources/Images/ShapePixel.png");

    private final ShapeType type;
    private final Color color;
    private final ImageView img;

    Pixel(ShapeType type, Color color, Vector2 size) {
        this.type = type;
        this.color = color;

        img = new ImageView(new Image(shapePixelURL.toURI().toString()));
        img.setFitWidth(30);
        img.setFitHeight(30);

        // Création de l'effet de couleur
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setInput(new javafx.scene.effect.ColorInput(
                0, 0, size.GetX(), size.GetY(),
                color
        ));

        // Appliquer l'effet de couleur à l'ImageView
        img.setEffect(colorAdjust);
    }

    public ShapeType getType() {
        return type;
    }
    public Color getColor() {
        return color;
    }

    public ImageView getImg() {
        return img;
    }
}
