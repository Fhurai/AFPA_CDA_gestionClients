package DAO;

import DAO.filesystem.FilesystemFactory;
import DAO.mongo.MongoFactory;
import DAO.mysql.MySqlFactory;
import logs.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

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
    public static void setTypeDatabase(TypeDatabase typeDatabase) throws SocieteDatabaseException {

        // Partie sauvegarde de la dernière connexion.
        String path = "config/connexion.csv";
        File f = new File(path);

        if (f.exists()) {
            // Écrivain de la table est initialisé.
            try {
                FileWriter fw = new FileWriter(path);

                fw.write(typeDatabase.getName());

                fw.close();
            } catch (IOException e) {
                throw new SocieteDatabaseException("Erreur lors de la " +
                        "sauvegarde de la dernière base de données.");
            }
        }else{
            LogManager.logs.log(Level.SEVERE, "Fichier configuration manquant");
            throw new SocieteDatabaseException("Fichier configuration manquant");
        }

        AbstractFactory.typeDatabase = typeDatabase;
    }

    /**
     * Méthode qui donne la factory à utiliser pour la connexion choisie.
     *
     * @return La factory.
     */
    public DAOFactory getFactory() throws SocieteDatabaseException {
        DAOFactory daoFactory;
        if (typeDatabase == null) {
            throw new SocieteDatabaseException("Aucun type de base de données" +
                    " n'a été définie");
        } else if (typeDatabase == TypeDatabase.MYSQL) {
            daoFactory = new MySqlFactory();
        } else if (typeDatabase == TypeDatabase.MONGODB) {
            daoFactory = new MongoFactory();
        } else if (typeDatabase == TypeDatabase.FILESYSTEM) {
            daoFactory = new FilesystemFactory();
        } else {
            throw new SocieteDatabaseException("Erreur inconnue");
        }
        return daoFactory;
    }

    public static void setDefaultDatabase() throws SocieteDatabaseException {
        // Partie sauvegarde de la dernière connexion.
        String path = "config/connexion.csv";
        File f = new File(path);

        if(f.exists()){
            try {
                Scanner sc = new Scanner(f);

                // Récupération de la ligne depuis le fichier.
                String line = sc.nextLine();

                AbstractFactory.setTypeDatabase(TypeDatabase.findByString(line));

            } catch (FileNotFoundException e) {
                // Exception attrapée, log de l'exception et lancement d'une
                // nouvelle exception plus lisible pour l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Erreur lors du chargement" +
                        " de la base de données.");
            }
        }else{
            LogManager.logs.log(Level.SEVERE, "Fichier configuration manquant");
            throw new SocieteDatabaseException("Fichier configuration manquant");
        }
    }
}
