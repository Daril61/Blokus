package blokus.game;

import blokus.utils.GameColor;
import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Classe qui permet de representer un pixel dans le jeu
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public class Pixel {
    /**
     * Type de la pièce à qui appartient le pixel
     */
    private final ShapeType type;
    /**
     * Couleur du pixel
     */
    private final GameColor color;
    /**
     * Image qui représente le pixel
     */
    private final ImageView img;

    /**
     * Constructeur de la classe
     *
     * @param type Type de la pièce
     * @param color Couleur du pixel
     * @param size Taille en pixel de l'image de pixel
     */
    Pixel(ShapeType type, GameColor color, Vector2 size) {
        this.type = type;
        this.color = color;

        img = new ImageView(new Image(color.getImageFile().toURI().toString()));
        img.setFitWidth(size.GetX());
        img.setFitHeight(size.GetY());
    }

    /**
     * Fonction qui retourne le type de la forme
     *
     * @return Le type de la pièce
     */
    public ShapeType getType() {
        return type;
    }
    /**
     * Fonction pour récupérer la couleur du pixel
     *
     * @return Couleur du pixel
     */
    public GameColor getColor() {
        return color;
    }
    /**
     * Fonction pour récupérer l'image du pixel
     *
     * @return L'image du pixel
     */
    public ImageView getImg() {
        return img;
    }
}
