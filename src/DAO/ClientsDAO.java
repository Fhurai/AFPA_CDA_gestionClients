package DAO;

import entities.Adresse;
import entities.Client;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Classe DAO pour les clients.
 */
public class ClientsDAO {

    // Initialisation du tableau des champs String qui utilisent
    // un LIKE et non une égalité.
    static String[] likeParameters = new String[]{"raisonSociale",
            "commentaires"};

    /**
     * Méthode qui analyse une ligne de résultat pour en sortir un client.
     * @param rs La ligne de résultats.
     * @return Le client issu de la ligne de résultats.
     * @throws SocieteDatabaseException L'exception si un client n'a pu être
     * récupéré de la ligne de résultats.
     */
    private static @NotNull Client parseClient(@NotNull ResultSet rs) throws SocieteDatabaseException {

        // Initialisation du client.
        Client client = new Client();

        try {
            // Valorisation des champs primitifs du client.
            client.setIdentifiant(rs.getInt("identifiant"));
            client.setRaisonSociale(rs.getString("raisonSociale"));
            client.setTelephone(rs.getString("telephone"));
            client.setMail(rs.getString("mail"));
            client.setCommentaires(rs.getString("commentaires"));
            client.setChiffreAffaires(rs.getLong("chiffreAffaires"));
            client.setNbEmployes(rs.getInt("nbEmployes"));

            // Valorisation de l'objet Adresse du client.
            Adresse adresse = AdresseDAO.find(rs.getInt("idAdresse"));
            client.setAdresse(adresse);

        } catch (SQLException | SocieteEntityException e) {
            // Exception attrapée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, e.toString());
            throw new SocieteDatabaseException("Erreur lors de la " +
                    "récupération du client.");
        }

        // Aucune erreur attrapée, le client valorisé est retourné.
        return client;
    }

    /**
     * Méthode qui donne la requête correspondante à l'action de requête et à
     * la sélection données en paramètres.
     * @param action L'action de requête à accomplir.
     * @param selection Le tableau de conditions à remplir pour la requête.
     * @return La requête générée sous forme d'une chaîne de caractères.
     */
    private static @NotNull String getQuery(@NotNull QueryAction action, String[][] selection) {

        // Initialisation du stringBuilder query à partir de la variable
        // action donnée en paramètre.
        StringBuilder query = switch (action) {
            case QueryAction.READ -> new StringBuilder("SELECT * " +
                    "FROM `clients`");
            case QueryAction.CREATE -> new StringBuilder("CREATE");
            case QueryAction.UPDATE -> new StringBuilder("UPDATE  `clients` " +
                    "SET `raisonSociale` = ?, " +
                    "    `telephone` = ?, " +
                    "    `mail` = ?, " +
                    "    `commentaires` = ?, " +
                    "    `chiffreAffaires` = ?, " +
                    "    `nbEmployes` = ?, " +
                    "    `idAdresse` = ?");
            case QueryAction.DELETE -> new StringBuilder("DELETE FROM clients");
        };

        if(selection != null) {
            // La sélection n'est pas vide, il faut ajouter une condition au
            // minimum.

            for (int i = 0; i < selection.length; i++) {
                // Parcours de la ligne des conditions de sélection.

                if(i == 0){
                    // Première condition, utilisation du WHERE.
                    query.append(" WHERE ");
                }else{
                    // Condition différente de la première, utilisation du AND.
                    query.append(" AND ");
                }

                if(Arrays.asList(likeParameters).contains(selection[i][0])) {
                    // Si le champ de la sélection est parmi les champs qui
                    // nécessitent un LIKE.
                    query.append(selection[i][0]).append(" LIKE ?");
                }else{
                    // Si le champ de la sélection nécessite une égalité.
                    query.append(selection[i][0]).append(" = ").append("?");
                }
            }
        }

        // Retourne la requête sous forme d'une chaîne de caractères.
        return query.toString();
    }

    /**
     * Méthode pour trouver l'ensemble des clients de la base de données.
     * @return Liste de clients.
     * @throws SocieteDatabaseException L'exception de lecture ou de
     * fermeture de la base de données.
     */
    public static @NotNull ArrayList<Client> findAll() throws SocieteDatabaseException {
        // Initialisation des variables.
        ArrayList<Client> allClients = new ArrayList<>();
        Connection con = Connexion.getInstance();
        Statement stmt;
        String query = getQuery(QueryAction.READ, null);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                // Tant qu'une ligne de données est disponible, ajout du
                // client issu de celle-ci dans la liste des clients.
                allClients.add(parseClient(rs));
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

        // Retourne la liste des clients.
        return allClients;
    }

    /**
     * Méthode pour trouver un client à partir d'un tableau de
     * conditions de sélection.
     * @param selection Tableau de sélection.
     * @return Client recherché.
     * @throws SocieteDatabaseException L'exception concernant la lecture ou
     * la fermeture de la base de données.
     */
    public static Client find(String[][] selection) throws SocieteDatabaseException {
        // Initialisation des variables.
        Client client = null;
        Connection con = Connexion.getInstance();
        PreparedStatement stmt;

        // Récupération de la requête de lecture à partir du tableau de
        // conditions de sélection.
        String query = getQuery(QueryAction.READ, selection);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            for (int i = 0; i < selection.length; i++) {
                if(Arrays.asList(likeParameters).contains(selection[i][0])){
                    stmt.setString(i+1, "%" + selection[i][1] + "%");
                }else{
                    stmt.setString(i+1, selection[i][1]);
                }
            }
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Si une ligne de résultats existe, le client peut être
                // valorisé.
                client = parseClient(rs);
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

        // Retourne le client trouvé.
        return client;
    }

    /**
     * Méthode pour trouver un client à partir de sa raison sociale.
     * @param raisonSociale La raison sociale recherchée.
     * @return Client avec la raison sociale.
     * @throws SocieteDatabaseException L'exception concernant la lecture ou
     * la fermeture de la base de données
     */
    public static Client findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"raisonSociale", raisonSociale}};

        // Retourne le client trouvé avec la condition de recherche.
        return find(selection);
    }

    /**
     * Méthode pour trouver un client à partir de son identifiant.
     * @param identifiant L'identifiant recherché.
     * @return Client avec la raison sociale.
     * @throws SocieteDatabaseException L'exception concernant la lecture ou
     * la fermeture de la base de données
     */
    public static Client findByIdentifiant(int identifiant) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"identifiant", Integer.toString(identifiant)}};

        // Retourne le client trouvé avec la condition de recherche.
        return find(selection);
    }

    public static void delete(Client client) throws SocieteDatabaseException {
        Connection con = Connexion.getInstance();
        PreparedStatement stmt;

        String[][] selection = {{"identifiant", String.valueOf(client.getIdentifiant())}};
        String query = getQuery(QueryAction.DELETE, selection);

        try {
            // Création de l'objet requête et exécution de celle-ci.
            stmt = con.prepareStatement(query);
            stmt.setString(1, selection[0][1]);
            System.out.println(stmt.toString());
            stmt.executeUpdate();

            AdresseDAO.delete(client.getAdresse().getIdentifiant());
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
