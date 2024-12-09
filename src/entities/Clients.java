package entities;

import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Classe de liste des clients
 */
public final class Clients {

    // Variables de classe
    public static ArrayList<Client> clients = new ArrayList<>();
    public static int compteurIdClients = 1;

    /**
     * Comparateur des clients par raison sociale
     */
    public static class RaisonSocialClientComparator implements Comparator<Client>
    {
        /**
         * Méthode de comparaison
         * @param o1 Le premier client à comparer.
         * @param o2 Le deuxième client à comparer
         * @return Indication de la comparaison (-1, 0, 1)
         */
        @Override
        public int compare(@NotNull Client o1, @NotNull Client o2) {
            return o1.getRaisonSociale().compareToIgnoreCase(o2.getRaisonSociale());
        }
    }

    /**
     * Méthode statique de tri des clients
     */
    public static void sortClients(){
        clients.sort(new RaisonSocialClientComparator());
    }

    /**
     * Méthode qui retourne le client lié à l'identifiant donné
     * @param identifiant L'identifiant du client recherché
     * @return Optional du client recherché
     */
    public static Optional<Client> get(int identifiant){

        // Parcours des clients existant et retourne le client qui a
        // l'identifiant donné
        for(Client c : clients){
            if(c.getIdentifiant() == identifiant){
                return Optional.of(c);
            }
        }

        // Aucun client existant trouvé, retourne un optional vide.
        return Optional.empty();
    }

    /**
     * Méthode qui retourne le client lié à la raison sociale donnée.
     * @param raisonSociale Raison sociale du client recherché
     * @return Optional du client recherché
     */
    public static Optional<Client> getFromRaisonSociale(String raisonSociale){

        // Parcours des clients existant et retourne le client qui a
        // la raison sociale donnée.
        for(Client c : clients){
            if(c.getRaisonSociale().equalsIgnoreCase(raisonSociale)){
                return Optional.of(c);
            }
        }

        // Aucun client existant trouvé, retourne un optional vide.
        return Optional.empty();
    }

    /**
     * Méthode statique d'ajout d'un client à la liste des clients
     * @param client Client à ajouter à la liste
     * @return Indication si le client a bien été ajouté.
     */
    public static boolean toClientsAdd(Client client){
        // Si client existe déjà en base ou il est nul
        if(clients.contains(client) || client == null){
            return false;
        }

        // Tri des clients
        sortClients();

        // Incrémentation du compteur ID apres l'insertion dans la liste qui
        // sert de bdd.
        Clients.compteurIdClients++;

        // Retourne si la liste a bien ajouté le client.
        return clients.add(client);
    }

    /**
     * Méthode pour remplir la liste des clients au lancement de l'application.
     */
    public static void populateClients() throws SocieteEntityException {

        try {
            // Ajout Efluid
            toClientsAdd(new Client("Efluid SAS", new Adresse("2bis",
                    "rue Ardant du Picq", "57004", "Metz"), "0387543400",
                    "contact@efluid.fr", "", 6388000, 250));

            // Ajout Happiso
            toClientsAdd(new Client("Happiso", new Adresse("25",
                    "rue de la Taye", "57130", "Jussy"), "0387758575",
                    "contact@happiso.fr", "", 4813, 6));

            // Ajout OGMI
            toClientsAdd(new Client("OGMI", new Adresse("3",
                    "rue des Michottes", "54000", "Nancy"), "0383375640",
                    "ogmi@ogmi.fr", "", 50000, 1));
        } catch (SocieteEntityException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            throw new SocieteEntityException("La liste des clients n'a pas pu être remplie !");
        }

    }
}
