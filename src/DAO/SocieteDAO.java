package DAO;

import entities.Societe;

/*
 * Classe DAO Société abstraite.
 */
abstract public class SocieteDAO<T extends Societe> extends DAO<T> {

    /**
     * Méthode pour trouver un objet de type T par son identifiant.
     *
     * @param identifiant Identifiant de l'objet de type T.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou de la
     *                                  fermeture des données.
     */
    abstract public T findById(int identifiant) throws SocieteDatabaseException;
}
