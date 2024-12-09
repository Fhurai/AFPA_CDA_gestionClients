package utilities;

import entities.*;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Classe utilitaire pour les fichiers
 */
public class Files {

    /**
     * Méthode de création des fichiers de BDD.
     */
    public static void dbCreate() {

        // POur chaque type de société (client ou prospect)
        for (TypeSociete type : TypeSociete.values()) {

            // Initialisation du fichier avec son chemin
            File file = new File("save/" + type.getName().toLowerCase() + "Table.csv");
            try {
                // Tentative de création du fichier
                if (file.createNewFile()) {
                    LogManager.logs.log(Level.INFO,
                            "DB File created for " + type.getName().toLowerCase() + "s");
                } else {
                    LogManager.logs.log(Level.INFO, "DB File already exists for " + type.getName().toLowerCase() + "s");
                }

            } catch (IOException e) {
                throw new SocieteUtilitiesException("Erreur lors de la " +
                        "creation de la base de données des " + type.getName().toLowerCase() + "s");
            }
        }
    }

    /**
     * Méthode de chargement des données de la BDD.
     * @throws SocieteUtilitiesException Exception sur les utilitaires.
     */
    public static void dbLoad() throws SocieteUtilitiesException {
        // POur chaque type de société (client ou prospect)
        for (TypeSociete type : TypeSociete.values()) {

            try {
                // Initialisation du fichier avec son chemin, et de son lecteur.
                File file = new File("save/" + type.getName().toLowerCase() + "Table.csv");
                Scanner scanner = new Scanner(file);

                //Lecteur lit le fichier tant qu'il a une ligne suivante.
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if (type == TypeSociete.CLIENT) {

                        if (!line.contains(";")) {
                            // Première ligne, sans ';', c'est donc le
                            // compteur d'identifiant.
                            Clients.compteurIdClients = Integer.parseInt(line);
                        } else {
                            // Autre ligne, c'est un enregistrement en bdd.
                            try {
                                // Création du client depuis les données de
                                // la bdd et ajout à la liste.
                                Client c =
                                        new Client(Integer.parseInt(line.split(";")[0]), line.split(";")[1], new Adresse(line.split(";")[2], line.split(";")[3], line.split(";")[4], line.split(";")[5]), line.split(";")[6], line.split(";")[7], line.split(";")[8], Long.parseLong(line.split(";")[9]), Integer.parseInt(line.split(";")[10]));
                                Clients.clients.add(c);

                            } catch (SocieteEntityException e) {
                                LogManager.logs.log(Level.SEVERE, e.getMessage());

                                throw new SocieteUtilitiesException("Erreur " +
                                        "lors de la charge des données");
                            }
                        }
                    } else {
                        if (!line.contains(";")) {
                            // Première ligne, sans ';', c'est donc le
                            // compteur d'identifiant.
                            Prospects.compteurIdProspects = Integer.parseInt(line);
                        } else {
                            // Autre ligne, c'est un enregistrement en bdd.
                            try {
                                // Création du prospect depuis les données de
                                // la bdd et ajout à la liste.
                                Prospects.prospects.add(new Prospect(Integer.parseInt(line.split(";")[0]), line.split(";")[1], new Adresse(line.split(";")[2], line.split(";")[3], line.split(";")[4], line.split(";")[5]), line.split(";")[6], line.split(";")[7], line.split(";")[8], LocalDate.parse(line.split(";")[9], Formatters.FORMAT_DDMMYYYY), line.split(";")[10]));
                            } catch (SocieteEntityException e) {
                                LogManager.logs.log(Level.SEVERE, e.getMessage());

                                throw new SocieteUtilitiesException("Erreur " +
                                        "lors de la charge des données");
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());

                throw new SocieteUtilitiesException("Erreur lors de " +
                        "l'ouverture de la base de données des " + type.getName().toLowerCase() + "s");
            }
        }
    }

    /**
     * Méthode de sauvegarde d'un type de société.
     * @param typeSociete Le type de société à sauvegarder.
     * @throws SocieteUtilitiesException L'exception sur les utilitaires.
     */
    public static void dbSave(@NotNull TypeSociete typeSociete) throws SocieteUtilitiesException {

        // Initialisation du fichier avec son chemin, et de son lecteur.
        File file = new File("save/" + typeSociete.getName().toLowerCase() + "Table.csv");

        if (file.exists()) {
            // Le fichier existe, possibilité d'écrire dedans.
            FileWriter fw;

            try {
                // Initialisation de l'écrivain du fichier.
                fw = new FileWriter("save/" + typeSociete.getName().toLowerCase() + "Table.csv");
            } catch (IOException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());

                throw new SocieteUtilitiesException("Erreur lors de " +
                        "l'ouverture de la base de données");
            }

            if (typeSociete == TypeSociete.CLIENT) {

                try {
                    // Tentative d'écriture du compteur d'identifiants dans
                    // le fichier.
                    fw.write(Clients.compteurIdClients + "\n");
                } catch (IOException e) {
                    LogManager.logs.log(Level.SEVERE, e.getMessage());

                    throw new SocieteUtilitiesException("Erreur lors " +
                            "de l'écriture dans la base de données des clients");
                }

                for (Client c : Clients.clients) {
                    // Pour chaque client dans la liste, écriture d'une ligne
                    // d'enregistrement
                    dbWriteLine(c, fw);
                }
            } else {

                try {
                    // Tentative d'écriture du compteur d'identifiants dans
                    // le fichier.
                    fw.write(Prospects.compteurIdProspects + "\n");
                } catch (IOException e) {
                    LogManager.logs.log(Level.SEVERE, e.getMessage());

                    throw new SocieteUtilitiesException("Erreur lors " +
                            "de l'écriture dans la base de données des " +
                            "prospects");
                }
                for (Prospect p : Prospects.prospects) {
                    // Pour chaque prospect dans la liste, écriture d'une ligne
                    // d'enregistrement
                    dbWriteLine(p, fw);
                }
            }

            try {
                // Tentative de fermeture du fichier
                fw.close();
            } catch (IOException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());

                throw new SocieteUtilitiesException("Erreur lors de " +
                        "la fermeture de la base de données");
            }
        } else {
            // Fichier non existant.
            LogManager.logs.log(Level.SEVERE,
                    "Fichier " + typeSociete.getName().toLowerCase() + "Table.csv manquant");

            throw new SocieteUtilitiesException("Fichier de sauvegarde " +
                    "manquant pour les " + typeSociete.getName().toLowerCase() + "s");
        }
    }

