/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.metier.Lignecommande;
import com.parseur.LignecommandeHandler;
import com.parseur.UtilisateurHandler;
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
@ManagedBean(name = "MBCommande")
public class MBCommande implements Serializable {

    private List<Lignecommande> lstLigneDeCommmande = new ArrayList<Lignecommande>();
    private Lignecommande modifLigneCommande = new Lignecommande();
    private Lignecommande supprLigneCommande = new Lignecommande();
    private Lignecommande ajoutLingeCommande = new Lignecommande();

    @PostConstruct
    public void init() {
        recupererListeCommande();
    }

    public void recupererListeCommande() {
        try {
            obtenirListeLingeCommande();
            //lstUtilisateur.clear();
            int debug = 0;
            lstLigneDeCommmande = LignecommandeHandler.getListLignecommande();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenirListeLingeCommande() throws ParserConfigurationException, SAXException, TransformerException {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listelignecommande", "GET");
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
        StreamResult result = new StreamResult(new File("G:\\LignecommandeTempAdmin.xml"));
        transformer.transform(source, result);

        parserXMLFichier("G:\\LignecommandeTempAdmin.xml");
    }

    public void parserXMLFichier(String nomFichier) {
        try {
            // création d'une fabrique de parseurs SAX
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler
            File fichier = new File(nomFichier);
            DefaultHandler gestionnaire = new LignecommandeHandler();
            parseur.parse(fichier, gestionnaire);
        } catch (ParserConfigurationException e) {
            System.err.println("Probleme lors de la creation du parser : " + e);
        } catch (SAXException e) {
            System.err.println("Probleme de parsing : " + e);
        } catch (IOException e) {
            System.err.println("Probleme d'entrée/sortie : " + e);
        }

    }

    public void modifierCommande() {

        String url = "http://localhost:8080/CaisseApplication-war/webresources/listelignecommande/modifierLigneCommandeById/" + modifLigneCommande.getIdlignedecommande() + "-" + modifLigneCommande.getDesignationcommande() + "-" + modifLigneCommande.getQuantite() + "-" + modifLigneCommande.getNomfournisseur() + "-" + modifLigneCommande.getPrixunitaire();

    
        String ret = "";
        try {
            ret = envoyerEtRecevoirMessage(url, "POST");
        } catch (IOException ex) {
            Logger.getLogger(MBCommande.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ret.equals("succes")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modification avec succès !", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

            recupererListeCommande();
        }
    }

    public void supprimerCommande() {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listelignecommande/supprimerLignecommande/" + supprLigneCommande.getIdlignedecommande(), "POST");
            if (ret.equals("succes")) {
                recupererListeCommande();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Suppression avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }
        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void ajouterCommande() {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listelignecommande/ajouterLignecommande/" + ajoutLingeCommande.getDesignationcommande() + "-" + ajoutLingeCommande.getNomfournisseur() + "-" + ajoutLingeCommande.getQuantite() + "-" + ajoutLingeCommande.getPrixunitaire(), "POST");

            if (!ret.isEmpty()) {
                recupererListeCommande();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ajout avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the lstLigneDeCommmande
     */
    public List<Lignecommande> getLstLigneDeCommmande() {
        return lstLigneDeCommmande;
    }

    /**
     * @param lstLigneDeCommmande the lstLigneDeCommmande to set
     */
    public void setLstLigneDeCommmande(List<Lignecommande> lstLigneDeCommmande) {
        this.lstLigneDeCommmande = lstLigneDeCommmande;
    }

    /**
     * @return the modifLigneCommande
     */
    public Lignecommande getModifLigneCommande() {
        return modifLigneCommande;
    }

    /**
     * @param modifLigneCommande the modifLigneCommande to set
     */
    public void setModifLigneCommande(Lignecommande modifLigneCommande) {
        this.modifLigneCommande = modifLigneCommande;
    }

    /**
     * @return the supprLigneCommande
     */
    public Lignecommande getSupprLigneCommande() {
        return supprLigneCommande;
    }

    /**
     * @param supprLigneCommande the supprLigneCommande to set
     */
    public void setSupprLigneCommande(Lignecommande supprLigneCommande) {
        this.supprLigneCommande = supprLigneCommande;
    }

    /**
     * @return the ajoutLingeCommande
     */
    public Lignecommande getAjoutLingeCommande() {
        return ajoutLingeCommande;
    }

    /**
     * @param ajoutLingeCommande the ajoutLingeCommande to set
     */
    public void setAjoutLingeCommande(Lignecommande ajoutLingeCommande) {
        this.ajoutLingeCommande = ajoutLingeCommande;
    }

}
