package blokus.utils.message;

import blokus.utils.eventArgs.GameRankArgs;

/**
 * Message qui donne l'état de fin de partie Victoire/Defaite
 *
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class GameRankMessage extends Message {

    /**
     * Argument pour l'evenement
     */
    public GameRankArgs args;

    /**
     * Constructeur de la classe
     * @param args Argument pour l'évènement
     */
    public GameRankMessage(GameRankArgs args) {
        this.args = args;
    }

    /**
     * Fonction pour récupérer le type de message
     * @return Type de message
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_RANK;
    }
}
