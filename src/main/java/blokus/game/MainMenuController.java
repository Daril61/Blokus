package blokus.game;

import blokus.utils.NetworkIdentity;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe qui s'occupe de la gestion de la scène du menu principal
 *
 * @author Romain Veydarier
 * @since 28/04/2023
 */
public class MainMenuController implements Initializable {

    /**
     * Groupe qui contient chaque élément du menu principal
     */
    @FXML
    private Group mainMenu;
    /**
     * Fonction qui contient chaque élément du menu de networking
     */
    @FXML
    private Group networkMenu;

    /**
     * Champs de texte pour l'adresse ip
     */
    @FXML
    private TextField ipField;

    /**
     * Player pour lancer les sons de clique sur les boutons
     */
    private MediaPlayer mediaPlayer;

    /**
     * Fonction execute au lancement de l'application
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::SetupMenu);
        Media media = new Media(new File("src/main/resources/sounds/buttonClicked.wav").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
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
        ButtonClicked();

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
        ButtonClicked();

        Platform.exit();
    }

    /**
     * Fonction pour héberger une partie
     */
    @FXML
    private void HostButton() throws IOException {
        ButtonClicked();

        GameApplication.getInstance().ConfigureNetwork(NetworkIdentity.SERVER);
    }

    /**
     * Fonction pour rejoindre une partie
     */
    @FXML
    private void JoinButton() throws IOException {
        ButtonClicked();

        // Récupération et sauvegarde de la valeur de l'InputField sur le GameApplication
        GameApplication.getInstance().ip = ipField.getText();

        GameApplication.getInstance().ConfigureNetwork(NetworkIdentity.CLIENT);
    }

    /**
     * Fonction pour revenir en arrière dans les menus
     */
    @FXML
    private void ReturnButton() {
        ButtonClicked();
        SetupMenu();
    }

    /**
     * Fonction appelée quand on clique sur un bouton pour lancer un son
     */
    private void ButtonClicked() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }
        // Remettre le son au début et le jouer
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }
}
