package blokus.utils.eventArgs;

import java.io.Serializable;

/**
 * Classe abstraite permettant de faire des arguments personnalisés
 */
public abstract class EventArgs implements Serializable {
    /**
     * Fonction pour récupérer le type d'argument
     * @return Type d'argument
     */
    public abstract EventArgsType getType();
}
