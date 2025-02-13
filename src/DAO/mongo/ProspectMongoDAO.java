package DAO.mongo;

import DAO.SocieteDatabaseException;
import builders.AdresseBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import entities.Adresse;
import entities.Prospect;
import entities.SocieteEntityException;
import logs.LogManager;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Classe DAO MongoDB pour les prospects.
 */
public class ProspectMongoDAO extends SocieteMongoDAO<Prospect> {

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
        // Initialisation variables.
        Prospect prospect;
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection de données prospects.
        MongoCollection<Document> collection = db.getCollection("prospects");

        // Récupération du premier document correspondant au nom donnée.
        Document doc = collection.find(Filters.eq("identifiant", identifiant)).first();

        if (doc != null) {
            // Si document trouvé, il est transformé en prospect.
            prospect = this.parse(doc);
        } else {
            // Pas de prospect trouvé, lancement d'une exception pour avertir
            // l'utilisateur.
            throw new SocieteDatabaseException("Le prospect n'existe pas");
        }

        return prospect;
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
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection de données prospects.
        MongoCollection<Document> collection = db.getCollection("prospects");

        // Récupération de l'ensemble des prospects.
        try (MongoCursor<Document> cursor = collection.find().iterator()) {

            while (cursor.hasNext()) {
                // Tant que des données prospects non exploitées existent.

                // Accès à ces données.
                Document doc = cursor.next();

                // Création du client à partir des données client.
                Prospect prospect = this.parse(doc);

                // Ajout du client créé dans la liste.
                prospects.add(prospect);
            }
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
        // Initialisation des variables.
        Prospect prospect;
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection de données prospects.
        MongoCollection<Document> collection = db.getCollection("prospects");

        // Récupération du premier document correspondant au nom donnée.
        Document doc = collection.find(Filters.regex("raisonSociale", name, "i")).first();

        if (doc != null) {
            // Si document trouvé, il est transformé en prospect.
            prospect = this.parse(doc);
        } else {
            // Pas de prospect trouvé, lancement d'une exception pour avertir
            // l'utilisateur.
            throw new SocieteDatabaseException("Le prospect n'existe pas");
        }

        // Retourne le prospect.
        return prospect;
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
        MongoDatabase db = ConnexionMongo.getInstance();

        // Récupération de la collection des prospects.
        MongoCollection<Document> collection = db.getCollection("prospects");

        // Suppression du client.
        DeleteResult result = collection.deleteOne(Filters.eq("identifiant",
                obj.getIdentifiant()));

        // Vérification qu'une unique suppression a eu lieu.
        return result.getDeletedCount() == 1;
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
        // Initialisation de la variable.
        MongoDatabase db = ConnexionMongo.getInstance();
        boolean ret;

        // Récupération de la collection des prospects.
        MongoCollection<Document> collection = db.getCollection("prospects");

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
                    Updates.set("dateProspection", obj.getDateProspection()),
                    Updates.set("prospectInteresse",
                            Objects.equals(obj.getProspectInteresse(), "oui")),
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
                    .append("dateProspection", obj.getDateProspection())
                    .append("prospectInteresse",
                            Objects.equals(obj.getProspectInteresse(), "oui"))
                    .append("adresse", adresseDoc);

            // Insertion du document dans la collection.
            InsertOneResult result = collection.insertOne(doc);

            // Vérification que le document a bien été inséré.
            ret = result.getInsertedId() == doc.get("_id");
        }

        // Retourne le résultat booléen des vérifications.
        return ret;
    }

    /**
     * Méthode qui construit un Prospect à partir d'un document.
     *
     * @param document Document de résultats.
     * @return Prospect Le Prospect récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    private @NotNull Prospect parse(@NotNull Document document) throws SocieteDatabaseException {
        // Initialisation Prospect.
        Prospect prospect = new Prospect();

        try {
            // Valorisation propriétés primitives.
            prospect.setIdentifiant(document.getInteger("identifiant"));
            prospect.setRaisonSociale(document.getString("raisonSociale"));
            prospect.setTelephone(document.getString("telephone"));
            prospect.setMail(document.getString("mail"));
            prospect.setCommentaires(document.getString("commentaires"));

            Date dt = document.getDate("dateProspection");
            LocalDate ldt = LocalDate.ofInstant(dt.toInstant(), ZoneId.systemDefault());

            prospect.setDateProspection(ldt);

            prospect.setProspectInteresse(document.getBoolean(
                    "prospectInteresse") ? "oui" : "non");

            // Valorisation propriétés objets.
            Document adresse = document.get("adresse", Document.class);
            prospect.setAdresse(AdresseBuilder.getNewAdresseBuilder()
                    .dIdentifiant(adresse.getInteger("identifiant"))
                    .deNumeroRue(adresse.getString("numRue"))
                    .deCodePostal(adresse.getString("codePostal"))
                    .deVille(adresse.getString("ville"))
                    .build());
        } catch (SocieteEntityException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "prospect depuis la base de données.");
        }

        // Retourne le Prospect valorisé.
        return prospect;
    }
}
