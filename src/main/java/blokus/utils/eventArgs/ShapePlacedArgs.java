package blokus.utils.eventArgs;

import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.scene.paint.Color;

public class ShapePlacedArgs extends EventArgs {

    public Vector2 position;
    public ShapeType type;
    public Color color;

    public int pId;

    public ShapePlacedArgs(Vector2 position, ShapeType type, Color color, int pId) {
        this.position = position;
        this.type = type;
        this.color = color;
        this.pId = pId;
    }

    @Override
    public EventArgsType getType() {
        return EventArgsType.SHAPE_PlACED;
    }
}
