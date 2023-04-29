package blokus.utils;

import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.EventArgsType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Event {

    private final Set<Consumer<EventArgs>> listeners = new HashSet<>();

    private final EventArgsType type;

    public Event(EventArgsType type) {
        this.type = type;
    }

    public void addListener(Consumer<EventArgs> listener) {
        listeners.add(listener);
    }

    public void broadcast(EventArgs args) {
        if(args.getType() != type) return;

        listeners.forEach(x -> x.accept(args));
    }
}
