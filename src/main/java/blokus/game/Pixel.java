package blokus.game;

import blokus.utils.ShapeType;
import blokus.utils.Vector2;

import java.io.File;

public class Pixel {

    private final static File shapePixelURL = new File("src/main/resources/Images/ShapePixel.png");

    private Vector2 position;
    private final ShapeType type;

    Pixel(Vector2 position, ShapeType type) {
        this.position = position;
        this.type = type;
    }
    public void SetPosition(Vector2 v) {
        position = v;
    }

    /**
     * Fonction pour retourner la position
     * @return La position du pixel
     */
    public Vector2 GetPosition() {
        return position;
    }
    public ShapeType GetType() {
        return type;
    }
}
