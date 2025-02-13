package DAO.filesystem;

import DAO.DAOFactory;
import DAO.SocieteDAO;
import DAO.SocieteDatabaseException;
import entities.Client;
import entities.Prospect;
import logs.LogManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class FilesystemFactory implements DAOFactory {

    /**
     * Méthode pour obtenir un objet DAO Filesystem pour contrat.
     *
     * @return objet DAO Filesystem pour contrat.
     */
    @Contract(" -> new")
    public static @NotNull ContratFilesystemDAO getContratDAO() {
        return new ContratFilesystemDAO();
    }
    /**
     * Méthode d'initialisation de l'usine et de la connexion DAO.
     *
     * @throws SocieteDatabaseException Exception lors de l'ouverture de la
     *                                  connexion.
     */
    @Override
    public void init() throws SocieteDatabaseException {
        ConnexionFilesystem.getInstance();
    }

    /**
     * Méthode de fermeture de la connexion.
     *
     * @throws SocieteDatabaseException Exception lors de la fermeture de la
     *                                  connexion.
     */
    @Override
    public void close() throws SocieteDatabaseException {
        // Log de la fermeture et fermeture de la connexion.
        LogManager.logs.log(Level.INFO, "Database fermée");
        ConnexionFilesystem.close();
    }

    /**
     * Getter classe DAO client.
     *
     * @return DAO client.
     */
    @Override
    public SocieteDAO<Client> getClientDAO() {
        return new ClientFilesystemDAO();
    }

    /**
     * Getter classe DAO prospect.
     *
     * @return DAO prospect.
     */
    @Override
    public SocieteDAO<Prospect> getProspectDAO() {
        return new ProspectFilesystemDAO();
    }
}
