package DAO;

import DAO.mysql.ConnexionMySql;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public abstract class DAO<T> {

    protected String[] likeProperties;

    public DAO(String[] likeProperties) {
        this.likeProperties = likeProperties;
    }

    /**
     * Méthode to get query string depending of query action & selection
     * conditions.
     * @return Generated query string
     */
    protected String getQueryString(@NotNull QueryAction action, String[][] selection){
        // Initialisation du stringBuilder query à partir de la variable
        // action donnée en paramètre.
        StringBuilder query = new StringBuilder(switch (action) {
            case QueryAction.READ -> "SELECT * FROM `" + this.getTable() + "`";
            case QueryAction.CREATE ->
                    "INSERT INTO `" + this.getTable() +
                    "`("+String.join(", ", this.getTablePropertiesLabels())
                    +") VALUES ("+this.getTablePropertiesToken()+")";
            case QueryAction.UPDATE -> "UPDATE `" + this.getTable() + "`";
            case QueryAction.DELETE -> "DELETE FROM `" + this.getTable() + "`";
        });

        if (selection != null) {
            // La sélection n'est pas vide, il faut ajouter une condition au
            // minimum.

            for (int i = 0; i < selection.length ; i++) {
                // Parcours de la ligne des conditions de sélection.

                if (i == 0) {
                    // Première condition, utilisation du WHERE.
                    query.append(" WHERE ");
                } else {
                    // Condition différente de la première, utilisation du AND.
                    query.append(" AND ");
                }

                if (Arrays.asList(likeProperties).contains(selection[i][0])) {
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
     * Méthode pour récupérer l'ensemble des objets de type T.
     * @return l'ensemble des objets de type T.
     */
    public ArrayList<T> findAll() throws SocieteDatabaseException{
        ArrayList<T> results = new ArrayList<>();
        Connection con = ConnexionMySql.getInstance();
        Statement stmt = null;
        String query = this.getQueryString(QueryAction.READ, null);

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
     * Méthode pour rechercher un objet de type T à partir d'un tableau de
     * sélection.
     * @param selection Tableau de sélection.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException
     */
    protected T find(String[][] selection) throws SocieteDatabaseException {
        T obj = null;
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = getQueryString(QueryAction.READ, selection);

        try {
            // Préparation de la requête.
            stmt = con.prepareStatement(query);

            for (int i = 0; i < selection.length; i++) {
                // Parcours des propriétés de sélection.

                if(Arrays.asList(likeProperties).contains(selection[i][0])){
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
     * Méthode pour rechercher un objet de type T à partir de son identifiant.
     * @param id Identifiant de l'objet recherché.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException
     */
    public T findById(int id) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"identifiant", Integer.toString(id)}};

        // Retourne le client trouvé avec la condition de recherche.
        return find(selection);
    }

    /**
     * Méthode pour supprimer un objet de type T
     * @param obj L'objet à supprimer.
     * @return Indication que l'objet a bien été supprimé.
     * @throws SocieteDatabaseException
     */
    public boolean delete(T obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;
        int rowsAffected = 0;
        String[][] selection = selectByPrimaryKey(obj);

        // Récupération de la requête de suppression.
        String query = getQueryString(QueryAction.DELETE, selection);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            this.bindPrimaryKey(obj, stmt);
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

    public boolean create(T obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        Connection con = ConnexionMySql.getInstance();
        PreparedStatement stmt = null;

        // Récupération de la requête de suppression.
        String query = getQueryString(QueryAction.CREATE, null);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.bindTableProperties(obj, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.setPrimaryKey(obj, rs);
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

        return false;
    }

    /**
     * Méthode pour récupérer la table de l'objet DAO.
     * @return La table de l'objet DAO.
     */
    protected abstract String getTable();

    protected abstract T parse(ResultSet rs) throws SocieteDatabaseException;

    protected abstract String[][] selectByPrimaryKey(T obj) throws SocieteDatabaseException;

    protected abstract void bindPrimaryKey(T obj, PreparedStatement stmt) throws SocieteDatabaseException;

    protected abstract String[] getTablePropertiesLabels();

    protected String getTablePropertiesToken(){
        StringBuilder tokens = new StringBuilder();
        int counter = 0;
        for (String s : Arrays.asList(this.getTablePropertiesLabels())) {
            tokens.append(counter > 0 ? ", " : "").append(" ? ");
            counter++;
        }

        return tokens.toString();
    }

    protected abstract void bindTableProperties(T obj, PreparedStatement stmt) throws SocieteDatabaseException;

    protected abstract void setPrimaryKey(T obj, ResultSet rs) throws SocieteDatabaseException;
}