    /**
     * Méthode d'écriture d'une ligne d'enregistrement dans les fichiers bdd
     * @param societe La société à écrire
     * @param fw L'écrivain du fichier
     * @throws SocieteUtilitiesException L'exception sur les utilitaires.
     */
    private static void dbWriteLine(Societe societe, FileWriter fw) throws SocieteUtilitiesException {

        if (societe instanceof Client c) {

            try {
                // Écriture de la ligne du client avec un retour à la ligne.
                fw.write(c.getIdentifiant() + ";" + c.getRaisonSociale() + ";"
                        + c.getAdresse().getNumeroRue() + ";" + c.getAdresse().getNomRue()
                        + ";" + c.getAdresse().getCodePostal() + ";" + c.getAdresse().getVille()
                        + ";" + c.getTelephone() + ";" + c.getMail() + ";" + c.getCommentaires()
                        + ";" + c.getChiffreAffaires() + ";" + c.getNbEmployes());
                fw.write("\n");
            } catch (IOException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());

                throw new SocieteUtilitiesException("Erreur lors " +
                        "de l'écriture dans la base de données des clients");
            }
        } else if (societe instanceof Prospect p) {

            try {
                // Écriture de la ligne du prospect avec un retour à la ligne.
                fw.write(p.getIdentifiant() + ";" + p.getRaisonSociale() + ";"
                        + p.getAdresse().getNumeroRue() + ";" + p.getAdresse().getNomRue()
                        + ";" + p.getAdresse().getCodePostal() + ";" + p.getAdresse().getVille()
                        + ";" + p.getTelephone() + ";" + p.getMail() + ";" + p.getCommentaires()
                        + ";" + p.getDateProspection().format(Formatters.FORMAT_DDMMYYYY)
                        + ";" + p.getProspectInteresse());
                fw.write("\n");
            } catch (IOException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());

                throw new SocieteUtilitiesException("Erreur lors " +
                        "de l'écriture dans la base de données des clients");
            }
        }
    }
}
