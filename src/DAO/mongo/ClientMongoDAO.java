package DAO.mongo;

import DAO.SocieteDatabaseException;
import builders.AdresseBuilder;
import builders.ClientBuilder;
import builders.ContratBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import entities.Client;
import entities.Societe;
import entities.SocieteEntityException;
import logs.LogManager;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Classe DAO Mongo pour les clients.
 */
public class ClientMongoDAO extends SocieteMongoDAO<Client> {

    /**
     * Constructor
     */
    public ClientMongoDAO() {
    }

    @Override
    protected boolean checkOtherRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        ProspectMongoDAO prospectMongoDAO = new ProspectMongoDAO();
        List<String> otherRaisonsSociales =
                prospectMongoDAO.findAll().stream()
                        .map(Societe::getRaisonSociale)
                        .toList();
        return otherRaisonsSociales.contains(raisonSociale);
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
        // Initialisation variables.
        Client client;
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection de données clients.
        MongoCollection<Document> collection = db.getCollection("clients");

        // Récupération du premier document correspondant à l'identifiant donné.
        Document doc = collection.find(Filters.eq("identifiant", identifiant)).first();

        if (doc != null) {
            // Si document trouvé, il est transformé en client.
            client = this.parse(doc);
        } else {
            // Pas de client trouvé, lancement d'une exception pour avertir
            // l'utilisateur.
            throw new SocieteDatabaseException("Le client n'existe pas");
        }

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
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection de données clients.
        MongoCollection<Document> collection = db.getCollection("clients");

        // Récupération de l'ensemble des clients.
        try (MongoCursor<Document> cursor = collection.find().iterator()) {

            while (cursor.hasNext()) {
                // Tant que des données clients non exploitées existent.

                // Accès à ces données.
                Document doc = cursor.next();

                // Création du client à partir des données client.
                Client client = this.parse(doc);

                // Ajout du client créé dans la liste.
                clients.add(client);
            }
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
        // Initialisation des variables.
        Client client;
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection de données clients.
        MongoCollection<Document> collection = db.getCollection("clients");

        // Récupération du premier document correspondant au nom donnée.
        Document doc = collection.find(Filters.regex("raisonSociale", name, "i")).first();

        if (doc != null) {
            // Si document trouvé, il est transformé en client.
            client = this.parse(doc);
        } else {
            // Pas de client trouvé, lancement d'une exception pour avertir
            // l'utilisateur.
            throw new SocieteDatabaseException("Le client n'existe pas");
        }

        // Retourne le client.
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
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection des clients.
        MongoCollection<Document> collection = db.getCollection("clients");

        // Suppression du client.
        DeleteResult result = collection.deleteOne(Filters.eq("identifiant",
                obj.getIdentifiant()));

        // Vérification qu'une unique suppression a eu lieu.
        return result.getDeletedCount() == 1;
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
        // Initialisation de la variable.
        MongoDatabase db = ConnexionMongo.getInstance();
        boolean ret;

        // Sécurité unicité
        if (this.checkRaisonSociale(obj.getRaisonSociale())  && obj.getIdentifiant() == 0) {
            throw new SocieteDatabaseException("La raison sociale existe déjà");
        }

        // Récupération de la collection des clients.
        MongoCollection<Document> collection = db.getCollection("clients");

        if (obj.getIdentifiant() > 0) {
            // Identifiant différent de 0, c'est une mise à jour.

            // Document adresse.
            Document adresseDoc = new Document("identifiant",
                    obj.getAdresse().getIdentifiant())
                    .append("numRue", obj.getAdresse().getNumeroRue())
                    .append("nomRue", obj.getAdresse().getNomRue())
                    .append("codePostal", obj.getAdresse().getCodePostal())
                    .append("ville", obj.getAdresse().getVille());

            // Regroupement de toutes les modifications à faire.
            Bson updates = Updates.combine(
                    Updates.set("raisonSociale", obj.getRaisonSociale()),
                    Updates.set("telephone", obj.getTelephone()),
                    Updates.set("mail", obj.getMail()),
                    Updates.set("commentaires", obj.getCommentaires()),
                    Updates.set("chiffreAffaires", String.valueOf(obj.getChiffreAffaires())),
                    Updates.set("nbEmployes", obj.getNbEmployes()),
                    Updates.set("adresse", adresseDoc)
            );

            // Mise à jour des champs du document client.
            UpdateResult result = collection.updateOne(Filters.eq("identifiant",
                    obj.getIdentifiant()), updates);

            // Vérification qu'une unique ligne a été modifiée.
            ret = result.getModifiedCount() == 1;
        } else {
            // identifiant à 0, c'est une création.

            // Récupération du nouvel identifiant.
            Document maxDoc = collection.find().sort(new Document("identifiant", -1)).first();
            int identifiant = (maxDoc != null) ?
                    maxDoc.getInteger("identifiant") + 1 : 1;

            // Création du document adresse.
            Document adresseDoc = new Document("identifiant", identifiant)
                    .append("numRue", obj.getAdresse().getNumeroRue())
                    .append("nomRue", obj.getAdresse().getNomRue())
                    .append("codePostal", obj.getAdresse().getCodePostal())
                    .append("ville", obj.getAdresse().getVille());

            // Création du document client.
            Document doc = new Document("_id", new ObjectId())
                    .append("identifiant", identifiant)
                    .append("raisonSociale", obj.getRaisonSociale())
                    .append("telephone", obj.getTelephone())
                    .append("mail", obj.getMail())
                    .append("commentaires", obj.getCommentaires())
                    .append("chiffreAffaires",
                            String.valueOf(obj.getChiffreAffaires()))
                    .append("nbEmployes", obj.getNbEmployes())
                    .append("adresse", adresseDoc)
                    .append("contrats", obj.getContrats());

            // Insertion du document dans la collection.
            InsertOneResult result = collection.insertOne(doc);

            // Vérification que le document a bien été inséré.
            ret = result.getInsertedId() == doc.get("_id");
        }

        // Retourne le résultat booléen des vérifications.
        return ret;
    }

