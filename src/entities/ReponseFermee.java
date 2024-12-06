package entities;

/**
 * Classe Énumérée Réponse fermée
 */
public enum ReponseFermee {
    // Valeurs énumérées
    OUI(1, "oui"),
    NON(0, "non");

    // Variables d'instance
    private final int number;
    private final String value;

    /**
     * Constructeur réponse fermée
     * @param number Valeur numérique
     * @param value Valeur chaîne de caractères
     */
    ReponseFermee(int number, String value) {
        this.number = number;
        this.value = value;
    }

    /**
     * Getter number
     * @return Valeur numérique de la valeur énumérée
     */
    public int getNumber() {
        return number;
    }

    /**
     * Getter value
     * @return Valeur chaîne de caractères de la valeur énumérée
     */
    public String getValue() {
        return value;
    }

    /**
     * Méthode pour savoir si une chaîne de caractères correspond à une
     * valeur énumérée
     * @param value La chaîne de caractères
     * @return Indication si la chaîne correspond à une valeur énumérée ou non
     */
    public static boolean exists(String value){
        for(ReponseFermee rf : ReponseFermee.values()){
            if(rf.getValue().equalsIgnoreCase(value)){
                return true;
            }
        }
        return false;
    }
}
