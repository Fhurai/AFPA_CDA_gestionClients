package DAO.mysql;

import DAO.DAO;
import DAO.SocieteDatabaseException;
import entities.Adresse;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class AdresseMySqlDAO extends DAO<Adresse> {

    /**
     * Constructor
     */
    public AdresseMySqlDAO() {
        super(new String[]{"numRue", "nomRue", "codePostal", "ville"});
    }

    /**
     * Méthode qui retourne la table de la classe DAO.
     *
     * @return String
     */
    @Override
    protected String getTable() {
        return "adresses";
    }

    /**
     * Méthode qui construit une Adresse à partir d'une ligne de résultats.
     *
     * @param rs Ligne de résultats
     * @return Adresse Le client récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    @Override
    protected Adresse parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        // Initialisation adresse.
        Adresse adresse = new Adresse();

        try {
            // Valorisation propriétés primitives.
            adresse.setIdentifiant(rs.getInt("identifiant"));
            adresse.setNumeroRue(rs.getString("numRue"));
            adresse.setNomRue(rs.getString("nomRue"));
            adresse.setCodePostal(rs.getString("codePostal"));
            adresse.setVille(rs.getString("ville"));
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        }

        // Retourne l'adresse valorisée.
        return adresse;
    }

    /**
     * Méthode donnant le tableau de sélection par la clé primaire
     *
     * @param obj L'objet sélectionné.
     * @return String[][] Tableau de sélection/
     */
    @Override
    protected String[][] selectByPrimaryKey(@NotNull Adresse obj) throws SocieteDatabaseException {
        return new String[][]{{"identifiant", String.valueOf(obj.getIdentifiant())}};
    }

    /**
     * Méthode qui lie la clé primaire à partir de l'adresse
     *
     * @param obj  Adresse à lier.
     * @param stmt La requête à préparer
     * @throws SocieteDatabaseException Exception d'impossibilité de liaison.
     */
    @Override
    protected void bindPrimaryKey(@NotNull Adresse obj,
                                  @NotNull PreparedStatement stmt, int nbParameters) throws SocieteDatabaseException {
        try {
            stmt.setInt(nbParameters, obj.getIdentifiant());
        } catch (SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Adresse n'arrive pas à faire " +
                    "une sélection simple.");
        }
    }

    /**
     * Méthode donnant les libellés de propriétés pour la classe Adresse.
     *
     * @return String[] Le tableau des libellés.
     */
    @Override
    protected String[] getTablePropertiesLabels() {
        return new String[]{"numRue", "nomRue", "codePostal", "ville"};
    }

    /**
     * Méthode donnant les libellés de propriétés avec jetons pour la classe
     * Adresse.
     *
     * @return String[] le tableau des libellés avec jetons.
     */
    @Override
    protected String[] getTablePropertiesLabelsTokens() {
        return new String[]{"numRue = ?", "nomRue = ?", "codePostal = ?", "ville = ?"};
    }

    /**
     * Méthode pour lier les valeurs de l'adresse à la requête.
     *
     * @param obj  L'adresse.
     * @param stmt Requête à lier.
     * @throws SocieteDatabaseException Exception lors de la liaison
     */
    @Override
    protected void bindTableProperties(Adresse obj, PreparedStatement stmt) throws SocieteDatabaseException {
        try {
            // Valorisation de la requête avec les propriétés du client.
            stmt.setString(1, obj.getNumeroRue());
            stmt.setString(2, obj.getNomRue());
            stmt.setString(3, obj.getCodePostal());
            stmt.setString(4, obj.getVille());
        } catch (SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Adresse n'arrive pas à être " +
                    "créé.");
        }
    }

    /**
     * Méthode pour affecter la valeur de la clé primaire de la société avec les
     * données de la ligne de résultat.
     *
     * @param obj Objet à valoriser.
     * @param rs  La ligne d'enregistrement dont est tirée la valeur.
     * @throws SocieteDatabaseException Exception lors de la récupération de
     *                                  données depuis l'enregistrement ou exception lors de la valorisation
     *                                  de la clé primaire.
     */
    @Override
    protected void setPrimaryKey(@NotNull Adresse obj, @NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            obj.setIdentifiant(rs.getInt(1));
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("La nouvelle adresse n'est pas" +
                    " bien indexée.");
        }
    }
}
