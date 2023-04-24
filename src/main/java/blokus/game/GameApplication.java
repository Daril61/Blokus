package blokus.game;

import blokus.utils.FxmlType;
import blokus.utils.GameUtils;
import blokus.utils.NetworkIdentity;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameApplication extends Application {

    private NetworkIdentity identity;
    public NetworkIdentity GetIdentity() {
        return identity;
    }

    @FXML
    private TextField ipField;

    private final List<Socket> connections = new ArrayList<>();
    private boolean gameStart;
    private Stage stage;

    /**
     * Fonction lancée au démarrage de l'application
     * @param stage Fenêtre de l'application
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        System.out.println("test");
    }

    public void ConfigureNetwork(NetworkIdentity identity) throws IOException {
        this.identity = identity;

        GameUtils.ChangeScene(stage, FxmlType.Lobby);

        switch (this.identity) {
            case SERVER -> {
                // Si on peut ouvrir une connexion de type serveur
                try (ServerSocket serverSocket = new ServerSocket(6066)) {
                    // Tant que la partie n'est pas lancée
                    while (gameStart && connections.size() <= 4) {
                        connections.add(serverSocket.accept());
                    }
                }
            }
            case CLIENT -> {
                // Récupération de l'ip entré par le joueur
                String ip = ipField.getText();

                // Si on peut ouvrir une connexion avec le serveur
                try(Socket socket = new Socket(ip, 6066)) {
                    connections.add(socket);
                }
            }
        }
    }
}
