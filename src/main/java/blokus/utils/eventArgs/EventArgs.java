package blokus.utils.eventArgs;

import java.io.Serializable;

public abstract class EventArgs implements Serializable {
    public EventArgsType type;

    EventArgs(EventArgsType type) {
        this.type = type;
    }
}
