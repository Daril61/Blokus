package blokus.utils.message;

/**
 * Message qui permet à un joueur d'abandonner
 *
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class AbandonedMessage extends Message {

    /**
     * Identifiant du joueur
     */
    public int pId;

    /**
     * Constructeur de la classe
     *
     * @param pId Identifiant du joueur
     */
    public AbandonedMessage(int pId) {
        this.pId = pId;
    }

    /**
     * Fonction pour récupérer le type de message
     * @return Type de message
     */
    @Override
    public MessageType getType() {
        return MessageType.ABANDONED;
    }
}