    /**
     * Méthode qui construit un Client à partir d'un document.
     *
     * @param document Document de résultats.
     * @return Client Le client récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Client parse(@NotNull Document document) throws SocieteDatabaseException {
        try {
            Document adresse = document.get("adresse", Document.class);

            ClientBuilder clientBuilder = ClientBuilder.getNewClientBuilder()
                    .dIdentifiant(document.getInteger("identifiant"))
                    .deRaisonSociale(document.getString("raisonSociale"))
                    .deTelephone(document.getString("telephone"))
                    .deMail(document.getString("mail"))
                    .deCommentaires(document.getString("commentaires"))
                    .deChiffreAffaires(document.getString("chiffreAffaires"))
                    .deNombreEmployes(document.getInteger("nbEmployes"))
                    .dAdresse(AdresseBuilder.getNewAdresseBuilder()
                            .dIdentifiant(adresse.getInteger("identifiant"))
                            .deNumeroRue(adresse.getString("numRue"))
                            .deNomRue(adresse.getString("nomRue"))
                            .deCodePostal(adresse.getString("codePostal"))
                            .deVille(adresse.getString("ville"))
                            .build())
                    .deContrats(new ArrayList<>());


            for (Document contrat : document.getList("contrats", Document.class)) {
                clientBuilder.ajouterContrat(ContratBuilder.getNewContratBuilder()
                        .dIdentifiant(contrat.getString("identifiant"))
                        .deLibelle(contrat.getString("libelleContrat"))
                        .deMontant(contrat.getString("montant"))
                        .build());
            }

            return clientBuilder.build();
        } catch (SocieteEntityException | NumberFormatException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.", e);
        }
    }
}
