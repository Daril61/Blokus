package blokus.game;


import blokus.utils.ShapeType;
import blokus.utils.Vector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shape implements Serializable {

    private final ShapeType type;
    private final List<Vector2> coords = new ArrayList<>();

    public Shape(ShapeType type) {
        this.type = type;
        if(type == ShapeType.NONE) return;

        for(int[] coord : type.getCoordsPixel()) {
            coords.add(new Vector2(coord[0], coord[1]));
        }
    }
    private int GetX(int index) {
        return coords.get(index).GetX();
    }
    private int GetY(int index) {
        return coords.get(index).GetY();
    }

    public Shape RightRotate() {
        Shape rValue = new Shape(type);

        for (int i = 0; i < coords.size(); i++) {
            rValue.coords.get(i).SetPosition(new Vector2(GetY(i), -GetX(i)));
        }

        return rValue;
    }

    public Shape LeftRotate() {
        Shape rValue = new Shape(type);
        for (int i = 0; i < coords.size(); i++) {
            rValue.coords.get(i).SetPosition(new Vector2(-GetY(i), GetX(i)));
        }

        return rValue;
    }

    public List<Vector2> getCoords() {
        return coords;
    }
    public ShapeType getType() {
        return type;
    }
}
