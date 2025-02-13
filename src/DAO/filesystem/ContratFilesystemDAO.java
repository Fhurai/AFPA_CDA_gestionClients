package DAO.filesystem;

import DAO.DAO;
import DAO.SocieteDatabaseException;
import entities.Client;
import entities.Contrat;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ContratFilesystemDAO extends DAO<Contrat> {

    /**
     * Constructor.
     */
    public ContratFilesystemDAO() {
    }

    /**
     * Méthode pour récupérer l'ensemble des contrats.
     *
     * @return l'ensemble des contrats.
     */
    @Override
    public ArrayList<Contrat> findAll() throws SocieteDatabaseException {
        // Initialisation variables.
        ArrayList<Contrat> contrats = new ArrayList<>();

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des contrats
        FilesystemTable table = db.getFilesystemTable("contrats");

        if (table != null) {
            // La table existe, parcours des enregistrements.
            for (String[] record : table.getRecords()) {

                // Transformation de l'enregistrement en contrat qui est
                // ajouté à la liste.
                Contrat contrat = this.parse(record);
                contrats.add(contrat);
            }
        } else {
            // Table non trouvée, log de l'erreur et avertissement de
            // l'utilisateur.
            LogManager.logs.log(Level.SEVERE, "Table contrats not found");
            throw new SocieteDatabaseException("Erreur lors de la lecture des" +
                    " contrats");
        }

        return contrats;
    }

    /**
     * Méthode pour rechercher un contrat.
     *
     * @param name Nom du contrat recherché.
     * @return contrat recherché.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public Contrat find(String name) throws SocieteDatabaseException {
        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des contrats
        FilesystemTable table = db.getFilesystemTable("contrats");

        // Récupération du premier enregistrement correspondant à
        // l'identifiant donné, transformation en client et retourne le client.
        return this.parse(table.find(name));
    }

    /**
     * Méthode pour rechercher un contrat par l'identifiant du client.
     *
     * @param idClient Le client du contrat recherché.
     * @return La liste des contrats du client.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    public ArrayList<Contrat> findByIdClient(int idClient) throws SocieteDatabaseException {
        // Initialisation de la variable.
        ArrayList<Contrat> contrats = new ArrayList<>();

        // Initialisation base de données.
        FilesystemDatabase db = ConnexionFilesystem.getInstance();

        // Récupération de la table des contrats
        FilesystemTable table = db.getFilesystemTable("contrats");

        @NotNull List<String[]> records =
                table.getRecords().stream().filter(r -> Integer.parseInt(r[3]) == idClient).toList();


        for (String[] record : records) {
            // Transformation de l'enregistrement en contrat qui est
            // ajouté à la liste.
            Contrat contrat = this.parse(record);
            contrats.add(contrat);
        }

        return contrats;
    }

    /**
     * Méthode pour supprimer un objet de type T
     *
     * @param obj L'objet à supprimer.
     * @return Indication que l'objet a bien été supprimé.
     * @throws SocieteDatabaseException Exception lors de la lecture ou lors
     *                                  de la fermeture des données.
     */
    @Override
    public boolean delete(@NotNull Contrat obj) throws SocieteDatabaseException {
        return false;
    }

    /**
     * Méthode qui sauvegarde un objet, soit en le créant, soit en le modifiant.
     *
     * @param obj L'objet à sauvegarder.
     * @return Indication si la sauvegarde s'est bien passé.
     * @throws SocieteDatabaseException Exception lors de la création, de la
     *                                  modification ou de la fermeture des données.
     */
    @Override
    public boolean save(Contrat obj) throws SocieteDatabaseException {
        return false;
    }

    /**
     * Méthode qui construit un Contrat à partir d'une ligne de résultats.
     *
     * @param record enregistrement de résultats.
     * @return Le contrat récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private Contrat parse(String[] record) throws SocieteDatabaseException {
        // Initialisation contrat.
        Contrat contrat = new Contrat();

        try {
            // Valorisation propriétés primitives.
            contrat.setIdentifiant(Integer.parseInt(record[0]));
            contrat.setLibelle(record[1]);
            contrat.setMontant(Float.parseFloat(record[2]));
            contrat.setIdClient(Integer.parseInt(record[3]));
        } catch (SocieteEntityException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "contrat depuis la base de données.");
        }

        // Retourne le contrat valorisé.
        return contrat;
    }
}
