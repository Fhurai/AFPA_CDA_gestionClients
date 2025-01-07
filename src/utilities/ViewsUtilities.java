package utilities;

import entities.Clients;
import entities.Prospects;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;
import view.Index;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Classe des utilitaires sur les vues.
 */
public class ViewsUtilities {

    /**
     * Méthode pour quitter l'application
     */
    public static void quitApplication(@NotNull JFrame frame) {
        LogManager.stop();
        frame.dispose();
    }

    /**
     * Méthode pour retourner sur la fenêtre d'accueil
     * @param frame La vue à disposer.
     */
    public static void returnIndex(@NotNull JFrame frame) {
        new Index().setVisible(true);
        frame.dispose();
    }

    /**
     * Méthode qui prépare le modèle de table en fonction de l'entête de
     * table fournie.
     * @param entete L'entête fourni.
     * @return Le modèle de table préparé.
     */
    public static @NotNull DefaultTableModel getModelTable(String[] entete){
        // Création du modèle de table avec toutes les lignes non éditables.
        DefaultTableModel modelTable = new DefaultTableModel(new Object[][]{}
                , entete){

            // Rend toutes les lignes non éditables.
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelTable.addRow(entete);

        if(entete[5].equals("Chiffre d'affaires")){
            // Si l'entête est pour un client.

            Clients.getClients().forEach(c -> modelTable.addRow(new Object[]{
                    c.getIdentifiant(),
                    c.getRaisonSociale(),
                    c.getAdresse(),
                    c.getTelephone(),
                    c.getMail(),
                    c.getChiffreAffaires(),
                    c.getNbEmployes()
            }));
        }else if(entete[5].equals("Date prospection")){
            // Si l'entête est pour un prospect.

            Prospects.getProspects().forEach(p -> modelTable.addRow(new Object[]{
                    p.getIdentifiant(),
                    p.getRaisonSociale(),
                    p.getAdresse(),
                    p.getTelephone(),
                    p.getMail(),
                    p.getDateProspection().format(Formatters.FORMAT_DDMMYYYY),
                    p.getProspectInteresse()
            }));
        }

        return modelTable;
    }
}
