package DAO.mysql;

import DAO.SocieteDatabaseException;
import builders.ClientBuilder;
import entities.Client;
import entities.Contrat;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Classe DAO MySql pour les clients.
 */
public class ClientMySqlDAO extends SocieteMySqlDAO<Client> {

    /**
     * Constructor
     */
    public ClientMySqlDAO() {
    }

    /**
     * Méthode pour récupérer l'ensemble des clients.
     *
     * @return l'ensemble des clients.
     */
    @Override
    public ArrayList<Client> findAll() throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Client> clients = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query = "SELECT * FROM clients";

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des clients.
                Client c = this.parse(rs);
                c.setContrats(MySqlFactory.getContratDAO().findByIdClient(c.getIdentifiant()));
                clients.add(c);
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

        return clients;
    }

    /**
     * Méthode pour rechercher un client.
     *
     * @param name Nom du client.
     * @return Client recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Client find(String name) throws SocieteDatabaseException {
        // Initialisation des variables.
        Client client = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM clients WHERE `raisonSociale` LIKE ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            stmt.setString(1, '%' + name + '%');

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                client = this.parse(rs);
                client.setContrats(MySqlFactory.getContratDAO().findByIdClient(client.getIdentifiant()));
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

        return client;
    }

    /**
     * Méthode pour rechercher un client.
     *
     * @param identifiant Identifiant du client.
     * @return Client recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    public Client findById(int identifiant) throws SocieteDatabaseException {
        // Initialisation des variables.
        Client client = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM clients WHERE `identifiant` = ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            stmt.setInt(1, identifiant);

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                client = this.parse(rs);
                client.setContrats(MySqlFactory.getContratDAO().findByIdClient(client.getIdentifiant()));
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

        return client;
    }

    /**
     * Méthode pour supprimer un client.
     *
     * @param obj Le client à supprimer.
     * @return Indication que l'objet a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Client obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        int rowsAffected;

        // Récupération de la requête de suppression.
        String query = "DELETE FROM clients WHERE identifiant = ?";

        try {
            con.setAutoCommit(false);

            // Suppression des contrats du client.
            for (Contrat contrat : obj.getContrats()) {
                MySqlFactory.getContratDAO().delete(contrat);
            }

            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setInt(1, obj.getIdentifiant());
            rowsAffected = stmt.executeUpdate();

            // Suppression de la date liée au client.
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
     * Méthode qui sauvegarde un client, soit en le créant, soit en le
     * modifiant.
     *
     * @param obj Le client à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(@NotNull Client obj) throws SocieteDatabaseException {
        // Initialisation des variables communes.
        Connection conn = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query;
        boolean ret = false;

        try {
            conn.setAutoCommit(false);

            if (obj.getIdentifiant() > 0) {

                // Update date.
                MySqlFactory.getAdresseDAO().save(obj.getAdresse());

                // Initialisation variables UPDATE
                query = "UPDATE clients SET `raisonSociale` = ?,`telephone` = ?,`mail` = ?, `commentaires` = ?,`chiffreAffaires` = ?,`nbEmployes` = ?,`idAdresse` = ?  WHERE `identifiant` = ?";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getRaisonSociale());
                stmt.setString(2, obj.getTelephone());
                stmt.setString(3, obj.getMail());
                stmt.setString(4, obj.getCommentaires());
                stmt.setLong(5, obj.getChiffreAffaires());
                stmt.setInt(6, obj.getNbEmployes());
                stmt.setInt(7, obj.getAdresse().getIdentifiant());
                stmt.setInt(8, obj.getIdentifiant());

                // Exécution de la requête.
                ret = stmt.executeUpdate() == 1;
            } else {

                // Création date.
                MySqlFactory.getAdresseDAO().save(obj.getAdresse());

                // Initialisation variables CREATE
                ResultSet rs;
                query = "INSERT INTO `clients`(`raisonSociale`, `telephone`, `mail`, `commentaires`, `chiffreAffaires`, `nbEmployes`, `idAdresse`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getRaisonSociale());
                stmt.setString(2, obj.getTelephone());
                stmt.setString(3, obj.getMail());
                stmt.setString(4, obj.getCommentaires());
                stmt.setLong(5, obj.getChiffreAffaires());
                stmt.setInt(6, obj.getNbEmployes());
                stmt.setInt(7, obj.getAdresse().getIdentifiant());

                // Exécution de la requête.
                stmt.executeUpdate();

                // Récupération des clés générées.
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ret = true;
                    obj.setIdentifiant(rs.getInt(1));
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
     * Méthode qui construit un Client à partir d'une ligne de résultats.
     *
     * @param rs Ligne de résultats
     * @return Client Le client récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Client parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            return ClientBuilder.getNewClientBuilder()
                    .dIdentifiant(rs.getInt("identifiant"))
                    .deRaisonSociale(rs.getString("raisonSociale"))
                    .deTelephone(rs.getString("telephone"))
                    .deMail(rs.getString("mail"))
                    .deCommentaires(rs.getString("commentaires"))
                    .deChiffreAffaires(rs.getString("chiffreAffaires"))
                    .deNombreEmployes(rs.getInt("nbEmployes"))
                    .dAdresse(MySqlFactory.getAdresseDAO().findById(rs.getInt("idAdresse")))
                    .build();
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du client depuis la base de données.");
        }
    }
}
