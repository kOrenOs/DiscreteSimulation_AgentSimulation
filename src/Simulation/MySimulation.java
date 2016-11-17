package Simulation;

import GUI.GUI_Interface;
import Agents.AgentEnviroment;
import Agents.AgentAPoint;
import Agents.AgentTraffic;
import Agents.AgentModel;
import Agents.AgentBPoint;
import Constants.SimulationConstants;
import OSPABA.*;
import java.util.ArrayList;
import Entity.Vehicle;
import Entity.GlobalStatistics;
import GUI.StructCarInformation;
import Entity.ReplicationStatistics;
import agents.AgentWays;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

public class MySimulation extends Simulation {

    private ArrayList<Vehicle> simulationVehicles = new ArrayList<Vehicle>();
    private ArrayList<Integer> carNumber = null;
    private HashMap<String, StructCarInformation> carInformation = new HashMap<String, StructCarInformation>();
    private ArrayList<StructCarInformation> bunchOfCarInformation = new ArrayList<>();
    private ReplicationStatistics statistics;
    private GlobalStatistics globalStats;
    private int price = 0;

    private String fileName = "results.txt";
    private double successRate = 0;

    public static final Random seedGenerator = new Random();

    private ArrayList<GUI_Interface> GUIs = new ArrayList<GUI_Interface>();
    private boolean buyUnloader = false;
    private boolean turnOffACompany = false;
    private boolean fullLoadingStyle = true;

    private boolean isMaxSpeed = false;

    public MySimulation(ArrayList<Integer> paCarNumber, boolean paBuyUnloader, boolean fullLoading, long seed) {
        init();
        carNumber = paCarNumber;

        statistics = new ReplicationStatistics(this);
        globalStats = new GlobalStatistics(statistics);

        buyUnloader = paBuyUnloader;
        fullLoadingStyle = fullLoading;

        seedGenerator.setSeed(seed);
        int vehicleNumber = 0;

        for (int i = 0; i < paCarNumber.size(); i++) {
            vehicleNumber = SimulationConstants.getVehicleCount(paCarNumber.get(i), i);
            for (int j = vehicleNumber; j > 0; j--) {
                simulationVehicles.add(getVehicle(j, i));
            }
        }

        price = SimulationConstants.getPrice(paCarNumber, paBuyUnloader);

        onRefreshUI(new Consumer<Simulation>() {

            @Override
            public void accept(Simulation t) {
                updateGraphics();
                setUsage();
                resorcesUpdate();
                setSuccess();
                carFailureTime();
                avgDurationPerCycle();
                avgLoadPerCycle();
                getRowAAvgTime();
                getRowBAvgTime();
                getRowAAvgLength();
                getRowBAvgLength();
                getStorageAB();
            }
        });
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();
        // Create global statistcis
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        for (GUI_Interface gui : GUIs) {
            gui.setReplicationNumber(currentReplication());
        }

        carReset();
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        super.replicationFinished();

        globalStats.resplicationEnd(currentTime(), turnOffACompany);
        if (currentReplication() > 0) {
            sendGlobalStats();
        }
        statistics.reset(this);
    }

    @Override
    public void simulationFinished() {
        // Dysplay simulation results
        super.simulationFinished();

        for (GUI_Interface gui : GUIs) {
            gui.endOfSimulation();
        }

        writeIntoFile(fileName);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        setAgentModel(new AgentModel(Id.agentModel, this, null));
        setAgentEnviroment(new AgentEnviroment(Id.agenEnviroment, this, agentModel()));
        setAgentTraffic(new AgentTraffic(Id.agentTraffic, this, agentModel()));
        setAgentAPoint(new AgentAPoint(Id.agentAPoint, this, agentTraffic()));
        setAgentBPoint(new AgentBPoint(Id.agentBPoint, this, agentTraffic()));
        setAgentWays(new AgentWays(Id.agentWays, this, agentTraffic()));
    }

    private AgentModel _agentModel;

    public AgentModel agentModel() {
        return _agentModel;
    }

    public void setAgentModel(AgentModel agentModel) {
        _agentModel = agentModel;
    }
    
    private AgentWays _agentWays;
    
    public void setAgentWays(AgentWays agentWays){
        _agentWays = agentWays;
    }

     public AgentWays agentWays() {
        return _agentWays;
    }
    
    private AgentEnviroment _agentEnviroment;

    public AgentEnviroment agentEnviroment() {
        return _agentEnviroment;
    }

    public void setAgentEnviroment(AgentEnviroment agentEnviroment) {
        _agentEnviroment = agentEnviroment;
    }

    private AgentTraffic _agentTraffic;

