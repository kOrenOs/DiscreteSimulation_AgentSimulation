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
public class ResourceMessage extends MyMessage{

    private double resourceAmount;
    
    public ResourceMessage(Simulation sim) {
        super(sim);
    }
    
    public ResourceMessage(MyMessage original) {
        super(original);
    }
    
    public void setAmount(double paAmount){
        resourceAmount = paAmount;
    }
    
    public double getAmount(){
        return resourceAmount;
    }
}
