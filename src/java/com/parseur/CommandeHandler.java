/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parseur;

import com.metier.Commande;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Romaric
 */
public class CommandeHandler extends DefaultHandler {

    /**
     * @return the listCommande
     */
    public static List<Commande> getListCommande() {
        return listCommande;
    }

    /**
     * @param aListCommande the listCommande to set
     */
    public static void setListCommande(List<Commande> aListCommande) {
        listCommande = aListCommande;
    }

    /**
     * @return the ajoutCommande
     */
    public static Commande getAjoutCommande() {
        return ajoutCommande;
    }

    /**
     * @param aAjoutCommande the ajoutCommande to set
     */
    public static void setAjoutCommande(Commande aAjoutCommande) {
        ajoutCommande = aAjoutCommande;
    }

   

    private StringBuffer buffer;
    private static List<Commande> listCommande = new ArrayList<Commande>();
    private static Commande ajoutCommande = new Commande();

    /**
     * Constructeur par defaut. Initialise le buffer.
     */
    public CommandeHandler() {
        super();
        buffer = new StringBuffer();
    }

    /**
     * Methode executee au debut de l'analyse.
     */
    @Override
    public void startDocument() throws SAXException {
        listCommande.clear();
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

        if (qName.equals("commandes")) {
           
            ajoutCommande = new Commande();

        } else if (qName.equals("idcommande")) {
            buffer = new StringBuffer();
        } else if (qName.equals("datecommande")) {
            buffer = new StringBuffer();
        } else if (qName.equals("etatcommande")) {
            buffer = new StringBuffer();
        } else {
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
        if (qName.equals("commandes")) {
            listCommande.add(ajoutCommande);
            //ajoutArticle = new Article();
        }
        if (qName.equals("idcommande")) {
            ajoutCommande.setIdcommande(buffer.toString());
            buffer = null;
        } else if (qName.equals("datecommande")) {     
            ajoutCommande.setDatecommande(buffer.toString());
            buffer = null;
        } else if (qName.equals("etatcommande")) {
            ajoutCommande.setEtatcommande(buffer.toString());
            buffer = null;
        } else if (qName.equals("idcommande")) {
            ajoutCommande.setIdcommande(buffer.toString());
            buffer = null;

        }

        //ajoutArticle.setListeArticle(getListArctile());
    }



}