    public AgentTraffic agentTraffic() {
        return _agentTraffic;
    }

    public void setAgentTraffic(AgentTraffic agentTraffic) {
        _agentTraffic = agentTraffic;
    }

    private AgentAPoint _agentAPoint;

    public AgentAPoint agentAPoint() {
        return _agentAPoint;
    }

    public void setAgentAPoint(AgentAPoint agentAPoint) {
        _agentAPoint = agentAPoint;
    }

    private AgentBPoint _agentBPoint;

    public AgentBPoint agentBPoint() {
        return _agentBPoint;
    }

    public void setAgentBPoint(AgentBPoint agentBPoint) {
        _agentBPoint = agentBPoint;
    }

    public ArrayList getConfigVehicles() {
        return simulationVehicles;
    }
    //meta! tag="end"

    private Vehicle getVehicle(int vehicleNumber, int vehicleType) {
        Vehicle newVehicle = null;
        switch (vehicleType) {
            case 0:
                newVehicle = new Vehicle(SimulationConstants.CAPACITY_A1, SimulationConstants.SPEED_A1,
                        SimulationConstants.FAIL_PROBAB_A1, SimulationConstants.REPAIR_TIME_A1, seedGenerator.nextLong(), "Type1_" + vehicleNumber);
                break;
            case 1:
                newVehicle = new Vehicle(SimulationConstants.CAPACITY_A2, SimulationConstants.SPEED_A2,
                        SimulationConstants.FAIL_PROBAB_A2, SimulationConstants.REPAIR_TIME_A2, seedGenerator.nextLong(), "Type2_" + vehicleNumber);
                break;
            case 2:
                newVehicle = new Vehicle(SimulationConstants.CAPACITY_A3, SimulationConstants.SPEED_A3,
                        SimulationConstants.FAIL_PROBAB_A3, SimulationConstants.REPAIR_TIME_A3, seedGenerator.nextLong(), "Type3_" + vehicleNumber);
                break;
            case 3:
                newVehicle = new Vehicle(SimulationConstants.CAPACITY_A4, SimulationConstants.SPEED_A4,
                        SimulationConstants.FAIL_PROBAB_A4, SimulationConstants.REPAIR_TIME_A4, seedGenerator.nextLong(), "Type4_" + vehicleNumber);
                break;
            case 4:
                newVehicle = new Vehicle(SimulationConstants.CAPACITY_A5, SimulationConstants.SPEED_A5,
                        SimulationConstants.FAIL_PROBAB_A5, SimulationConstants.REPAIR_TIME_A5, seedGenerator.nextLong(), "Type5_" + vehicleNumber);
                break;
        }

        StructCarInformation temp = new StructCarInformation(newVehicle.getName(),
                newVehicle.getCapacity() + "", newVehicle.getSpeed() + "");
        temp.setMomentalFilled(0);

        carInformation.put(newVehicle.getName(), temp);
        bunchOfCarInformation.add(temp);

        return newVehicle;
    }

    public void addGUI(GUI_Interface paGUI) {
        GUIs.add(paGUI);
    }

    public ArrayList<GUI_Interface> getGUIs() {
        return GUIs;
    }

    public synchronized void sendStorageAInformation(int paStorageA) {
        for (GUI_Interface gui : GUIs) {
            gui.setStateOfAStorage(paStorageA + "");
        }
    }

    public synchronized void sendStorageBInformation(int paStorageB) {
        for (GUI_Interface gui : GUIs) {
            gui.setStateOfBStorage(paStorageB + "");
        }

        setTime();
    }

    public synchronized void sendCarInformation(String paVehicleName, String paPossition, double timeOfAction) {
        StructCarInformation temp = carInformation.get(paVehicleName);
        temp.setPossition(paPossition);
        temp.setTimeOfAction(timeOfAction, currentTime());

        updateCarInformation();
    }

    public synchronized void updateCarInformation() {
        if (!isMaxSpeed) {
            for (GUI_Interface gui : GUIs) {
                gui.setCarInformation(bunchOfCarInformation, currentTime());
            }
        }
    }

    public synchronized void updateFilled(String paVehicleName, int paFilled) {
        StructCarInformation temp = carInformation.get(paVehicleName);
        temp.setMomentalFilled(paFilled);
    }

    public void updateMachineStatus(String paMachineName, String paMachineStatus) {
        for (GUI_Interface gui : GUIs) {
            gui.setMachineInformation(paMachineName, paMachineStatus);
        }
    }

    public void setSpeed(double paInterval, double paDuration) {
        isMaxSpeed = false;
        setSimSpeed(paInterval, paDuration);
    }

    public void setMaxSpeed() {
        isMaxSpeed = !isMaxSpeed;
        setMaxSimSpeed();
    }

