package blokus.utils.message;

/**
 * Message qu'un joueur envoie quand il a fini de placer ses bateaux
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class ReadyMessage extends Message {

    @Override
    public MessageType getType() {
        return MessageType.READY;
    }
}
