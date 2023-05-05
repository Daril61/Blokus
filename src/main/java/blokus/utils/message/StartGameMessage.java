package blokus.utils.message;

/**
 * Message que la partie commence
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class StartGameMessage extends Message {

    /**
     * Fonction pour récupérer le type de message
     * @return Type de message
     */
    @Override
    public MessageType getType() {
        return MessageType.START_GAME;
    }
}
