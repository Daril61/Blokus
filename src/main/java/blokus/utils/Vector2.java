package blokus.utils;

public class Vector2 {

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

}
