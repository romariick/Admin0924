/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metier;

/**
 *
 * @author Romaric
 */
public class Lignecommande {
    
    private String idlignedecommande;
    private String idcommande;
    private String designationcommande;
    private String nomfournisseur;
    private String quantite;
    private String prixunitaire;

    /**
     * @return the idlignedecommande
     */
    public String getIdlignedecommande() {
        return idlignedecommande;
    }

    /**
     * @param idlignedecommande the idlignedecommande to set
     */
    public void setIdlignedecommande(String idlignedecommande) {
        this.idlignedecommande = idlignedecommande;
    }

    /**
     * @return the idcommande
     */
    public String getIdcommande() {
        return idcommande;
    }

    /**
     * @param idcommande the idcommande to set
     */
    public void setIdcommande(String idcommande) {
        this.idcommande = idcommande;
    }

    /**
     * @return the designationcommande
     */
    public String getDesignationcommande() {
        return designationcommande;
    }

    /**
     * @param designationcommande the designationcommande to set
     */
    public void setDesignationcommande(String designationcommande) {
        this.designationcommande = designationcommande;
    }

    /**
     * @return the nomfournisseur
     */
    public String getNomfournisseur() {
        return nomfournisseur;
    }

    /**
     * @param nomfournisseur the nomfournisseur to set
     */
    public void setNomfournisseur(String nomfournisseur) {
        this.nomfournisseur = nomfournisseur;
    }

    /**
     * @return the quantite
     */
    public String getQuantite() {
        return quantite;
    }

    /**
     * @param quantite the quantite to set
     */
    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    /**
     * @return the prixunitaire
     */
    public String getPrixunitaire() {
        return prixunitaire;
    }

    /**
     * @param prixunitaire the prixunitaire to set
     */
    public void setPrixunitaire(String prixunitaire) {
        this.prixunitaire = prixunitaire;
    }
    
    
   
}
