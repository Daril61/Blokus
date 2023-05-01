package blokus.utils.eventArgs;

import blokus.game.Shape;
import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.scene.paint.Color;

public class ShapePlacedArgs extends EventArgs {

    public Vector2 position;
    public Shape shape;

    public int pId;

    public ShapePlacedArgs(Vector2 position, Shape shape, int pId) {
        this.position = position;
        this.shape = shape;
        this.pId = pId;
    }

    @Override
    public EventArgsType getType() {
        return EventArgsType.SHAPE_PlACED;
    }
}
