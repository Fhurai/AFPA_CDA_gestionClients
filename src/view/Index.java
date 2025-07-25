package view;

import DAO.AbstractFactory;
import DAO.SocieteDatabaseException;
import DAO.TypeDatabase;
import entities.*;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

import static utilities.ViewsUtilities.quitApplication;

/**
 * Fenêtre d'accueil sur l'application.
 */
public class Index extends JFrame {
    private final Dimension windowSize = new Dimension(700, 250);
    private JPanel contentPane;
    private JButton quitButton;
    private JPanel AppliNamePanel;
    private JPanel BottomButtonsPanel;
    private JPanel ContainerPanel;
    private JLabel AppliNameLabel;
    private JButton prospectsButton;
    private JButton clientsButton;
    private JButton suppressionButton;
    private JButton modificationButton;
    private JButton listeButton;
    private JButton creationButton;
    private JButton selectionnerButton;
    private JComboBox<String> selectionComboBox;
    private JPanel TypePanel;
    private JPanel ActionPanel;
    private JPanel EditPanel;
    private JLabel choiceAlertLabel;
    private JLabel choiceTypeLabel;
    private JLabel choiceActionLabel;
    private JLabel choiceEditLabel;
    private JButton contratsButton;
    private JComboBox dbComboBox;
    private JButton connecterButton;

    private TypeSociete typeChoice;
    private TypeAction actionChoice;
    private Societe editChoice = null;

    private final AbstractFactory factory;

