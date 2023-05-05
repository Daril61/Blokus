package blokus.utils.eventArgs;

/**
 * Argument de l'évènement pour le message TurnMessage
 *
 * @see blokus.utils.message.TurnMessage
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class TurnArgs extends EventArgs {

    /**
     * Fonction pour récupérer le type d'argument
     * @return Type d'argument
     */
    @Override
    public EventArgsType getType() {
        return EventArgsType.TURN_ARGS;
    }
}
