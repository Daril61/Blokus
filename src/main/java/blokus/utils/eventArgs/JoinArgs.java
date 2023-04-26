package blokus.utils.eventArgs;

public class JoinArgs extends EventArgs {

    public String pName = "Default Player name";
    public int pId = 0;

    public JoinArgs(EventArgsType type, String pName, int pId) {
        super(type);

        this.pName = pName;
        this.pId = pId;
    }
}
