package DAO.mysql;

import DAO.SocieteDAO;
import entities.Societe;

/**
 * Classe DAO pour les sociétés.
 *
 * @param <T> Le type de classe à typer.
 */
abstract public class SocieteMySqlDAO<T extends Societe> extends SocieteDAO<T> {

}
