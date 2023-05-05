package blokus.utils.eventArgs;

import blokus.game.Shape;
import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.scene.paint.Color;

/**
 * Argument de l'évènement pour le message PlaceShapeMessage
 *
 * @see blokus.utils.message.PlaceShapeMessage
 * @since 28/04/2023
 * @author Romain Veydarier
 */
public class ShapePlacedArgs extends EventArgs {

    /**
     * Position de la pièce
     */
    public Vector2 position;
    /**
     * Pièce à poser
     */
    public Shape shape;

    /**
     * Identifiant du joueur
     */
    public int pId;

    /**
     * Constructeur de la classe
     *
     * @param position Position du joueur
     * @param shape Pièce à poser
     * @param pId Identifiant du joueur
     */
    public ShapePlacedArgs(Vector2 position, Shape shape, int pId) {
        this.position = position;
        this.shape = shape;
        this.pId = pId;
    }

    /**
     * Fonction pour récupérer le type d'argument
     * @return Type d'argument
     */
    @Override
    public EventArgsType getType() {
        return EventArgsType.SHAPE_PlACED;
    }
}
