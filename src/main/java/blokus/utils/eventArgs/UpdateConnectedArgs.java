package blokus.utils.eventArgs;

import blokus.utils.PlayerStruct;

import java.util.List;

/**
 * Argument de l'évènement pour le message UpdateConnectedMessage
 *
 * @see blokus.utils.message.UpdateConnectedMessage
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class UpdateConnectedArgs extends EventArgs {

    /**
     * Liste des joueurs dans l'ordre qui on rejoint
     */
    public List<PlayerStruct> players;

    /**
     * Constructeur de la classe
     *
     * @param players Liste de joueur
      */
    public UpdateConnectedArgs(List<PlayerStruct> players) {
        this.players = players;
    }

    /**
     * Fonction pour récupérer le type d'argument
     * @return Type d'argument
     */
    @Override
    public EventArgsType getType() {
        return EventArgsType.UPDATE_CONNECTED;
    }
}
