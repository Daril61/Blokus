package blokus.utils;

import java.io.Serializable;

public class PlayerStruct implements Serializable {
    public String pName;
    public int pId;

    public PlayerStruct(String pName, int pId) {
        this.pName = pName;
        this.pId = pId;
    }
}
