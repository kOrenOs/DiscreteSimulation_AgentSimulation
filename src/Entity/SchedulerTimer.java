/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Constants.SimulationConstants;

/**
 *
 * @author korenciak.marek
 */
public class SchedulerTimer {

    private int timeOfStart = 0;
    private int timeOfEnd = 0;
    
    public SchedulerTimer(int paTimeOfStart, int paTimeOfEnd) {
        timeOfStart = paTimeOfStart;
        timeOfEnd = paTimeOfEnd;
    }
    
    public boolean getWorking(double sysTime){
        if ((SimulationConstants.START_TIME + sysTime) % (24*60) < timeOfEnd
                && (SimulationConstants.START_TIME + sysTime) % (24*60) >= timeOfStart) {
            return true;
        }else{
            return false;
        }
    }
    
    public double getTimeOfSleep(double sysTime){
        double temp = (sysTime+SimulationConstants.START_TIME)%(24*60);
        if(temp >= timeOfEnd){
            return (24*60)-temp+timeOfStart;
        }
        if(temp<timeOfStart){
            return timeOfStart - temp;
        }
        return timeOfEnd - temp;
    }
}
