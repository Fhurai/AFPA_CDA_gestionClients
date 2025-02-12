package DAO.mysql;

import DAO.SocieteDatabaseException;
import logs.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class ConnexionMySql {

    private static Connection connexion = null;

    static{
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                if (connexion != null){
                    try{
                        LogManager.logs.log(Level.INFO, "Database fermée");
                        connexion.close();
                    }catch(SQLException e){
                        LogManager.logs.log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        });
    }

    private ConnexionMySql(){

    }

    public static Connection getInstance() throws SocieteDatabaseException {
        if (connexion == null){

            final Properties dataProperties = new Properties();

            File fichier = new File("database.mysql.properties");
            FileInputStream input = null;
            try {
                input = new FileInputStream(fichier);
                dataProperties.load(input);

                connexion = DriverManager.getConnection(
                        dataProperties.getProperty("url"),
                        dataProperties.getProperty("login"),
                        dataProperties.getProperty("password")
                );
                LogManager.logs.log(Level.INFO, "Database ouverte");
            } catch (FileNotFoundException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Fichier de paramétrage " +
                        "non trouvé.");
            } catch (IOException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Fichier de paramétrage " +
                        "rencontre un problème.");
            } catch (SQLException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("La connexion n'a pu être " +
                        "ouverte");
            }
        }
        return connexion;
    }
}
