/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class BunchOfCars {
    
    private ArrayList<Vehicle> bunch = new ArrayList<Vehicle>();
    private double timeOfCome = -1;

    public BunchOfCars(Vehicle paVehicle, double paTimeOfCome) {
        bunch.add(paVehicle);
        timeOfCome = paTimeOfCome;
    }
    
    public void addVehicle(Vehicle paVehicle){
        bunch.add(paVehicle);
    }
    
    public double getTime(){
        return timeOfCome;
    }
    
    public ArrayList<Vehicle> getBunch(){
        return bunch;
    }
}
