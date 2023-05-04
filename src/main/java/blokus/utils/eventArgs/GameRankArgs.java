package blokus.utils.eventArgs;

public class GameRankArgs extends EventArgs {

    public int rank;

    public GameRankArgs(int rank) {
        this.rank = rank;
    }

    @Override
    public EventArgsType getType() {
        return EventArgsType.GAME_RANK;
    }
}
