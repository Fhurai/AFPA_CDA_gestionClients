package view;

import entities.*;
import org.jetbrains.annotations.NotNull;
import utilities.Formatters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.Objects;

import static utilities.ViewsUtilities.quitApplication;
import static utilities.ViewsUtilities.returnIndex;

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
    private JComboBox prospectInteresseComboBox;
    private JPanel commentairesPanel;
    private JLabel commentairesLabel;
    private JTextArea commentairesTextArea;
    private JPanel boutonPanel;
    private JButton btnButton;
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
            for (ReponseFermee rf : ReponseFermee.values()) {
                this.prospectInteresseComboBox.addItem(rf.getValue());
            }
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

        btnButton.addActionListener(e -> {
            try {
                actionPerformed();
            } catch (SocieteEntityException see) {
                System.out.println(see.getMessage());
            }
        });
    }


    private void actionPerformed() throws SocieteEntityException {
        switch (this.typeAction) {
            case CREATION:
                if (this.typeSociete == TypeSociete.CLIENT) {
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
                // Modification de tous les champs
                // Save()
                if (this.typeSociete == TypeSociete.CLIENT) {
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

                    int index = Clients.clients.indexOf(client);
                    Clients.clients.set(index, client);
                    JOptionPane.showMessageDialog(this, "Client modifié " +
                            "avec succès !");
                } else if (this.typeSociete == TypeSociete.PROSPECT) {
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

                    int index = Prospects.prospects.indexOf(prospect);
                    Prospects.prospects.set(index, prospect);
                    JOptionPane.showMessageDialog(this, "Prospect modifié " +
                            "avec succès !");
                }
                break;
            case SUPPRESSION:
                int reponse = -1;
                if (this.typeSociete == TypeSociete.CLIENT) {
                    reponse = JOptionPane.showConfirmDialog(this, "Souhaitez" +
                            " vous supprimer " + client.getRaisonSociale() + " ?");
                    if (reponse == JOptionPane.OK_OPTION){
                        Clients.clients.remove(client);
                        JOptionPane.showMessageDialog(this, "Client supprimé " +
                                "avec succès !");
                    }
                } else if (this.typeSociete == TypeSociete.PROSPECT) {
                    reponse = JOptionPane.showConfirmDialog(this, "Souhaitez " +
                            "vous supprimer " + prospect.getRaisonSociale() + " ?");
                    if (reponse == JOptionPane.OK_OPTION){
                        Prospects.prospects.remove(prospect);
                        JOptionPane.showMessageDialog(this, "Prospect " +
                                "supprimé avec succès !");
                    }
                }
                    break;
        }

        returnIndex(this);
    }
}
