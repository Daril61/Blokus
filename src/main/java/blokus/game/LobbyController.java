package blokus.game;

import blokus.utils.NetworkIdentity;
import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.UpdateConnectedArgs;
import blokus.utils.PlayerStruct;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Classe qui s'occupe de la gestion de la scène de Lobby
 *
 * @author Romain Veydarier
 * @since 28/04/2023
 */
public class LobbyController implements Initializable {

    /**
     * Variable qui contient le chaque élément pour le joueur 1
     */
    @FXML
    private Group groupJ1;
    /**
     * Variable qui contient le status de connexion du joueur 1
     */
    @FXML
    private Text networkInfoJ1;

    /**
     * Variable qui contient le chaque élément pour le joueur 2
     */
    @FXML
    private Group groupJ2;
    /**
     * Variable qui contient le status de connexion du joueur 2
     */
    @FXML
    private Text networkInfoJ2;

    /**
     * Variable qui contient le chaque élément pour le joueur 3
     */
    @FXML
    private Group groupJ3;
    /**
     * Variable qui contient le status de connexion du joueur 3
     */
    @FXML
    private Text networkInfoJ3;

    /**
     * Variable qui contient le chaque élément pour le joueur 4
     */
    @FXML
    private Group groupJ4;
    /**
     * Variable qui contient le status de connexion du joueur 4
     */
    @FXML
    private Text networkInfoJ4;

    /**
     * Bouton qui permet de lancer la partie
     */
    @FXML
    private Button startButton;

    /**
     * Fonction execute au lancement de l'application
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameApplication.getInstance().OnPlayersUpdateEvent.addListener(this::OnClientConnect);
        startButton.setVisible(false);
        System.out.println("Initialisation du controller du lobby");
    }

    /**
     * Fonction appelée quand il y a une modification de la liste des joueurs
     * @param args Liste des joueurs connectés à la partie
     */
    private void OnClientConnect(EventArgs args) {
        List<PlayerStruct> struct = ((UpdateConnectedArgs)args).players;

        String WAIT_PLAYER_TEXT = "En attente de joueurs";
        networkInfoJ2.setText(WAIT_PLAYER_TEXT);
        networkInfoJ3.setText(WAIT_PLAYER_TEXT);
        networkInfoJ4.setText(WAIT_PLAYER_TEXT);
        
        if(struct.size() >= 1) {
            networkInfoJ1.setText(struct.get(0).pName);
            if (struct.size() >= 2) {
                networkInfoJ2.setText(struct.get(1).pName);
                if (struct.size() >= 3) {
                    networkInfoJ3.setText(struct.get(2).pName);
                    if (struct.size() >= 4) {
                        networkInfoJ4.setText(struct.get(3).pName);
                    }
                }
            }
        }

        // S'il y a assez de joueur pour lancer la partie et que c'est le serveur alors on active le bouton
        if(struct.size() >= 4 && GameApplication.getInstance().getIdentity() == NetworkIdentity.SERVER) {
            startButton.setVisible(true);
        }

        System.out.println("OnClientConnect");
    }

    /**
     * Fonction appelée quand le bouton Start est appuyé
     * @throws IOException S'il y a une erreur lors du changement de scène
     */
    @FXML
    private void StartButton() throws IOException {
        GameApplication.getInstance().StartGame();
    }
}
