/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.metier.Fournisseur;
import com.metier.Utilisateur;
import com.parseur.FournisseurHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Romaric
 */
@ViewScoped
@ManagedBean(name = "MBFournisseur")
public class MBFournisseur implements Serializable {

    private List<Fournisseur> lstFournisseur = new ArrayList<Fournisseur>();
    private Fournisseur modificationFournisseur = new Fournisseur();
    private Fournisseur ajoutFournisseur = new Fournisseur();
    private Fournisseur supprFournisseur = new Fournisseur();
    private Fournisseur modifFournisseur = new Fournisseur();

    @PostConstruct
    public void init() {
        recupererListeFournisseur();
    }

    public void recupererListeFournisseur() {
        try {
            obtenirListeUtilisateurs();
            //lstUtilisateur.clear();
            lstFournisseur = FournisseurHandler.getListFournisseur();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenirListeUtilisateurs() throws ParserConfigurationException, SAXException, TransformerException {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeFournisseur", "GET");
            stringToDom(ret);
        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String envoyerEtRecevoirMessage(String adresseUrl, String methodeHTTP) throws MalformedURLException, IOException {

        String traitement = adresseUrl.replaceAll(" ", "%20");
        // String retencode = URLEncoder.encode(traitement, "UTF-8");
        URL url = new URL(traitement);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(methodeHTTP);
        StringBuilder sb = new StringBuilder();
        conn.setRequestProperty("Accept", "application/xml");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;

        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();

        return sb.toString();
    }

    void stringToDom(String xmlSource) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(doc);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(new File("G:\\FournisseurTempAdmin.xml"));
        transformer.transform(source, result);

        parserXMLFichier("G:\\FournisseurTempAdmin.xml");
    }

    public void parserXMLFichier(String nomFichier) {
        try {
            // création d'une fabrique de parseurs SAX
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler
            File fichier = new File(nomFichier);
            DefaultHandler gestionnaire = new FournisseurHandler();
            parseur.parse(fichier, gestionnaire);
        } catch (ParserConfigurationException e) {
            System.err.println("Probleme lors de la creation du parser : " + e);
        } catch (SAXException e) {
            System.err.println("Probleme de parsing : " + e);
        } catch (IOException e) {
            System.err.println("Probleme d'entrée/sortie : " + e);
        }

    }

    public void ajouterFournisseur() {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeFournisseur/ajouterFournisseur/" + ajoutFournisseur.getNomfornisseur() + "-" + ajoutFournisseur.getAdressefournisseur() + "-" + ajoutFournisseur.getVillefornisseur() + "-" + ajoutFournisseur.getTelfournisseur() + "-" + ajoutFournisseur.getEmailfournisseur(), "POST");
            if (!ret.isEmpty()) {
                recupererListeFournisseur();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ajout avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void supprimerFournisseur() {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeFournisseur/supprimerFournisseur/" + supprFournisseur.getIdfornisseur(), "POST");
            if (ret.equals("succes")) {
                recupererListeFournisseur();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Suppression avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }
        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modifierFournisseur() throws IOException {

        String url = "http://localhost:8080/CaisseApplication-war/webresources/listeFournisseur/modifierFournisseurById/" + modifFournisseur.getIdfornisseur() + "-" + modifFournisseur.getNomfornisseur() + "-" + modifFournisseur.getAdressefournisseur() + "-" + modifFournisseur.getVillefornisseur() + "-" + modifFournisseur.getTelfournisseur() + "-" + modifFournisseur.getEmailfournisseur();

        String ret = envoyerEtRecevoirMessage(url, "POST");
        if (ret.equals("succes")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modification avec succès !", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

            recupererListeFournisseur();
        }
    }

    /**
     * @return the lstFournisseur
     */
    public List<Fournisseur> getLstFournisseur() {
        return lstFournisseur;
    }

    /**
     * @param lstFournisseur the lstFournisseur to set
     */
    public void setLstFournisseur(List<Fournisseur> lstFournisseur) {
        this.lstFournisseur = lstFournisseur;
    }

    /**
     * @return the modificationFournisseur
     */
    public Fournisseur getModificationFournisseur() {
        return modificationFournisseur;
    }

    /**
     * @param modificationFournisseur the modificationFournisseur to set
     */
    public void setModificationFournisseur(Fournisseur modificationFournisseur) {
        this.modificationFournisseur = modificationFournisseur;
    }

    /**
     * @return the ajoutFournisseur
     */
    public Fournisseur getAjoutFournisseur() {
        return ajoutFournisseur;
    }

    /**
     * @param ajoutFournisseur the ajoutFournisseur to set
     */
    public void setAjoutFournisseur(Fournisseur ajoutFournisseur) {
        this.ajoutFournisseur = ajoutFournisseur;
    }

    /**
     * @return the supprFournisseur
     */
    public Fournisseur getSupprFournisseur() {
        return supprFournisseur;
    }

    /**
     * @param supprFournisseur the supprFournisseur to set
     */
    public void setSupprFournisseur(Fournisseur supprFournisseur) {
        this.supprFournisseur = supprFournisseur;
    }

    /**
     * @return the modifFournisseur
     */
    public Fournisseur getModifFournisseur() {
        return modifFournisseur;
    }

    /**
     * @param modifFournisseur the modifFournisseur to set
     */
    public void setModifFournisseur(Fournisseur modifFournisseur) {
        this.modifFournisseur = modifFournisseur;
    }
}
