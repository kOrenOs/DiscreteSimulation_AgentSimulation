/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public interface GUI_Interface {

    public void setCurrentTime(double simTime);

    public void setCarInformation(ArrayList<StructCarInformation> carInformation, double simTime);

    public void setMachineInformation(String machineName, String machineState);

    public void setStateOfAStorage(String storageAmount);

    public void setStateOfBStorage(String storageAmount);

    public void setLoaderAUsage(double usage);

    public void setLoaderBUsage(double usage);

    public void setUnloaderAUsage(double usage);

    public void setUnloaderBUsage(double usage);

    public void setResourcesToA(long resources);

    public void setResourcesToB(long resources);

    public void setResourcesOutcome(long resources);

    public void setSuccess(double success);

    public void setCarFailureTime(double failureTime);

    public void setAvgLoadPerCycle(double load);

    public void setAvgDurationPerCycle(double duration);

    public void setAvgTimeRowA(double duration);

    public void setAvgTimeRowB(double duration);

    public void setAvgLengthRowA(double duration);

    public void setAvgLengthRowB(double duration);

    public void setAvgABStorage(double storageA, double storageB);

    public void setReplicationNumber(int replicationNumber);

    public void setLoaderAUsageGlobal(double[] usage, double normalUsage);

    public void setLoaderBUsageGlobal(double[] usage, double normalUsage);

    public void setUnloaderAUsageGlobal(double[] usage, double normalUsage);

    public void setUnloaderBUsageGlobal(double[] usage, double normalUsage);

    public void setResourcesToAGlobal(double[] resources, double normalResources);

    public void setResourcesToBGlobal(double[] resources, double normalResources);

    public void setResourcesOutcomeGlobal(double[] resources, double normalResources);

    public void setSuccessGlobal(double[] success, double normalSuccess);

    public void setCarFailureTimeGlobal(double[] failureTime, double normalFailureTime);

    public void setAvgLoadPerCycleGlobal(double[] load, double normalLoad);

    public void setAvgDurationPerCycleGlobal(double[] duration, double normalDuration);

    public void setAvgTimeRowAGlobal(double[] duration, double normalDuration);

    public void setAvgTimeRowBGlobal(double[] duration, double normalDuration);

    public void setAvgLengthRowAGlobal(double[] duration, double normalDuration);

    public void setAvgLengthRowBGlobal(double[] duration, double normalDuration);

    public void setAvgABStorageGlobal(double[] storageA, double[] storageB, double normalDurationA, double normalDurationB);
    
    public void setTurnOffTime(double turnOffTime);
    
    public void endOfSimulation();
    
    public void setGraphInformation(double storageA, double storageB, double time);
}