    public void setTime() {
        for (GUI_Interface gui : GUIs) {
            gui.setCurrentTime(currentTime());
            gui.setGraphInformation(statistics.getStorageAMean(currentTime()),
                    statistics.getStorageBMean(currentTime()), currentTime());
        }
    }

    public void setBuyAndCompanyTurnOffAndLoadStyle(boolean paBuyUnloader, boolean paCompanyTurnOff, boolean paStyle) {
        buyUnloader = paBuyUnloader;
        turnOffACompany = paCompanyTurnOff;
        fullLoadingStyle = paStyle;
    }

    public boolean getBuyUnloader() {
        return buyUnloader;
    }

    public boolean getCompanyTurnOff() {
        return turnOffACompany;
    }

    public boolean getLoadingStyle() {
        return fullLoadingStyle;
    }

    private void updateGraphics() {
        setTime();
        updateCarInformation();
    }

    private void carReset() {
        for (Vehicle vehicle : simulationVehicles) {
            vehicle.setMomentalnFilled(0);
            vehicle.setRandom(seedGenerator.nextLong());
        }
    }

    public ReplicationStatistics getStats() {
        return statistics;
    }

    public void setUsage() {
        for (GUI_Interface gui : GUIs) {
            gui.setLoaderAUsage(statistics.getPointAUsage().getStatA(currentTime()));
            gui.setLoaderBUsage(statistics.getPointAUsage().getStatB(currentTime()));
            gui.setUnloaderAUsage(statistics.getPointBUsage().getStatA(currentTime()));
            if (buyUnloader) {
                gui.setUnloaderBUsage(statistics.getPointBUsage().getStatB(currentTime()));
            }
        }
    }

    private void resorcesUpdate() {
        for (GUI_Interface gui : GUIs) {
            gui.setResourcesToA(statistics.getResourcesToA());
            gui.setResourcesToB(statistics.getResourcesToB());
            gui.setResourcesOutcome(statistics.getResourcesOutcome());
        }
    }

    private void setSuccess() {
        for (GUI_Interface gui : GUIs) {
            gui.setSuccess((statistics.getSuccessTakeOff() * 1.0) / statistics.getNumberOfSuccessOfTime(currentTime()));
        }
    }

    private void carFailureTime() {
        for (GUI_Interface gui : GUIs) {
            gui.setCarFailureTime(statistics.getFailureOfCars());
        }
    }

