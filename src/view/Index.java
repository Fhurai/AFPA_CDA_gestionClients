package view;

import entities.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import static utilities.ViewsUtilities.quitApplication;

/**
 * Fenêtre d'accueil sur l'application.
 */
public class Index extends JFrame {
    private final Dimension windowSize = new Dimension(600, 250);
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
    private JComboBox selectionComboBox;
    private JPanel TypePanel;
    private JPanel ActionPanel;
    private JPanel EditPanel;
    private JLabel choiceAlertLabel;
    private JLabel choiceTypeLabel;
    private JLabel choiceActionLabel;
    private JLabel choiceEditLabel;

    private TypeSociete typeChoice;
    private TypeAction actionChoice;
    private Societe editChoice;

    /**
     * Constructeur
     */
    public Index() {
        init();
        setListeners();
    }

    /**
     * Méthode d'initialisation de la vue.
     */
    private void init() {
        // Valorisation du contenu de la vue.
        setContentPane(contentPane);

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

        // Boutons sélection
        selectionnerButton.addActionListener(e -> choiceEdit());
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
        if (type == TypeSociete.CLIENT) {
            Clients.clients.forEach(client -> selectionComboBox.addItem(client.getRaisonSociale()));
        } else if (type == TypeSociete.PROSPECT) {
            Prospects.prospects.forEach(prospect -> selectionComboBox.addItem(prospect.getRaisonSociale()));
        }

        // Affichage du panneau de choix d'action.
        ActionPanel.setVisible(true);

        // Valorisation de la valeur globale du choix de type de société.
        typeChoice = type;
    }

    /**
     * Méthode pour les actions suivant un choix d'action.
     *
     * @param type Type d'action à manipuler.
     */
    private void choiceAction(@NotNull TypeAction type) {

        switch (type) {
            case TypeAction.CREATION:
                // Création choisie
                EditPanel.setVisible(false);
                choiceEditLabel.setText("");
                break;
            case TypeAction.LISTE:
                // Liste choisie
                EditPanel.setVisible(false);
                choiceEditLabel.setText("");
                new List(this.typeChoice).setVisible(true);
                this.dispose();
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

        // Valorisation de la valeur globale du choix de type d'action.
        actionChoice = type;
    }

    /**
     * Méthode pour les actions suivant un choix d'édition/suppression
     */
    private void choiceEdit() {

        if (typeChoice == TypeSociete.CLIENT) {
            // Le choix est un client
            Clients.clients.forEach(client -> {
                if (client.getRaisonSociale().equalsIgnoreCase(Objects.requireNonNull(selectionComboBox.getSelectedItem()).toString())) {
                    editChoice = client;
                }
            });
        } else if (typeChoice == TypeSociete.PROSPECT) {
            // Le choix est un prospect
            Prospects.prospects.forEach(prospect -> {
                if (prospect.getRaisonSociale().equalsIgnoreCase(Objects.requireNonNull(selectionComboBox.getSelectedItem()).toString())) {
                    editChoice = prospect;
                }
            });
        }

        System.out.println(this.actionChoice.getName() + " " + this.typeChoice.getName() + " " + this.editChoice.getRaisonSociale());
    }
}
