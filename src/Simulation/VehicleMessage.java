/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import Entity.Vehicle;
import OSPABA.Simulation;
import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class VehicleMessage extends MyMessage {

    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    private int resources = -1;

    public VehicleMessage(MyMessage original) {
        super(original);
    }

    public VehicleMessage(Simulation sim) {
        super(sim);
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public Vehicle getVehicle() {
        if (vehicles.size() == 1) {
            return vehicles.get(0);
        }
        return null;
    }

    public void addVehicle(Vehicle paVehicle) {
        vehicles.add(paVehicle);
    }

    public void addVehicles(ArrayList<Vehicle> paVehicle) {
        vehicles.addAll(paVehicle);
    }

    public void resetVehicles() {
        vehicles.clear();
    }

    public void setResources(int paResources) {
        resources = paResources;
    }

    public int getResources() {
        return resources;
    }
}
