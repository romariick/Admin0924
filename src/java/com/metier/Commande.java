/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metier;

import java.util.Date;

/**
 *
 * @author Romaric
 */
public class Commande {
    
    private String idcommande;
    private String datecommande;
    private String etatcommande;

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
     * @return the etatcommande
     */
    public String getEtatcommande() {
        return etatcommande;
    }

    /**
     * @param etatcommande the etatcommande to set
     */
    public void setEtatcommande(String etatcommande) {
        this.etatcommande = etatcommande;
    }

    /**
     * @return the datecommande
     */
    public String getDatecommande() {
        return datecommande;
    }

    /**
     * @param datecommande the datecommande to set
     */
    public void setDatecommande(String datecommande) {
        this.datecommande = datecommande;
    }
    
    
    
    
    
}
