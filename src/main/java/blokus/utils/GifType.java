package blokus.utils;

import java.io.File;

/**
 * Enumeration des types de gif disponible dans le jeu
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public enum GifType {
    Victory("src/main/resources/gif/Victory.gif"),
    Defeat("src/main/resources/gif/Defeat.gif");

    /**
     * Variable qui contient le lien vers le gif
     */
    private final File gifURL;

    /**
     * Constructeur de la classe
     * @param gifURL URL du gif
     */
    GifType(String gifURL) {
        this.gifURL = new File(gifURL);
    }

    /**
     * Fonction pour récupérer le lien vers le gif
     *
     * @return Le lien vers le fichier gif
     */
    public File getGifFile() {
        return gifURL;
    }
}
