/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import Constants.SimulationConstants;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class StartSimulation {

    private ArrayList<Integer> maxNumber = new ArrayList<>();

    public static void main(String[] args) {
        StartSimulation sim = new StartSimulation();
    }

    public StartSimulation() {
        //firstStep();
        maxNumber = new ArrayList<>();
        maxNumber.add(3);
        maxNumber.add(14);
        maxNumber.add(13);
        maxNumber.add(39);
        maxNumber.add(2);
        secondStep();
        System.out.println("");
    }

    private void firstStep() {
        MySimulation simulation = null;
        ArrayList<Integer> temp = new ArrayList<>();
        Random rand = new Random();

        maxNumber.add(3);

        for (int i = 1; i < 4; i++) {
            for (int j = 8; j < 50; j++) {
                temp.clear();
                for (int k = 0; k < 5; k++) {
                    if (k != i) {
                        temp.add(0);
                    } else {
                        temp.add(j);
                    }
                }
                System.out.println(temp.toString());
                simulation = new MySimulation(temp, false, false, rand.nextLong());
                simulation.simulate(1, SimulationConstants.TIME_DISTANCE_OF_BUILDING);
                System.out.println(simulation.getSuccessRate());
                if (simulation.getSuccessRate() == 1) {
                    maxNumber.add(j);
                    break;
                }
            }
        }

        maxNumber.add(2);
    }

    private void secondStep() {
        MySimulation simulation = null;
        Random rand = new Random();
        String path = "fastResults.txt";
        String pathCorrect = "fastResultsCorrect.txt";
        FileWriter fw = null;

        boolean vehicleFull = true;

        int bestPrice = Integer.MAX_VALUE;
        int tempPrice = 0;

        try {
            fw = new FileWriter(path+"False", true);
            for (int i = 0; i <= maxNumber.get(3); i++) {
                for (int j = 0; j <= maxNumber.get(0); j++) {
                    for (int k = 1; k <= maxNumber.get(1); k++) {
                        for (int l = 0; l <= maxNumber.get(2); l++) {
                            for (int m = 0; m <= maxNumber.get(4); m++) {
                                ArrayList<Integer> temp = getToArray(j,k,l,i,m);
                                tempPrice = SimulationConstants.getPrice(temp, false);

                                if (tempPrice <= bestPrice) {
                                    System.out.println(temp.toString());
                                    simulation = new MySimulation(temp, false, vehicleFull, rand.nextLong());
                                    simulation.simulate(2, SimulationConstants.TIME_DISTANCE_OF_BUILDING);
                                    System.out.println(simulation.getSuccessRate());
                                    fw.append(getCarNumberToString(temp) + ", " + false + ", " + vehicleFull + ", " + simulation.getSuccessRate() + ", " + tempPrice + "\n");
                                    if (simulation.getSuccessRate() >= 0.90) {
                                        writeOut(temp, false, vehicleFull, simulation.getSuccessRate(), tempPrice, pathCorrect+"False");
                                    }
                                    if (simulation.getSuccessRate() == 1) {
                                        if (tempPrice < bestPrice) {
                                            bestPrice = tempPrice;
                                        }
                                        break;
                                    }
                                } else {
                                    break;
                                }

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        
        bestPrice = Integer.MAX_VALUE;
        tempPrice = 0;

        try {
            fw = new FileWriter(path+"True", true);
            for (int i = 0; i <= maxNumber.get(3); i++) {
                for (int j = 0; j <= maxNumber.get(0); j++) {
                    for (int k = 1; k <= maxNumber.get(1); k++) {
                        for (int l = 0; l <= maxNumber.get(2); l++) {
                            for (int m = 0; m <= maxNumber.get(4); m++) {
                                ArrayList<Integer> temp = getToArray(j,k,l,i,m);
                                tempPrice = SimulationConstants.getPrice(temp, true);

                                if (tempPrice <= bestPrice) {
                                    System.out.println(temp.toString());
                                    simulation = new MySimulation(temp, true, vehicleFull, rand.nextLong());
                                    simulation.simulate(1, SimulationConstants.TIME_DISTANCE_OF_BUILDING);
                                    System.out.println(simulation.getSuccessRate());
                                    fw.append(getCarNumberToString(temp) + ", " + true + ", " + vehicleFull + ", " + simulation.getSuccessRate() + ", " + tempPrice + "\n");
                                    if (simulation.getSuccessRate() >= 0.90) {
                                        writeOut(temp, true, vehicleFull, simulation.getSuccessRate(), tempPrice, pathCorrect+"True");
                                    }
                                    if (simulation.getSuccessRate() == 1) {
                                        if (tempPrice < bestPrice) {
                                            bestPrice = tempPrice;
                                        }
                                        break;
                                    }
                                } else {
                                    break;
                                }

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    private void writeOut(ArrayList<Integer> carConfig, boolean buyUnloader, boolean fullVehicle, double success, int price, String path) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(path, true);

            fw.append(getCarNumberToString(carConfig) + ", " + buyUnloader + ", " + fullVehicle + ", " + success + ", " + price + "\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    private String getCarNumberToString(ArrayList<Integer> carConfig) {
        String temp = "";
        for (Integer car : carConfig) {
            temp += car + ", ";
        }
        return temp;
    }

    private ArrayList getToArray(int t1, int t2, int t3, int t4, int t5) {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(t1);
        temp.add(t2);
        temp.add(t3);
        temp.add(t4);
        temp.add(t5);

        return temp;
    }
}
