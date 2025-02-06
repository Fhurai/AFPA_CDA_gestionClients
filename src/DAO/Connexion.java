package DAO;

import logs.LogManager;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class Connexion {

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

    private Connexion(){

    }

    public static Connection getInstance() throws SocieteDatabaseException {
        if (connexion == null){

            final Properties dataProperties = new Properties();

            File fichier = new File("database.properties");
            FileInputStream input = null;
            try {
                input = new FileInputStream(fichier);
                dataProperties.load(input);

                connexion = DriverManager.getConnection(
                        dataProperties.getProperty("url"),
                        dataProperties.getProperty("login"),
                        dataProperties.getProperty("password")
                );
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
