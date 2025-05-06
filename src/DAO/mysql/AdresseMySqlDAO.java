package DAO.mysql;

import DAO.DAO;
import DAO.SocieteDatabaseException;
import builders.AdresseBuilder;
import entities.Adresse;
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
 * Classe DAO pour les adresses.
 */
public class AdresseMySqlDAO extends DAO<Adresse> {

    /**
     * Constructor
     */
    public AdresseMySqlDAO() {
        super();
    }

    /**
     * Méthode pour récupérer l'ensemble des objets de type T.
     *
     * @return l'ensemble des objets de type T.
     */
    @Override
    public ArrayList<Adresse> findAll() throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Adresse> adresses = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query = "SELECT * FROM adresses";

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des clients.
                adresses.add(this.parse(rs));
            }
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de " + "la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " + "fermeture de l'accès aux données.");
        }

        return adresses;
    }

    /**
     * Méthode pour rechercher une adresse par une chaîne de caractères.
     *
     * @param name Nom de l'adresse recherchée.
     * @return Adresse recherchée.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Adresse find(String name) throws SocieteDatabaseException {
        // Initialisation des variables.
        Adresse adresse = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM adresses WHERE CONCAT(numRue, nomRue, codePostal, ville) LIKE ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            stmt.setString(1, '%' + name + '%');

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                adresse = this.parse(rs);
            }

        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de " + "la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " + "fermeture de l'accès aux données.");
        }

        return adresse;
    }

    /**
     * Méthode pour rechercher une adresse par un identifiant.
     *
     * @param identifiant identifiant de l'adresse recherchée.
     * @return Adresse recherchée.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    public Adresse findById(int identifiant) throws SocieteDatabaseException {
        Adresse adresse = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = "SELECT * FROM adresses WHERE identifiant = ?";

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            stmt.setInt(1, identifiant);

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                adresse = this.parse(rs);
            }

        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de " + "la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " + "fermeture de l'accès aux données.");
        }

        return adresse;
    }

    /**
     * Méthode pour supprimer un objet de type T
     *
     * @param obj L'objet à supprimer.
     * @return Indication que l'objet a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Adresse obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        int rowsAffected;

        // Récupération de la requête de suppression.
        String query = "DELETE FROM adresses WHERE identifiant = ?";

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setInt(1, obj.getIdentifiant());
            rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de " + "la base de données.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " + "fermeture de l'accès aux données.");
        }

        // Retourne l'indication si la requête a modifié une et une seule
        // ligne d'enregistrement.
        return rowsAffected == 1;
    }

    /**
     * Méthode qui sauvegarde un objet, soit en le créant, soit en le modifiant.
     *
     * @param obj L'objet à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(@NotNull Adresse obj) throws SocieteDatabaseException {
        // Initialisation des variables communes.
        Connection conn = ConnexionMySql.getInstance();
        PreparedStatement stmt;
        String query;
        boolean ret = false;

        try {
            if (obj.getIdentifiant() > 0) {
                // Initialisation variables UPDATE
                query = "UPDATE adresses SET numRue = ?, nomRue = ?, " + "codePostal = ?, ville = ?" + " WHERE identifiant = ?";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getNumeroRue());
                stmt.setString(2, obj.getNomRue());
                stmt.setString(3, obj.getCodePostal());
                stmt.setString(4, obj.getVille());
                stmt.setInt(5, obj.getIdentifiant());

                // Exécution de la requête.
                ret = stmt.executeUpdate() == 1;
            } else {
                // Initialisation variables CREATE
                ResultSet rs;
                query = "INSERT INTO adresses (`numRue`,`nomRue`," +
                        "`codePostal`,`ville`) VALUES (?, ?, ?, ?)";

                // Préparation requête à exécuter.
                stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                // Liaison des données de la société dans la requête.
                stmt.setString(1, obj.getNumeroRue());
                stmt.setString(2, obj.getNomRue());
                stmt.setString(3, obj.getCodePostal());
                stmt.setString(4, obj.getVille());

                // Exécution de la requête.
                stmt.executeUpdate();

                // Récupération des clés générées.
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ret = true;
                    obj.setIdentifiant(rs.getInt(1));
                }
            }
        } catch (SQLException | SocieteEntityException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " + "sauvegarde.");
        }

        try {
            // Fermeture de la requête.
            stmt.close();
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la " + "fermeture de l'accès aux données.");
        }

        // Retourne si la sauvegarde s'est bien passée ou non.
        return ret;
    }

    /**
     * Méthode qui construit une Adresse à partir d'une ligne de résultats.
     *
     * @param rs Ligne de résultats
     * @return Adresse L'adresse récupérée.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Adresse parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            // Valorisation propriétés primitives et construction de l'objet
            // à retourner.
            return AdresseBuilder.getNewAdresseBuilder()
                    .dIdentifiant(rs.getInt("identifiant"))
                    .deNumeroRue(rs.getString("numRue"))
                    .deNomRue(rs.getString("nomRue"))
                    .deCodePostal(rs.getString("codePostal"))
                    .deVille(rs.getString("ville"))
                    .build();
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du " + "client depuis la base de données.", e);
        }
    }
}
