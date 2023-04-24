package blokus.utils;

/**
 * Enumeration des types de scène disponible dans le jeu
 */
public enum FxmlType {
    MainMenu("MainMenu.fxml", "Menu principal"),
    Lobby("Lobby.fxml", "Menu multijoueur"),
    GameScene("GameScene.fxml", "Scene de jeu");

    /**
     * Nom du fichier FXML
     */
    private final String fxmlName;

    /**
     * Nom de la fenêtre
     */
    private final String windowName;

    FxmlType(String fxml, String windowName) {
        this.fxmlName = fxml;
        this.windowName = windowName;
    }

    /**
     * Fonction pour récupérer le nom du fichier FXML
     * @return Le nom du fichier
     */
    public String getFxmlName() {
        return fxmlName;
    }

    /**
     * Fonction pour récupérer le nom de la fenêtre
     * @return Le nom de la fenêtre
     */
    public String getWindowName() {
        return windowName;
    }
}
