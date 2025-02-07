package DAO.mysql;

import DAO.DAO;
import DAO.QueryAction;
import DAO.SocieteDatabaseException;
import entities.Societe;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

abstract public class SocieteMySqlDAO<T extends Societe> implements DAO {

    //******************************************
    // Partie implémentation.
    //******************************************

    protected String[] likeParameters;

    /**
     * Méthode pour trouver l'ensemble des sociétés.
     * @return Liste des sociétés.
     * @throws SocieteDatabaseException L'exception de lecture ou de
     * fermeture de la base de données.
     */
    @Override
    public ArrayList<T> findAll() throws SocieteDatabaseException {
        // Initialisation des variables.
        ArrayList<T> results = new ArrayList<>();
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
     * Méthode pour trouver un objet de type T à partir d'un tableau de
     * condition de sélection.
     * @param selection Tableau de sélection.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException L'exception concernant la lecture ou
     * la fermeture de la base de données.
     */
    @Override
    public T find(String[][] selection) throws SocieteDatabaseException {
        // Initialisation des variables.
        T obj = null;
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

                if(Arrays.asList(likeParameters).contains(selection[i][0])){
                    // Propriété de sélection nécessite le mot-clé LIKE.
                    stmt.setString(i+1, "%" + selection[i][1] + "%");
                }else{
                    // Propriété de sélection ne nécessite pas le mot-clé LIKE.
                    stmt.setString(i+1, selection[i][1]);
                }
            }

            // Exécution de la requête avec les propriétés liées.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultat existe, l'objet de type T peut
                // être valorisé.
                obj = this.parse(rs);
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

        return obj;
    }

    /**
     * Méthode pour trouver un objet de type T à partir de son identifiant.
     * @param id Identifiant de sélection.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException L'exception concernant la lecture ou
     * la fermeture de la base de données.
     */
    @Override
    public T findById(int id) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"identifiant", Integer.toString(id)}};

        // Retourne le client trouvé avec la condition de recherche.
        return find(selection);
    }

    public boolean save(Societe societe) {
        return false;
    }

    /**
     Méthode pour supprimer une société.
     * @param societe La société à supprimer.
     * @return Indication si la société a bien été supprimé ou non.
     * @throws SocieteDatabaseException L'exception de lecture de la base de
     * données.
     */
    public boolean delete(@NotNull T societe) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;
        int rowsAffected = 0;
        String[][] selection = {{"identifiant",
                String.valueOf(societe.getIdentifiant())}};

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
            case QueryAction.READ -> "SELECT * FROM `" + this.getTable() + "`";
            case QueryAction.CREATE ->
                    "INSERT INTO `" + this.getTable() + "`() " +
                            "VALUES ()";
            case QueryAction.UPDATE -> "UPDATE `" + this.getTable() + "`";
            case QueryAction.DELETE -> "DELETE FROM `" + this.getTable() + "`";
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

                if (Arrays.asList(likeParameters).contains(selection[i][0])) {
                    // Si le champ de la sélection est parmi les champs qui
                    // nécessitent un LIKE.
                    query.append(selection[i][0]).append(" LIKE ").append("?");
                } else {
                    // Si le champ de la sélection nécessite une égalité.
                    query.append(selection[i][0]).append(" = ").append("?");
                }
            }
        }

        // Retourne la requête sous forme d'une chaîne de caractères.
        return query.toString();
    }

    /**
     * Méthode qui définit les paramètres nécessitant le mot-clé LIKE.
     * @param likeParameters Tableau de chaîne de caractères correspondant
     *                       aux propriétés nécessitant le mot-clé LIKE.
     */
    protected void defineLikeParameters(String[] likeParameters){
        this.likeParameters = likeParameters;
    }

    //******************************************
    // Partie abstraite
    //******************************************

    abstract protected T parse(ResultSet rs) throws SocieteDatabaseException;

    abstract protected String getTable();
}
