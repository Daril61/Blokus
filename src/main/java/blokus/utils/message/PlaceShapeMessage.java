package blokus.utils.message;

import blokus.utils.eventArgs.ShapePlacedArgs;

public class PlaceShapeMessage extends Message{

    public ShapePlacedArgs args;

    public PlaceShapeMessage(ShapePlacedArgs args) {
        this.args = args;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLACE_SHAPE;
    }
}
