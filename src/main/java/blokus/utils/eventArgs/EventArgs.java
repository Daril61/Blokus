package blokus.utils.eventArgs;

import blokus.utils.message.MessageType;

import java.io.Serializable;

public abstract class EventArgs implements Serializable {
    public abstract EventArgsType getType();
}
