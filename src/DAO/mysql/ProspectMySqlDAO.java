package DAO.mysql;

import DAO.SocieteDatabaseException;
import entities.Prospect;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;
import utilities.Formatters;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Classe DAO pour les prospects.
 */
public class ProspectMySqlDAO extends SocieteMySqlDAO<Prospect> {

    /**
     * Constructor
     */
    public ProspectMySqlDAO() {
    }

    /**
     * Méthode pour récupérer l'ensemble des prospects.
     *
     * @return l'ensemble des prospects.
     */
    @Override
    public ArrayList<Prospect> findAll() throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Prospect> prospects = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query = "SELECT * FROM prospects";

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des clients.
                prospects.add(this.parse(rs));
            }
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture de l'accès aux données.");
        }

        return prospects;
    }

    /**
     * Méthode pour rechercher un prospect.
     *
     * @param name Nom du prospect.
     * @return prospect recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Prospect find(String name) throws SocieteDatabaseException {
        // Initialisation des variables.
        Prospect prospect = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM prospects WHERE `raisonSociale` LIKE ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            stmt.setString(1, '%' + name + '%');

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                prospect = this.parse(rs);
            }

        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture de l'accès aux données.");
        }

        return prospect;
    }

    /**
     * Méthode pour rechercher un prospect.
     *
     * @param identifiant Identifiant du prospect.
     * @return prospect recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    public Prospect findById(int identifiant) throws SocieteDatabaseException {
        // Initialisation des variables.
        Prospect prospect = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM prospects WHERE `identifiant` = ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            stmt.setInt(1, identifiant);

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                prospect = this.parse(rs);
            }

        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture de l'accès aux données.");
        }

        return prospect;
    }

    /**
     * Méthode pour supprimer un prospect.
     *
     * @param obj Le prospect à supprimer.
     * @return Indication que le prospect a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Prospect obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        int rowsAffected;

        // Récupération de la requête de suppression.
        String query = "DELETE FROM prospects WHERE identifiant = ?";

        try {
            con.setAutoCommit(false);

            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setInt(1, obj.getIdentifiant());
            rowsAffected = stmt.executeUpdate();

            // Suppression de la date liée au prospect.
            MySqlFactory.getAdresseDAO().delete(obj.getAdresse());

            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                // Exception attrapée, log de l'erreur et avertissement de
                // l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Erreur lors de la sauvegarde.");
            }

            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture de l'accès aux données.");
        }

        // Retourne l'indication si la requête a modifié une et une seule
        // ligne d'enregistrement.
        return rowsAffected == 1;
    }

    /**
     * Méthode qui sauvegarde un prospect, soit en le créant, soit en le
     * modifiant.
     *
     * @param obj Le prospect à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(@NotNull Prospect obj) throws SocieteDatabaseException {
        // Initialisation des variables communes.
        Connection conn = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query;
        boolean ret = false;

        try {
            conn.setAutoCommit(false);

            if (obj.getIdentifiant() > 0) {
                // Initialisation variables UPDATE
                query = "UPDATE prospects SET `raisonSociale` = ?,`telephone` = ?,`mail` = ?, `commentaires` = ?,`dateProspection` = ?,`prospectInteresse` = ?,`idAdresse` = ?  WHERE `identifiant` = ?";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getRaisonSociale());
                stmt.setString(2, obj.getTelephone());
                stmt.setString(3, obj.getMail());
                stmt.setString(4, obj.getCommentaires());
                stmt.setDate(5, Date.valueOf(obj.getDateProspection()));
                stmt.setInt(6, Objects.equals(obj.getProspectInteresse(), "oui") ? 1 : 0);
                stmt.setInt(7, obj.getAdresse().getIdentifiant());
                stmt.setInt(8, obj.getIdentifiant());

                // Exécution de la requête.
                ret = stmt.executeUpdate() == 1;
            } else {
                // Initialisation variables CREATE
                ResultSet rs;
                query = "INSERT INTO `prospects`(`raisonSociale`, `telephone`, `mail`, `commentaires`, `dateProspection`, `prospectInteresse`, `idAdresse`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getRaisonSociale());
                stmt.setString(2, obj.getTelephone());
                stmt.setString(3, obj.getMail());
                stmt.setString(4, obj.getCommentaires());
                stmt.setDate(5, Date.valueOf(obj.getDateProspection()));
                stmt.setInt(6, Objects.equals(obj.getProspectInteresse(), "oui") ? 1 : 0);
                stmt.setInt(7, obj.getAdresse().getIdentifiant());

                // Exécution de la requête.
                stmt.executeUpdate();

                // Récupération des clés générées.
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ret = true;
                    obj.setIdentifiant(rs.getInt("identifiant"));
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException | SocieteEntityException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // Exception attrapée, log de l'erreur et avertissement de
                // l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Erreur lors de la sauvegarde.");
            }

            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la sauvegarde.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture de l'accès aux données.");
        }

        // Retourne si la sauvegarde s'est bien passée ou non.
        return ret;
    }

    /**
     * Méthode qui construit un Prospect à partir d'une ligne de résultats.
     *
     * @param rs Ligne de résultats
     * @return Prospect Le Prospect récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Prospect parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        // Initialisation Prospect.
        Prospect prospect = new Prospect();

        try {
            // Valorisation propriétés primitives.
            prospect.setIdentifiant(rs.getInt("identifiant"));
            prospect.setRaisonSociale(rs.getString("raisonSociale"));
            prospect.setTelephone(rs.getString("telephone"));
            prospect.setMail(rs.getString("mail"));
            prospect.setCommentaires(rs.getString("commentaires"));

            // Valorisation propriété date.
            String[] dt = rs.getString("dateProspection").split("-");
            LocalDate ldt = LocalDate.parse(dt[2] + '/' + dt[1] + '/' + dt[0], Formatters.FORMAT_DDMMYYYY);
            prospect.setDateProspection(ldt);

            // Valorisation propriétés booléennes.
            prospect.setProspectInteresse(rs.getInt("prospectInteresse") == 1 ? "oui" : "non");

            // Valorisation propriétés objets.
            prospect.setAdresse(MySqlFactory.getAdresseDAO().findById(rs.getInt(
                    "idAdresse")));
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du Prospect depuis la base de données.");
        }

        // Retourne le Prospect valorisé.
        return prospect;
    }
}
