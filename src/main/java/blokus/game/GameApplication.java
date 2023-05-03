package blokus.game;

import blokus.utils.*;
import blokus.utils.eventArgs.EventArgsType;
import blokus.utils.eventArgs.UpdateConnectedArgs;
import blokus.utils.PlayerStruct;
import blokus.utils.message.StartGameMessage;
import blokus.utils.message.UpdateConnectedMessage;
import blokus.utils.message.LeaveEventMessage;
import blokus.utils.message.Message;
import javafx.application.Application;
import javafx.scene.SubScene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui s'occupe de la gestion de l'application
 *
 * @author Romain Veydarier
 * @since 28/04/2023
 */
public class GameApplication extends Application {

    private static GameApplication instance;
    public static GameApplication getInstance() {
        return instance;
    }

    private Stage stage;
    private boolean gameStart;

    private NetworkIdentity identity;
    public NetworkIdentity getIdentity() {
        return identity;
    }

    public int pId;

    public String ip;
    public boolean myTurn = false;

    private final List<Socket> connections = new ArrayList<>();
    private final List<ObjectOutputStream> outputStreams = new ArrayList<>();
    public List<PlayerStruct> playerStructs = new ArrayList<>();
    private final List<Reader> readers = new ArrayList<>();
    public int getNumberOfPlayers() {
        return connections.size();
    }

    public Event OnPlayersUpdateEvent;
    public Event OnShapePlacedEvent;
    public Event WhenMyTurn;

    private ServerSocket serverSocket;

    /**
     * Fonction lancée au démarrage de l'application
     * @param stage Fenêtre de l'application
     */
    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        this.stage = stage;

        OnPlayersUpdateEvent = new Event(EventArgsType.UPDATE_CONNECTED);
        OnShapePlacedEvent = new Event(EventArgsType.SHAPE_PlACED);
        WhenMyTurn = new Event(null);

