package DAO.mysql;

import DAO.DAO;
import DAO.SocieteDatabaseException;
import entities.Contrat;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class ContratMySqlDAO extends DAO<Contrat> {

    /**
     * Constructeur
     */
    public ContratMySqlDAO() {
        super();
    }

    /**
     * Méthode pour récupérer l'ensemble des contrats.
     *
     * @return l'ensemble des contrats.
     */
    @Override
    public ArrayList<Contrat> findAll() throws SocieteDatabaseException {
        ArrayList<Contrat> contrats = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;
        String query = "SELECT * FROM contrats";

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des contrats.
                contrats.add(this.parse(rs));
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

        return contrats;
    }

    /**
     * Méthode pour rechercher un contrat.
     *
     * @param name Nom du contrat recherché.
     * @return contrat recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Contrat find(String name) throws SocieteDatabaseException {
        // Initialisation des variables.
        Contrat contrat = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM contrats WHERE `libelleContrat` LIKE ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            // Liaison du nom recherché à la requête.
            stmt.setString(1, '%' + name + '%');

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                contrat = this.parse(rs);
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

        return contrat;
    }

    /**
     * Méthode pour rechercher un contrat.
     *
     * @param idClient Le client du contrat recherché.
     * @return La liste des contrats du client.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    public ArrayList<Contrat> findByIdClient(int idClient) throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Contrat> contrats = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;
        String query = "SELECT * FROM contrats where `idClient` = ?";

        try {
            // Création de l'objet requête.
            stmt = con.prepareStatement(query);

            // Liaison du nom recherché à la requête.
            stmt.setInt(1, idClient);

            // Exécution de la requête.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des contrats.
                contrats.add(this.parse(rs));
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

        return contrats;
    }

    /**
     * Méthode pour supprimer un contrat.
     *
     * @param obj Le contrat à supprimer.
     * @return Indication que le contrat a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Contrat obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        int rowsAffected;

        // Récupération de la requête de suppression.
        String query = "DELETE FROM contrats WHERE idContrat = ?";

        try {
            con.setAutoCommit(false);

            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setInt(1, obj.getIdentifiant());
            rowsAffected = stmt.executeUpdate();

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
     * Méthode qui sauvegarde un contrat, soit en le créant, soit en le
     * modifiant.
     *
     * @param obj Le contrat à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(@NotNull Contrat obj) throws SocieteDatabaseException {
        // Initialisation des variables communes.
        Connection conn = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query;
        boolean ret = false;

        try {
            conn.setAutoCommit(false);

            if (obj.getIdentifiant() > 0) {
                // Initialisation variables UPDATE
                query = "UPDATE `contrats` SET `libelleContrat` = ?, `montant` = ?,`idClient` = ? WHERE `idContrat`= ?";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getLibelle());
                stmt.setDouble(2, obj.getMontant());
                stmt.setInt(3, obj.getIdClient());
                stmt.setInt(4, obj.getIdentifiant());

                // Exécution de la requête.
                ret = stmt.executeUpdate() == 1;
            } else {
                // Initialisation variables CREATE
                ResultSet rs;
                query = "INSERT INTO `contrats`(`libelleContrat`, `montant`, `idClient`) VALUES (?, ?, ?)";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getLibelle());
                stmt.setDouble(2, obj.getMontant());
                stmt.setInt(3, obj.getIdClient());

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
     * Méthode qui construit un Contrat à partir d'une ligne de résultats.
     *
     * @param rs Ligne de résultats
     * @return Contrat Le Contrat récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Contrat parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        // Initialisation Contrat.
        Contrat contrat = new Contrat();

        try {
            // Valorisation propriétés primitives.
            contrat.setIdentifiant(rs.getInt("idContrat"));
            contrat.setIdClient(rs.getInt("idClient"));
            contrat.setLibelle(rs.getString("libelleContrat"));
            contrat.setMontant(rs.getDouble("montant"));
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du Contrat depuis la base de données.");
        }

        // Retourne le Contrat valorisé.
        return contrat;
    }
}
