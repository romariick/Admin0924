/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.metier.Article;
import com.metier.Fournisseur;
import com.parseur.ArticleHandler;
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
import java.util.Map;
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
@ManagedBean(name = "MBArticle")
public class MBArticle implements Serializable {

    /**
     * @return the sauvegardeNonPayez
     */
    public static Double getSauvegardeNonPayez() {
        return sauvegardeNonPayez;
    }

    /**
     * @param aSauvegardeNonPayez the sauvegardeNonPayez to set
     */
    public static void setSauvegardeNonPayez(Double aSauvegardeNonPayez) {
        sauvegardeNonPayez = aSauvegardeNonPayez;
    }

    public List<Article> lstArticle = new ArrayList<Article>();
    private List<Article> lstAchat = new ArrayList<Article>();
    private List<Article> tempAchat = new ArrayList<Article>();
    private static List<Article> sauvegardeAchatNonPayez = new ArrayList<Article>();
    private Article ajoutArticle = new Article();
    private Article modifArticle = new Article();
    private Article supprArticle = new Article();
    private Integer recupIdCategorie;
    private Integer recupIdCategorieModif;
    private String recupDisponibilite;
    private String recupDispoModif;
    private Double netApayez = 0.0;
    private static Double sauvegardeNonPayez = 0.0;
    private String codebarre = "";
    private Integer idcategorie;
    private boolean recupSelectionner;
    private List<String> lstDispo = new ArrayList<String>();
    private List<Fournisseur> lstFournisseur = new ArrayList<Fournisseur>();
    private Integer recupIdFournisseur;
    private List<Article> tempAchatarticle = new ArrayList<Article>();

