package DAO;

import DAO.filesystem.FilesystemFactory;
import DAO.mongo.MongoFactory;
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
    public DAOFactory getFactory() throws SocieteDatabaseException {
        DAOFactory daoFactory = null;
        if (typeDatabase == null) {
            throw new SocieteDatabaseException("Aucun type de base de données" +
                    " n'a été définie");
        } else if (typeDatabase == TypeDatabase.MYSQL) {
            daoFactory = new MySqlFactory();
        } else if (typeDatabase == TypeDatabase.MONGODB) {
            daoFactory = new MongoFactory();
        }else if(typeDatabase == TypeDatabase.FILESYSTEM){
            daoFactory = new FilesystemFactory();
        } else {
            throw new SocieteDatabaseException("Erreur inconnue");
        }
        return daoFactory;
    }
}
