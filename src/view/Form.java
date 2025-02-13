package view;

import DAO.AbstractFactory;
import builders.AdresseBuilder;
import builders.ClientBuilder;
import builders.ProspectBuilder;
import entities.*;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;
import utilities.Formatters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.util.logging.Level;

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
     *
     * @param typeSociete Le type de société (CLIENT ou PROSPECT)
     * @param typeAction  Le type d'action (CREATION, LISTE, MODIFICATION ou
     *                    SUPPRESSION).
     * @param societe     La société en cours de modification ou de suppression.
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
     *
     * @param typeSociete Le type de société (CLIENT ou PROSPECT)
     */
    public Form(TypeSociete typeSociete) {
        // Valorisation des variables de la vue pour le type de société et
        // d'action.
        this.typeSociete = typeSociete;
        this.typeAction = TypeAction.CREATION;

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

            this.identifiantTextfield.setText("0");
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
        btnButton.addActionListener(e -> actionPerformed());
    }

    /**
     * Méthode d'action suite au formulaire.
     */
    private void actionPerformed() {

        try {
            switch (this.typeAction) {
                // Cas d'utilisation du formulaire.

                case CREATION:
                    // Cas de la création.

                    if (this.typeSociete == TypeSociete.CLIENT) {
                        // Si le formulaire est pour un client.

                        // Création du client et ajout de celui-ci à la liste des
                        // clients en mémoire.
                        this.setClientFromFields();

                        // Sauvegarde du client dans la base de données.
                        new AbstractFactory().getFactory().getClientDAO().save(client);


                        // Avertissement de l'utilisateur.
                        JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
                    } else if (this.typeSociete == TypeSociete.PROSPECT) {
                        // Si le formulaire est pour un client.

                        // Création du prospect et ajout de celui-ci à la liste des
                        // prospects en mémoire.
                        this.setProspectFromFields();

                        // Sauvegarde du prospect dans la base de données.
                        new AbstractFactory().getFactory().getProspectDAO().save(prospect);

                        // Avertissement de l'utilisateur.
                        JOptionPane.showMessageDialog(this, "Prospect ajouté avec succès !");
                    }

                    break;
                case MODIFICATION:
                    // Cas de la modification.

                    if (this.typeSociete == TypeSociete.CLIENT) {
                        // Remplissage du client avec les données du formulaire
                        this.setClientFromFields();

                        // Sauvegarde du client dans la base de données.
                        new AbstractFactory().getFactory().getClientDAO().save(client);

                        // Avertissement de l'utilisateur.
                        JOptionPane.showMessageDialog(this, "Client modifié " +
                                "avec succès !");
                    } else if (this.typeSociete == TypeSociete.PROSPECT) {
                        // Remplissage du prospect avec les données du formulaire
                        this.setProspectFromFields();

                        // Sauvegarde du prospect dans la base de données.
                        new AbstractFactory().getFactory().getProspectDAO().save(prospect);

                        // Avertissement de l'utilisateur.
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
                            // Suppression du client apres confirmation
                            // utilisateur.
                            new AbstractFactory().getFactory().getClientDAO().delete(client);

                            // Avertissement de l'utilisateur.
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
                            // Suppression du prospect apres confirmation
                            // utilisateur.
                            new AbstractFactory().getFactory().getProspectDAO().delete(prospect);

                            // Avertissement de l'utilisateur.
                            JOptionPane.showMessageDialog(this, "Prospect " +
                                    "supprimé avec succès !");
                        }
                    }
                    break;
            }

            // Retour sur la vue d'index de l'application.
            returnIndex(this);
        } catch (NumberFormatException nfe) {
            LogManager.logs.log(Level.SEVERE, nfe.getMessage(), nfe);
            JOptionPane.showMessageDialog(null, "Erreur dans le format du " +
                    "nombre.");
        } catch (DateTimeException dte) {
            LogManager.logs.log(Level.SEVERE, dte.getMessage(), dte);
            JOptionPane.showMessageDialog(null, "Erreur dans la date.");
        } catch (SocieteEntityException see) {
            LogManager.logs.log(Level.SEVERE, see.getMessage(), see);
            JOptionPane.showMessageDialog(null, see.getMessage());
        } catch (Exception e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(null, "Erreur inconnue.");
            System.exit(1);
        }
    }

    /**
     * Methode qui transpose les données du formulaire dans l'objet Client de
     * la vue.
     *
     * @throws SocieteEntityException Exception de l'identifiant ou de la
     * raison sociale.
     */
    private void setClientFromFields() throws SocieteEntityException {
        int id = client != null ? client.getIdentifiant() : 0;
        int idAdresse = client != null ?
                client.getAdresse().getIdentifiant() : 0;
        client = ClientBuilder.getNewClientBuilder()
                .deRaisonSociale(this.raisonTextfield.getText())
                .dAdresse(AdresseBuilder.getNewAdresseBuilder()
                        .dIdentifiant(0)
                        .deNumeroRue(this.numRueTextfield.getText())
                        .deNomRue(this.nomRueTextfield.getText())
                        .deCodePostal(this.codePostalTextfield.getText())
                        .deVille(this.villeTextfield.getText())
                        .build())
                .deTelephone(this.telephoneTextfield.getText())
                .deMail(this.mailTextfield.getText())
                .deCommentaires(this.commentairesTextArea.getText())
                .deChiffreAffaires(this.chiffreAffaireTextfield.getText())
                .deNombreEmployes(this.nbEmployesTextfield.getText())
                .build();

        if(id > 0) client.setIdentifiant(id);
        if(idAdresse > 0) client.getAdresse().setIdentifiant(idAdresse);
    }

    /**
     * Méthode qui transpose les données du formulaire dans l'objet Prospect
     * de la vue.
     *
     * @throws SocieteEntityException Exception de l'identifiant ou de la
     * raison sociale.
     */
    private void setProspectFromFields() throws SocieteEntityException {

        int id =  prospect != null ? prospect.getIdentifiant() : 0;
        int idAdresse =  prospect != null ? prospect.getAdresse().getIdentifiant() : 0;

        prospect = ProspectBuilder.getNewProspectBuilder()
                .deRaisonSociale(this.raisonTextfield.getText())
                .dAdresse(AdresseBuilder.getNewAdresseBuilder()
                        .deNumeroRue(this.numRueTextfield.getText())
                        .deNomRue(this.nomRueTextfield.getText())
                        .deCodePostal(this.codePostalTextfield.getText())
                        .deVille(this.villeTextfield.getText())
                        .build())
                .deTelephone(this.telephoneTextfield.getText())
                .deMail(this.mailTextfield.getText())
                .deCommentaires(this.commentairesTextArea.getText())
                .deDateProspection(this.dateProspectionTextfield.getText())
                .dInteresse((String) this.prospectInteresseComboBox.getSelectedItem())
                .build();


        if(id > 0) prospect.setIdentifiant(id);
        if(idAdresse > 0) prospect.getAdresse().setIdentifiant(idAdresse);
    }
}
