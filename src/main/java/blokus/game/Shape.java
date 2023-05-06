package blokus.game;

import blokus.utils.ShapeType;
import blokus.utils.Vector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui permet de representer une forme dans le jeu
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public class Shape implements Serializable {

    /**
     * Type de la pièce
     */
    private final ShapeType type;
    /**
     * Liste des coordonnées des pixels de la pièce
     */
    private final List<Vector2> coords = new ArrayList<>();

    /**
     * Constructeur de la classe
     *
     * @param type Type de la pièce
     */
    public Shape(ShapeType type) {
        this.type = type;
        if(type == ShapeType.NONE) return;

        for(int[] coord : type.getCoordsPixel()) {
            coords.add(new Vector2(coord[0], coord[1]));
        }
    }

    /**
     * Fonction pour récupérer le X d'un des pixels
     * @param index Identifiant du pixel
     * @return la position X
     */
    private int GetX(int index) {
        return coords.get(index).GetX();
    }
    /**
     * Fonction pour récupérer le Y d'un des pixels
     * @param index Identifiant du pixel
     * @return la position Y
     */
    private int GetY(int index) {
        return coords.get(index).GetY();
    }

    /**
     * Fonction pour faire une rotation vers la gauche
     *
     * @return la nouvelle pièce après rotation
     */
    public Shape LeftRotate() {
        Shape rValue = new Shape(type);

        for (int i = 0; i < coords.size(); i++) {
            rValue.coords.get(i).SetPosition(new Vector2(GetY(i), -GetX(i)));
        }

        return rValue;
    }

    /**
     * Fonction pour faire une rotation vers la droite
     *
     * @return la nouvelle pièce après rotation
     */
    public Shape RightRotate() {
        Shape rValue = new Shape(type);
        for (int i = 0; i < coords.size(); i++) {
            rValue.coords.get(i).SetPosition(new Vector2(-GetY(i), GetX(i)));
        }

        return rValue;
    }

    /**
     * Fonction pour récupérer la liste des coordonnées
     *
     * @return La liste des coordonnées
     */
    public List<Vector2> getCoords() {
        return coords;
    }

    /**
     * Fonction pour récupérer le type de la pièce
     *
     * @return La type de la pièce
     */
    public ShapeType getType() {
        return type;
    }
}