        System.out.println("test");
        GameUtils.ChangeScene(stage, FxmlType.MainMenu);
    }

    /**
     * Fonction qui permet de configurer l'application
     * @param identity Variable qui permet d'identifier un futur SERVEUR ou CLIENT
     * @throws IOException Si une erreur se produit lors de la création des flux.
     */
    public void ConfigureNetwork(NetworkIdentity identity) throws IOException {
        this.identity = identity;

        System.out.println(identity == NetworkIdentity.SERVER ? "NetworkIdentity.SERVER" : "NetworkIdentity.CLIENT");

        GameUtils.ChangeScene(stage, FxmlType.Lobby);

        switch (identity) {
            case SERVER -> {
                serverSocket = new ServerSocket(6066);
                // Ajout du client dans les listes des clients qui on rejoint
                playerStructs.add(new PlayerStruct("test0", 0));

                // Tant qu'il manque des joueurs
                while (connections.size() < 3) {
                    WaitPlayerToJoin();
                }

                System.out.println("Fin de la configuration du serveur");
            }
            case CLIENT -> {
                System.out.println("Tentative de connexion au serveur à l'ip : "+  ip);

                // Si on peut ouvrir une connexion avec le serveur
                Socket socket = new Socket(ip, 6066);

                System.out.println("Connexion réussie !");

                connections.add(socket);
                InitializeStream(socket);

                System.out.println("Fin de la configuration du client");
            }
        }
    }

    /**
     * Fonction qui permet d'attendre et d'accepter une connexion d'un nouveau joueur
     * @throws IOException S'il y a un problème avec la connexion du nouveau joueur
     */
    private void WaitPlayerToJoin() throws IOException {
        // On ajoute le socket de connexion entre l'hébergeur de la partie et le joueur
        Socket socket = serverSocket.accept();

        connections.add(socket);
        InitializeStream(socket);
        int pId = getNumberOfPlayers();

        // Ajout des informations du nouveau joueur
        playerStructs.add(new PlayerStruct("test" + (pId), pId));

        // Créez une copie de la liste de joinStructs
        UpdateConnectedArgs updateConnectedArgs = new UpdateConnectedArgs(new ArrayList<>(playerStructs));
        // On envoie la liste des joueurs aux joueurs
        int i = 1;
        for(ObjectOutputStream stream : outputStreams) {
            SendMessage(new UpdateConnectedMessage(updateConnectedArgs, i), stream);
            i++;
        }
        OnPlayersUpdateEvent.broadcast(updateConnectedArgs);
    }

    /**
     * Fonction qui permet de lancer la partie
     * @throws IOException S'il y a une erreur lors du changement de scène
     */
    public void StartGame() throws IOException {
        // Si la partie est déjà lancée alors on retourne
        if(gameStart) return;

        gameStart = true;
        SendMessageToAll(new StartGameMessage());
        OnStartGame();
    }

    /**
     * Fonction appelée quand la partie est lancée
     * @throws IOException S'il y a une erreur lors du changement de scène
     */
    public void OnStartGame() throws IOException {
        GameUtils.ChangeScene(stage, FxmlType.GameScene);
    }

    /**
     * Fonction qui permet de récupérer le flux pour écrire, et de lancer le système d'écoute pour un socket
     * @param s Un socket
     * @throws IOException S'il y a une erreur lors de la récupérer du flux d'écriture, ou sur le lancement du système d'écoute
     */
    private void InitializeStream(Socket s) throws IOException {
        // Récupération du lien pour envoyer des messages
        outputStreams.add(new ObjectOutputStream(s.getOutputStream()));

        // Récupération du système de lecture des messages
        Reader lecteur = new Reader(s);
        lecteur.start();
        readers.add(lecteur);
    }

    /**
     * Fonction appelée lors de la déconnexion d'un joueur uniquement sur le serveur
     * @param pId L'identifiant du joueur qui se déconnecte
     * @throws IOException S'il y a un problème avec la gestion des sockets du joueur
     */
    public void OnClientDisconnect(int pId) throws IOException {
        if(identity == NetworkIdentity.CLIENT) return;

        connections.remove(pId);
        outputStreams.remove(pId);
        playerStructs.remove(pId);
        readers.remove(pId);

        // Si la partie est lancée alors on le retire simplement de la liste
        if(gameStart) return;

        // Créez une copie de la liste de joinStructs
        UpdateConnectedArgs updateConnectedArgs = new UpdateConnectedArgs(new ArrayList<>(playerStructs));
        // On envoie la liste des joueurs aux joueurs
        int i = 1;
        for(ObjectOutputStream stream : outputStreams) {
            SendMessage(new UpdateConnectedMessage(updateConnectedArgs, i), stream);
            i++;
        }
        OnPlayersUpdateEvent.broadcast(updateConnectedArgs);

        // Si la partie n'est pas lancée alors on attend un nouveau joueur
        WaitPlayerToJoin();
    }

    /**
     * Fonction utilisé pour envoyer un message à l'hôte du serveur
     * @param message Message que l'on envoie
     */
    public void SendMessage(Message message) {
        // Si c'est le serveur qui envoie le message alors il envoie le serveur à chaque personne
        if(identity == NetworkIdentity.SERVER) {
            SendMessageToAll(message);
            return;
        }

        System.out.println("SendMessage");
        SendMessage(message, outputStreams.get(0));
    }

    /**
     * Fonction qui permet au serveur d'envoyer un message à un joueur spécifique
     *
     * @param message Message que l'on envoie
     */
    public void SendMessage(Message message, int pId) {
        if(identity == NetworkIdentity.CLIENT) return;

        SendMessage(message, outputStreams.get(0));
    }

    /**
     * Fonction qui permet au serveur d'envoyer un message à chaque client connecté
     */
    public void SendMessageToAll(Message message) {
        if(identity == NetworkIdentity.CLIENT) return;

        for(ObjectOutputStream stream : outputStreams) {
            SendMessage(message, stream);
        }
    }

    /**
     * Fonction qui permet d'envoyer un message à un flux d'envoi
     * @param message Message que l'on envoi
     * @param out Flux d'envoi
     */
    private void SendMessage(Message message, ObjectOutputStream out) {
        try {
            out.writeObject(message);
        } catch (IOException e) {

            System.out.println("Erreur : " + e.getMessage());

        }
    }

    /**
     * Fonction appelée quand l'application s'arrête
     * @throws IOException Si une erreur se produit lors de la manipulation des sockets.
     */
    @Override
    public void stop() throws IOException {
        // Si on est un client alors on envoie au serveur que l'on se déconnecte
        if(identity == NetworkIdentity.CLIENT) {
            SendMessage(new LeaveEventMessage(pId));
        }

        // On arrête les systèmes d'écoutes
        for(Reader r : readers) {
            r.interrupt();
        }

        // Récupération de chaque connexion
        for(Socket s : connections) {
            s.close();
        }
    }
}
