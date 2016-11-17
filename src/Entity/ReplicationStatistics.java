/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Constants.SimulationConstants;
import static Constants.SimulationConstants.TAKE_OUT_END_TIME;
import static Constants.SimulationConstants.TAKE_OUT_START_TIME;
import OSPStat.Stat;
import Simulation.MySimulation;

/**
 *
 * @author korenciak.marek
 */
public class ReplicationStatistics {

    private int succesTakeOff = 0;
    private long allResourcesComeToA = 0;
    private long allResourcesOutcome = 0;
    private long allResourcesComeToB = 0;

    private Stat failureOfCars;
    private Stat avgLoad;
    private Stat avgDurationOfOneCycle;
    private Stat rowA;
    private Stat rowB;

    private RowLengthKeeper storageA;
    private RowLengthKeeper storageB;

    private Row rowALength;
    private Row rowBLength;

    private MachineUsage pointAUsage;
    private MachineUsage pointBUsage;
    
    private double timeAfterTurnOff = -1;

    public ReplicationStatistics(MySimulation simulation) {
        storageB = new RowLengthKeeper();
        storageA = new RowLengthKeeper();
        reset(simulation);
    }

    public void addRowA(Row paRowA) {
        rowALength = paRowA;
    }

    public void addRowB(Row paRowB) {
        rowBLength = paRowB;
    }

    public double getAvgRowALength(double paTime) {
        return rowALength.getStats(paTime);
    }

    public double getAvgRowBLength(double paTime) {
        return rowBLength.getStats(paTime);
    }

    public void addPointAUsage(MachineUsage paMachineAUsage) {
        pointAUsage = paMachineAUsage;
    }

    public MachineUsage getPointAUsage() {
        return pointAUsage;
    }

    public void addPointBUsage(MachineUsage paMachineAUsage) {
        pointBUsage = paMachineAUsage;
    }

    public MachineUsage getPointBUsage() {
        return pointBUsage;
    }

    public double getFailureOfCars() {
        return failureOfCars.mean() * failureOfCars.sampleSize();
    }

    public int getSuccessTakeOff() {
        return succesTakeOff;
    }

    public void addRowA(double time) {
        rowA.addSample(time);
    }

    public void addRowB(double time) {
        rowB.addSample(time);
    }

    public double getRowAMean() {
        return rowA.mean();
    }

    public double getRowBMean() {
        return rowB.mean();
    }

    public void addStorageA(double paTime, double amount) {
        storageA.add(paTime, amount);
    }

    public void addStorageB(double paTime, double amount) {
        storageB.add(paTime, amount);
    }

    public double getStorageAMean(double paTime) {
        return storageA.getStats(paTime);
    }

    public double getStorageBMean(double paTime) {
        return storageB.getStats(paTime);
    }
    
    public void addTimeAfterTurnOff(double paTime){
        if(timeAfterTurnOff == -1){
            timeAfterTurnOff =paTime;
        }
    }
    
    public double getTimeAfterTurnOff(){
        if(timeAfterTurnOff == -1){
            return SimulationConstants.TIME_DISTANCE_OF_BUILDING - SimulationConstants.COMPANY_A_TURN_OFF;
        }
        return timeAfterTurnOff;
    }

    public int getNumberOfSuccessOfTime(double simTime) {
        int times = 0;
        int temp = (int) Math.floor((simTime + SimulationConstants.START_TIME) / (24 * 60));       //days

        times = temp * SimulationConstants.NUMBER_OF_TAKE_OUT_COME_PER_DAY;

        temp = (int) ((simTime + SimulationConstants.START_TIME) % (24 * 60) - TAKE_OUT_START_TIME);
        if (temp < 0) {
            return times;
        }

        if (temp > TAKE_OUT_END_TIME - TAKE_OUT_START_TIME) {
            return times + SimulationConstants.NUMBER_OF_TAKE_OUT_COME_PER_DAY;
        }

        return times + temp / SimulationConstants.MINUTES_BETWEEN_TAKE_OUT + 1;
    }

    public double getAvgDurationOfOneCycle() {
        return avgDurationOfOneCycle.mean();
    }

    public double getAvgLoad() {
        return avgLoad.mean();
    }

    public void addFailureOfCarsStat(double paSample) {
        failureOfCars.addSample(paSample);
    }

    public void addSuccesTakeOffStat(int paSuccess) {
        succesTakeOff += paSuccess;
    }

    public void addAvgDurationOfOneCycleStat(double paSample) {
        avgDurationOfOneCycle.addSample(paSample);
    }

    public void addAvgLoad(double paLoad) {
        avgLoad.addSample(paLoad);
    }

    public void addResourcesToA(long paResources) {
        allResourcesComeToA += paResources;
    }

    public void addResourcesToB(long paResources) {
        allResourcesComeToB += paResources;
    }

    public void addResourcesOutcome(long paResources) {
        allResourcesOutcome += paResources;
    }

    public long getResourcesToA() {
        return allResourcesComeToA;
    }

    public long getResourcesToB() {
        return allResourcesComeToB;
    }

    public long getResourcesOutcome() {
        return allResourcesOutcome;
    }

    public void reset(MySimulation simulation) {
        succesTakeOff = 0;
        allResourcesComeToA = 0;
        allResourcesOutcome = 0;
        allResourcesComeToB = 0;

        failureOfCars = new Stat();
        avgDurationOfOneCycle = new Stat();
        avgLoad = new Stat();

        rowA = new Stat();
        rowB = new Stat();
        
        timeAfterTurnOff = -1;

        if (rowALength != null) {
            rowALength.reset();
            rowBLength.reset();

            pointAUsage.reset();
            pointBUsage.reset();

            storageA.reset();
            storageB.reset();
        }
    }
}
