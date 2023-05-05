package blokus.utils;

import java.io.Serializable;

/**
 * Structure de données pour un joueur
 *
 * @author Romain Veydarier
 * @since 04/05/2023
 */
public class PlayerStruct implements Serializable {
    /**
     * Nom du joueur
     */
    public String pName;
    /**
     * Identifiant du joueur
     */
    public int pId;

    /**
     * Constructeur de la classe
     *
     * @param pName Nom du joueur
     * @param pId Identifiant du joueur
     */
    public PlayerStruct(String pName, int pId) {
        this.pName = pName;
        this.pId = pId;
    }
}
