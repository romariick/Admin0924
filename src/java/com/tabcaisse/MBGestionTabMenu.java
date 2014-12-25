/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tabcaisse;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Lova
 */
@SessionScoped
@ManagedBean(name = "MBGestionTabMenu")
public class MBGestionTabMenu implements Serializable {

    private String index = "0";

    @PostConstruct
    public void init() {
    }

    public void onTabChange(TabChangeEvent event) {

        index = event.getTab().getId();

        if (index.equals("idGestion")) {
            setIndex("0");
        } else if (index.equals("idEmplacements")) {
            setIndex("1");
        } else if (index.equals("idStock")) {

            setIndex("2");
        } else if (index.equals("idCommandes")) {
            setIndex("3");
        } else if (index.equals("idFacturation")) {
            setIndex("4");
        } else if (index.equals("idStatistique")) {
            setIndex("5");
        }
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }
}
