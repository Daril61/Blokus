package blokus.utils;

import blokus.game.GameApplication;
import blokus.game.GameSceneController;
import blokus.utils.eventArgs.ShapePlacedArgs;
import blokus.utils.eventArgs.UpdateConnectedArgs;
import blokus.utils.message.LeaveEventMessage;
import blokus.utils.message.PlaceShapeMessage;
import blokus.utils.message.UpdateConnectedMessage;
import blokus.utils.message.Message;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Cette classe permet de lire des messages depuis un socket.
 * Les messages lus sont envoyés à la méthode handleReceivedMessage.
 *
 * @since 23/04/2023
 * @author Romain Veydarier
 */
public class Reader extends Thread {

    /**
     * Le flux d'entrée pour la lecture des messages.
     */
    private final ObjectInputStream lecteurFlux;

    /**
     * Indique si le lecteur doit continuer à écouter les messages.
     */
    private boolean ecouter = true;

    /**
     * Le socket à partir duquel les messages sont lus.
     */
    private final Socket socket;

    /**
     * Constructeur de la classe.
     *
     * @param socket Le socket à partir duquel les messages seront lus.
     */
    public Reader(Socket socket) {
        this.socket = socket;

        try {
            System.out.println("Tentative de création du flux de lecture");
            lecteurFlux = new ObjectInputStream(socket.getInputStream());
            System.out.println("Flux de lecture créé avec succès");
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la création du flux de lecture", e);
        }
    }

    /**
     * Méthode exécutée lors du démarrage du thread.
     * Elle écoute en continu les messages reçus et les envoie à la méthode handleReceivedMessage.
     */
    @Override
    public void run() {
        System.out.println("Début de l'écoute");
        while (ecouter) {
            if (socket.isClosed() || Thread.interrupted()) {
                ecouter = false;
                break;
            }

            try {
                Message message = (Message) lecteurFlux.readObject();
                System.out.println("On reçois un message !");
                handleReceivedMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la réception du message: " + e.getMessage());
                ecouter = false;
                break;
            }
        }
        System.out.println("Fin de l'écoute");
    }

    /**
     * Méthode appelée lorsqu'un message est reçu.
     * Vous pouvez implémenter cette méthode dans une sous-classe pour traiter les messages reçus.
     *
     * @param message Le message reçu.
     */
    private void handleReceivedMessage(Message message) throws IOException {
        switch (message.getType()) {
            case UPDATE_CONNECTED -> {
                UpdateConnectedMessage m = (UpdateConnectedMessage) message;
                UpdateConnectedArgs updateConnectedArgs = m.updateConnectedArgs;

                // On sauvegarde l'identifiant du joueur et la liste des joueurs
                GameApplication.getInstance().pId = m.pId;
                GameApplication.getInstance().playerStructs = updateConnectedArgs.players;

                // On lance l'évènement qui prévient quand il y a une update au niveau des joueurs
                GameApplication.getInstance().OnPlayersUpdateEvent.broadcast(updateConnectedArgs);
            }
            case LEAVE_GAME -> {
                LeaveEventMessage m = (LeaveEventMessage) message;

                GameApplication.getInstance().OnClientDisconnect(m.pId);
            }
            case START_GAME -> {
                Platform.runLater(() -> {
                    try {
                        GameApplication.getInstance().OnStartGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            case PLACE_SHAPE -> {
                PlaceShapeMessage m = (PlaceShapeMessage)message;
                ShapePlacedArgs args = m.args;

                Platform.runLater(() -> {
                    GameApplication.getInstance().OnShapePlacedEvent.broadcast(args);
                    if(GameApplication.getInstance().getIdentity() == NetworkIdentity.SERVER) {
                        GameApplication.getInstance().SendMessageToAll(m);
                    }
                });
            }
        }
    }
}