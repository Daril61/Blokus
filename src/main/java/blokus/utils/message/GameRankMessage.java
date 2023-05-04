package blokus.utils.message;

import blokus.utils.eventArgs.GameRankArgs;

public class GameRankMessage extends Message {

    public GameRankArgs args;

    public GameRankMessage(GameRankArgs args) {
        this.args = args;
    }

    @Override
    public MessageType getType() {
        return MessageType.GAME_RANK;
    }
}
