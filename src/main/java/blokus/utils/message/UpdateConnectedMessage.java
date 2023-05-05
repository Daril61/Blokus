package blokus.utils.message;

import blokus.utils.eventArgs.UpdateConnectedArgs;

/**
 * Message quand un joueur se connecte ou se déconnecte
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class UpdateConnectedMessage extends Message {
    /**
     * Argument de l'évènement
     */
    public UpdateConnectedArgs updateConnectedArgs;
    /**
     * Identifiant du joueur
     */
    public int pId;

    /**
     * Constructeur de la classe
     *
     * @param args Argument de l'évènement
     * @param pId Identifiant du joueur
     */
    public UpdateConnectedMessage(UpdateConnectedArgs args, int pId) {
        this.updateConnectedArgs = args;
        this.pId = pId;
    }

    /**
     * Fonction pour récupérer le type de message
     * @return Type de message
     */
    @Override
    public MessageType getType() {
        return MessageType.UPDATE_CONNECTED;
    }
}
