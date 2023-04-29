package blokus.utils.message;

public class StartGameMessage extends Message {

    @Override
    public MessageType getType() {
        return MessageType.START_GAME;
    }
}
