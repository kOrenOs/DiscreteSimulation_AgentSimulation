/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author korenciak.marek
 */
public class TimeStopper {
    private double time = 0;
    private double start = 0;

    public void start(double simTime){
       start = simTime;
    }
    
    public void pause(double simTime){
        time += simTime - start;
    }
    
    public double getTimeAndReset(double simTime){
        time += simTime - start;
        double temp = time;
        time = 0;
        return temp;
    }
    
    public double getTime(){
        return start;
    }
}
