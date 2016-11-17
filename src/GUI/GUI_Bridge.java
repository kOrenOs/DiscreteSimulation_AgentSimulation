/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Constants.SimulationConstants;
import Simulation.MySimulation;
import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class GUI_Bridge implements Runnable {

    private MySimulation simulation;
    private int replications = -1;
    private boolean buyUnloader = false;
    private boolean turnOffACompany = false;
    private boolean fullLoader = false;
    private boolean started = false;
    private double interval = 1;
    private double duration = 0.5;

    public void setSimulation(ArrayList<Integer> carNumber, boolean paBuyUnloader, boolean vehicleFull, long seed) {
        simulation = new MySimulation(carNumber, paBuyUnloader, vehicleFull, seed);
    }

    public int maxNumberOfCars(int paType) {
        return SimulationConstants.maxNumberOfCars(paType);
    }

    public int getPriceOfSet(ArrayList<Integer> carNumber, boolean paBuyUnloader) {
        return SimulationConstants.getPrice(carNumber, paBuyUnloader);
    }

    public void setSpeed(double paInterval, double paDuration) {
        if (started) {
            simulation.setSpeed(paInterval, paDuration / 1000);
        }
        interval = paInterval;
        duration = paDuration / 1000;
    }

    public void stopSimulation() {
        if (started) {
            simulation.stopSimulation();
        }
    }

    public void pauseSimulation() {
        if (started) {
            simulation.pauseSimulation();
        }
    }

    public void resumeSimulation() {
        if (started) {
            simulation.resumeSimulation();
        }
    }

    public void addGUI(GUI_Interface paGUI) {
        simulation.addGUI(paGUI);
    }

    public void simulate(int replicationCount) {
        simulation.simulate(replicationCount);
    }

    public void simulate(int replicationCount, double simEndTime) {
        simulation.simulate(replicationCount, simEndTime);
    }

    public void setReplications(int paReplications) {
        replications = paReplications;
    }

    public void setBuyAndCompanyTurnOff(boolean paBuyUnloader, boolean paTurnOffACompany, boolean paFullLoader) {
        simulation.setBuyAndCompanyTurnOffAndLoadStyle(paBuyUnloader, paTurnOffACompany, paFullLoader);

    }

    public void setMaxSpeed() {
        if (started) {
            simulation.setMaxSpeed();
        }
    }

    @Override
    public void run() {
        started = true;
        simulation.setSpeed(interval, duration);
        simulate(replications, SimulationConstants.TIME_DISTANCE_OF_BUILDING);
        started = false;
    }
}
