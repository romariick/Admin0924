/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.metier.Utilisateur;
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
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
@SessionScoped
@ManagedBean(name = "MBSession")
public class MBSession implements Serializable {

    private String login;
    private String motdepasse;
    private List<Utilisateur> lstUtilisateur = new ArrayList<Utilisateur>();
    private Utilisateur recuperUtilisateur = new Utilisateur();

    public String sauthentifier() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String url = "http://localhost:8080/CaisseApplication-war/webresources/listeUtilisateur/obtenirUtilisateurByMotdepasse/"+login+"-"+motdepasse;
        String ret = "";
        String retxml = envoyerEtRecevoirXml(url, "POST");
        if (!retxml.isEmpty()) {
            stringToDom(retxml);
            recuperUtilisateur = UtilisateurHandler.getAjoutUtilisateur();
            ret = "succes";
        } else {
            ret = "authentification";
        }
        return ret;
    }

    public String envoyerEtRecevoirXml(String adresseUrl, String methodeHTTP) throws MalformedURLException, IOException {

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
        StreamResult result = new StreamResult(new File("G:\\Utilisateur.xml"));
        transformer.transform(source, result);

        parserXMLFichier("G:\\Utilisateur.xml");
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
  public String deconnexion() {
      
        return "authentification";
    }
    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the motdepasse
     */
    public String getMotdepasse() {
        return motdepasse;
    }

    /**
     * @param motdepasse the motdepasse to set
     */
    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
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
     * @return the recuperUtilisateur
     */
    public Utilisateur getRecuperUtilisateur() {
        return recuperUtilisateur;
    }

    /**
     * @param recuperUtilisateur the recuperUtilisateur to set
     */
    public void setRecuperUtilisateur(Utilisateur recuperUtilisateur) {
        this.recuperUtilisateur = recuperUtilisateur;
    }

}