    @PostConstruct
    public void init() {
        try {

            lstDispo.add("Oui");
            lstDispo.add("Non");

            initialiserListeProduit();
            recupererListeFournisseur();
            //lstArticle.clear();

            if (!sauvegardeAchatNonPayez.isEmpty()) {
                tempAchat = sauvegardeAchatNonPayez;
                netApayez = sauvegardeNonPayez;
            }
        } catch (Exception ex) {
            Logger.getLogger(MBArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String listerArticle(String adresseUrl, String methodeHTTP) throws MalformedURLException, IOException {

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

    public void parserXML(String url, String method) throws Exception {

        String str = listerArticle(url, method);

        stringToDom(str);
    }

    public void parserXMLParCategorie(String url, String method) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String str = listerArticle(url, method);
        String textXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<article> ";
        String baliseFermante = "</article>";

        String res = textXML.concat(str).concat(baliseFermante);
        stringToDom(res);
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
        StreamResult result = new StreamResult(new File("G:\\test.xml"));
        transformer.transform(source, result);

        parserXMLFichier("G:\\test.xml");
    }

    public void parserXMLFichier(String nomFichier) {
        try {
            // création d'une fabrique de parseurs SAX
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler
            File fichier = new File(nomFichier);
            DefaultHandler gestionnaire = new ArticleHandler();
            parseur.parse(fichier, gestionnaire);
          
        } catch (ParserConfigurationException e) {
            System.err.println("Probleme lors de la creation du parser : " + e);
        } catch (SAXException e) {
            System.err.println("Probleme de parsing : " + e);
        } catch (IOException e) {
            System.err.println("Probleme d'entrée/sortie : " + e);
        }

    }

    public void obtenireParCategorie() {
        try {
            parserXMLParCategorie("http://localhost:8080/CaisseApplication-war/webresources/listearticle/obtenirArticleByCategorie/" + idcategorie.toString(), "POST");

        } catch (Exception ex) {
            Logger.getLogger(MBArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void recupererListeProduitSelectionner() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String codebarre = params.get("codebarre");

        String str = listerArticle("http://localhost:8080/CaisseApplication-war/webresources/listearticle/obtenirArticleByCodeBarre/" + codebarre, "POST");

        if (!str.isEmpty()) {
            stringToDom(str);
//            List<Article> tempList = new ArrayList<Article>();
//            tempList = ArticleHandler.getListArctile();
            //int debug = 0;
        }
//        for(Article recupArticleAcheter : lstArticle){
//            if(recupArticleAcheter.getSelectProduit()){
//                System.out.println("True");        
//            }else{
//                System.out.println("False");
//            }
//        }
//        
    }

    public void initialiserListeProduit() {
        try {
            parserXML("http://localhost:8080/CaisseApplication-war/webresources/listearticle", "GET");
            lstArticle = ArticleHandler.getListArctile();
        } catch (Exception ex) {
            Logger.getLogger(MBArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenirListeFournisseur() {

    }

    public void ajouterProduit() {
        try {
            ajoutArticle.setEtat("Etat");
            ajoutArticle.setIdfornisseur(recupIdFournisseur);
            ajoutArticle.setPromotion(recupDisponibilite);
             ajoutArticle.setReduction(recupDisponibilite);
             ajoutArticle.setEtat(recupDisponibilite);
            

            
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listearticle/ajouterArticle/" + ajoutArticle.getNomproduit()
                    +"-"  +ajoutArticle.getIdfornisseur()
                    + "-" + Long.parseLong(ajoutArticle.getPrix())
                    + "-" + ajoutArticle.getMarque()
                    + "-" + ajoutArticle.getPromotion()
                    + "-" + ajoutArticle.getReduction()
                    + "-" + recupDisponibilite
                    + "-" + ajoutArticle.getCodeBarre()
                    + "-" + ajoutArticle.getEtat()
                    + "-" + ajoutArticle.getNombre() + "-" + recupIdCategorie.toString(), "POST");

            if (ret.equals("succes")) {
                initialiserListeProduit();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ajout avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }
        } catch (IOException ex) {
            Logger.getLogger(MBArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modifierProduit() {
        try {
            modifArticle.setPromotion("Oui");
            modifArticle.setReduction("Oui");
            
            
            
            
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listearticle/modifierArticleByCodeBarre/" + modifArticle.getCodeBarre()+ "-"+ modifArticle.getIdfornisseur() + "-" + modifArticle.getMarque() + "-" + modifArticle.getNomproduit()
                    + "-" + modifArticle.getPrix()
                    + "-" + modifArticle.getPromotion()
                    + "-" + modifArticle.getReduction()
                    + "-" + recupDispoModif
                    + "-" + modifArticle.getNombre(), "POST");

            if (ret.equals("succes")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modification avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                initialiserListeProduit();

            }
        } catch (IOException ex) {
            Logger.getLogger(MBArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void supprimerProduit() {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listearticle/supprimerArticle/" + supprArticle.getCodeBarre(), "POST");
            if (ret.equals("succes")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Suppression avec succès !", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                initialiserListeProduit();
            }
        } catch (IOException ex) {
            Logger.getLogger(MBArticle.class.getName()).log(Level.SEVERE, null, ex);
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

    public void validerAchat() {
        sauvegardeAchatNonPayez.clear();
        sauvegardeNonPayez = 0.0;
        netApayez = 0.0;
    }

    public void acheterProduit() throws IOException, ParserConfigurationException, SAXException, TransformerException {

        String str = listerArticle("http://localhost:8080/CaisseApplication-war/webresources/listearticle/obtenirArticleByCodeBarre/" + codebarre, "POST");

        if (!str.isEmpty()) {
            stringToDom(str);
            if (!codebarre.isEmpty()) {
                
                String acheter = listerArticle("http://localhost:8080/CaisseApplication-war/webresources/listearticle/acheterArticleByCodeBarre/" + codebarre, "POST");
                if (acheter.equals("achatok")) {
                   
                     lstAchat = ArticleHandler.getListArctile();

                    Article sauvarticle = new Article();

                    sauvarticle.setCodeBarre(lstAchat.get(0).getCodeBarre());
                    sauvarticle.setPrix(lstAchat.get(0).getPrix());
                    sauvarticle.setNomproduit(lstAchat.get(0).getNomproduit());
                    sauvarticle.setDisponibilite(lstAchat.get(0).getDisponibilite());
                    sauvarticle.setReduction(lstAchat.get(0).getReduction());

                    netApayez += Double.parseDouble(lstAchat.get(0).getPrix());
                    tempAchat.add(sauvarticle);

                    sauvegardeAchatNonPayez = tempAchat;
                    sauvegardeNonPayez = netApayez;

                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Achat avec succès !", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);

                }
            }
        }

    }

    /*Récuperer liste fournisseur*/
    public void recupererListeFournisseur() {
        try {
            obtenirListeFournisseurs();
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

    public void obtenirListeFournisseurs() throws ParserConfigurationException, SAXException, TransformerException {
        try {
            String ret = envoyerEtRecevoirMessage("http://localhost:8080/CaisseApplication-war/webresources/listeFournisseur", "GET");
            stringToDomFournisseur(ret);
        } catch (IOException ex) {
            Logger.getLogger(MBUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stringToDomFournisseur(String xmlSource) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
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

        parserXMLFichierFournisseur("G:\\FournisseurTempAdmin.xml");
    }

    public void parserXMLFichierFournisseur(String nomFichier) {
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

    /**
     * @return the lstArticle
     */
    public List<Article> getLstArticle() {
        return lstArticle;
    }

    /**
     * @param lstArticle the lstArticle to set
     */
    public void setLstArticle(List<Article> lstArticle) {
        this.lstArticle = lstArticle;
    }

    /**
     * @return the codebarre
     */
    public String getCodebarre() {
        return codebarre;
    }

    /**
     * @param codebarre the codebarre to set
     */
    public void setCodebarre(String codebarre) {
        this.codebarre = codebarre;
    }

    /**
     * @return the lstAchat
     */
    public List<Article> getLstAchat() {
        return lstAchat;
    }

    /**
     * @param lstAchat the lstAchat to set
     */
    public void setLstAchat(List<Article> lstAchat) {
        this.lstAchat = lstAchat;
    }

    /**
     * @return the tempAchat
     */
    public List<Article> getTempAchat() {
        return tempAchat;
    }

    /**
     * @param tempAchat the tempAchat to set
     */
    public void setTempAchat(List<Article> tempAchat) {
        this.tempAchat = tempAchat;
    }

    /**
     * @return the netApayez
     */
    public Double getNetApayez() {
        return netApayez;
    }

    /**
     * @param netApayez the netApayez to set
     */
    public void setNetApayez(Double netApayez) {
        this.netApayez = netApayez;
    }

    /**
     * @return the idcategorie
     */
    public Integer getIdcategorie() {
        return idcategorie;
    }

    /**
     * @param idcategorie the idcategorie to set
     */
    public void setIdcategorie(Integer idcategorie) {
        this.idcategorie = idcategorie;
    }

    /**
     * @return the recupSelectionner
     */
    public boolean isRecupSelectionner() {
        return recupSelectionner;
    }

    /**
     * @param recupSelectionner the recupSelectionner to set
     */
    public void setRecupSelectionner(boolean recupSelectionner) {
        this.recupSelectionner = recupSelectionner;
    }

    /**
     * @return the sauvegardeAchatNonPayez
     */
    public static List<Article> getSauvegardeAchatNonPayez() {
        return sauvegardeAchatNonPayez;
    }

    /**
     * @param sauvegardeAchatNonPayez the sauvegardeAchatNonPayez to set
     */
    public static void setSauvegardeAchatNonPayez(List<Article> sauvegardeAchatNonPayez) {
        sauvegardeAchatNonPayez = sauvegardeAchatNonPayez;
    }

    /**
     * @return the ajoutArticle
     */
    public Article getAjoutArticle() {
        return ajoutArticle;
    }

    /**
     * @param ajoutArticle the ajoutArticle to set
     */
    public void setAjoutArticle(Article ajoutArticle) {
        this.ajoutArticle = ajoutArticle;
    }

    /**
     * @return the recupIdCategorie
     */
    public Integer getRecupIdCategorie() {
        return recupIdCategorie;
    }

    /**
     * @param recupIdCategorie the recupIdCategorie to set
     */
    public void setRecupIdCategorie(Integer recupIdCategorie) {
        this.recupIdCategorie = recupIdCategorie;
    }

    /**
     * @return the recupDisponibilite
     */
    public String getRecupDisponibilite() {
        return recupDisponibilite;
    }

    /**
     * @param recupDisponibilite the recupDisponibilite to set
     */
    public void setRecupDisponibilite(String recupDisponibilite) {
        this.recupDisponibilite = recupDisponibilite;
    }

    /**
     * @return the lstDispo
     */
    public List<String> getLstDispo() {
        return lstDispo;
    }

    /**
     * @param lstDispo the lstDispo to set
     */
    public void setLstDispo(List<String> lstDispo) {
        this.lstDispo = lstDispo;
    }

    /**
     * @return the modifArticle
     */
    public Article getModifArticle() {
        return modifArticle;
    }

    /**
     * @param modifArticle the modifArticle to set
     */
    public void setModifArticle(Article modifArticle) {
        this.modifArticle = modifArticle;
    }

    /**
     * @return the recupIdCategorieModif
     */
    public Integer getRecupIdCategorieModif() {
        return recupIdCategorieModif;
    }

    /**
     * @param recupIdCategorieModif the recupIdCategorieModif to set
     */
    public void setRecupIdCategorieModif(Integer recupIdCategorieModif) {
        this.recupIdCategorieModif = recupIdCategorieModif;
    }

    /**
     * @return the recupDispoModif
     */
    public String getRecupDispoModif() {
        return recupDispoModif;
    }

    /**
     * @param recupDispoModif the recupDispoModif to set
     */
    public void setRecupDispoModif(String recupDispoModif) {
        this.recupDispoModif = recupDispoModif;
    }

    /**
     * @return the supprArticle
     */
    public Article getSupprArticle() {
        return supprArticle;
    }

    /**
     * @param supprArticle the supprArticle to set
     */
    public void setSupprArticle(Article supprArticle) {
        this.supprArticle = supprArticle;
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
     * @return the recupIdFournisseur
     */
    public Integer getRecupIdFournisseur() {
        return recupIdFournisseur;
    }

    /**
     * @param recupIdFournisseur the recupIdFournisseur to set
     */
    public void setRecupIdFournisseur(Integer recupIdFournisseur) {
        this.recupIdFournisseur = recupIdFournisseur;
    }

    /**
     * @return the tempAchatarticle
     */
    public List<Article> getTempAchatarticle() {
        return tempAchatarticle;
    }

    /**
     * @param tempAchatarticle the tempAchatarticle to set
     */
    public void setTempAchatarticle(List<Article> tempAchatarticle) {
        this.tempAchatarticle = tempAchatarticle;
    }

}
