package blokus.utils.eventArgs;

public abstract class EventArgs {
    public EventArgsType type;

    EventArgs(EventArgsType type) {
        this.type = type;
    }
}
