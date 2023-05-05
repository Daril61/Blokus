package blokus.utils.message;

/**
 * Message que c'est au tour du joueur qui reçoit le message
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class TurnMessage extends Message {

    /**
     * Fonction pour récupérer le type de message
     * @return Type de message
     */
    @Override
    public MessageType getType() {
        return MessageType.TURN;
    }
}
