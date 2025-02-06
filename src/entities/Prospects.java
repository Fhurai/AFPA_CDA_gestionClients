package entities;

import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;

import static utilities.Formatters.FORMAT_DDMMYYYY;

/**
 * Classe de liste des prospects
 */
public final class Prospects {

    // Variables de classe
    private static ArrayList<Prospect> prospects = new ArrayList<>();
    private static int compteurIdProspects = 1;

    /**
     * Méthode statique de tri des prospects
     */
    public static void sortProspects() {
        prospects.sort(new RaisonSocialProspectsComparator());
    }

    /**
     * Méthode qui retourne le prospect lié à l'identifiant donné
     * @param id Identifiant du prospect recherché
     * @return Optional du prospect recherché
     */
    public static Optional<Prospect> get(int id) {
        // Parcours des clients existant et retourne le prospect qui a
        // l'identifiant donné
        for (Prospect p : prospects) {
            if(p.getIdentifiant() == id) {
                return Optional.of(p);
            }
        }

        // Aucun prospect existant trouvé, retourne un optional vide.
        return Optional.empty();
    }

    /**
     * Méthode qui retourne la liste des prospects.
     * @return Liste des prospects.
     */
    public static ArrayList<Prospect> getProspects() {
        return prospects;
    }

    /**
     * Méthode qui retourne le compteur de prospects.
     * @return Le compteur de prospects.
     */
    public static int getCompteurIdProspects() {
        return compteurIdProspects;
    }

    /**
     * Setter Compteur prospects
     * @param compteur Nouveau compte
     */
    public static void setCompteurIdProspects(int compteur) {
        compteurIdProspects = compteur;
    }

    /**
     * Méthode qui retourne le prospect lié à la raison sociale donnée.
     * @param raisonSociale Raison sociale du prospect recherché
     * @return Optional du prospect recherché
     */
    public static Optional<Prospect> getFromRaisonSociale(String raisonSociale) {
        // Parcours des clients existant et retourne le prospect qui a
        // la raison sociale donnée.
        for (Prospect p : prospects) {
            if(p.getRaisonSociale().equalsIgnoreCase(raisonSociale)) {
                return Optional.of(p);
            }
        }

        // Aucun prospect existant trouvé, retourne un optional vide.
        return Optional.empty();
    }

    /**
     * Méthode statique d'ajout d'un prospect à la liste des prospects
     *
     * @param prospect Prospect à ajouter à la liste
     * @return Indication si le prospect a bien été ajouté.
     */
    public static boolean toProspectsAdd(Prospect prospect) {
        // Si prospect existe déjà en base ou il est nul
        if (prospects.contains(prospect) || prospect == null) {
            return false;
        }

        // Tri des prospect
        sortProspects();

        // Incrémentation du compteur ID apres l'insertion dans la liste qui
        // sert de bdd.
        Prospects.compteurIdProspects++;

        // Retourne si la liste a bien ajouté le prospect.
        return prospects.add(prospect);
    }

    /**
     * Méthode pour remplir la liste des prospects au lancement de
     * l'application.
     */
    public static void populateProspects() throws SocieteEntityException {
        try {
            // Ajout CGI
            toProspectsAdd(new Prospect("CGI", new Adresse(4, "28",
                    "Boulevard " +
                    "Albert 1er", "54000", "NANCY"), "0388553370", "contact" +
                    "@cgi.com", "", LocalDate.parse("15/11/2024", FORMAT_DDMMYYYY),
                    ReponseFermee.NON.getValue()));

            // Ajout ATOS
            toProspectsAdd(new Prospect("ATOS", new Adresse(5, "80", "QUAI " +
                    "VOLTAIRE", "95870", "BEZONS"), "0173260000", "contact" +
                    "@atos.fr", "", LocalDate.parse("28/05/2024", FORMAT_DDMMYYYY), ReponseFermee.NON.getValue()));

            // Ajout OGMI
            toProspectsAdd(new Prospect("Expectra", new Adresse(6, "276",
                    "AVENUE DU PRESIDENT WILSON", "93210", "SAINT-DENIS"),
                    "0387172390", "contact@expectra.fr", "", LocalDate.parse("10/10/2024", FORMAT_DDMMYYYY), ReponseFermee.OUI.getValue()));
        } catch (SocieteEntityException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            throw new SocieteEntityException("La liste des prospects n'a pas " +
                    "pu être remplie !");
        }
    }

    /**
     * Comparateur des prospects par raison sociale
     */
    public static class RaisonSocialProspectsComparator implements Comparator<Prospect> {

        /**
         * Méthode de comparaison
         *
         * @param o1 Le premier prospect à comparer.
         * @param o2 Le deuxième prospect à comparer
         * @return Indication de la comparaison (-1, 0, 1)
         */
        @Override
        public int compare(@NotNull Prospect o1, @NotNull Prospect o2) {
            return o1.getRaisonSociale().compareToIgnoreCase(o2.getRaisonSociale());
        }
    }
}
