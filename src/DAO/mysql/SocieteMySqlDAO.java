package DAO.mysql;

import DAO.DAO;
import DAO.QueryAction;
import DAO.SocieteDatabaseException;
import entities.Societe;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

abstract public class SocieteMySqlDAO<T extends Societe> extends DAO<T> {

    /**
     * Constructeur.
     *
     * @param likeProperties Le tableau des champs nécessitant un mot-clé
     *                       LIKE en SQL.
     */
    public SocieteMySqlDAO(String[] likeProperties) {
        super(likeProperties);
    }

    /**
     * Méthode donnant le tableau de sélection par la clé primaire
     *
     * @param obj L'objet sélectionné.
     * @return String[][] Tableau de sélection/
     */
    protected String[][] selectByPrimaryKey(@NotNull T obj) {
        return new String[][]{{"identifiant", String.valueOf(obj.getIdentifiant())}};
    }

    /**
     * Méthode qui lie la clé primaire à partir de l'objet de type T
     *
     * @param obj  Objet à lier.
     * @param stmt La requête à préparer
     * @throws SocieteDatabaseException Exception d'impossibilité de liaison.
     */
    protected void bindPrimaryKey(@NotNull T obj,
                                  @NotNull PreparedStatement stmt,
                                  int nbParameters) throws SocieteDatabaseException {
        try {
            stmt.setInt(nbParameters, obj.getIdentifiant());
        } catch (SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Société n'arrive pas à faire " +
                    "une sélection simple.");
        }
    }

    /**
     * Méthode pour affecter la valeur de la clé primaire de la société avec les
     * données de la ligne de résultat.
     *
     * @param obj La société.
     * @param rs  La ligne
     * @throws SocieteDatabaseException Exception lors de la récupération de
     *                                  données depuis l'enregistrement ou exception lors de la valorisation
     *                                  de la clé primaire.
     */
    @Override
    protected void setPrimaryKey(@NotNull T obj, @NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            obj.setIdentifiant(rs.getInt(1));
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Le nouveau client n'est pas" +
                    " bien indexé.");
        }
    }

    /**
     * Méthode pour rechercher un objet de type T à partir de sa raison sociale.
     *
     * @param raisonSociale La raison sociale de l'objet recherché.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    public T findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"raisonSociale", raisonSociale}};

        // Retourne le client trouvé avec la condition de recherche.
        return this.find(selection);
    }

    /**
     * Méthode pour supprimer la société et son adresse.
     *
     * @param obj La société à supprimer.
     * @return Indication que la société a bien été supprimée.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(T obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection conn = ConnexionMySql.getInstance();
        boolean ret = false;

        try {
            // Désactive le commit automatique.
            conn.setAutoCommit(false);

            // Suppression de la société et de son adresse.
            ret = super.delete(obj);
            MySqlFactory.getAdresseDAO().delete(obj.getAdresse());

            // Validation des changements en base.
            conn.commit();

            // Ré-activation du commit automatique.
            conn.setAutoCommit(false);
        } catch (SQLException e) {

            // Si exception il y a, appel à la méthode de gestion.
            this.manageSqlException(e, conn);
        }

        // Retourne le résultat de la méthode mère.
        return ret;
    }

    /**
     * Méthode qui sauvegarde une société, soit en la créant, soit en la
     * modifiant.
     *
     * @param obj L'objet à sauvegarder.
     * @return Indication que la société a bien été sauvegardée.
     * @throws SocieteDatabaseException Exception lors de la création, la
     *                                  modification ou la fermeture des données.
     */
    public boolean save(@NotNull T obj) throws SocieteDatabaseException {
        // Initialisation des variables communes.
        Connection conn = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query;
        boolean ret = false;

        try {
            conn.setAutoCommit(false);

            MySqlFactory.getAdresseDAO().save(obj.getAdresse());

            if (obj.getIdentifiant() > 0) {
                // Initialisation variables UPDATE
                String[][] selection = selectByPrimaryKey(obj);
                query = this.getQueryString(QueryAction.UPDATE, selection);

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query);

                // Liaison des données de la société dans la requête.
                this.bindTableProperties(obj, stmt);
                this.bindPrimaryKey(obj, stmt,
                        this.getTablePropertiesLabels().length + 1);

                // Exécution de la requête.
                ret = stmt.executeUpdate() == 1;
            } else {
                // Initialisation variables CREATE
                ResultSet rs;
                query = this.getQueryString(QueryAction.CREATE, null);

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                // Liaison des données de la société dans la requête.
                this.bindTableProperties(obj, stmt);

                // Exécution de la requête.
                stmt.executeUpdate();

                // Récupération des clés générées.
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ret = true;
                    this.setPrimaryKey(obj, rs);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            this.manageSqlException(e, conn);
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " +
                    "sauvegarde.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " +
                    "fermeture de l'accès aux données.");
        }

        // Retourne si la sauvegarde s'est bien passée ou non.
        return ret;
    }

    /**
     * Méthode de gestion d'une SqlException.
     *
     * @param e   L'exception à gérer.
     * @param con La connexion a fermer.
     * @throws SocieteDatabaseException Exception d'intégrité ou de
     *                                  modification des données.
     */
    private void manageSqlException(@NotNull SQLException e, Connection con) throws SocieteDatabaseException {
        // Log le message de l'exception.
        LogManager.logs.log(Level.SEVERE, e.getMessage());

        if (con != null) {
            // Si la connexion est toujours ouverte.

            try {
                // Annulation des changements.
                con.rollback();

                // Ré-activate du commit automatique.
                con.setAutoCommit(false);
            } catch (SQLException ex) {
                // Une autre exception attrapée.

                // Log de la deuxième exception.
                LogManager.logs.log(Level.SEVERE, e.getMessage());

                // Lancement d'une exception lisible par l'utilisateur.
                throw new SocieteDatabaseException("Les données de " +
                        "sociétés ne sont plus intègres.");
            }
        }

        // Lancement d'une exception lisible par l'utilisateur.
        throw new SocieteDatabaseException("Problème lors de la " +
                "modification de la société.");
    }
}
