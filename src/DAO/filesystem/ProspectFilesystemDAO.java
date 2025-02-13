package DAO.filesystem;

import DAO.SocieteDatabaseException;
import builders.AdresseBuilder;
import builders.ProspectBuilder;
import entities.Adresse;
import entities.Prospect;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;
import utilities.Formatters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Classe DAO Filesystem pour les prospects.
 */
public class ProspectFilesystemDAO extends SocieteFilesystemDAO<Prospect> {

    /**
     * Constructor.
     */
    public ProspectFilesystemDAO() {
    }

    /**
     * Méthode pour rechercher un prospect.
     *
     * @param identifiant Identifiant du prospect.
     * @return prospect recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Prospect findById(int identifiant) throws SocieteDatabaseException {
        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des prospects
        FilesystemTable table = db.getFilesystemTable("prospects");

        // Récupération du premier enregistrement correspondant à
        // l'identifiant donné, transformation en client et retourne le client.
        return this.parse(table.get(identifiant));
    }

    /**
     * Méthode pour récupérer l'ensemble des prospects.
     *
     * @return l'ensemble des prospects.
     */
    @Override
    public ArrayList<Prospect> findAll() throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Prospect> prospects = new ArrayList<>();

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des prospects
        FilesystemTable table = db.getFilesystemTable("prospects");

        if (table != null) {
            // La table existe, parcours des enregistrements.
            for (String[] record : table.getRecords()) {

                // Transformation de l'enregistrement en prospect qui est
                // ajouté à la liste.
                Prospect prospect = this.parse(record);
                prospects.add(prospect);
            }
        } else {
            // Table non trouvée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, "Table prospects not found");
            throw new SocieteDatabaseException("Erreur lors de la lecture des" +
                    " prospects");
        }

        // Retourne la liste des prospects.
        return prospects;
    }

    /**
     * Méthode pour rechercher un prospect.
     *
     * @param name Nom du prospect.
     * @return prospect recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Prospect find(String name) throws SocieteDatabaseException {
        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des prospects
        FilesystemTable table = db.getFilesystemTable("prospects");

        // Récupération du premier enregistrement correspondant à
        // l'identifiant donné, transformation en client et retourne le client.
        return this.parse(table.find(name));
    }

    /**
     * Méthode pour supprimer un prospect.
     *
     * @param obj Le prospect à supprimer.
     * @return Indication que le prospect a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Prospect obj) throws SocieteDatabaseException {
        // Initialisation de la variable.
        boolean ret;

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des prospects
        FilesystemTable table = db.getFilesystemTable("prospects");

        // Suppression du client dans la table.
        ret = table.delete(obj.getIdentifiant());

        // Sauvegarde de la table.
        table.closeTable();

        // Retourne si la suppression s'est bien passé ou non.
        return ret;
    }

    /**
     * Méthode qui sauvegarde un prospect, soit en le créant, soit en le
     * modifiant.
     *
     * @param obj Le prospect à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(@NotNull Prospect obj) throws SocieteDatabaseException {
        // Initialisation des variables.
        boolean ret;
        String[] record;

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des prospects
        FilesystemTable table = db.getFilesystemTable("prospects");

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
        record[9] = obj.getDateProspection().format(Formatters.FORMAT_DDMMYYYY);
        record[10] = obj.getProspectInteresse();

        ret = (obj.getIdentifiant() > 0) ? table.update(record) : table.create(record);

        table.closeTable();

        // Retourne le résultat booléen des vérifications.
        return ret;
    }

    /**
     * Méthode qui construit un Prospect à partir d'un enregistrement.
     *
     * @param record enregistrement de résultats.
     * @return Prospect le prospect récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private Prospect parse(String[] record) throws SocieteDatabaseException {
        try {
            return ProspectBuilder.getNewProspectBuilder()
                    .dIdentifiant(record[0])
                    .deRaisonSociale(record[1])
                    .dAdresse(AdresseBuilder.getNewAdresseBuilder()
                            .dIdentifiant(record[0])
                            .deNumeroRue(record[2])
                            .deNomRue(record[3])
                            .deCodePostal(record[4])
                            .deVille(record[5])
                            .build())
                    .deTelephone(record[6])
                    .deMail(record[7])
                    .deCommentaires(record[8])
                    .deDateProspection(record[9])
                    .dInteresse(record[10])
                            .build();
        } catch (SocieteEntityException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du prospect depuis la base de données.");
        }
    }
}
