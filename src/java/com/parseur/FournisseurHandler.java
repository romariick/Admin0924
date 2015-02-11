/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parseur;

import com.metier.Fournisseur;
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
public class FournisseurHandler extends DefaultHandler {

    /**
     * @return the listFournisseur
     */
    public static List<Fournisseur> getListFournisseur() {
        return listFournisseur;
    }

    /**
     * @param aListFournisseur the listFournisseur to set
     */
    public static void setListFournisseur(List<Fournisseur> aListFournisseur) {
        listFournisseur = aListFournisseur;
    }

    /**
     * @return the ajoutFournisseur
     */
    public static Fournisseur getAjoutFournisseur() {
        return ajoutFournisseur;
    }

    /**
     * @param aAjoutFournisseur the ajoutFournisseur to set
     */
    public static void setAjoutFournisseur(Fournisseur aAjoutFournisseur) {
        ajoutFournisseur = aAjoutFournisseur;
    }

  

    private StringBuffer buffer;
    private static List<Fournisseur> listFournisseur = new ArrayList<Fournisseur>();
    private static Fournisseur ajoutFournisseur = new Fournisseur();

    /**
     * Constructeur par defaut. Initialise le buffer.
     */
    public FournisseurHandler() {
        super();
        buffer = new StringBuffer();
    }

    /**
     * Methode executee au debut de l'analyse.
     */
    @Override
    public void startDocument() throws SAXException {
        getListFournisseur().clear();
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

        if (qName.equals("fournisseurs")) {
            System.out.println("Debut fournisseurs");
            ajoutFournisseur = new Fournisseur();

        } else if (qName.equals("nomfornisseur")) {
            buffer = new StringBuffer();
        } else if (qName.equals("adressefournisseur")) {
            buffer = new StringBuffer();
        } else if (qName.equals("villefornisseur")) {
            buffer = new StringBuffer();
        } else if (qName.equals("telfournisseur")) {
            buffer = new StringBuffer();
        } else if (qName.equals("emailfournisseur")) {
            buffer = new StringBuffer();
        }else if (qName.equals("idfornisseur")) {
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
        if (qName.equals("fournisseurs")) {
            listFournisseur.add(ajoutFournisseur);
            //ajoutArticle = new Article();
        }
        if (qName.equals("nomfornisseur")) {
            ajoutFournisseur.setNomfornisseur(buffer.toString());
            buffer = null;
        } else if (qName.equals("adressefournisseur")) {
            ajoutFournisseur.setAdressefournisseur(buffer.toString());
            buffer = null;
        } else if (qName.equals("villefornisseur")) {
            ajoutFournisseur.setVillefornisseur(buffer.toString());
            buffer = null;
        } else if (qName.equals("telfournisseur")) {
            ajoutFournisseur.setTelfournisseur(buffer.toString());
            buffer = null;

        }else if(qName.equals("emailfournisseur")){
            ajoutFournisseur.setEmailfournisseur(buffer.toString());
            buffer = null;
            
        }else if(qName.equals("idfornisseur")){
            ajoutFournisseur.setIdfornisseur(buffer.toString());
            buffer = null;
            
        }

        //ajoutArticle.setListeArticle(getListArctile());
    }

}
