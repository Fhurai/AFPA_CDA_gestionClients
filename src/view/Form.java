package view;

import entities.*;
import org.jetbrains.annotations.NotNull;
import utilities.Files;
import utilities.Formatters;
import utilities.SocieteUtilitiesException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.Objects;

import static utilities.ViewsUtilities.quitApplication;
import static utilities.ViewsUtilities.returnIndex;

/**
 * Fenêtre de formulaire
 */
public class Form extends JFrame {
    private final Dimension windowSize = new Dimension(350, 800);
    private final TypeSociete typeSociete;
    private final TypeAction typeAction;
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
    private JComboBox<String> prospectInteresseComboBox;
    private JPanel commentairesPanel;
    private JLabel commentairesLabel;
    private JTextArea commentairesTextArea;
    private JPanel boutonPanel;
    private JButton btnButton;
    private Client client = null;
    private Prospect prospect = null;

    /**
     * Constructeur avec toutes les variables nécessaires à la modification
     * ou à la suppression.
     * @param typeSociete Le type de société (CLIENT ou PROSPECT)
     * @param typeAction Le type d'action (CREATION, LISTE, MODIFICATION ou
     *                   SUPPRESSION).
     * @param societe La société en cours de modification ou de suppression.
     */
    public Form(TypeSociete typeSociete, TypeAction typeAction,
                Societe societe) {
        // Valorisation des variables de la vue pour le type de société et
        // d'action.
        this.typeSociete = typeSociete;
        this.typeAction = typeAction;

        // Si la société est un client, c'est la variable de la vue pour le
        // client qui est valorisée, sinon c'est la variable de la vue pour
        // le prospect.
        if (societe instanceof Client) {
            client = (Client) societe;
        } else if (societe instanceof Prospect) {
            prospect = (Prospect) societe;
        }

        // Initialisation de la vue et de ses écouteurs d'évènements.
        init();
        setListeners();
    }

    /**
     * Constructeur avec toutes les variables nécessaires à la création
     * @param typeSociete Le type de société (CLIENT ou PROSPECT)
     * @param typeAction Le type d'action (CREATION, LISTE, MODIFICATION ou
     *                   SUPPRESSION).
     */
    public Form(TypeSociete typeSociete, TypeAction typeAction) {
        // Valorisation des variables de la vue pour le type de société et
        // d'action.
        this.typeSociete = typeSociete;
        this.typeAction = typeAction;

        // Initialisation de la vue et de ses écouteurs d'évènements.
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
            // Si le formulaire est pour un client

            // Affichage de la partie client et cache-cache de la partie
            // prospect.
            clientPanel.setVisible(true);
            prospectPanel.setVisible(false);
        } else if (typeSociete == TypeSociete.PROSPECT) {
            // Si le formulaire est pour un prospect

            // Cache-cache de la partie client et affichage de la partie
            // prospect.
            clientPanel.setVisible(false);
            prospectPanel.setVisible(true);

            // Remplissage de liste déroulante des réponses fermées possibles.
            for (ReponseFermee rf : ReponseFermee.values()) {
                this.prospectInteresseComboBox.addItem(rf.getValue());
            }
        }

