/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import OSPABA.Simulation;

/**
 *
 * @author korenciak.marek
 */
public class CompanyMessage extends MyMessage{

    private String message = "";
    
    public CompanyMessage(Simulation sim) {
        super(sim);
    }

    public CompanyMessage(MyMessage original) {
        super(original);
        // copy() is called in superclass
    }
    
    public void setMessage(String mes){
        message = mes;
    }
    
    public String getMessage(){
        return message;
    }
}
