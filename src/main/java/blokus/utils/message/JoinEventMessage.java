package blokus.utils.message;

import blokus.utils.eventArgs.JoinArgs;

/**
 * Message qu'un joueur envoie quand il attaque l'autre joueur
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class JoinEventMessage extends Message {
    public JoinArgs joinArgs;

    public JoinEventMessage(JoinArgs args) {
        this.joinArgs = args;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN;
    }
}
