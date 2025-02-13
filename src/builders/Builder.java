package builders;

/**
 * Classe générique builder
 *
 * @param <T> Classe de l'objet à construire.
 */
abstract public class Builder<T> {

    /**
     * L'objet en cours de construction.
     */
    private final T entity;

    /**
     * Constructor.
     *
     * @param entity Objet à construire.
     */
    public Builder(T entity) {
        this.entity = entity;
    }

    /**
     * Getter objet à construire.
     *
     * @return Objet à construire.
     */
    public T getEntity() {
        return entity;
    }

    abstract public T build();
}
