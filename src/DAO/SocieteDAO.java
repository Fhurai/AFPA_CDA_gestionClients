package DAO;

import entities.Societe;

import java.util.List;

/**
 * Classe DAO Société abstraite.
 */
abstract public class SocieteDAO<T extends Societe> extends DAO<T> {

    /**
     * Check si la raison sociale donnée existe déjà dans la table actuelle.
     *
     * @param raisonSociale La raison sociale à check.
     * @return Indication si la raison sociale existe ou non.
     * @throws SocieteDatabaseException Exception si la recherche des raisons
     *                                  sociales rencontre un problème.
     */
    protected boolean checkRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        List<String> raisonsSociales = this.findAll().stream()
                .map(Societe::getRaisonSociale)
                .toList();

        return raisonsSociales.contains(raisonSociale) || checkOtherRaisonSociale(raisonSociale);
    }

    /**
     * Check si la raison sociale donnée existe déjà dans l'autre table.
     *
     * @param raisonSociale La raison sociale à check.
     * @return Indication si la raison sociale existe ou non.
     * @throws SocieteDatabaseException Exception si la recherche des raisons
     *                                  sociales rencontre un problème.
     */
    abstract protected boolean checkOtherRaisonSociale(String raisonSociale) throws SocieteDatabaseException;

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
