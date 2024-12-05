package entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
     * Méthode statique d'ajout d'un client à la liste des clients
     * @param client Client à ajouter à la liste
     * @return Indication si le client a bien été ajouté.
     */
    public static boolean toClientsAdd(Client client){
        List<String> raisonsSociales =
                clients.stream().map(Societe::getRaisonSociale).toList();

        if(clients.contains(client) || client == null || raisonsSociales.contains(client.getRaisonSociale())){
            return false;
        }

        sortClients();

        return clients.add(client);
    }

    /**
     * Méthode pour convertir la liste des clients en chaîne de caractères.
     * @return La liste des clients en chaîne de caractères.
     */
    public static @NotNull String toClientsString()
    {
        StringBuilder sb = new StringBuilder();

        for(Client c : clients){
            sb.append(c.toString()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Méthode pour remplir la liste des clients au lancement de l'application.
     */
    public static void populateClients() throws SocieteEntityException {
        try {
            toClientsAdd(new Client("Efluid SAS", new Adresse("2bis", "rue Ardant du Picq", "57004", "Metz"), "0387543400", "contact@efluid.fr", "", 6388000, 250));
            toClientsAdd(new Client("Happiso", new Adresse("25", "rue de la Taye", "57130", "Jussy"), "0387758575", "contact@happiso.fr", "", 4813, 6));
            toClientsAdd(new Client("OGMI", new Adresse("3", "rue des Michottes", "54000", "Nancy"), "0383375640", "ogmi@ogmi.fr", "", 50000, 1));
        } catch (SocieteEntityException e) {
            throw new SocieteEntityException("La liste des clients n'a pas pu" +
                    " être remplie !");
        }

    }
}
