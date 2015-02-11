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
public class Categorie {

    private Integer idcategorie;
    private String libelle;
    private String description;
    private String codecategorie;

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
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the codecategorie
     */
    public String getCodecategorie() {
        return codecategorie;
    }

    /**
     * @param codecategorie the codecategorie to set
     */
    public void setCodecategorie(String codecategorie) {
        this.codecategorie = codecategorie;
    }

}
