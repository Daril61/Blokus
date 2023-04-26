package blokus.game;

import blokus.utils.*;
import blokus.utils.eventArgs.EventArgsType;
import blokus.utils.eventArgs.JoinArgs;
import blokus.utils.eventArgs.JoinStruct;
import blokus.utils.message.Message;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameApplication extends Application {

    private static GameApplication instance;

    public static GameApplication getInstance() {
        return instance;
    }

    private static NetworkIdentity identity;
    public NetworkIdentity GetIdentity() {
        return identity;
    }

    public String ip;

    private final List<Socket> connections = new ArrayList<>();
    private final List<JoinStruct> joinStructs = new ArrayList<>();
    private List<Reader> readers = new ArrayList<>();

    public int getNumberOfPlayers() { return connections.size(); }

    private boolean gameStart;
    private Stage stage;

    public Event onClientConnectEvent;

    private ObjectOutputStream outputStream;

    /**
     * Fonction lancée au démarrage de l'application
     * @param stage Fenêtre de l'application
     */
    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        this.stage = stage;

        onClientConnectEvent = new Event(EventArgsType.JOIN);

        System.out.println("test");
        GameUtils.ChangeScene(stage, FxmlType.MainMenu);
    }

    public void ConfigureNetwork(NetworkIdentity identity) throws IOException {
        GameApplication.identity = identity;

        System.out.println(identity == NetworkIdentity.SERVER ? "NetworkIdentity.SERVER" : "NetworkIdentity.CLIENT");

        GameUtils.ChangeScene(stage, FxmlType.Lobby);

        switch (GameApplication.identity) {
            case SERVER -> {
                // Si on peut ouvrir une connexion de type serveur
                try (ServerSocket serverSocket = new ServerSocket(6066)) {
                    // Tant que la partie n'est pas lancée
                    while (!gameStart && connections.size() < 3) {
                        System.out.println("En attente de client : ");
                        // On ajoute le socket de connexion entre l'hébergeur de la partie et le joueur
                        Socket socket = serverSocket.accept();
                        System.out.println("Ajout du client : En cours");
                        connections.add(socket);

                        joinStructs.add(new JoinStruct("test", getNumberOfPlayers()));

                        // On envoie la liste des joueurs au nouveau joueur
                        JoinArgs joinArgs = new JoinArgs(EventArgsType.JOIN, joinStructs);

                        onClientConnectEvent.broadcast(joinArgs);
                        System.out.println("Ajout du client : Validé");
                    }
                }
            }
            case CLIENT -> {
                System.out.println("Tentative de connexion au serveur à l'ip : "+  ip);

                // Si on peut ouvrir une connexion avec le serveur
                Socket socket = new Socket(ip, 6066);

                System.out.println("Connexion réussie !");

                connections.add(socket);

                System.out.println("Fin des configurations");
            }
        }

        // Pour chaque connections on ouvre un lecteur de message
        for(Socket s : connections) {
            outputStream = new ObjectOutputStream(s.getOutputStream());

            System.out.println("Connection : nbConnections : " + connections.size());
            Reader lecteur = new Reader(s);
            lecteur.start();

            if(identity == NetworkIdentity.SERVER)
                readers.add(lecteur);
        }
    }

    /**
     * Fonction qui permet d'envoyer un message à un autre client
     *
     * @param message Message que l'on envoie
     */
    public static void SendMessage(Message message, Socket client) {
        try {
            ObjectOutputStream ecritureFlux = new ObjectOutputStream(client.getOutputStream());

            ecritureFlux.writeObject(message);

        } catch (IOException e) {

            System.out.println("Erreur : " + e.getMessage());

        }
    }

    @Override
    public void stop() throws IOException {
        // On arrete les systèmes d'écoutes
        for(Reader r : readers) {
            r.interrupt();
        }

        // Récupération de chaque connections
        for(Socket s : connections) {
            s.close();
        }
    }
}