    private void avgDurationPerCycle() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgDurationPerCycle(statistics.getAvgDurationOfOneCycle());
        }
    }

    private void avgLoadPerCycle() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgLoadPerCycle(statistics.getAvgLoad());
        }
    }

    private void getRowAAvgTime() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgTimeRowA(statistics.getRowAMean());
        }
    }

    private void getRowBAvgTime() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgTimeRowB(statistics.getRowBMean());
        }
    }

    private void getRowAAvgLength() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgLengthRowA(statistics.getAvgRowALength(currentTime()));
        }
    }

    private void getRowBAvgLength() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgLengthRowB(statistics.getAvgRowBLength(currentTime()));
        }
    }

    private void getStorageAB() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgABStorage(statistics.getStorageAMean(currentTime()), statistics.getStorageBMean(currentTime()));
        }
    }

    private void sendGlobalStats() {
        for (GUI_Interface gui : GUIs) {
            gui.setAvgABStorageGlobal(globalStats.getStorageA90Conf(), globalStats.getStorageB90Conf(),
                    globalStats.getStorageA(), globalStats.getStorageB());
            gui.setAvgDurationPerCycleGlobal(globalStats.getAvgDurationOfOneCycle90Conf(), globalStats.getAvgDurationOfOneCycle());
            gui.setAvgLengthRowAGlobal(globalStats.getRowALength90Conf(), globalStats.getRowALength());
            gui.setAvgLengthRowBGlobal(globalStats.getRowBLength90Conf(), globalStats.getRowBLength());
            gui.setAvgLoadPerCycleGlobal(globalStats.getAvgLoad90Conf(), globalStats.getAvgLoad());
            gui.setAvgTimeRowAGlobal(globalStats.getRowA90Conf(), globalStats.getRowA());
            gui.setAvgTimeRowBGlobal(globalStats.getRowB90Conf(), globalStats.getRowB());
            gui.setCarFailureTimeGlobal(globalStats.getFailureOfCars90Conf(), globalStats.getFailureOfCars());
            gui.setLoaderAUsageGlobal(globalStats.getPointAUsageA90Conf(), globalStats.getPointAUsageA());
            gui.setLoaderBUsageGlobal(globalStats.getPointAUsageB90Conf(), globalStats.getPointAUsageB());
            gui.setUnloaderAUsageGlobal(globalStats.getPointBUsageA90Conf(), globalStats.getPointBUsageA());
            if (buyUnloader) {
                gui.setUnloaderBUsageGlobal(globalStats.getPointBUsageB90Conf(), globalStats.getPointBUsageB());
            }
            gui.setResourcesOutcomeGlobal(globalStats.getAllResourcesOutcome90Conf(), globalStats.getAllResourcesOutcome());
            gui.setResourcesToAGlobal(globalStats.getAllResourcesComeToA90Conf(), globalStats.getAllResourcesComeToA());
            gui.setResourcesToBGlobal(globalStats.getAllResourcesComeToB90Conf(), globalStats.getAllResourcesComeToB());
            gui.setSuccessGlobal(globalStats.getSuccesTakeOff90Conf(), globalStats.getSuccesTakeOff());
            gui.setTurnOffTime(globalStats.getAvgTurnOffTime());
        }
    }

    private void writeIntoFile(String fileName) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName, true);

            fw.append("************************** NEW CONFIGURATION *************************\n\n");
            fw.append("Config: " + carNumber + ", unloader: " + buyUnloader + ", full vehicle: " + fullLoadingStyle + "\n");
            fw.append("Replications: " + replicationCount() + "\n");
            fw.append("Price: " + price + "\n");
            fw.append("Success: " + globalStats.getSuccesTakeOff() + "     confidence: " + arrayToString(globalStats.getSuccesTakeOff90Conf()) + "\n");
            fw.append("Avg round time: " + globalStats.getAvgDurationOfOneCycle() + "     confidence: " + arrayToString(globalStats.getAvgDurationOfOneCycle90Conf()) + "\n");
            fw.append("Avg load per round: " + globalStats.getAvgLoad() + "     confidence: " + arrayToString(globalStats.getAvgLoad90Conf()) + "\n");
            fw.append("Avg usage of A point A loader: " + globalStats.getPointAUsageA() + "     confidence: " + arrayToString(globalStats.getPointAUsageA90Conf()) + "\n");
            fw.append("Avg usage of A point B loader: " + globalStats.getPointAUsageB() + "     confidence: " + arrayToString(globalStats.getPointAUsageB90Conf()) + "\n");
            fw.append("Avg usage of B point A loader: " + globalStats.getPointBUsageA() + "     confidence: " + arrayToString(globalStats.getPointBUsageA90Conf()) + "\n");
            if (buyUnloader) {
                fw.append("Avg usage of B point B loader: " + globalStats.getPointBUsageB() + "     confidence: " + arrayToString(globalStats.getPointBUsageB90Conf()) + "\n");
            }
            fw.append("Avg storage A filled: " + globalStats.getStorageA() + "     confidence: " + arrayToString(globalStats.getStorageA90Conf()) + "\n");
            fw.append("Avg storage B filled: " + globalStats.getStorageB() + "     confidence: " + arrayToString(globalStats.getStorageB90Conf()) + "\n");
            fw.append("Avg waiting A row: " + globalStats.getRowA() + "     confidence: " + arrayToString(globalStats.getRowA90Conf()) + "\n");
            fw.append("Avg row A length: " + globalStats.getRowALength() + "     confidence: " + arrayToString(globalStats.getRowALength90Conf()) + "\n");
            fw.append("Avg waiting B row: " + globalStats.getRowB() + "     confidence: " + arrayToString(globalStats.getRowB90Conf()) + "\n");
            fw.append("Avg row B length: " + globalStats.getRowBLength() + "     confidence: " + arrayToString(globalStats.getRowBLength90Conf()) + "\n");
            fw.append("Turn off company A worki time: " + globalStats.getAvgTurnOffTime() + "\n");
            fw.append("\n\n");

            fw.close();
        } catch (IOException e) {
        }

        try {
            fw = new FileWriter("inputConfig.txt", true);
            fw.append(getCarNumberToString() + "" + buyUnloader + ", " + globalStats.getSuccesTakeOff() + ", " + price + "\n");
            fw.close();

        } catch (IOException e) {
        }
        successRate = globalStats.getSuccesTakeOff();
    }

    public double getSuccessRate() {
        return successRate;
    }

    private String getCarNumberToString() {
        String temp = "";
        for (Integer car : carNumber) {
            temp += car + ", ";
        }
        return temp;
    }

    private String arrayToString(double[] array) {
        String temp = "[";
        for (int i = 0; i < array.length - 1; i++) {
            temp += array[i] + ", ";
        }
        temp += array[array.length - 1] + "]";
        return temp;
    }
}
