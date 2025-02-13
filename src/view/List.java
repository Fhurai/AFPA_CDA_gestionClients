package view;

import DAO.AbstractFactory;
import DAO.SocieteDatabaseException;
import entities.Client;
import entities.Prospect;
import entities.TypeAction;
import entities.TypeSociete;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    private JButton contratsButton;

    /**
     * Constructeur
     *
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
        contentPane.registerKeyboardAction(e -> returnIndex(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.addWindowListener(windowAdapter);

        // Butons de modification et de suppression d'une société
        // sélectionnée dans la liste.
        modificationButton.addActionListener(e -> selectEdit(TypeAction.MODIFICATION));
        suppressionButton.addActionListener(e -> selectEdit(TypeAction.SUPPRESSION));

        if (typeSociete == TypeSociete.CLIENT) {
            contratsButton.setVisible(true);
            contratsButton.addActionListener(e -> openContracts());
        } else {
            contratsButton.setVisible(false);
        }
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
                try {
                    yield getModelTable(entete);
                } catch (SocieteDatabaseException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    yield null;
                }
            }
            case TypeSociete.PROSPECT -> {
                // Cas des prospects

                // Création de l'entête et du modèle de tableau à partir de
                // celui-ci
                entete = new String[]{"ID", "Raison sociale", "Adresse", "N° " +
                        "Téléphone", "Adresse mail", "Date prospection",
                        "Prospect intéressé"};
                try {
                    yield getModelTable(entete);
                } catch (SocieteDatabaseException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    yield null;
                }
            }
        };

        // Valorisation du tableau avec le modèle généré précédemment.
        table1.setModel(modelTable);
    }

    /**
     * Méthode d'action sur le tableau et sa ligne sélectionnée.
     *
     * @param action Action à accomplir sur la ligne sélectionnée.
     */
    private void selectEdit(TypeAction action) {
        // Récupération de la ligne sélectionnée.
        int x = table1.getSelectedRow();

        if (x != -1) {
            // Récupération de l'identifiant de la ligne sélectionnée.
            int identifiant = (int) table1.getValueAt(x, 0);

            if (this.typeSociete == TypeSociete.CLIENT) {
                // Si la liste est pour des clients

                // Récupération du client
                try {
                    Client client = new AbstractFactory().getFactory().getClientDAO().findById(x);

                    if (client != null) {
                        // Le client existe, le formulaire est à ouvrir avec l'action
                        // voulue
                        new Form(this.typeSociete, action, client).setVisible(true);
                        dispose();
                    } else {

                        JOptionPane.showMessageDialog(this, "Client introuvable, " +
                                "rechargement de la liste des clients.");
                        this.fillTable();
                    }
                } catch (SocieteDatabaseException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    System.exit(1);
                }
            } else if (this.typeSociete == TypeSociete.PROSPECT) {
                // Si la liste est pour des prospects

                // Récupération du prospect
                Prospect prospect = null;
                try {
                    prospect = new AbstractFactory().getFactory().getProspectDAO().findById(x);

                    if (prospect != null) {
                        // Le prospect existe, le formulaire est à ouvrir avec l'action
                        // voulue
                        new Form(this.typeSociete, action, prospect).setVisible(true);
                        dispose();
                    } else {

                        JOptionPane.showMessageDialog(this, "Prospect " +
                                "introuvable, rechargement de la liste des " +
                                "prospectss .");
                        this.fillTable();
                    }
                } catch (SocieteDatabaseException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    System.exit(1);
                }


            }
        } else {
            // Pas de ligne sélectionnée, avertissement de l'utilisateur.
            JOptionPane.showMessageDialog(this,
                    "Pas de " + this.typeSociete.getName() + " sélectionné.");
        }
    }

    /**
     * Méthode pour ouvrir la fenêtre des contrats d'un client.
     */
    private void openContracts() {
        // Récupération de la ligne sélectionnée.
        int x = table1.getSelectedRow();

        if (x != -1) {
            // Récupération de l'identifiant de la ligne sélectionnée.
            int identifiant = (int) table1.getValueAt(table1.getSelectedRow(), 0);

            try {
                // Récupération du client dont les contrats sont consultés.
                Client client = new AbstractFactory().getFactory().getClientDAO().findById(identifiant);

                if (client != null) {
                    // Client bien récupéré.

                    if (client.getContrats() != null && !client.getContrats().isEmpty()) {
                        // Client a des contrats, ouverture de la fenêtre des
                        // contrats.
                        new Contracts(client).setVisible(true);
                        dispose();
                    } else {
                        // Pas de contrat pour le client, avertissement de
                        // l'utilisateur.
                        JOptionPane.showMessageDialog(this, "Client sans " +
                                "contrat, veuillez sélectionner un autre " +
                                "client.");
                    }
                } else {
                    // Client non trouvé (possible collision d'utilisateurs),
                    // avertissement de l'utilisateur.
                    JOptionPane.showMessageDialog(this, "Client introuvable, " +
                            "rechargement de la liste des clients.");

                    // Client possiblement disparu, rechargement de la liste
                    // des clients.
                    this.fillTable();
                }
            } catch (Exception e) {
                // Erreur rencontrée, affichage à l'utilisateur et fermeture
                // de l'application.
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(1);
            }
        } else {
            // Pas de ligne sélectionnée, avertissement de l'utilisateur.
            JOptionPane.showMessageDialog(this,
                    "Pas de " + this.typeSociete.getName() + " sélectionné.");
        }
    }
}