    /**
     * Constructeur
     */
    public Index() {
        // Initialisation de la vue et de ses écouteurs d'évènements.
        init();
        setListeners();

        factory = new AbstractFactory();
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

        // Remplissage du combobox de sélection de la base de données.
        dbComboBox.removeAllItems();
        for (TypeDatabase tdb : TypeDatabase.values()) {
            dbComboBox.addItem(tdb.getName());
        }
        dbComboBox.setSelectedIndex(AbstractFactory.getTypeDatabase().getNumber() - 1);
        connecterButton.setVisible(false);

        // Valorisation du bouton par défaut.
        this.getRootPane().setDefaultButton(quitButton);

        // Valorisation de la taille de la fenêtre.
        this.setSize(windowSize);
        this.setMinimumSize(windowSize);

        // Vue centrée sur l'écran.
        this.setLocationRelativeTo(null);

        // Cache-cache de certains éléments graphiques.
        choiceAlertLabel.setVisible(false);
        ActionPanel.setVisible(false);
        EditPanel.setVisible(false);
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
                quitApplication((JFrame) e.getComponent());
            }
        };

        // Boutons et touche clavier pour fermer l'application.
        quitButton.addActionListener(e -> quitApplication(this));
        contentPane.registerKeyboardAction(e -> quitApplication(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.addWindowListener(windowAdapter);

        // Boutons type panel
        clientsButton.addActionListener(e -> choiceType(TypeSociete.CLIENT));
        prospectsButton.addActionListener(e -> choiceType(TypeSociete.PROSPECT));

        // Boutons type action
        creationButton.addActionListener(e -> choiceAction(TypeAction.CREATION));
        listeButton.addActionListener(e -> choiceAction(TypeAction.LISTE));
        modificationButton.addActionListener(e -> choiceAction(TypeAction.MODIFICATION));
        suppressionButton.addActionListener(e -> choiceAction(TypeAction.SUPPRESSION));
        contratsButton.addActionListener(e -> choiceAction(TypeAction.CONTRATS));

        // Boutons sélection société.
        selectionnerButton.addActionListener(e -> choiceEdit());

        // Combobox sélection base de données.
        dbComboBox.addActionListener(e -> {
            connecterButton.setVisible(!Objects.equals(dbComboBox.getSelectedItem(), AbstractFactory.getTypeDatabase().getName()));
        });

        // Bouton sélection base de données.
        connecterButton.addActionListener(e -> {
            try {
                AbstractFactory.setTypeDatabase(TypeDatabase.findByString((String) dbComboBox.getSelectedItem()));
                factory.getFactory().init();
                JOptionPane.showMessageDialog(this, "Changement de base de " +
                        "donnéees...");
            } catch (SocieteDatabaseException ex) {
                LogManager.logs.log(Level.SEVERE, ex.getMessage());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
            init();
        });
    }

    /**
     * Méthode pour les actions suivant un choix de type de société.
     *
     * @param type Type de société à manipuler.
     */
    private void choiceType(@NotNull TypeSociete type) {
        // Label alertant l'utilisateur de son choix de type de société.
        choiceAlertLabel.setForeground(new Color(89, 132, 87));
        choiceAlertLabel.setVisible(true);
        choiceAlertLabel.setText("Vous avez choisi " + type.getName() + "s");

        // Reset de la liste déroulante et remplissage de celle-ci avec les
        // sociétés correspondantes au type choisi.
        selectionComboBox.removeAllItems();
        try {
            if (type == TypeSociete.CLIENT) {
                // Affiche le bouton de sélection pour les contrats.
                this.contratsButton.setVisible(true);

                // Récupération de tous les clients de la base de données.
                ArrayList<Client> clients =
                        new AbstractFactory().getFactory().getClientDAO().findAll();

                if(!clients.isEmpty()) {
                    // Un client existe.

                    // Remplissage de la liste déroulante des clients.
                    clients.forEach(client -> selectionComboBox.addItem(client.getRaisonSociale()));

                    // Activation des boutons nécessitant au moins un
                    // client.
                    this.listeButton.setEnabled(true);
                    this.contratsButton.setEnabled(true);
                    this.modificationButton.setEnabled(true);
                    this.suppressionButton.setEnabled(true);
                }else{
                    // Aucun client n'existe.

                    // Désactivation des boutons nécessitant au moins un
                    // client.
                    this.listeButton.setEnabled(false);
                    this.contratsButton.setEnabled(false);
                    this.modificationButton.setEnabled(false);
                    this.suppressionButton.setEnabled(false);
                }
            } else if (type == TypeSociete.PROSPECT) {
                // N'affiche pas le bouton de sélection pour les contrats.
                this.contratsButton.setVisible(false);

                ArrayList<Prospect> prospects =
                        new AbstractFactory().getFactory().getProspectDAO().findAll();

                if(!prospects.isEmpty()) {
                    // Un prospect existe.

                    // Remplissage de la liste déroulante des prospects.
                    prospects.forEach(prospect -> selectionComboBox.addItem(prospect.getRaisonSociale()));

                    // Activation des boutons nécessitant au moins un
                    // prospect.
                    this.listeButton.setEnabled(true);
                    this.modificationButton.setEnabled(true);
                    this.suppressionButton.setEnabled(true);
                }else{
                    // Aucun client n'existe.

                    // Désactivation des boutons nécessitant au moins un
                    // prospect.
                    this.listeButton.setEnabled(false);
                    this.modificationButton.setEnabled(false);
                    this.suppressionButton.setEnabled(false);
                }
            }

            // Affichage du panneau de choix d'action.
            ActionPanel.setVisible(true);

            // Valorisation de la valeur globale du choix de type de société.
            this.typeChoice = type;
        } catch (SocieteDatabaseException e) {
            if(e.getCause() != null && e.getCause() instanceof SocieteEntityException) {
                JOptionPane.showMessageDialog(null, "Erreur d'intégrité dans " +
                        "les données de la base de données ! \nVeuillez " +
                        "consulter le service informatique !");
                LogManager.logs.warning(e.getCause().getMessage());
            }else{
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Méthode pour les actions suivant un choix d'action.
     *
     * @param type Type d'action à manipuler.
     */
    private void choiceAction(@NotNull TypeAction type) {

        // Valorisation de la valeur globale du choix de type d'action.
        actionChoice = type;

        switch (type) {

            case TypeAction.CREATION:
                // Création choisie
                EditPanel.setVisible(false);
                choiceEditLabel.setText("");

                new Form(this.typeChoice).setVisible(true);
                this.dispose();
                break;

            case TypeAction.LISTE:
                // Liste choisie
                EditPanel.setVisible(false);
                choiceEditLabel.setText("");

                new List(this.typeChoice).setVisible(true);
                this.dispose();
                break;

            case TypeAction.CONTRATS:
                // Contrats choisis
                EditPanel.setVisible(true);
                choiceEditLabel.setText("Quelle société consulter ?");
                break;

            case TypeAction.MODIFICATION:
                // Modification choisie
                EditPanel.setVisible(true);
                choiceEditLabel.setText("Quelle société modifier ?");
                break;

            case TypeAction.SUPPRESSION:
                // Suppression choisie
                EditPanel.setVisible(true);
                choiceEditLabel.setText("Quelle société supprimer ?");
                break;
        }
    }

    /**
     * Méthode pour les actions suivant un choix d'édition/suppression
     */
    private void choiceEdit() {

        try {
            if (typeChoice == TypeSociete.CLIENT) {
                // Le choix est un client
                editChoice =
                        new AbstractFactory().getFactory().getClientDAO().find(Objects.requireNonNull(selectionComboBox.getSelectedItem()).toString());
            } else if (typeChoice == TypeSociete.PROSPECT) {
                // Le choix est un prospect
                editChoice = new AbstractFactory().getFactory().getProspectDAO().find(Objects.requireNonNull(selectionComboBox.getSelectedItem()).toString());
            }

            if (this.actionChoice == TypeAction.CONTRATS) {
                if (this.typeChoice == TypeSociete.CLIENT && !((Client) this.editChoice).getContrats().isEmpty()) {
                    new Contracts((Client) this.editChoice).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Client sans " +
                            "contrat, veuillez sélectionner un autre " +
                            "client.");
                }
            } else {
                new Form(this.typeChoice, this.actionChoice, this.editChoice).setVisible(true);
                dispose();
            }
        } catch (SocieteDatabaseException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ouverture " +
                    "du formulaire.");
        }
    }
}
