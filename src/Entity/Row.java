/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.LinkedList;

/**
 *
 * @author korenciak.marek
 */
public class Row {

    private LinkedList<Vehicle> vehicles;
    private double timeOfChange = 0;
    private double sum = 0;
    private double timeOfSleep = 0;
    private boolean stopped = false;

    public Row() {
        vehicles = new LinkedList<Vehicle>();
    }

    public void addVehicle(Vehicle paVehicle, double paTime) {
        addChange(paTime);
        vehicles.add(paVehicle);
    }

    public Vehicle getFirstVehicle(double paTime) {
        addChange(paTime);
        return vehicles.remove(0);
    }

    public double getStats(double paTime) {
        addChange(paTime);
        double temp = paTime - timeOfSleep;
        return sum / temp;
    }

    public int size() {
        return vehicles.size();
    }

    public LinkedList<Vehicle> allItems() {
        return vehicles;
    }

    public void addChange(double paTime) {
        if (!stopped) {
            double temp = paTime - timeOfChange;
            sum += temp * vehicles.size();
            timeOfChange = paTime;
        }
    }

    public void stopRow(double paTime) {
        addChange(paTime);
        stopped = true;
        timeOfChange = paTime;
        for (Vehicle vehicle : vehicles) {
            vehicle.pauseCycle(paTime);
            vehicle.pauseRow(paTime);
        }
    }

    public void startRow(double paTime) {
        stopped = false;
        timeOfChange = paTime;
        timeOfSleep += paTime - timeOfChange;
        for (Vehicle vehicle : vehicles) {
            vehicle.startActionCycle(paTime);
            vehicle.startActionRow(paTime);
        }
    }

    public void reset() {
        vehicles.clear();
        timeOfChange = 0;
        timeOfSleep = 0;
        sum = 0;
        stopped = false;
    }
}
