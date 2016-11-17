/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class SimulationConstants {
    public static final int CAPACITY_A1 = 10;
    public static final double SPEED_A1 = 60/60.0;
    public static final double FAIL_PROBAB_A1 = 0.12;
    public static final int REPAIR_TIME_A1 = 80;
    public static final int PRICE_A1 = 30000;
    public static final int MAX_COUNT_A1 = 3;
    
    public static final int CAPACITY_A2 = 20;
    public static final double SPEED_A2 = 50/60.0;
    public static final double FAIL_PROBAB_A2 = 0.04;
    public static final int REPAIR_TIME_A2 = 50;
    public static final int PRICE_A2 = 55000;
    public static final int MAX_COUNT_A2 = -1;
    
    public static final int CAPACITY_A3 = 25;
    public static final double SPEED_A3 = 45/60.0;
    public static final double FAIL_PROBAB_A3 = 0.04;
    public static final int REPAIR_TIME_A3 = 100;
    public static final int PRICE_A3 = 40000;
    public static final int MAX_COUNT_A3 = -1;
    
    public static final int CAPACITY_A4 = 5;
    public static final double SPEED_A4 = 70/60.0;
    public static final double FAIL_PROBAB_A4 = 0.11;
    public static final int REPAIR_TIME_A4 = 44;
    public static final int PRICE_A4 = 60000;
    public static final int MAX_COUNT_A4 = -1;
    
    public static final int CAPACITY_A5 = 40;
    public static final double SPEED_A5 = 30/60.0;
    public static final double FAIL_PROBAB_A5 = 0.06;
    public static final int REPAIR_TIME_A5 = 170;
    public static final int PRICE_A5 = 10000;
    public static final int MAX_COUNT_A5 = 2;
    
    public static final double LOAD_MACHINE1_SPEED = 180/60.0;
    public static final int LOAD_MACHINE1_START_TIME = 7*60;
    public static final int LOAD_MACHINE1_END_TIME = 18*60;
    
    public static final double LOAD_MACHINE2_SPEED = 250/60.0;
    public static final int LOAD_MACHINE2_START_TIME = 9*60;
    public static final int LOAD_MACHINE2_END_TIME = 22*60;
    
    public static final double UNLOAD_MACHINE1_SPEED = 190/60.0;
    public static final int UNLOAD_MACHINE1_START_TIME = 7*60+30;
    public static final int UNLOAD_MACHINE1_END_TIME = 22*60;
    
    public static final double UNLOAD_MACHINE2_SPEED = 190/60.0;
    public static final int UNLOAD_MACHINE2_START_TIME = 7*60+30;
    public static final int UNLOAD_MACHINE2_END_TIME = 22*60;
    
    public static final int UNLOAD_MACHINE_PRICE = 130000;
    
    public static final int WAY_AB_DISTANCE = 45;
    public static final int WAY_BC_DISTANCE = 15;
    public static final int WAY_CA_DISTANCE = 35;
    
    public static final int CAPACITY_STOCK = 10000;
    
    public static final int START_LOAD_A = 3500;
    public static final int START_LOAD_B = 1300;
    
    public static final int START_TIME = 7*60;
    
    public static final int COMPANY_A_TURN_OFF = (2+30*3)*24*60;                  //+2 because May and July have 31 days
    public static final int TIME_DISTANCE_OF_BUILDING = (365+6*30+4)*24*60;     //+4 because there are 4 months with 31 days in May to October 
    
    public static final int TAKE_OUT_START_TIME = 7*60;
    public static final int TAKE_OUT_END_TIME = 22*60;
    public static final int MINUTES_BETWEEN_TAKE_OUT = 30;
    public static final int NUMBER_OF_TAKE_OUT_COME_PER_DAY = ((TAKE_OUT_END_TIME-TAKE_OUT_START_TIME)/MINUTES_BETWEEN_TAKE_OUT) +1;
    public static final int NUMBER_OF_TAKE_OUT_COME = (365+6*30+4)*NUMBER_OF_TAKE_OUT_COME_PER_DAY+1;
    
    public static final boolean TURN_ON_POSSITION_NOTES = false;
    
    public static int getPrice(ArrayList<Integer> cars, boolean paBuyUnloader) {
        int temp = 0;
        for (int i = 0; i < 5; i++) {
            temp += getVehiclePrice(i)*cars.get(i);
        }
        if(paBuyUnloader){
            temp += SimulationConstants.UNLOAD_MACHINE_PRICE;
        }
        
        return temp;
    }
    
    public static int getVehiclePrice(int vehicleType) {
        switch (vehicleType) {
            case 0:
                return SimulationConstants.PRICE_A1;
            case 1:
                return SimulationConstants.PRICE_A2;
            case 2:
                return SimulationConstants.PRICE_A3;
            case 3:
                return SimulationConstants.PRICE_A4;
            case 4:
                return SimulationConstants.PRICE_A5;
            default:
                return -1;
        }
    }
    
    public static int maxNumberOfCars(int paType) {
        switch (paType) {
            case 0:
                return SimulationConstants.MAX_COUNT_A1;
            case 1:
                return SimulationConstants.MAX_COUNT_A2;
            case 2:
                return SimulationConstants.MAX_COUNT_A3;
            case 3:
                return SimulationConstants.MAX_COUNT_A4;
            case 4:
                return SimulationConstants.MAX_COUNT_A5;
            default:
                return -1;
        }
    }
    
    public static int getVehicleCount(int count, int vehicleType) {
        switch (vehicleType) {
            case 0:
                if (count > SimulationConstants.MAX_COUNT_A1 && SimulationConstants.MAX_COUNT_A1 != -1) {
                    return SimulationConstants.MAX_COUNT_A1;
                }
                break;
            case 1:
                if (count > SimulationConstants.MAX_COUNT_A2 && SimulationConstants.MAX_COUNT_A2 != -1) {
                    return SimulationConstants.MAX_COUNT_A2;
                }
                break;
            case 2:
                if (count > SimulationConstants.MAX_COUNT_A3 && SimulationConstants.MAX_COUNT_A3 != -1) {
                    return SimulationConstants.MAX_COUNT_A3;
                }
                break;
            case 3:
                if (count > SimulationConstants.MAX_COUNT_A4 && SimulationConstants.MAX_COUNT_A4 != -1) {
                    return SimulationConstants.MAX_COUNT_A4;
                }
                break;
            case 4:
                if (count > SimulationConstants.MAX_COUNT_A5 && SimulationConstants.MAX_COUNT_A5 != -1) {
                    return SimulationConstants.MAX_COUNT_A5;
                }
                break;
        }
        return count;
    }
}
