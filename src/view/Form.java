package view;

import entities.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static utilities.ViewsUtilities.quitApplication;
import static utilities.ViewsUtilities.returnIndex;

public class Form extends JFrame {
    private final Dimension windowSize = new Dimension(350, 800);
    private JPanel contentPane;
    private JPanel AppliNamePanel;
    private JLabel AppliNameLabel;
    private JPanel BottomButtonsPanel;
    private JButton quitButton;
    private JButton accueilButton;
    private JPanel ContainerPanel;
    private JTextField identifiantTextfield;
    private JLabel identifiantLabel;
    private JPanel identifiantPanel;
    private JPanel raisonPanel;
    private JLabel raisonLabel;
    private JTextField raisonTextfield;
    private JPanel adressePanel;
    private JLabel numRueLabel;
    private JTextField numRueTextfield;
    private JPanel numRuePanel;
    private JLabel nomRueLabel;
    private JTextField nomRueTextfield;
    private JPanel nomRuePanel;
    private JPanel codePostalPanel;
    private JLabel codePostalLabel;
    private JTextField codePostalTextfield;
    private JPanel villePanel;
    private JLabel villeLabel;
    private JTextField villeTextfield;
    private JPanel telephonePanel;
    private JLabel telephoneLabel;
    private JTextField telephoneTextfield;
    private JPanel mailPanel;
    private JLabel mailLabel;
    private JTextField mailTextfield;
    private JPanel clientPanel;
    private JPanel prospectPanel;
    private JLabel chiffreAffaireLabel;
    private JPanel chiffreAffairePanel;
    private JTextField chiffreAffaireTextfield;
    private JPanel nbEmployesPanel;
    private JLabel nbEmployesLabel;
    private JTextField nbEmployesTextfield;
    private JPanel dateProspectionPanel;
    private JLabel dateProspectionLabel;
    private JTextField dateProspectionTextfield;
    private JPanel prospectInteressePanel;
    private JLabel prospectInteresseLabel;
    private JComboBox prospectInteresseComboBox;
    private JPanel commentiresPanel;
    private JLabel commentairesLabel;
    private JTextArea commentairesTextArea;
    private JPanel boutonPanel;
    private JButton btnButton;

    private TypeSociete typeSociete;
    private TypeAction typeAction;
    private Client client = null;
    private Prospect prospect = null;

    public Form(TypeSociete typeSociete, TypeAction typeAction,
                Societe societe) {
        this.typeSociete = typeSociete;
        this.typeAction = typeAction;

        if (societe instanceof Client) {
            client = (Client) societe;
        } else if (societe instanceof Prospect) {
            prospect = (Prospect) societe;
        }

        init();
        setListeners();
    }

    public Form(TypeSociete typeSociete, TypeAction typeAction) {
        this.typeSociete = typeSociete;
        this.typeAction = typeAction;

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
        this.getRootPane().setDefaultButton(accueilButton);

        // Valorisation de la taille de la fenêtre.
        this.setSize(windowSize);
        this.setMinimumSize(windowSize);

        // Vue centrée sur l'écran.
        this.setLocationRelativeTo(null);

        if (typeSociete == TypeSociete.CLIENT) {
            clientPanel.setVisible(true);
            prospectPanel.setVisible(false);
        } else if (typeSociete == TypeSociete.PROSPECT) {
            clientPanel.setVisible(false);
            prospectPanel.setVisible(true);
        }

        if (this.typeAction == TypeAction.CREATION || this.typeAction == TypeAction.MODIFICATION) {
            btnButton.setText("Sauvegarder");
        } else {
            btnButton.setText("Supprimer");
            raisonTextfield.setEditable(false);
            numRueTextfield.setEditable(false);
            nomRueTextfield.setEditable(false);
            codePostalTextfield.setEditable(false);
            villeTextfield.setEditable(false);
            telephoneTextfield.setEditable(false);
            mailTextfield.setEditable(false);
            chiffreAffaireTextfield.setEditable(false);
            nbEmployesTextfield.setEditable(false);
            dateProspectionTextfield.setEditable(false);
            prospectInteresseComboBox.setEditable(false);
            commentairesTextArea.setEditable(false);
        }

        if (this.typeAction == TypeAction.MODIFICATION || this.typeAction == TypeAction.SUPPRESSION) {
            this.identifiantTextfield.setText(String.valueOf(typeSociete == TypeSociete.CLIENT ? this.client.getIdentifiant() : this.prospect.getIdentifiant()));
            this.raisonTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getRaisonSociale() : this.prospect.getRaisonSociale());
            
        } else {
            this.identifiantTextfield.setText(String.valueOf(typeSociete == TypeSociete.CLIENT ? Clients.compteurIdClients : Prospects.compteurIdProspects));
        }
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
}
