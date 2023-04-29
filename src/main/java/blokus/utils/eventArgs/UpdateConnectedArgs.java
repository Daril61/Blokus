package blokus.utils.eventArgs;

import blokus.utils.PlayerStruct;

import java.util.List;

public class UpdateConnectedArgs extends EventArgs {

    public List<PlayerStruct> players;

    public UpdateConnectedArgs(List<PlayerStruct> players) {
        this.players = players;
    }

    @Override
    public EventArgsType getType() {
        return EventArgsType.UPDATE_CONNECTED;
    }
}
