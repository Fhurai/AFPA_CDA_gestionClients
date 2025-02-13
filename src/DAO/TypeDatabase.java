package DAO;

import org.jetbrains.annotations.Nullable;

public enum TypeDatabase {
    // Valeurs énumérées
    MYSQL(1, "MySql"),
    MONGODB(2, "MongoDB"),
    FILESYSTEM(3, "FileSystem");

    // Variable d'instance
    private final int number;
    private final String name;

    /**
     * Constructeur type database.
     *
     * @param number Valeur numérique
     * @param name   Nom de la valeur
     */
    TypeDatabase(int number, String name) {
        this.number = number;
        this.name = name;
    }

    /**
     * Méthode pour retrouver un type de base par sa valeur numérique.
     *
     * @param number Valeur numérique recherchée.
     * @return Type de base de donnée recherchée.
     */
    public static @Nullable TypeDatabase findByNumber(int number) {
        for (TypeDatabase db : TypeDatabase.values()) {
            if (db.getNumber() == number) {
                return db;
            }
        }
        return null;
    }

    /**
     * Méthode pour retrouver un type de base par sa valeur numérique.
     *
     * @param name Valeur alphanumérique recherchée.
     * @return Type de base de donnée recherchée.
     */
    public static @Nullable TypeDatabase findByString(String name) {
        for (TypeDatabase db : TypeDatabase.values()) {
            if (db.getName().equals(name)) {
                return db;
            }
        }
        return null;
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
