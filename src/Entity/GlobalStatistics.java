/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Constants.SimulationConstants;
import OSPStat.Stat;

/**
 *
 * @author korenciak.marek
 */
public class GlobalStatistics {

    private ReplicationStatistics localStats;

    private Stat succesTakeOff;
    private Stat allResourcesComeToA;
    private Stat allResourcesOutcome;
    private Stat allResourcesComeToB;

    private Stat failureOfCars;
    private Stat avgLoad;
    private Stat avgDurationOfOneCycle;
    private Stat rowA;
    private Stat rowB;

    private Stat rowALength;
    private Stat rowBLength;
    
    private Stat storageA;
    private Stat storageB;

    private Stat pointAUsageA;
    private Stat pointAUsageB;
    private Stat pointBUsageA;
    private Stat pointBUsageB;
    
    private Stat avgTurnOffTime;

    public GlobalStatistics(ReplicationStatistics paStats) {
        localStats = paStats;
        reset();
    }

    public void resplicationEnd(double paTime, boolean turnOff) {
        succesTakeOff.addSample((localStats.getSuccessTakeOff() * 1.0) / SimulationConstants.NUMBER_OF_TAKE_OUT_COME);
        allResourcesComeToA.addSample(localStats.getResourcesToA());
        allResourcesOutcome.addSample(localStats.getResourcesOutcome());
        allResourcesComeToB.addSample(localStats.getResourcesToB());

        failureOfCars.addSample(localStats.getFailureOfCars());
        avgLoad.addSample(localStats.getAvgLoad());
        avgDurationOfOneCycle.addSample(localStats.getAvgDurationOfOneCycle());
        rowA.addSample(localStats.getRowAMean());
        rowB.addSample(localStats.getRowBMean());
        
        rowALength.addSample(localStats.getAvgRowALength(paTime));
        rowBLength.addSample(localStats.getAvgRowBLength(paTime));

        pointAUsageA.addSample(localStats.getPointAUsage().getStatA(paTime));
        pointAUsageB.addSample(localStats.getPointAUsage().getStatB(paTime));
        pointBUsageA.addSample(localStats.getPointBUsage().getStatA(paTime));
        pointBUsageB.addSample(localStats.getPointBUsage().getStatB(paTime));        
        
        storageA.addSample(localStats.getStorageAMean(paTime));
        storageB.addSample(localStats.getStorageBMean(paTime));
        
        if(turnOff){
            avgTurnOffTime.addSample(localStats.getTimeAfterTurnOff());
        }
    }

    public void reset() {
        succesTakeOff = new Stat();
        allResourcesComeToA = new Stat();
        allResourcesOutcome = new Stat();
        allResourcesComeToB = new Stat();
        failureOfCars = new Stat();
        avgLoad = new Stat();
        avgDurationOfOneCycle = new Stat();
        rowA = new Stat();
        rowB = new Stat();
        rowALength = new Stat();
        rowBLength = new Stat();
        pointAUsageA = new Stat();
        pointAUsageB = new Stat();
        pointBUsageA = new Stat();
        pointBUsageB = new Stat();
        storageA = new Stat();
        storageB = new Stat();
        avgTurnOffTime = new Stat();
    }

    public ReplicationStatistics getLocalStats() {
        return localStats;
    }

    public double[] getSuccesTakeOff90Conf() {
        return succesTakeOff.confidenceInterval_90();
    }

    public double[] getAllResourcesComeToA90Conf() {
        return allResourcesComeToA.confidenceInterval_90();
    }

    public double[] getAllResourcesOutcome90Conf() {
        return allResourcesOutcome.confidenceInterval_90();
    }

    public double[] getAllResourcesComeToB90Conf() {
        return allResourcesComeToB.confidenceInterval_90();
    }

    public double[] getFailureOfCars90Conf() {
        return failureOfCars.confidenceInterval_90();
    }

    public double[] getAvgLoad90Conf() {
        return avgLoad.confidenceInterval_90();
    }

    public double[] getAvgDurationOfOneCycle90Conf() {
        return avgDurationOfOneCycle.confidenceInterval_90();
    }

    public double[] getRowA90Conf() {
        return rowA.confidenceInterval_90();
    }

    public double[] getRowB90Conf() {
        return rowB.confidenceInterval_90();
    }

    public double[] getRowALength90Conf() {
        return rowALength.confidenceInterval_90();
    }

    public double[] getRowBLength90Conf() {
        return rowBLength.confidenceInterval_90();
    }

    public double[] getStorageA90Conf() {
        return storageA.confidenceInterval_90();
    }

    public double[] getStorageB90Conf() {
        return storageB.confidenceInterval_90();
    }

    public double[] getPointAUsageA90Conf() {
        return pointAUsageA.confidenceInterval_90();
    }

    public double[] getPointAUsageB90Conf() {
        return pointAUsageB.confidenceInterval_90();
    }

    public double[] getPointBUsageA90Conf() {
        return pointBUsageA.confidenceInterval_90();
    }
    
    public double[] getPointBUsageB90Conf() {
        return pointBUsageB.confidenceInterval_90();
    }

    public double getSuccesTakeOff() {
        return succesTakeOff.mean();
    }

    public double getAllResourcesComeToA() {
        return allResourcesComeToA.mean();
    }

    public double getAllResourcesOutcome() {
        return allResourcesOutcome.mean();
    }

    public double getAllResourcesComeToB() {
        return allResourcesComeToB.mean();
    }

    public double getFailureOfCars() {
        return failureOfCars.mean();
    }

    public double getAvgLoad() {
        return avgLoad.mean();
    }

    public double getAvgDurationOfOneCycle() {
        return avgDurationOfOneCycle.mean();
    }

    public double getRowA() {
        return rowA.mean();
    }

    public double getRowB() {
        return rowB.mean();
    }

    public double getRowALength() {
        return rowALength.mean();
    }

    public double getRowBLength() {
        return rowBLength.mean();
    }

    public double getStorageA() {
        return storageA.mean();
    }

    public double getStorageB() {
        return storageB.mean();
    }

    public double getPointAUsageA() {
        return pointAUsageA.mean();
    }

    public double getPointAUsageB() {
        return pointAUsageB.mean();
    }

    public double getPointBUsageA() {
        return pointBUsageA.mean();
    }

    public double getPointBUsageB() {
        return pointBUsageB.mean();
    }

    public double getAvgTurnOffTime() {
        return avgTurnOffTime.mean();
    }
    
    public double[] getAvgTurnOffTime90Conf() {
        return avgTurnOffTime.confidenceInterval_90();
    }
}
