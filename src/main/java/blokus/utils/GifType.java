package blokus.utils;

import java.io.File;

/**
 * Enumeration des types de gif disponible dans le jeu
 */
public enum GifType {
    Victory("src/main/resources/gif/Victory.gif"),
    Defeat("src/main/resources/gif/Defeat.gif");

    /**
     * Variable qui contient le lien vers le gif
     */
    private final File gifURL;

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
