package blokus.utils.message;

import java.io.Serializable;

/**
 * Classe abstraite permettant de faire des messages personnalisés
 */
public abstract class Message implements Serializable {

    /**
     * Fonction pour récupérer le type du message
     * @return Un type de message
     */
    public abstract MessageType getType();

}
