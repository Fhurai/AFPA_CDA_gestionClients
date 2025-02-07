package DAO;

import java.util.ArrayList;

public interface DAO<T> {

    /**
     * Méthode pour récupérer la liste des objets de type T.
     * @return Liste des objets de type T.
     */
    abstract public ArrayList<T> findAll() throws SocieteDatabaseException;

    /**
     * Méthode pour récupérer un objet à partir d'un tableau de sélection.
     * @param selection Tableau de sélection.
     * @return Objet de type T recherché.
     */
    abstract public T find(String[][] selection) throws SocieteDatabaseException;

    /**
     * Méthode pour récupérer un objet à partir de son identifiant.
     * @param id Identifiant de sélection.
     * @return Objet de type T recherché.
     */
    abstract public T findById(int id) throws SocieteDatabaseException;

    /**
     * Méthode pour sauvegarder la création ou la modification d'un objet de
     * type T.
     * @param t L'objet à sauvegarder.
     * @return True si la sauvegarde s'est bien passé, sinon false.
     */
    abstract public boolean save(T t);

    /**
     * Méthode pou supprimer la création ou la modification d'un objet de
     * type T.
     *
     * @param t L'objet à supprimer.
     * @return True si la suppression s'est bien passé, sinon false.
     */
    public default boolean delete(T t) throws SocieteDatabaseException {
        return false;
    }
}
