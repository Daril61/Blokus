package blokus.game;


import blokus.utils.ShapeType;
import blokus.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Shape {

    /**
     * Liste de pixels de la forme
     */
    private final List<Pixel> pixels = new ArrayList<>();

    private final ShapeType type;
    private final List<Vector2> coords = new ArrayList<>();

    Shape(ShapeType type) {
        this.type = type;
        for(int[] coord : type.getCoordsPixel()) {
            coords.add(new Vector2(coord[0], coord[1]));
        }
    }

    private void SetPosition(Vector2 position) {
        for(Pixel p : pixels) {
            p.SetPosition(new Vector2(0, 0));
        }
    }

    private void SetPosition(int index, Vector2 pos) {
        coords.get(index).SetPosition(pos);
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

    public List<Vector2> GetCoords() {
        return coords;
    }
}
