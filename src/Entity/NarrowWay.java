/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.LinkedList;
import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class NarrowWay {

    private LinkedList<BunchOfCars> bunchOfBunches = new LinkedList<>();
    private int wayDistance = -1;

    public NarrowWay(int paWayDistance) {
        wayDistance = paWayDistance;
    }

    public double addCar(Vehicle paVehicle, double paSimTime) {
        double timeOfTravel = wayDistance / paVehicle.getSpeed();
        double timeOfCome = timeOfTravel + paSimTime;
        if (bunchOfBunches.size() != 0) {
            if (timeOfCome <= bunchOfBunches.getLast().getTime()) {
                bunchOfBunches.getLast().addVehicle(paVehicle);
                return -1;
            } else {
                bunchOfBunches.add(new BunchOfCars(paVehicle, timeOfCome));
                return timeOfTravel;
            }
        } else {
            bunchOfBunches.add(new BunchOfCars(paVehicle, timeOfCome));
            return timeOfTravel;
        }
    }

    public ArrayList<Vehicle> getBunch() {
        return bunchOfBunches.remove(0).getBunch();
    }
    
    public double getTimeOfLastBunch(){
        return bunchOfBunches.get(bunchOfBunches.size()-1).getTime();
    }
}