        if (this.typeAction == TypeAction.CREATION || this.typeAction == TypeAction.MODIFICATION) {
            // Si le formulaire est pour de la création ou de la
            // modification, affichage du bouton sauvegarder.
            btnButton.setText("Sauvegarder");
        } else {
            // Si le formulaire est pour de la suppression, affichage du
            // bouton supprimer.
            btnButton.setText("Supprimer");

            // Tous les champs ne sont plus éditables.
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
            // Si le formulaire est pour de la modification ou de la
            // suppression.

            // Valorisation de tous les champs avec les valeurs de la société.
            this.identifiantTextfield.setText(String.valueOf(typeSociete == TypeSociete.CLIENT ?
                    this.client.getIdentifiant() : this.prospect.getIdentifiant()));
            this.raisonTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getRaisonSociale() : this.prospect.getRaisonSociale());
            this.numRueTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getAdresse().getNumeroRue() : this.prospect.getAdresse().getNumeroRue());
            this.nomRueTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getAdresse().getNomRue() : this.prospect.getAdresse().getNomRue());
            this.codePostalTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getAdresse().getCodePostal() : this.prospect.getAdresse().getCodePostal());
            this.villeTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getAdresse().getVille() : this.prospect.getAdresse().getVille());
            this.telephoneTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getTelephone() : this.prospect.getTelephone());
            this.mailTextfield.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getMail() : this.prospect.getMail());
            this.commentairesTextArea.setText(typeSociete == TypeSociete.CLIENT ?
                    this.client.getCommentaires() : this.prospect.getCommentaires());

            if (typeSociete == TypeSociete.CLIENT) {
                this.chiffreAffaireTextfield.setText(String.valueOf(this.client.getChiffreAffaires()));
                this.nbEmployesTextfield.setText(String.valueOf(this.client.getNbEmployes()));
            } else {
                this.dateProspectionTextfield.setText(this.prospect.getDateProspection().format(Formatters.FORMAT_DDMMYYYY));
                this.prospectInteresseComboBox.setSelectedItem(this.prospect.getProspectInteresse());
            }

        } else {
            // Si le formulaire est pour de la création, valorisation
            // uniquement pour le champ identifiant.

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

        // Bouton pour les actions suite au formulaire.
        btnButton.addActionListener(e -> {
            try {
                actionPerformed();
            } catch (SocieteEntityException see) {
                JOptionPane.showMessageDialog(null, see.getMessage());
            }
        });
    }

    /**
     * Méthode d'action suite au formulaire.
     * @throws SocieteEntityException Exception des entités.
     */
    private void actionPerformed() throws SocieteEntityException {

        switch (this.typeAction) {
            // Cas d'utilisation du formulaire.

            case CREATION:
                // Cas de la création.

                if (this.typeSociete == TypeSociete.CLIENT) {
                    // Si le formulaire est pour un client.

                    // Création du client et ajout de celui-ci à la liste des
                    // clients en mémoire.
                    client = new Client(this.raisonTextfield.getText(),
                            new Adresse(
                                    this.numRueTextfield.getText(),
                                    this.nomRueTextfield.getText(),
                                    this.codePostalTextfield.getText(),
                                    this.villeTextfield.getText()
                            ),
                            this.telephoneTextfield.getText(),
                            this.mailTextfield.getText(),
                            this.commentairesTextArea.getText(),
                            Long.parseLong(this.chiffreAffaireTextfield.getText()),
                            Integer.parseInt(this.nbEmployesTextfield.getText()));
                    Clients.toClientsAdd(client);

                    JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
                } else if (this.typeSociete == TypeSociete.PROSPECT) {
                    // Si le formulaire est pour un client.

                    // Création du prospect et ajout de celui-ci à la liste des
                    // prospects en mémoire.
                    prospect = new Prospect(
                            this.raisonTextfield.getText(),
                            new Adresse(
                                    this.numRueTextfield.getText(),
                                    this.nomRueTextfield.getText(),
                                    this.codePostalTextfield.getText(),
                                    this.villeTextfield.getText()
                            ),
                            this.telephoneTextfield.getText(),
                            this.mailTextfield.getText(),
                            this.commentairesTextArea.getText(),
                            LocalDate.parse(this.dateProspectionTextfield.getText(), Formatters.FORMAT_DDMMYYYY),
                            (String) this.prospectInteresseComboBox.getSelectedItem());
                    Prospects.toProspectsAdd(prospect);

                    JOptionPane.showMessageDialog(this, "Prospect ajouté avec succès !");
                }
                break;
            case MODIFICATION:
                // Cas de la modification.

                if (this.typeSociete == TypeSociete.CLIENT) {
                    // Remplissage du client avec les données du formulaire
                    client.setRaisonSociale(this.raisonTextfield.getText());
                    client.getAdresse().setNumeroRue(this.numRueTextfield.getText());
                    client.getAdresse().setNomRue(this.nomRueTextfield.getText());
                    client.getAdresse().setCodePostal(this.codePostalTextfield.getText());
                    client.getAdresse().setVille(this.villeTextfield.getText());
                    client.setTelephone(this.telephoneTextfield.getText());
                    client.setMail(this.mailTextfield.getText());
                    client.setCommentaires(this.commentairesTextArea.getText());
                    client.setChiffreAffaires(Long.parseLong(this.chiffreAffaireTextfield.getText()));
                    client.setNbEmployes(Integer.parseInt(this.nbEmployesTextfield.getText()));

                    // Recherche du client modifié parmi la liste des clients
                    // et modification de celui-c.
                    int index = Clients.clients.indexOf(client);
                    Clients.clients.set(index, client);
                    JOptionPane.showMessageDialog(this, "Client modifié " +
                            "avec succès !");
                } else if (this.typeSociete == TypeSociete.PROSPECT) {
                    // Remplissage du prospect avec les données du formulaire
                    prospect.setRaisonSociale(this.raisonTextfield.getText());
                    prospect.getAdresse().setNumeroRue(this.numRueTextfield.getText());
                    prospect.getAdresse().setNomRue(this.nomRueTextfield.getText());
                    prospect.getAdresse().setCodePostal(this.codePostalTextfield.getText());
                    prospect.getAdresse().setVille(this.villeTextfield.getText());
                    prospect.setTelephone(this.telephoneTextfield.getText());
                    prospect.setMail(this.mailTextfield.getText());
                    prospect.setCommentaires(this.commentairesTextArea.getText());
                    prospect.setDateProspection(LocalDate.parse(this.dateProspectionTextfield.getText(), Formatters.FORMAT_DDMMYYYY));
                    prospect.setProspectInteresse(Objects.requireNonNull(this.prospectInteresseComboBox.getSelectedItem()).toString());

                    // Recherche du prospect modifié parmi la liste des
                    // prospects et modification de celui-c.
                    int index = Prospects.prospects.indexOf(prospect);
                    Prospects.prospects.set(index, prospect);
                    JOptionPane.showMessageDialog(this, "Prospect modifié " +
                            "avec succès !");
                }
                break;
            case SUPPRESSION:
                // Cas de la suppression.

                // Initialisation de la réponse.
                int reponse;

                if (this.typeSociete == TypeSociete.CLIENT) {
                    // Si le formulaire est pour un client.

                    // Demande de confirmation de suppression et suppression
                    // si la demande est confirmée.
                    reponse = JOptionPane.showConfirmDialog(this, "Souhaitez" +
                            " vous supprimer " + client.getRaisonSociale() + " ?");

                    if (reponse == JOptionPane.OK_OPTION) {
                        Clients.clients.remove(client);
                        JOptionPane.showMessageDialog(this, "Client supprimé " +
                                "avec succès !");
                    }
                } else if (this.typeSociete == TypeSociete.PROSPECT) {
                    // Si le formulaire est pour un prospect

                    // Demande de confirmation de suppression et suppression
                    // si la demande est confirmée.
                    reponse = JOptionPane.showConfirmDialog(this, "Souhaitez " +
                            "vous supprimer " + prospect.getRaisonSociale() + " ?");

                    if (reponse == JOptionPane.OK_OPTION) {
                        Prospects.prospects.remove(prospect);
                        JOptionPane.showMessageDialog(this, "Prospect " +
                                "supprimé avec succès !");
                    }
                }
                break;
        }

        try {
            // Tentative de sauvegarde de l'action en base de données avec un
            // retour sur la vue d'accueil.
            Files.dbSave(this.typeSociete);
            returnIndex(this);
        } catch (SocieteUtilitiesException sue) {
            JOptionPane.showMessageDialog(null, sue.getMessage());
        }
    }
}
