package blokus.utils.message;

public class TurnMessage extends Message {

    @Override
    public MessageType getType() {
        return MessageType.TURN;
    }
}
