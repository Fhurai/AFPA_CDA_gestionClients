package DAO;

import entities.Adresse;
import entities.Client;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class AdresseDAO {

    private static @NotNull Adresse parseAdresse(@NotNull ResultSet rs) throws SocieteDatabaseException {

        // Initialisation du client.
        Adresse adresse = new Adresse();

        try {
            // Valorisation de l'objet Adresse.
            adresse.setIdentifiant(rs.getInt("identifiant"));
            adresse.setNumeroRue(rs.getString("numRue"));
            adresse.setNomRue(rs.getString("nomRue"));
            adresse.setCodePostal(rs.getString("codePostal"));
            adresse.setVille(rs.getString("ville"));

        } catch (SQLException | SocieteEntityException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.toString());
            throw new SocieteDatabaseException("Erreur lors de la " +
                    "récupération du client.");
        }

        // Aucune erreur attrapée, l'adresse valorisée est retournée.
        return adresse;
    }

    /**
     * Méthode qui donne la requête correspondante à l'action de requête et à
     * la sélection données en paramètres.
     *
     * @param action    L'action de requête à accomplir.
     * @param selection Le tableau de conditions à remplir pour la requête.
     * @return La requête générée sous forme d'une chaîne de caractères.
     */
    private static @NotNull String getQuery(@NotNull QueryAction action, String[][] selection) {

        // Initialisation du stringBuilder query à partir de la variable
        // action donnée en paramètre.
        StringBuilder query = switch (action) {
            case QueryAction.READ -> new StringBuilder("SELECT * " +
                    "FROM `adresses`");
            case QueryAction.CREATE ->
                    new StringBuilder("INSERT INTO `adresses`( " +
                            "    `numRue`, " +
                            "    `nomRue`, " +
                            "    `codePostal`, " +
                            "    `ville` " +
                            ") " +
                            "VALUES(" +
                            "    ?, " +
                            "    ?, " +
                            "    ?, " +
                            "    ?)");
            case QueryAction.UPDATE -> new StringBuilder("UPDATE  `adresses` " +
                    "SET `numRue` = ?, " +
                    "    `nomRue` = ?, " +
                    "    `codePostal` = ?, " +
                    "    `ville` = ?");
            case QueryAction.DELETE ->
                    new StringBuilder("DELETE FROM adresses");
        };

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
                query.append(selection[i][0]).append(" = ?");
            }
        }

        // Retourne la requête sous forme d'une chaîne de caractères.
        return query.toString();
    }

    public static Adresse find(int identifiant) throws SocieteDatabaseException {
        // Initialisation des variables.
        Adresse adresse = null;
        Connection con = Connexion.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String[][] selection = {{"identifiant", String.valueOf(identifiant)}};
        String query = getQuery(QueryAction.READ, selection);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setString(1, selection[0][1]);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultats existe, l'adresse peut être
                // valorisée.
                adresse = parseAdresse(rs);
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

        // Retourne l'adresse trouvé.
        return adresse;
    }

    public static void delete(int identifiant) throws SocieteDatabaseException {
        Connection con = Connexion.getInstance();
        PreparedStatement stmt;


        String[][] selection = {{"identifiant", String.valueOf(identifiant)}};
        String query = getQuery(QueryAction.DELETE, selection);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setString(1, selection[0][1]);
            stmt.executeUpdate();
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
    }
}
