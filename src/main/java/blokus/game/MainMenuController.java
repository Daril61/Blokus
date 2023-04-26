package blokus.game;

import blokus.utils.NetworkIdentity;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Group mainMenu;
    @FXML
    private Group networkMenu;

    @FXML
    private TextField ipField;

    /**
     * Fonction execute au lancement de l'application
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::SetupMenu);
    }

    /**
     * Fonction pour configurer les différents menus, afin d'activer uniquement le menu principal
     */
    private void SetupMenu() {
        // Désactivation de tous les menus
        networkMenu.setVisible(false);

        // Activation du menu principal
        mainMenu.setVisible(true);
    }

    /**
     * Fonction pour ouvrir le menu de multijoueur
     */
    @FXML
    private void PlayButton() {
        // Désactivation du menu principal
        mainMenu.setVisible(false);

        // Activation du menu multijoueur
        networkMenu.setVisible(true);
    }

    /**
     * Fonction qui permet de quitter l'application
     */
    @FXML
    private void QuitButton() {
        Platform.exit();
    }

    /**
     * Fonction pour héberger une partie
     */
    @FXML
    private void HostButton() throws IOException {
        GameApplication.getInstance().ConfigureNetwork(NetworkIdentity.SERVER);
    }

    /**
     * Fonction pour rejoindre une partie
     */
    @FXML
    private void JoinButton() throws IOException {
        // Récupération et sauvegarde de la valeur de l'InputField sur le GameApplication
        GameApplication.getInstance().ip = ipField.getText();

        GameApplication.getInstance().ConfigureNetwork(NetworkIdentity.CLIENT);
    }

    /**
     * Fonction pour revenir en arrière dans les menus
     */
    @FXML
    private void ReturnButton() {
        SetupMenu();
    }
}
