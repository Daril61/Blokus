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

    private static GameApplication instance;

    public static GameApplication getInstance() {
        return instance;
    }

    private NetworkIdentity identity;
    public NetworkIdentity GetIdentity() {
        return identity;
    }

    public String ip;

    private final List<Socket> connections = new ArrayList<>();
    private boolean gameStart;
    private Stage stage;

    /**
     * Fonction lancée au démarrage de l'application
     * @param stage Fenêtre de l'application
     */
    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        this.stage = stage;

        System.out.println("test");
        GameUtils.ChangeScene(stage, FxmlType.MainMenu);
    }

    public void ConfigureNetwork(NetworkIdentity identity) throws IOException {
        this.identity = identity;

        System.out.println(identity == NetworkIdentity.SERVER ? "NetworkIdentity.SERVER" : "NetworkIdentity.CLIENT");

        GameUtils.ChangeScene(stage, FxmlType.Lobby);

        switch (this.identity) {
            case SERVER -> {
                // Si on peut ouvrir une connexion de type serveur
                try (ServerSocket serverSocket = new ServerSocket(6066)) {
                    // Tant que la partie n'est pas lancée
                    while (!gameStart && connections.size() <= 4) {
                        connections.add(serverSocket.accept());
                    }
                }
            }
            case CLIENT -> {
                // Si on peut ouvrir une connexion avec le serveur
                try(Socket socket = new Socket(ip, 6066)) {
                    connections.add(socket);
                }
            }
        }

    }
}
