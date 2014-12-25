/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parseur;

import com.controller.MBArticle;
import com.metier.Article;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Romaric
 */
public class ArticleHandler extends DefaultHandler {

        /**
     * @return the listArctile
     */
    public static List<Article> getListArctile() {
        return listArctile;
    }

    /**
     * @param aListArctile the listArctile to set
     */
    public static void setListArctile(List<Article> aListArctile) {
        listArctile = aListArctile;
    }

    
    private StringBuffer buffer;
    private static List<Article> listArctile = new ArrayList<Article>();
    private Article ajoutArticle = new Article();

    /**
     * Constructeur par defaut. Initialise le buffer.
     */
    public ArticleHandler() {
        super();
        buffer = new StringBuffer();
    }

    /**
     * Methode executee au debut de l'analyse.
     */
    @Override
    public void startDocument() throws SAXException {
        listArctile.clear();
        System.out.println("Debut de l'analyse" + System.getProperty("line.separator"));
    }

    /**
     * Methode executee a la fin de l'analyse.
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("Fin de l'analyse");
    }

    /**
     * Methode executee des que des caracteres sont lus.
     *
     * @param chars un tableau contenant les caracteres lus
     * @param debut l'indice de debut dans le tableau
     * @param fin l'indice de fin dans le tableau
     */
    @Override
    public void characters(char[] chars, int debut, int fin) throws SAXException {
        String lecture = new String(chars, debut, fin);
        if (buffer != null) {
            buffer.append(lecture);
        }
    }

    /**
     * Methode executee des qu'un element est rencontre.
     *
     * @param uri l'uri du namespace ou chaine vide si aucun namespace
     * @param localName le nom local (sans prefixe) ou chaine vide si aucun
     * namespace
     * @param qName le nom de l'element (avec prefixe)
     * @param attributes les attributs attaches a l'element
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equals("articles")) {
            System.out.println("Debut article");
            ajoutArticle = new Article();

        } else if (qName.equals("nomproduit")) {
            buffer = new StringBuffer();
        } else if(qName.equals("prix")) {
            buffer = new StringBuffer();           
        }else if(qName.equals("marque")){
            buffer = new StringBuffer();
        }else if(qName.equals("codebarre")){
            buffer = new StringBuffer();
            
        }else{
            buffer = null;
        }
    }

    /**
     * Methode executee lorsqu'un element est ferme.
     *
     * @param uri l'uri du namespace ou chaine vide si aucun namespace
     * @param localName le nom local (sans prefixe) ou chaine vide si aucun
     * namespace
     * @param qName le nom de l'element (avec prefixe)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("articles")) {
            listArctile.add(ajoutArticle);
            //ajoutArticle = new Article();
        }
        if (qName.equals("nomproduit")) {
            ajoutArticle.setNomproduit(buffer.toString());
            buffer = null;
        }else
        if (qName.equals("prix")) {
            ajoutArticle.setPrix(buffer.toString());
            buffer = null;
        }else
        if (qName.equals("marque")) {
            ajoutArticle.setMarque(buffer.toString());
            buffer = null;

        }else 
            if(qName.equals("codebarre")){
                ajoutArticle.setCodeBarre(buffer.toString());
                buffer = null;
                
            }

        //ajoutArticle.setListeArticle(getListArctile());
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

}
