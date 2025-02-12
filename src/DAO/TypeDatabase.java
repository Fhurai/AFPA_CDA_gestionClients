package DAO;

public enum TypeDatabase {
    // Valeurs énumérées
    MYSQL(1, "MySQL"),
    MONGODB(2, "MongoDB"),
    FILESYSTEM(3, "FileSystem");

    // Variable d'instance
    private final int number;
    private final String name;

    /**
     * Constructeur type database.
     *
     * @param number Valeur numérique
     * @param name Nom de la valeur
     */
    TypeDatabase(int number, String name) {
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
