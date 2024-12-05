package entities;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static utilities.Formatters.FORMAT_DDMMYYYY;

/**
 * Classe de liste des prospects
 */
public final class Prospects {

    // Variables de classe
    public static ArrayList<Prospect> prospects = new ArrayList<>();
    public static int compteurIdProspects = 1;

    /**
     * Méthode statique de tri des prospects
     */
    public static void sortProspects() {
        prospects.sort(new RaisonSocialProspectsComparator());
    }

    /**
     * Méthode statique d'ajout d'un prospects à la liste des prospects
     *
     * @param prospect Prospect à ajouter à la liste
     * @return Indication si le prospect a bien été ajouté.
     */
    public static boolean toProspectsAdd(Prospect prospect) {
        List<String> raisonsSociales =
                prospects.stream().map(Societe::getRaisonSociale).toList();

        if (prospects.contains(prospect) || prospect == null || raisonsSociales.contains(prospect.getRaisonSociale())) {
            return false;
        }

        sortProspects();

        return prospects.add(prospect);
    }

    /**
     * Méthode pour convertir la liste des prospects en chaîne de caractères.
     *
     * @return La liste des prospects en chaîne de caractères.
     */
    public static @NotNull String toProspectsString() {
        StringBuilder sb = new StringBuilder();

        for (Prospect p : prospects) {
            sb.append(p.getRaisonSociale());
        }

        return sb.toString();
    }

    /**
     * Méthode pour remplir la liste des prospects au lancement de
     * l'application.
     */
    public static void populateProspects() throws SocieteEntityException {
        try {
            toProspectsAdd(new Prospect("CGI", new Adresse("28", "Boulevard " +
                    "Albert 1er", "54000", "NANCY"), "0388553370", "contact" +
                    "@cgi.com", "", LocalDate.parse("15/11/2024", FORMAT_DDMMYYYY),
                    ReponseFermee.NON.getValue()));

            toProspectsAdd(new Prospect("ATOS", new Adresse("80", "QUAI " +
                    "VOLTAIRE", "95870", "BEZONS"), "0173260000", "contact" +
                    "@atos.fr", "", LocalDate.parse("28/05/2024", FORMAT_DDMMYYYY), ReponseFermee.NON.getValue()));

            toProspectsAdd(new Prospect("Expectra", new Adresse("276",
                    "AVENUE DU PRESIDENT WILSON", "93210", "SAINT-DENIS"),
                    "0387172390", "contact@expectra.fr", "", LocalDate.parse("10/10/2024", FORMAT_DDMMYYYY), ReponseFermee.OUI.getValue()));
        } catch (SocieteEntityException e) {
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
