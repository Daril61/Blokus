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

    Pixel(ShapeType type, Color color) {
        this.type = type;
        this.color = color;

        img = new ImageView(new Image(shapePixelURL.toURI().toString()));
        img.setFitWidth(30);
        img.setFitHeight(30);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(color.getHue() / 360.0);

        // Appliquer l'effet ColorAdjust à l'ImageView
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
