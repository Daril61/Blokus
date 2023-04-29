package blokus.utils.message;

import blokus.utils.eventArgs.UpdateConnectedArgs;

/**
 * Message quand un joueur se connecte ou se déconnecte
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class UpdateConnectedMessage extends Message {
    public UpdateConnectedArgs updateConnectedArgs;
    public int pId;

    public UpdateConnectedMessage(UpdateConnectedArgs args, int pId) {
        this.updateConnectedArgs = args;
        this.pId = pId;
    }

    @Override
    public MessageType getType() {
        return MessageType.UPDATE_CONNECTED;
    }
}
