package blokus.utils;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.Serializable;

/**
 * Enumération des couleurs possibles dans le jeu
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public enum GameColor implements Serializable {
    BLEU(Color.rgb(17, 0, 255, 0.8), new Vector2(19, 0), "src/main/resources/Images/ShapePixel_BLEU.png"),
    JAUNE(Color.rgb(251, 255, 0, 0.8), new Vector2(19, 19), "src/main/resources/Images/ShapePixel_JAUNE.png"),
    ROUGE(Color.rgb(255, 31, 31, 0.8), new Vector2(0, 19), "src/main/resources/Images/ShapePixel_ROUGE.png"),
    VERT(Color.rgb(13, 255, 0, 0.8), new Vector2(0, 0), "src/main/resources/Images/ShapePixel_VERT.png");

    /**
     * Une couleur
     */
    private final Color color;
    /**
     * La position de départ de la pièce
     */
    private final Vector2 gridStartPos;
    /**
     * L'url vers l'image du pixel
     */
    private final File imageURL;

    /**
     * Constructeur de la classe
     *
     * @param color Couleur du pixel
     * @param gridStartPos La position de départ
     * @param imgURL URL vers l'image du pixel
     */
    GameColor(Color color, Vector2 gridStartPos, String imgURL) {
        this.color = color;
        this.gridStartPos = gridStartPos;
        this.imageURL = new File(imgURL);
    }

    /**
     * Fonction pour récupérer la couleur du pixel
     * @return une couleur
     */
    public Color getColor() {
        return color;
    }

    /**
     * Fonction pour récupérer la position de départ de la couleur
     * @return Une position
     */
    public Vector2 getGridStartPos() {
        return gridStartPos;
    }

    /**
     * Fonction qui retourne l'url de l'image du pixel
     * @return Un fichier pour récupérer l'url
     */
    public File getImageFile() {
        return imageURL;
    }
}
