package entities;

/**
 * Classe Énumérée Type Action
 */
public enum TypeAction {
    // Valeurs énumérées
    CREATION(1,"creation"),
    MODIFICATION(2,"modification"),
    SUPPRESSION(3,"suppression"),
    LISTE(4,"liste");

    // Variable d'instance
    private final int number;
    private final String name;

    /**
     * Constructeur type action
     * @param number Valeur numérique
     * @param name Nom de la valeur
     */
    TypeAction(int number, String name) {
        this.number = number;
        this.name = name;
    }

    /**
     * Getter number
     * @return Valeur numérique de la valeur énumérée
     */
    public int getNumber() {
        return number;
    }

    /**
     * Getter name
     * @return Nom de la valeur énumérée
     */
    public String getName() {
        return name;
    }
}
