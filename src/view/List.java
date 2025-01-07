package view;

import entities.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;

import static utilities.ViewsUtilities.*;

/**
 * Fenêtre de liste des sociétés
 */
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

        // Butons de modification et de suppression d'une société
        // sélectionnée dans la liste.
        modificationButton.addActionListener(e -> selectEdit(TypeAction.MODIFICATION));
        suppressionButton.addActionListener(e -> selectEdit(TypeAction.SUPPRESSION));
    }

    /**
     * Méthode de remplissage de la table.
     */
    private void fillTable() {
        // Initialisation des variables nécessaires au remplissage du tableau.
        DefaultTableModel modelTable;
        String[] entete;

        modelTable = switch (this.typeSociete) {
            //Cas d'utilisation du tableau.

            case TypeSociete.CLIENT -> {
                // Cas des clients

                // Création de l'entête et du modèle de tableau à partir de
                // celui-ci
                entete = new String[]{"ID", "Raison sociale", "Adresse", "N° " +
                        "Téléphone", "Adresse mail", "Chiffre d'affaires",
                        "Nombre employés"};
                yield getModelTable(entete);
            }
            case TypeSociete.PROSPECT -> {
                // Cas des prospects

                // Création de l'entête et du modèle de tableau à partir de
                // celui-ci
                entete = new String[]{"ID", "Raison sociale", "Adresse", "N° " +
                        "Téléphone", "Adresse mail", "Date prospection",
                        "Prospect intéressé"};
                yield getModelTable(entete);
            }
        };

        // Valorisation du tableau avec le modèle généré précédemment.
        table1.setModel(modelTable);
    }

    /**
     * Méthode d'action sur le tableau et sa ligne sélectionnée.
     * @param action Action à accomplir sur la ligne sélectionnée.
     */
    private void selectEdit(TypeAction action){
        // Récupération de l'index de la ligne sélectionnée.
        int x = table1.getSelectedRow();

        if(x != -1){
            if(this.typeSociete == TypeSociete.CLIENT){
                // Si la liste est pour des clients

                // Récupération du client
                Optional<Client> c =
                        Clients.get(Integer.parseInt(this.table1.getValueAt(x,0).toString()));

                if(c.isPresent()){
                    // Le client existe, le formulaire est à ouvrir avec l'action
                    // voulue
                    new Form(this.typeSociete, action, c.get()).setVisible(true);
                    dispose();
                }
            }else if(this.typeSociete == TypeSociete.PROSPECT){
                // Si la liste est pour des prospects

                // Récupération du prospect
                Optional<Prospect> p =
                        Prospects.get(Integer.parseInt(this.table1.getValueAt(x,0).toString()));

                if(p.isPresent()){
                    // Le prospect existe, le formulaire est à ouvrir avec l'action
                    // voulue
                    new Form(this.typeSociete, action, p.get()).setVisible(true);
                    dispose();
                }
            }
        }else{
            JOptionPane.showMessageDialog(this,
                    "Pas de "+this.typeSociete.getName()+ " sélectionné.");
        }
    }
}
