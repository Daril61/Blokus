package blokus.utils.message;

/**
 * Message quand un joueur quitte la partie avec la liste des joueurs connectée
 *
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class LeaveEventMessage extends Message {

    public int pId;

    public LeaveEventMessage(int pId) {
        this.pId = pId;
    }

    @Override
    public MessageType getType() {
        return MessageType.LEAVE_GAME;
    }

}

