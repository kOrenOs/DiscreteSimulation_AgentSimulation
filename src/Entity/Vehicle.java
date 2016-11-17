/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Simulation.VehicleMessage;
import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class Vehicle {

    private int capacity;
    private double speed;
    private double failProb;
    private int repairTime;
    private Random failGenerator;
    private String name;
    private int momentalFilled = 0;
    private double lastCanteen = 0;

    private VehicleMessage request;
    private TimeStopper stopperCycle = new TimeStopper();
    private TimeStopper stopperWaitingRows = new TimeStopper();

    public Vehicle(int paCapacity, double paSpeed, double paFailProb, int paRepairTime, long seed, String paName) {
        capacity = paCapacity;
        speed = paSpeed;
        failProb = paFailProb;
        repairTime = paRepairTime;
        failGenerator = new Random(seed);
        name = paName;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getFailProb() {
        return failProb;
    }

    public int getRepairTime() {
        return repairTime;
    }

    public String getName() {
        return name;
    }

    public void startActionCycle(double time) {
        stopperCycle.start(time);
    }
    
    public void startActionRow(double time) {
        stopperWaitingRows.start(time);
    }

    public boolean vehicleFail() {
        if (failGenerator.nextDouble() < failProb) {
            return true;
        }
        return false;
    }

    public double endOfActionCycle(double time) {
        return stopperCycle.getTimeAndReset(time);
    }
    
    public void pauseCycle(double time){
        stopperCycle.pause(time);
    }
    
    public double endOfActionRow(double time) {
        return stopperWaitingRows.getTimeAndReset(time);
    }
    
    public void pauseRow(double time){
        stopperWaitingRows.pause(time);
    }

    public void setMomentalnFilled(int filled) {
        momentalFilled = filled;
    }

    public int getMomentalFilled() {
        return momentalFilled;
    }
    
    public void setRandom(long paSeed){
        failGenerator = new Random(paSeed);
        stopperCycle = new TimeStopper();
    }
    
    public void setRequest(VehicleMessage message){
        request = message;
        request.addVehicle(this);
    }
    
    public VehicleMessage getReqest(){
        return request;
    }
    
    public double getCountingTime(){
        return stopperWaitingRows.getTime();
    }
    
    public void setLastCanteen(double time){
        lastCanteen = time;
    }
    
    public double getCanteen(){
        return lastCanteen;
    }
}
