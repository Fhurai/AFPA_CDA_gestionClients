package DAO.mysql;

import DAO.SocieteDatabaseException;
import entities.Adresse;
import entities.Client;
import entities.Societe;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Classe DAO pour les clients.
 */
public class ClientsMySqlDAO extends SocieteMySqlDAO<Client> {

    /**
     * Constructeur.
     */
    public ClientsMySqlDAO() {
        this.defineLikeParameters(new String[]{"raisonSociale", "commentaires"});
    }

    /**
     * Méthode pour récupérer un client de la data donnée en argument.
     * @param rs La ligne d'enregistrement.
     * @return L'objet Client tiré des données.
     */
    @Override
    protected Client parse(ResultSet rs) throws SocieteDatabaseException {
        Client client = new Client();

        try {
            client.setIdentifiant(rs.getInt("identifiant"));
            client.setRaisonSociale(rs.getString("raisonSociale"));
            client.setTelephone(rs.getString("telephone"));
            client.setMail(rs.getString("mail"));
            client.setCommentaires(rs.getString("commentaires"));
            client.setChiffreAffaires(rs.getLong("chiffreAffaires"));
            client.setNbEmployes(rs.getInt("nbEmployes"));

            Adresse adresse =
                    MySqlFactory.getAdresseDAO().findById(rs.getInt(
                            "idAdresse"));
            client.setAdresse(adresse);
        } catch (SocieteEntityException | SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        }

        return client;
    }

    /**
     * Méthode qui retourne le nom de la table de la classe DAO.
     * @return Nom de la table.
     */
    @Override
    protected String getTable() {
        return "clients";
    }

    public Client findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"raisonSociale", raisonSociale}};

        // Retourne le client trouvé avec la condition de recherche.
        return this.find(selection);
    }

    /**
     * @param o L'objet à sauvegarder.
     * @return
     */
    @Override
    public boolean save(Object o) {
        return false;
    }

    /**
     * @param client Le client à supprimer.
     * @return Indication si le client a été supprimé.
     * @throws SocieteDatabaseException L'exception de lecture de la base de
     * données.
     */
    @Override
    public boolean delete(@NotNull Client client) throws SocieteDatabaseException {
        return super.delete(client);
    }
}
