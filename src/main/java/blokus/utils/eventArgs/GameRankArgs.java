package blokus.utils.eventArgs;

/**
 * Argument de l'évènement pour le message GameRankMessage
 *
 * @see blokus.utils.message.GameRankMessage
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class GameRankArgs extends EventArgs {

    /**
     * Type de victoire
     */
    public int rank;

    /**
     * Constructeur de la classe
     *
     * @param rank Type de victoire
     */
    public GameRankArgs(int rank) {
        this.rank = rank;
    }

    /**
     * Fonction pour récupérer le type d'argument
     * @return Type d'argument
     */
    @Override
    public EventArgsType getType() {
        return EventArgsType.GAME_RANK;
    }
}
