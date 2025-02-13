package view;

import DAO.AbstractFactory;
import entities.Client;
import entities.Contrat;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static utilities.ViewsUtilities.quitApplication;
import static utilities.ViewsUtilities.returnIndex;

/**
 * Fenêtre des contrats.
 */
public class Contracts extends JFrame {
    private final Dimension windowSize = new Dimension(450, 350);
    private Client client;
    private JPanel contentPane;
    private JPanel AppliNamePanel;
    private JLabel AppliNameLabel;
    private JPanel BottomButtonsPanel;
    private JButton quitButton;
    private JButton accueilButton;
    private JPanel ContainerPanel;
    private JTable table1;

    /**
     * Constructeur avec le client dont les contrats sont listés.
     *
     * @param client Le client dont les contrats sont listés.
     */
    public Contracts(Client client) {
        // Valorisation de la variable des contrats.
        this.client = client;

        // Initialisation de la vue et de ses écouteurs d'évènements.
        init();
        setListeners();

        // Remplissage de la table de listing.
        fillTable();
    }

    /**
     * Méthode d'initialisation de la vue.
     */
    private void init() {
        // Valorisation du contenu de la vue.
        setContentPane(contentPane);

        // Nom de l'appli en fonction de la base de donnée en cours
        // d'utilisation.
        this.AppliNameLabel.setText("Gestion fichier clients " + AbstractFactory.getTypeDatabase().getName());

        // Valorisation du bouton par défaut.
        this.getRootPane().setDefaultButton(accueilButton);

        // Valorisation de la taille de la fenêtre.
        this.setSize(windowSize);
        this.setMinimumSize(windowSize);

        // Vue centrée sur l'écran.
        this.setLocationRelativeTo(null);
    }

    /**
     * Méthode d'initialisation des écouteurs d'évènements de la vue.
     */
    private void setListeners() {
        // Valorisation de l'action par défaut de fermeture de la fenêtre.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Gestion de l'évènement de fermeture de la fenêtre.
        WindowAdapter windowAdapter = new WindowAdapter() {
            public void windowClosing(@NotNull WindowEvent e) {
                returnIndex((JFrame) e.getComponent());
            }
        };

        // Bouton pour fermer l'application.
        quitButton.addActionListener(e -> quitApplication(this));

        // Boutons et touche clavier pour retourner sur la fenêtre d'accueil.
        accueilButton.addActionListener(e -> returnIndex(this));
        contentPane.registerKeyboardAction(e -> returnIndex(this), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.addWindowListener(windowAdapter);
    }

    /**
     * Méthode de remplissage du tableau.
     */
    private void fillTable() {
        // Initialisation des variables nécessaires au remplissage du tableau.
        DefaultTableModel modelTable = null;
        String[] entete;

        entete = new String[]{"Identifiant", "Libellé", "Montant (€)"};

        // Création du modèle de table avec toutes les lignes non éditables.
        modelTable = new DefaultTableModel(new Object[][]{}
                , entete) {

            // Rend toutes les lignes non éditables.
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Ajout de l'entête au modèle.
        modelTable.addRow(entete);

        // Remplissage du tableau avec les contrats du client.
        if (this.client.getContrats() != null && !this.client.getContrats().isEmpty()) {
            for (Contrat contrat : this.client.getContrats()) {
                modelTable.addRow(new Object[]{
                        contrat.getIdentifiant(),
                        contrat.getLibelle(),
                        contrat.getMontant()
                });
            }
        }

        table1.setModel(modelTable);
    }
}
