package blokus.utils.eventArgs;

import java.io.Serializable;

public class JoinStruct implements Serializable {
    public String pName = "Default Player name";
    public int pId = 0;

    public JoinStruct(String pName, int pId) {
        this.pName = pName;
        this.pId = pId;
    }
}
