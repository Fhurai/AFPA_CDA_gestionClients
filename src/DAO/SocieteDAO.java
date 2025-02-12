package DAO;

import entities.Client;
import entities.Societe;

abstract public class SocieteDAO<T extends Societe> extends DAO<T> {

    abstract public T findById(int identifiant) throws SocieteDatabaseException;
}
