package DAO.filesystem;

import DAO.SocieteDatabaseException;
import entities.Adresse;
import entities.Client;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Classe DAO Filesystem pour les clients.
 */
public class ClientFilesystemDAO extends SocieteFilesystemDAO<Client> {

    /**
     * Constructor
     */
    public ClientFilesystemDAO() {
    }

    /**
     * Méthode pour rechercher un client.
     *
     * @param identifiant Identifiant du client.
     * @return Client recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Client findById(int identifiant) throws SocieteDatabaseException {
        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des clients
        FilesystemTable table = db.getFilesystemTable("clients");

        // Récupération du premier enregistrement correspondant à
        // l'identifiant donné et transformation en client.
        Client client = this.parse(table.get(identifiant));

        // Récupération des contrats du client.
        client.setContrats(FilesystemFactory.getContratDAO().findByIdClient(identifiant));

        // Retourne le client trouvé.
        return client;
    }

    /**
     * Méthode pour récupérer l'ensemble des clients.
     *
     * @return l'ensemble des clients.
     */
    @Override
    public ArrayList<Client> findAll() throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Client> clients = new ArrayList<>();

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des clients
        FilesystemTable table = db.getFilesystemTable("clients");

        if (table != null) {
            // La table existe, parcours des enregistrements.
            for (String[] record : table.getRecords()) {

                // Transformation de l'enregistrement en client qui est
                // ajouté à la liste.
                Client client = this.parse(record);
                client.setContrats(FilesystemFactory.getContratDAO().findByIdClient(Integer.parseInt(record[0])));
                clients.add(client);
            }
        } else {
            // Table non trouvée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, "Table clients not found");
            throw new SocieteDatabaseException("Erreur lors de la lecture des" +
                    " clients");
        }

        // Retourne la liste des clients.
        return clients;
    }

    /**
     * Méthode pour rechercher un client.
     *
     * @param name Nom du client.
     * @return Client recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Client find(String name) throws SocieteDatabaseException {
        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des clients
        FilesystemTable table = db.getFilesystemTable("clients");

        // Récupération du premier enregistrement correspondant au nom donné et
        // transformation en client.
        Client client = this.parse(table.find(name));

        // Récupération des contrats du client.
        client.setContrats(FilesystemFactory.getContratDAO().findByIdClient(client.getIdentifiant()));

        // Retourne le client trouvé.
        return client;
    }

    /**
     * Méthode pour supprimer un client.
     *
     * @param obj Le client à supprimer.
     * @return Indication que l'objet a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Client obj) throws SocieteDatabaseException {
        // Initialisation de la variable.
        boolean ret;

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des clients
        FilesystemTable table = db.getFilesystemTable("clients");

        // Suppression du client dans la table.
        ret = table.delete(obj.getIdentifiant());

        // Sauvegarde de la table.
        table.closeTable();

        // Retourne si la suppression s'est bien passé ou non.
        return ret;
    }

    /**
     * Méthode qui sauvegarde un client, soit en le créant, soit en le
     * modifiant.
     *
     * @param obj Le client à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(@NotNull Client obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        boolean ret;
        String[] record;

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des clients
        FilesystemTable table = db.getFilesystemTable("clients");

        record = (obj.getIdentifiant() > 0) ?
                table.get(obj.getIdentifiant()) : new String[11];

        record[0] = String.valueOf(obj.getIdentifiant());
        record[1] = obj.getRaisonSociale();
        record[2] = obj.getAdresse().getNumeroRue();
        record[3] = obj.getAdresse().getNomRue();
        record[4] = obj.getAdresse().getCodePostal();
        record[5] = obj.getAdresse().getVille();
        record[6] = obj.getTelephone();
        record[7] = obj.getMail();
        record[8] = obj.getCommentaires().replace("\n", "").replace("\r", "");
        record[9] = String.valueOf(obj.getChiffreAffaires());
        record[10] = String.valueOf(obj.getNbEmployes());

        ret = (obj.getIdentifiant() > 0) ? table.update(record) : table.create(record);

        table.closeTable();

        // Retourne le résultat booléen des vérifications.
        return ret;
    }

    /**
     * Méthode qui construit un Client à partir d'un enregistrement.
     *
     * @param record enregistrement de résultats.
     * @return Le client récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Client parse(String[] record) throws SocieteDatabaseException {
        // Initialisation client.
        Client client = new Client();

        try {
            // Valorisation propriétés primitives.
            client.setIdentifiant(Integer.parseInt(record[0]));
            client.setRaisonSociale(record[1]);
            client.setTelephone(record[6]);
            client.setMail(record[7]);
            client.setCommentaires(record[8]);
            client.setChiffreAffaires(Long.parseLong(record[9]));
            client.setNbEmployes(Integer.parseInt(record[10]));

            // Valorisation propriétés objets.
            Adresse adresse = new Adresse(
                    Integer.parseInt(record[0]),
                    record[2],
                    record[3],
                    record[4],
                    record[5]);
            client.setAdresse(adresse);
        } catch (SocieteEntityException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du client depuis la base de données.");
        }

        // Retourne le client valorisé.
        return client;
    }
}
