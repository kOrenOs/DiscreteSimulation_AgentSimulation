/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entity.CarPossition;

/**
 *
 * @author korenciak.marek
 */
public class StructCarInformation {
    private String name = "";
    private double momentalFilled = 0;
    private String capacity = "";
    private String speed = "";
    private String possition = "";
    private double timeOfAction  = -1;
    private double startOfAction = -1;
    
    public StructCarInformation(String paName, String paCapacity, String paSpeed){
        name=paName;
        capacity = paCapacity;
        speed = paSpeed;
    }
    
    public void setPossition(String paPossition){
        possition = paPossition;
    }
    
    public void setMomentalFilled(int paFilled){
        momentalFilled = paFilled;
    }

    public String getName() {
        return name;
    }

    public double getMomentalFilled() {
        return momentalFilled;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getSpeed() {
        return speed;
    }

    public String getPossition() {
        return possition;
    }

    public void setTimeOfAction(double paTimeOfAction, double simTime) {
        timeOfAction = paTimeOfAction + simTime;
    }

    public double getTimeOfAction() {
        return timeOfAction;
    }
}
