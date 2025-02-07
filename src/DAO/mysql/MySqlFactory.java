package DAO.mysql;

/**
 * Classe factory pour les objets DAO MySql.
 */
public class MySqlFactory {

    /**
     * Méthode pour obtenir un objet DAO MySql pour adresse.
     * @return objet DAO MySql pour adresse.
     */
    public static AdresseMySqlDAO getAdresseDAO() {
        return new AdresseMySqlDAO();
    }

    /**
     * Méthode pour obtenir un objet DAO MySql pour client.
     * @return objet DAO MySql pour client.
     */
    public static ClientsMySqlDAO getClientsDAO() {
        return new ClientsMySqlDAO();
    }

    public static ProspectsMySqlDAO getProspectsDAO() {
        return new ProspectsMySqlDAO();
    }
}
