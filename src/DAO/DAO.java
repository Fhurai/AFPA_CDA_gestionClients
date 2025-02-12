package DAO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Classe abstrait d'accès DAO.
 *
 * @param <T> Classe pour laquelle créer la classe DAO.
 */
public abstract class DAO<T> {

    /**
     * Constructeur
     */
    public DAO() {
    }

    /**
     * Méthode pour récupérer l'ensemble des objets de type T.
     *
     * @return l'ensemble des objets de type T.
     */
    abstract public ArrayList<T> findAll() throws SocieteDatabaseException;

    /**
     * Méthode pour rechercher un objet de type T.
     *
     * @param name Nom de l'objet T recherché.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    abstract public T find(String name) throws SocieteDatabaseException;

    /**
     * Méthode pour supprimer un objet de type T
     *
     * @param obj L'objet à supprimer.
     * @return Indication que l'objet a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    abstract public boolean delete(@NotNull T obj) throws SocieteDatabaseException;

    /**
     * Méthode qui sauvegarde un objet, soit en le créant, soit en le modifiant.
     *
     * @param obj L'objet à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    abstract public boolean save(T obj) throws SocieteDatabaseException;
}
