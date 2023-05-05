package blokus.utils;

import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.EventArgsType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Classe pour créer un évent dans le jeu
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public class Event {

    /**
     * La liste de tous les listeners
     */
    private final Set<Consumer<EventArgs>> listeners = new HashSet<>();

    /**
     * Le type d'argument que l'évent accepte
     */
    private final EventArgsType type;

    /**
     * Constructeur de la classe
     *
     * @param type Le type d'argument
     */
    public Event(EventArgsType type) {
        this.type = type;
    }

    /**
     * Fonction pour ajouter un listener à l'event
     * @param listener Le nouveau listener
     */
    public void addListener(Consumer<EventArgs> listener) {
        listeners.add(listener);
    }

    /**
     * Fonction pour prévenir tous les listeners du lancement de l'évent
     * @param args Les arguments de l'évent
     */
    public void broadcast(EventArgs args) {
        if(args.getType() != type) return;

        listeners.forEach(x -> x.accept(args));
    }
}
