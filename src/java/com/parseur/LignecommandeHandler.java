/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parseur;

import com.metier.Lignecommande;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Romaric
 */
public class LignecommandeHandler extends DefaultHandler {

    /**
     * @return the listLignecommande
     */
    public static List<Lignecommande> getListLignecommande() {
        return listLignecommande;
    }

    /**
     * @param aListLignecommande the listLignecommande to set
     */
    public static void setListLignecommande(List<Lignecommande> aListLignecommande) {
        listLignecommande = aListLignecommande;
    }

    /**
     * @return the ajoutLignecommande
     */
    public static Lignecommande getAjoutLignecommande() {
        return ajoutLignecommande;
    }

    /**
     * @param aAjoutLignecommande the ajoutLignecommande to set
     */
    public static void setAjoutLignecommande(Lignecommande aAjoutLignecommande) {
        ajoutLignecommande = aAjoutLignecommande;
    }

   
    private StringBuffer buffer;
    private static List<Lignecommande> listLignecommande = new ArrayList<Lignecommande>();
    private static Lignecommande ajoutLignecommande = new Lignecommande();

    /**
     * Constructeur par defaut. Initialise le buffer.
     */
    public LignecommandeHandler() {
        super();
        buffer = new StringBuffer();
    }

    /**
     * Methode executee au debut de l'analyse.
     */
    @Override
    public void startDocument() throws SAXException {
        listLignecommande.clear();
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

        
        if(qName.equals("lignecommandes")) {
          
            ajoutLignecommande = new Lignecommande();

        } else if (qName.equals("designationcommande")) {
            buffer = new StringBuffer();
        } else if (qName.equals("idlignecommande")) {
            buffer = new StringBuffer();
        } 
        else if (qName.equals("nomfournisseur")) {
            buffer = new StringBuffer();
        } else if (qName.equals("quantite")) {
            buffer = new StringBuffer();
        } else if (qName.equals("idcommande")) {
              buffer = new StringBuffer();
        } else if (qName.equals("prixunitaire")) {
             buffer = new StringBuffer();
        }else {
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
        if (qName.equals("lignecommandes")) {
            listLignecommande.add(ajoutLignecommande);
            //ajoutArticle = new Article();
        }
        if(qName.equals("idlignecommande")) {
            ajoutLignecommande.setIdlignedecommande(buffer.toString());
            buffer = null;
        } else if (qName.equals("idcommande")) {
            ajoutLignecommande.setIdcommande(buffer.toString());
            buffer = null;
        } else if (qName.equals("designationcommande")) {
            ajoutLignecommande.setDesignationcommande(buffer.toString());
            buffer = null;
        } else if (qName.equals("nomfournisseur")) {
            ajoutLignecommande.setNomfournisseur(buffer.toString());
            buffer = null;

        }else if(qName.equals("quantite")){
            ajoutLignecommande.setQuantite(buffer.toString());
            buffer = null;
            
        
        }else if(qName.equals("prixunitaire")){
            ajoutLignecommande.setPrixunitaire(buffer.toString());
            buffer = null;
            
        }


        //ajoutArticle.setListeArticle(getListArctile());
    }


}
