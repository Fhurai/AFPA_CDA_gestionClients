package DAO;

import DAO.mysql.MySqlFactory;

/**
 * Classe abstraite des factories.
 */
public class AbstractFactory {

    // Variable de classe.
    private static TypeDatabase typeDatabase;

    /**
     * Getter type database.
     *
     * @return Type database.
     */
    public static TypeDatabase getTypeDatabase() {
        return typeDatabase;
    }

    /**
     * Setter type database.
     *
     * @param typeDatabase Le nouveau type de database.
     */
    public static void setTypeDatabase(TypeDatabase typeDatabase) {
        AbstractFactory.typeDatabase = typeDatabase;
    }

    /**
     * Méthode qui donne la factory à utiliser pour la connexion choisie.
     *
     * @return La factory.
     */
    public DAOFactory getFactory() {
        return new MySqlFactory();
    }
}
