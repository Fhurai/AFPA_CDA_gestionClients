package view;

import entities.TypeSociete;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static utilities.ViewsUtilities.*;

public class List extends JFrame {
    private final Dimension windowSize = new Dimension(900, 250);
    private final TypeSociete typeSociete;
    private JPanel contentPane;
    private JPanel AppliNamePanel;
    private JLabel AppliNameLabel;
    private JPanel BottomButtonsPanel;
    private JButton quitButton;
    private JPanel ContainerPanel;
    private JButton accueilButton;
    private JTable table1;
    private JButton modificationButton;
    private JButton suppressionButton;

    /**
     * Constructeur
     * @param typeSociete Le type de sociétés à lister.
     */
    public List(TypeSociete typeSociete) {
        this.typeSociete = typeSociete;
        init();
        setListeners();
        fillTable();
    }

    /**
     * Méthode d'initialisation de la vue.
     */
    private void init() {
        // Valorisation du contenu de la vue.
        setContentPane(contentPane);

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
        contentPane.registerKeyboardAction(e -> returnIndex(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.addWindowListener(windowAdapter);
    }

    /**
     * Méthode de remplissage de la table.
     */
    private void fillTable() {
        DefaultTableModel modelTable;
        String[] entete;

        modelTable = switch (this.typeSociete) {
            case TypeSociete.CLIENT -> {
                entete = new String[]{"ID", "Raison sociale", "Adresse", "N° " +
                        "Téléphone", "Adresse mail", "Chiffre d'affaires",
                        "Nombre employés"};
                yield getModelTable(entete);
            }
            case TypeSociete.PROSPECT -> {
                entete = new String[]{"ID", "Raison sociale", "Adresse", "N° " +
                        "Téléphone", "Adresse mail", "Date prospection",
                        "Prospect intéressé"};
                yield getModelTable(entete);
            }
        };

        table1.setModel(modelTable);
    }
}
