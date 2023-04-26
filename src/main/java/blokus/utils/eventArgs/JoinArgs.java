package blokus.utils.eventArgs;

import java.util.ArrayList;
import java.util.List;

public class JoinArgs extends EventArgs {

    public List<JoinStruct> joins = new ArrayList<>();

    public JoinArgs(EventArgsType type, List<JoinStruct> joins) {
        super(type);

        this.joins = joins;
    }
}
