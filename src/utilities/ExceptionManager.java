package utilities;

import DAO.SocieteDatabaseException;
import entities.SocieteEntityException;
import logs.LogManager;

import javax.swing.*;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.logging.Level;

public class ExceptionManager {

    static ExceptionManager instance = new ExceptionManager();

    public ExceptionManager() {}

    public void consumeException(Exception e) {
        if(e.getClass().equals(SocieteDatabaseException.class)|| e.getClass() == SocieteEntityException.class) {
            this.afficheException(e.getMessage());
        } else {
            LogManager.logs.log(Level.INFO, e.getMessage());
            this.afficheException("ExceptionManager : Exception inconnue à la" +
                    " consommation");
        }

        if(e.getClass().equals(SocieteDatabaseException.class)) {
            System.exit(1);
        }
    }

    public void propagateDatabaseException(Exception e) throws SocieteDatabaseException {
        if(!e.getClass().equals(SocieteDatabaseException.class)) {
            if(e.getClass().equals(SocieteEntityException.class)) {
                throw new SocieteDatabaseException(e.getMessage(), e);
            }

            if(e.getClass().equals(SQLException.class)) {
                this.logException(e.getMessage());
                throw new SocieteDatabaseException(this.translateSqlState((SQLException) e), e);
            }
        }
    }

    public void propagateEntityException(Exception e) throws SocieteEntityException {
        if(!e.getClass().equals(SocieteEntityException.class)) {
            if(e.getClass().equals(DateTimeException.class)) {
                throw new SocieteEntityException("La valeur de la date de " +
                        "prospection est incorrecte", e);
            }

            if(e.getClass().equals(NumberFormatException.class)) {
                throw new SocieteEntityException("La valeur du chiffre " +
                        "d'affaires n'est pas valide", e);
            }
        }
    }

    private void afficheException(String message){
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void logException(String message){
        LogManager.logs.log(Level.SEVERE, message);
    }

    private String translateSqlState(SQLException e){
        String ret = "";
        switch (e.getErrorCode()){
            case 1602:
                ret = "La raison sociale existe déjà";
            default:
                ret = "Erreur SQL non reconnue";
                break;
        }
        return ret;
    }
}
