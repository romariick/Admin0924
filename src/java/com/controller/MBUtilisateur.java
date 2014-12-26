/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.metier.Utilisateur;
import com.parseur.ArticleHandler;
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
@ManagedBean(name = "MBUtilisateur")
public class MBUtilisateur implements Serializable {

    private Utilisateur ajoutUtilisateur = new Utilisateur();
    private Utilisateur modificationUtilisateur = new Utilisateur();
    private List<Utilisateur> lstUtilisateur = new ArrayList<Utilisateur>();

    @PostConstruct
    public void init() {
        recupererListeUtilisateur();
    }

    public void recupererListeUtilisateur() {
        try {
            obtenirListeUtilisateurs();
            lstUtilisateur.clear();
            lstUtilisateur = UtilisateurHandler.getListUtilisateur();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modifierUtilisateur() throws IOException {
        String login = modificationUtilisateur.getLogin();
        String mdp = modificationUtilisateur.getMotdepasse();
        String url = "http://localhost:8080/CaisseApplication-war/webresources/listeUtilisateur/modifierUtisateurById/"+modificationUtilisateur.getIdutilisateur().toString()+"-"+modificationUtilisateur.getNom()+"-"+modificationUtilisateur.getPrenom()+"-"+modificationUtilisateur.getLogin()+"-"+modificationUtilisateur.getMotdepasse();
       
        
//       
        String ret =  envoyerEtRecevoirMessage(url, "POST");;
        if (ret.equals("succes")) {
            recupererListeUtilisateur();
        }
    }

    public void ajouterUtilisateur() {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeUtilisateur/ajouterUtilisateur/" + ajoutUtilisateur.getNom() + "-" + ajoutUtilisateur.getPrenom() + "-" + ajoutUtilisateur.getLogin() + "-" + ajoutUtilisateur.getMotdepasse(), "POST");

            if (!ret.isEmpty()) {
                recupererListeUtilisateur();
            }

        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenirListeUtilisateurs() throws ParserConfigurationException, SAXException, TransformerException {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeUtilisateur", "GET");
            stringToDom(ret);
        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        StreamResult result = new StreamResult(new File("G:\\UtilisateurTempAdmin.xml"));
        transformer.transform(source, result);

        parserXMLFichier("G:\\UtilisateurTempAdmin.xml");
    }

    public void parserXMLFichier(String nomFichier) {
        try {
            // création d'une fabrique de parseurs SAX
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler
            File fichier = new File(nomFichier);
            DefaultHandler gestionnaire = new UtilisateurHandler();
            parseur.parse(fichier, gestionnaire);
        } catch (ParserConfigurationException e) {
            System.err.println("Probleme lors de la creation du parser : " + e);
        } catch (SAXException e) {
            System.err.println("Probleme de parsing : " + e);
        } catch (IOException e) {
            System.err.println("Probleme d'entrée/sortie : " + e);
        }

    }

    public String envoyerEtRecevoirMessage(String adresseUrl, String methodeHTTP) throws MalformedURLException, IOException {

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

    /**
     * @return the ajoutUtilisateur
     */
    public Utilisateur getAjoutUtilisateur() {
        return ajoutUtilisateur;
    }

    /**
     * @param ajoutUtilisateur the ajoutUtilisateur to set
     */
    public void setAjoutUtilisateur(Utilisateur ajoutUtilisateur) {
        this.ajoutUtilisateur = ajoutUtilisateur;
    }

    /**
     * @return the lstUtilisateur
     */
    public List<Utilisateur> getLstUtilisateur() {
        return lstUtilisateur;
    }

    /**
     * @param lstUtilisateur the lstUtilisateur to set
     */
    public void setLstUtilisateur(List<Utilisateur> lstUtilisateur) {
        this.lstUtilisateur = lstUtilisateur;
    }

    /**
     * @return the modificationUtilisateur
     */
    public Utilisateur getModificationUtilisateur() {
        return modificationUtilisateur;
    }

    /**
     * @param modificationUtilisateur the modificationUtilisateur to set
     */
    public void setModificationUtilisateur(Utilisateur modificationUtilisateur) {
        this.modificationUtilisateur = modificationUtilisateur;
    }

}
