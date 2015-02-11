/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.metier.Categorie;
import com.parseur.CategorieHandler;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
@ManagedBean(name = "MBCategorie")
public class MBCategorie implements Serializable {

    private List<Categorie> lstCategorie = new ArrayList<Categorie>();
    private Categorie ajoutCategorie = new Categorie();
    private Categorie supprCategorie = new Categorie();
    private Categorie modifCategorie = new Categorie();

    @PostConstruct
    public void init() {
        initialiserListeCategorie();
    }

    public void initialiserListeCategorie() {
        try {
            parserXML();
            //        lstCategorie.clear();
            lstCategorie = CategorieHandler.getListCategorie();
        } catch (Exception ex) {
            Logger.getLogger(MBCategorie.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void parserXML() throws Exception {

        String str = envoyerRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeCategorie", "GET");

        stringToDom(str);
    }

    public String envoyerRecevoirMessage(String adresseUrl, String methodeHTTP) throws MalformedURLException, IOException {

        URL url = new URL(adresseUrl);
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
        StreamResult result = new StreamResult(new File("G:\\categorieTemp.xml"));
        transformer.transform(source, result);

        parserXMLFichier("G:\\categorieTemp.xml");
    }

    public void parserXMLFichier(String nomFichier) {
        try {
            // création d'une fabrique de parseurs SAX
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler
            File fichier = new File(nomFichier);
            DefaultHandler gestionnaire = new CategorieHandler();
            parseur.parse(fichier, gestionnaire);
        } catch (ParserConfigurationException e) {
            System.err.println("Probleme lors de la creation du parser : " + e);
        } catch (SAXException e) {
            System.err.println("Probleme de parsing : " + e);
        } catch (IOException e) {
            System.err.println("Probleme d'entrée/sortie : " + e);
        }

    }

    public void ajouterCategorie() {

        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeCategorie/ajouterCategorie/" + ajoutCategorie.getLibelle() + "-" + ajoutCategorie.getCodecategorie() + "-" + ajoutCategorie.getDescription(), "POST");

            if (ret.equals("success")) {
                initialiserListeCategorie();
            }
        } catch (IOException ex) {
            Logger.getLogger(MBCategorie.class.getName()).log(Level.SEVERE, null, ex);
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

    public void supprimerCategorie() {

        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeCategorie/supprimerCategorie/" + supprCategorie.getIdcategorie(), "POST");
            if (ret.equals("success")) {
                initialiserListeCategorie();
            }
        } catch (IOException ex) {
            Logger.getLogger(MBCategorie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modifierCategorie(){
        try {
            envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeCategorie/modifierCategorieByIdCategorie/" + modifCategorie.getIdcategorie()+"-"+modifCategorie.getCodecategorie()+"-"+modifCategorie.getLibelle()+"-"+modifCategorie.getDescription(), "POST");
            initialiserListeCategorie();
        } catch (IOException ex) {
            Logger.getLogger(MBCategorie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * @return the lstCategorie
     */
    public List<Categorie> getLstCategorie() {
        return lstCategorie;
    }

    /**
     * @param lstCategorie the lstCategorie to set
     */
    public void setLstCategorie(List<Categorie> lstCategorie) {
        this.lstCategorie = lstCategorie;
    }

    /**
     * @return the ajoutCategorie
     */
    public Categorie getAjoutCategorie() {
        return ajoutCategorie;
    }

    /**
     * @param ajoutCategorie the ajoutCategorie to set
     */
    public void setAjoutCategorie(Categorie ajoutCategorie) {
        this.ajoutCategorie = ajoutCategorie;
    }

    /**
     * @return the supprCategorie
     */
    public Categorie getSupprCategorie() {
        return supprCategorie;
    }

    /**
     * @param supprCategorie the supprCategorie to set
     */
    public void setSupprCategorie(Categorie supprCategorie) {
        this.supprCategorie = supprCategorie;
    }

    /**
     * @return the modifCategorie
     */
    public Categorie getModifCategorie() {
        return modifCategorie;
    }

    /**
     * @param modifCategorie the modifCategorie to set
     */
    public void setModifCategorie(Categorie modifCategorie) {
        this.modifCategorie = modifCategorie;
    }
}
