package blokus.utils;

import java.io.Serializable;
import java.util.Objects;

public class Vector2 implements Serializable {

    private int x;
    private int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void SetPosition(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public int GetX() {
        return x;
    }
    public int GetY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
