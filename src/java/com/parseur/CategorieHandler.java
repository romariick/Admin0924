/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parseur;

import com.metier.Categorie;
import com.metier.Utilisateur;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Romaric
 */
public class CategorieHandler extends DefaultHandler {

    /**
     * @return the listCategorie
     */
    public static List<Categorie> getListCategorie() {
        return listCategorie;
    }

    /**
     * @param aListCategorie the listCategorie to set
     */
    public static void setListCategorie(List<Categorie> aListCategorie) {
        listCategorie = aListCategorie;
    }

    /**
     * @return the ajoutCategorie
     */
    public static Categorie getAjoutCategorie() {
        return ajoutCategorie;
    }

    /**
     * @param aAjoutCategorie the ajoutCategorie to set
     */
    public static void setAjoutCategorie(Categorie aAjoutCategorie) {
        ajoutCategorie = aAjoutCategorie;
    }

  

    private StringBuffer buffer;
    private static List<Categorie> listCategorie = new ArrayList<Categorie>();
    private static  Categorie ajoutCategorie = new Categorie();

    /**
     * Constructeur par defaut. Initialise le buffer.
     */
    public CategorieHandler() {
        super();
        buffer = new StringBuffer();
    }

    /**
     * Methode executee au debut de l'analyse.
     */
    @Override
    public void startDocument() throws SAXException {
       listCategorie.clear();
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

        if (qName.equals("categories")) {
            System.out.println("Debut utilisateur");
            setAjoutCategorie(new Categorie());

        } else if (qName.equals("libellecategorie")) {
            buffer = new StringBuffer();
        } else if(qName.equals("descriptioncategorie")) {
            buffer = new StringBuffer();
        }else if(qName.equals("idcategorie")){
            buffer = new StringBuffer();
        }
        else{
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
        if (qName.equals("categories")) {
            listCategorie.add(getAjoutCategorie());
            //ajoutArticle = new Article();
        }
        if (qName.equals("libellecategorie")) {
            getAjoutCategorie().setLibelle(buffer.toString());
            buffer = null;
        }else
        if (qName.equals("descriptioncategorie")) {
            getAjoutCategorie().setDescription(buffer.toString());
            buffer = null;
        }else
            if(qName.equals("idcategorie")){
                String temp = buffer.toString();
                ajoutCategorie.setIdcategorie(Integer.parseInt(temp));
                            buffer = null;

            }

        //ajoutArticle.setListeArticle(getListArctile());
    }

  
  

}
