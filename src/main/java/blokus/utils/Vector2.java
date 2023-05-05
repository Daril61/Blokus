package blokus.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe qui contient 2 coordonnées X et Y
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public class Vector2 implements Serializable {

    /**
     * Position X
     */
    private int x;
    /**
     * Position Y
     */
    private int y;

    /**
     * Constructeur de la classe
     * @param x Position X
     * @param y Position Y
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Fonction qui permet de changer les positions x et y du vecteur par celle d'un autre vecteur
     * @param v un Vecteur2D
     */
    public void SetPosition(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Fonction pour récupérer la position X
     * @return La position X
     */
    public int GetX() {
        return x;
    }
    /**
     * Fonction pour récupérer la position Y
     * @return La position Y
     */
    public int GetY() {
        return y;
    }

    /**
     * Fonction qui permet de comparer 2 vecteurs avec l'opérateur ==
     * @param o un autre Vecteur
     * @return Vrai si les 2 positions x et y sont les mêmes que sur l'autre vecteur
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
