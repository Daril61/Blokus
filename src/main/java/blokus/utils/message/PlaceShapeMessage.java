package blokus.utils.message;

import blokus.utils.eventArgs.ShapePlacedArgs;

/**
 * Message quand une pièce est placée sur la grille
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class PlaceShapeMessage extends Message{

    /**
     * Argument de l'évènement
     */
    public ShapePlacedArgs args;

    /**
     * Contructeur de la classe
     *
     * @param args Argument de l'évènement
     */
    public PlaceShapeMessage(ShapePlacedArgs args) {
        this.args = args;
    }

    /**
     * Fonction pour récupérer le type de message
     * @return Type de message
     */
    @Override
    public MessageType getType() {
        return MessageType.PLACE_SHAPE;
    }
}
