package entities;

/**
 * Classe Énumérée Type Société
 */
public enum TypeSociete {
    // Valeurs énumérées
    CLIENT(1, "client"),
    PROSPECT(2, "prospect");

    // Variable d'instance
    private final int number;
    private final String name;

    /**
     * Constructeur type société
     *
     * @param number Valeur numérique
     * @param name   Nom de la valeur
     */
    TypeSociete(int number, String name) {
        this.number = number;
        this.name = name;
    }

    /**
     * Getter number
     *
     * @return Valeur numérique de la valeur énumérée
     */
    public int getNumber() {
        return number;
    }

    /**
     * Getter name
     *
     * @return Nom de la valeur énumérée
     */
    public String getName() {
        return name;
    }
}
