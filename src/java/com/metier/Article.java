/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metier;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Romaric
 */
public class Article {

    private Integer idArticle;
    private Integer idCouleur;
    private String nomproduit;
    private String prix;
    private String marque;
    private String promotion;
    private String reduction;
    private String disponibilite;
    private Date dateAjout;
    private String codeBarre;
    private String etat;
    public List<Article> listeArticle;
    private boolean selectProduit = false;
    private String nombre;

    public Article() {
    }

    /**
     * @return the idArticle
     */
    public Integer getIdArticle() {
        return idArticle;
    }

    /**
     * @param idArticle the idArticle to set
     */
    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    /**
     * @return the idCouleur
     */
    public Integer getIdCouleur() {
        return idCouleur;
    }

    /**
     * @param idCouleur the idCouleur to set
     */
    public void setIdCouleur(Integer idCouleur) {
        this.idCouleur = idCouleur;
    }

    /**
     * @return the nomproduit
     */
    public String getNomproduit() {
        return nomproduit;
    }

    /**
     * @param nomproduit the nomproduit to set
     */
    public void setNomproduit(String nomproduit) {
        this.nomproduit = nomproduit;
    }

    /**
     * @return the prix
     */
    public String getPrix() {
        return prix;
    }

    /**
     * @param prix the prix to set
     */
    public void setPrix(String prix) {
        this.prix = prix;
    }

    /**
     * @return the marque
     */
    public String getMarque() {
        return marque;
    }

    /**
     * @param marque the marque to set
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    /**
     * @return the promotion
     */
    public String getPromotion() {
        return promotion;
    }

    /**
     * @param promotion the promotion to set
     */
    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    /**
     * @return the reduction
     */
    public String getReduction() {
        return reduction;
    }

    /**
     * @param reduction the reduction to set
     */
    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

    /**
     * @return the disponibilite
     */
    public String getDisponibilite() {
        return disponibilite;
    }

    /**
     * @param disponibilite the disponibilite to set
     */
    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    /**
     * @return the dateAjout
     */
    public Date getDateAjout() {
        return dateAjout;
    }

    /**
     * @param dateAjout the dateAjout to set
     */
    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    /**
     * @return the codeBarre
     */
    public String getCodeBarre() {
        return codeBarre;
    }

    /**
     * @param codeBarre the codeBarre to set
     */
    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    /**
     * @return the etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @param etat the etat to set
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * @return the listeArticle
     */
    public List<Article> getListeArticle() {
        return listeArticle;
    }

    /**
     * @param listeArticle the listeArticle to set
     */
    public void setListeArticle(List<Article> listeArticle) {
        this.listeArticle = listeArticle;
    }

    /**
     * @return the selectProduit
     */
    public boolean isSelectProduit() {
        return selectProduit;
    }

    /**
     * @param selectProduit the selectProduit to set
     */
    public void setSelectProduit(boolean selectProduit) {
        this.selectProduit = selectProduit;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

  

}
