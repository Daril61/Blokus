package blokus.game;

import blokus.utils.*;
import blokus.utils.eventArgs.EventArgsType;
import blokus.utils.eventArgs.GameRankArgs;
import blokus.utils.eventArgs.TurnArgs;
import blokus.utils.eventArgs.UpdateConnectedArgs;
import blokus.utils.PlayerStruct;
import blokus.utils.message.*;
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

    /**
     * Instance de l'application.
     */
    private static GameApplication instance;
    /**
     * Méthode permettant d'obtenir l'instance de l'application.
     *
     * @return l'instance de l'application
     */
    public static GameApplication getInstance() {
        return instance;
    }

    /**
     * Fenêtre principale de l'application.
     */
    private Stage stage;
    /**
     * Méthode permettant d'obtenir la fenêtre principale de l'application.
     *
     * @return la fenêtre principale de l'application
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Booléen indiquant si le jeu a commencé.
     */
    private boolean gameStart;

    /**
     * Identité du joueur sur le réseau.
     */
    private NetworkIdentity identity;
    /**
     * Méthode permettant d'obtenir l'identité du joueur sur le réseau.
     *
     * @return l'identité du joueur sur le réseau
     */
    public NetworkIdentity getIdentity() {
        return identity;
    }

    /**
     * Identifiant du joueur.
     */
    public int pId;
    /**
     * Adresse IP utilisée par le joueur.
     */
    public String ip;

    /**
     * Booléen indiquant si c'est au tour du joueur.
     */
    public boolean myTurn = false;

    /**
     * Liste des connexions au réseau.
     */
    private final List<Socket> connections = new ArrayList<>();
    /**
     * Liste des flux de sortie des connexions au réseau.
     */
    private final List<ObjectOutputStream> outputStreams = new ArrayList<>();
    /**
     * Liste des structures des joueurs.
     */
    public List<PlayerStruct> playerStructs = new ArrayList<>();
    /**
     * Liste des lecteurs des connexions au réseau.
     */
    private final List<Reader> readers = new ArrayList<>();
    /**
     * Liste des ordres de passage des joueurs.
     */
    private final List<ObjectOutputStream> pOrder = new ArrayList<>();

    /**
     * Identifiant de l'ordre de passage des joueurs.
     */
    private int idOrder = 0;

    /**
     * Méthode permettant d'obtenir le nombre de joueurs connectés au réseau.
     *
     * @return le nombre de joueurs connectés au réseau
     */
    public int getNumberOfPlayers() {
        return connections.size();
    }

    /**
     * Événement déclenché lors d'une mise à jour des joueurs.
     */
    public Event OnPlayersUpdateEvent;
    /**
     * Événement déclenché lorsqu'une forme est placée.
     */
    public Event OnShapePlacedEvent;
    /**
     * Événement déclenché lorsque c'est au tour du joueur.
     */
    public Event WhenMyTurn;
    /**
     * Événement déclenché lors de la réception du classement du jeu.
     */
    public Event OnGameRankReceivedEvent;

    /**
     * Socket du serveur réseau.
     */
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
        WhenMyTurn = new Event(EventArgsType.TURN_ARGS);
        OnGameRankReceivedEvent = new Event(EventArgsType.GAME_RANK);

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

        pOrder.add(null);
        pOrder.addAll(outputStreams);

        SendMessageToAll(new StartGameMessage());
        myTurn = true;
        WhenMyTurn.broadcast(new TurnArgs());
        OnStartGame();
    }

    /**
     * Fonction appelée quand on clique sur le bouton "Menu principal" dans la scène de jeu
     */
    public void GameFinished() throws IOException {
        // On arrête les systèmes d'écoutes
        for(Reader r : readers) {
            r.interrupt();
        }

        // Récupération de chaque connexion
        for(Socket s : connections) {
            s.close();
        }

        if(identity == NetworkIdentity.SERVER)
            serverSocket.close();

        readers.clear();
        connections.clear();
        playerStructs.clear();
        outputStreams.clear();

        GameUtils.ChangeScene(stage, FxmlType.MainMenu);
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
            ChangeTurn();

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
     * Fonction qui permet de changer de tour
     */
    public void ChangeTurn() {
        idOrder++;
        if(idOrder >= pOrder.size()) {
            idOrder = 0;
        }

        // Si c'est le serveur
        if(pOrder.get(idOrder) == null) {
            myTurn = true;
            WhenMyTurn.broadcast(new TurnArgs());

            return;
        }
        SendMessage(new TurnMessage(), pOrder.get(idOrder));
    }
    /**
     * Fonction pour retirer un joueur de la liste quand il abandonne
     * @param pId Identifiant du joueur
     */
    public void RemoveTurnPlayer(int pId) {
        // Si c'est le serveur
        if(pId == -1) {
            pOrder.remove(null);
        } else {
            pOrder.remove(outputStreams.get(pId-1));
        }

        // S'il reste qu'une personne alors elle a gagné
        if(pOrder.size() == 1) {
            if(pOrder.get(0) == null)
            {
                GameRankArgs args = new GameRankArgs(0);
                SendMessageToAll(new GameRankMessage(args));

                OnGameRankReceivedEvent.broadcast(new GameRankArgs(1));
            } else {
                int index = outputStreams.indexOf(pOrder.get(0));
                ObjectOutputStream o = outputStreams.get(index);
                SendMessage(new GameRankMessage(new GameRankArgs(1)), o);

                GameRankArgs args = new GameRankArgs(0);
                for (ObjectOutputStream outputStream : outputStreams) {
                    if (outputStream == o) continue;

                    SendMessage(new GameRankMessage(args), outputStream);
                }

                OnGameRankReceivedEvent.broadcast(args);
            }

            return;
        }

        // Sinon on continue
        ChangeTurn();
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
