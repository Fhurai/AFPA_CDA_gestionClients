package DAO.mysql;

import DAO.DAO;
import DAO.QueryAction;
import DAO.SocieteDatabaseException;
import entities.Adresse;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class AdresseMySqlDAO implements DAO<Adresse> {


    /**
     * Méthode pour trouver l'ensemble des adresses.
     *
     * @return Liste des adresses.
     * @throws SocieteDatabaseException L'exception de lecture ou de
     *                                  fermeture de la base de données.
     */
    @Override
    public ArrayList<Adresse> findAll() throws SocieteDatabaseException {
        // Initialisation des variables.
        ArrayList<Adresse> results = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        Statement stmt;
        String query = this.getQuery(QueryAction.READ, null);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des clients.
                results.add(this.parse(rs));
            }
        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de " +
                    "la base de données.");
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

        // Retourne la liste des sociétés.
        return results;
    }

    /**
     * @param selection Tableau de sélection.
     * @return
     * @throws SocieteDatabaseException
     */
    @Override
    public Adresse find(String[][] selection) throws SocieteDatabaseException {
        // Initialisation des variables.
        Adresse adresse = new Adresse();
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = getQuery(QueryAction.READ, selection);

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            for (int i = 0; i < selection.length; i++) {
                // Parcours des propriétés de sélection.

                stmt.setString(i+1, "%" + selection[i][1] + "%");
            }

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
            throw new SocieteDatabaseException("Erreur lors de la lecture de " +
                    "la base de données.");
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

        return adresse;
    }

    /**
     * @param id Identifiant de sélection.
     * @return
     * @throws SocieteDatabaseException
     */
    @Override
    public Adresse findById(int id) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"identifiant", Integer.toString(id)}};

        // Retourne le client trouvé avec la condition de recherche.
        return find(selection);
    }

    /**
     * @param adresse L'objet à sauvegarder.
     * @return
     */
    @Override
    public boolean save(Adresse adresse) {
        return false;
    }

    /**
     * @param adresse L'objet à supprimer.
     * @return
     * @throws SocieteDatabaseException
     */
    @Override
    public boolean delete(Adresse adresse) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;
        int rowsAffected = 0;
        String[][] selection = {{"identifiant",
                String.valueOf(adresse.getIdentifiant())}};

        // Récupération de la requête de suppression.
        String query = getQuery(QueryAction.DELETE, selection);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setString(1, selection[0][1]);
            rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la lecture de " +
                    "la base de données.");
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

        // Retourne l'indication si la requête a modifié une et une seule
        // ligne d'enregistrement.
        return rowsAffected == 1;
    }

    /***
     * Méthode qui donne la requête correspondante à l'action de requête et à
     * la sélection données en paramètres.
     * @param action L'action de requête à accomplir.
     * @param selection Le tableau de conditions à remplir pour la requête.
     * @return La requête générée sous forme d'une chaîne de caractères.
     */
    private @NotNull String getQuery(@NotNull QueryAction action, String[][] selection) {
        // Initialisation du stringBuilder query à partir de la variable
        // action donnée en paramètre.
        StringBuilder query = new StringBuilder(switch (action) {
            case QueryAction.READ -> "SELECT * FROM `adresses`";
            case QueryAction.CREATE -> "INSERT INTO `adresses`() " +
                    "VALUES ()";
            case QueryAction.UPDATE -> "UPDATE `adresses`";
            case QueryAction.DELETE -> "DELETE FROM `adresses`";
        });

        if (selection != null) {
            // La sélection n'est pas vide, il faut ajouter une condition au
            // minimum.

            for (int i = 0; i < selection.length; i++) {
                // Parcours de la ligne des conditions de sélection.

                if (i == 0) {
                    // Première condition, utilisation du WHERE.
                    query.append(" WHERE ");
                } else {
                    // Condition différente de la première, utilisation du AND.
                    query.append(" AND ");
                }

                // Si le champ de la sélection est parmi les champs qui
                // nécessitent un LIKE.
                query.append(selection[i][0]).append(" LIKE ").append("?");
            }
        }

        // Retourne la requête sous forme d'une chaîne de caractères.
        return query.toString();
    }

    /**
     * Méthode pour récupérer un client de la data donnée en argument.
     *
     * @param rs La ligne d'enregistrement.
     * @return L'objet Client tiré des données.
     */
    protected Adresse parse(ResultSet rs) throws SocieteDatabaseException {
        Adresse adresse = new Adresse();

        try {
            adresse.setIdentifiant(rs.getInt("identifiant"));
            adresse.setNumeroRue(rs.getString("numRue"));
            adresse.setNomRue(rs.getString("nomRue"));
            adresse.setCodePostal(rs.getString("codePostal"));
            adresse.setVille(rs.getString("ville"));
        } catch (SocieteEntityException e) {
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        }

        return adresse;
    }
}
