package DAO;

import entities.Adresse;
import entities.Client;
import entities.Contrat;
import entities.Prospect;

import java.sql.Connection;

public interface DAOFactory {

    Connection getInstance() throws SocieteDatabaseException;

    SocieteDAO<Client> getClientDAO();

    SocieteDAO<Prospect> getProspectDAO();
}
