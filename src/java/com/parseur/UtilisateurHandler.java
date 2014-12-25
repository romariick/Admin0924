/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parseur;

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
public class UtilisateurHandler extends DefaultHandler {

    /**
     * @return the listUtilisateur
     */
    public static List<Utilisateur> getListUtilisateur() {
        return listUtilisateur;
    }

    /**
     * @param aListUtilisateur the listUtilisateur to set
     */
    public static void setListUtilisateur(List<Utilisateur> aListUtilisateur) {
        listUtilisateur = aListUtilisateur;
    }

    private StringBuffer buffer;
    private static List<Utilisateur> listUtilisateur = new ArrayList<Utilisateur>();
    private static  Utilisateur ajoutUtilisateur = new Utilisateur();

    /**
     * Constructeur par defaut. Initialise le buffer.
     */
    public UtilisateurHandler() {
        super();
        buffer = new StringBuffer();
    }

    /**
     * Methode executee au debut de l'analyse.
     */
    @Override
    public void startDocument() throws SAXException {
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

        if (qName.equals("utilisateurs")) {
            System.out.println("Debut utilisateur");
            ajoutUtilisateur = new Utilisateur();

        } else if (qName.equals("nom")) {
            buffer = new StringBuffer();
        } else if(qName.equals("prenom")) {
            buffer = new StringBuffer();           
        }else if(qName.equals("login")){
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
        if (qName.equals("utilisateurs")) {
            listUtilisateur.add(ajoutUtilisateur);
            //ajoutArticle = new Article();
        }
        if (qName.equals("nom")) {
            ajoutUtilisateur.setNom(buffer.toString());
            buffer = null;
        }else
        if (qName.equals("prenom")) {
            ajoutUtilisateur.setPrenom(buffer.toString());
            buffer = null;
        }else
        if (qName.equals("login")) {
            ajoutUtilisateur.setLogin(buffer.toString());
            buffer = null;

        }

        //ajoutArticle.setListeArticle(getListArctile());
    }

    /**
     * @return the ajoutUtilisateur
     */
    public static Utilisateur getAjoutUtilisateur() {
        return ajoutUtilisateur;
    }

    /**
     * @param ajoutUtilisateur the ajoutUtilisateur to set
     */
    public static void setAjoutUtilisateur(Utilisateur ajoutUtilisateur) {
        ajoutUtilisateur = ajoutUtilisateur;
    }

  

}
