package Managers;

import Simulation.Id;
import Simulation.Mc;
import Simulation.MySimulation;
import Agents.AgentBPoint;
import Constants.SimulationConstants;
import Entity.CarPossition;
import Entity.MachineState;
import Entity.MachineUsage;
import Entity.Vehicle;
import Entity.Row;
import Entity.StatInterface;
import OSPABA.*;
import Simulation.MyMessage;
import Simulation.ResourceMessage;
import Simulation.VehicleMessage;

//meta! id="18"
public class ManagerBPoint extends Manager implements StatInterface {

    private Row rowBeforeB;
    private int amountInStock = SimulationConstants.START_LOAD_B;

    private boolean machineA = false;
    private boolean machineB = false;

    private boolean machineAWaiting = false;
    private boolean machineBWaiting = false;

    private boolean machineBBought = false;

    private boolean machinesSleep = false;

    private Vehicle vehInB = null;
    private Vehicle vehInA = null;

    private double sleepTimeMachineA = 0;
    private double sleepTimeMachineB = 0;

    private double sleepTimeAll = 0;

    private double machineAStatVar = -1;
    private double machineBStatVar = -1;

    private double machineAllStatVar = -1;

    private double[] statResult;
    private double timeOfLastStatCompute = -1;

    public ManagerBPoint(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        rowBeforeB = new Row();
        ((MySimulation) mySim()).getStats().addRowB(rowBeforeB);

        amountInStock = SimulationConstants.START_LOAD_B;

        machineA = false;
        machineB = false;

        machineAWaiting = false;
        machineBWaiting = false;

        machineBBought = ((MySimulation) mySim()).getBuyUnloader();

        machinesSleep = false;

        vehInB = null;
        vehInA = null;

        sleepTimeMachineA = 0;
        sleepTimeMachineB = 0;

        sleepTimeAll = 0;

        machineAStatVar = -1;
        machineBStatVar = -1;

        machineAllStatVar = -1;

        statResult = null;
        timeOfLastStatCompute = -1;

        if (petriNet() != null) {
            petriNet().clear();
        }

        if (SimulationConstants.START_TIME < SimulationConstants.UNLOAD_MACHINE1_START_TIME
                || SimulationConstants.START_TIME >= SimulationConstants.UNLOAD_MACHINE1_END_TIME) {
            machinesSleep = true;
            machineBStatVar = 0;
            machineAStatVar = 0;
        }

        machineBBought = ((MySimulation) mySim()).getBuyUnloader();
        ((MySimulation) mySim()).sendStorageBInformation(amountInStock);
        ((MySimulation) mySim()).getStats().addPointBUsage(new MachineUsage(this));

        if (!machineBBought) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.turnOff);
        } else {
            ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.dontWorking);
        }
        ((MySimulation) mySim()).updateMachineStatus("unloaderA", MachineState.dontWorking);
    }

    //meta! sender="AgentDopravy", id="124", type="Notice"
    public void processExportResource(MessageForm message) {
        double amount = ((ResourceMessage) message).getAmount();
        storageChange();
        if (amount >= amountInStock) {
            ((MySimulation) mySim()).getStats().addResourcesOutcome(amountInStock);
            amountInStock = 0;
        } else {
            amountInStock -= amount;
            ((MySimulation) mySim()).getStats().addResourcesOutcome((long) amount);
            ((MySimulation) mySim()).getStats().addSuccesTakeOffStat(1);
        }
        
        if(((MySimulation) mySim()).getCompanyTurnOff() && mySim().currentTime() > 
                SimulationConstants.START_TIME+SimulationConstants.COMPANY_A_TURN_OFF && amountInStock==0){
            ((MySimulation)mySim()).getStats().addTimeAfterTurnOff(mySim().currentTime()-
                    (SimulationConstants.START_TIME+SimulationConstants.COMPANY_A_TURN_OFF));
        }
        
        ((MySimulation) mySim()).sendStorageBInformation(amountInStock);

        endOfWaiting(message);
    }

    //meta! sender="ProcesVykladacA", id="67", type="Finish"
    public void processFinishProcesUnloaderA(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();
        if (temp.getMomentalFilled() == 0) {
            response(temp.getReqest());
            vehInA = null;

            if (rowBeforeB.size() == 0) {
                machineA = false;
                return;
            }

            if (getFreeSpace() > 0 && !machinesSleep) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processUnloaderA);
                notice(msg);
                vehInA = msg.getVehicle();
                return;
            } else {
                machineA = false;
                if (!machinesSleep) {
                    machineAWaiting = true;
                } else {
                    machineAStatVar = mySim().currentTime();
                }
            }
        } else {
            if (getFreeSpace() > 0) {
                VehicleMessage msg = new VehicleMessage((MyMessage) message);
                msg.setResources(getResources(vehInA));
                msg.setAddressee(Id.processUnloaderA);
                msg.setCode(Mc.start);
                msg.resetVehicles();
                notice(msg);
                return;
            } else {
                if (machinesSleep) {
                    machineAStatVar = mySim().currentTime();
                }
                machineAWaiting = true;
            }
        }
    }

    //meta! sender="ProcesVykladacB", id="72", type="Finish"
    public void processFinishProcesUnloaderB(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();
        if (temp.getMomentalFilled() == 0) {
            response(temp.getReqest());
            vehInB = null;

            if (rowBeforeB.size() == 0) {
                machineB = false;
                return;
            }

            if (getFreeSpace() > 0 && !machinesSleep) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processUnloaderB);
                notice(msg);
                vehInB = msg.getVehicle();
                return;
            } else {
                machineB = false;
                if (!machinesSleep) {
                    machineBWaiting = true;
                } else {
                    machineBStatVar = mySim().currentTime();
                }
            }
        } else {
            if (getFreeSpace() > 0) {
                VehicleMessage msg = new VehicleMessage((MyMessage) message);
                msg.setResources(getResources(vehInB));
                msg.setAddressee(Id.processUnloaderB);
                msg.setCode(Mc.start);
                msg.resetVehicles();
                notice(msg);
                return;
            } else {
                if (machinesSleep) {
                    machineBStatVar = mySim().currentTime();
                }
                machineBWaiting = true;
            }
        }
    }

    //meta! sender="AgentDopravy", id="37", type="Request"
    public void processUnload(MessageForm message) {
        for (Vehicle vehicle : ((VehicleMessage) message).getVehicles()) {
            rowBeforeB.addVehicle(vehicle, mySim().currentTime());

            vehicle.setRequest(new VehicleMessage((MyMessage) message.createCopy()));

            if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
                System.out.println(vehicle.getName() + "- rad B v case " + mySim().currentTime());
            }

            if (machinesSleep) {
                vehicle.pauseCycle(mySim().currentTime());
            } else {
                vehicle.startActionRow(mySim().currentTime());
            }

            ((MySimulation) mySim()).getStats().addAvgLoad(vehicle.getMomentalFilled());
            ((MySimulation) mySim()).sendCarInformation(vehicle.getName(), CarPossition.radB, -1);
        }

        if (machineBBought) {
            unloaderBStart(message);
        }
        unloaderAStart(message);
    }

    public void sleepChangeA(MessageForm message) {
        machinesSleep = !machinesSleep;
        if (!machinesSleep) {
            turnOnStats();
            endOfWaiting(message);
            if (!machineAWaiting) {
                unloaderAStart(message);
            }
            if (!machineBWaiting) {
                unloaderBStart(message);
            }
            if (machineBBought) {
                sleepTimeMachineB += mySim().currentTime() - machineBStatVar;
                machineBStatVar = -1;
            }
            sleepTimeMachineA += mySim().currentTime() - machineAStatVar;
            machineAStatVar = -1;
        } else {
            turnOffStats();
            endOfWaiting(message);
            machineBStatVar = mySim().currentTime();
            machineAStatVar = mySim().currentTime();
        }
    }

    private void processInitialisation(MessageForm message) {
        MyMessage msg = new MyMessage(mySim());
        msg.setCode(Mc.start);
        msg.setAddressee(Id.timeSchedulerB);
        notice(msg);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {

            case Mc.initialisation:
                processInitialisation(message);
                break;
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.exportResource:
                processExportResource(message);
                break;

            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.processUnloaderA:
                        processFinishProcesUnloaderA(message);
                        break;

                    case Id.processUnloaderB:
                        processFinishProcesUnloaderB(message);
                        break;

                    case Id.timeSchedulerB:
                        sleepChangeA(message);
                        break;
                }
                break;

            case Mc.unload:
                processUnload(message);
                break;

            default:
                processDefault(message);
                break;
        }
        sendMachineAState();
        sendMachineBState();
    }
    //meta! tag="end"

    @Override
    public AgentBPoint myAgent() {
        return (AgentBPoint) super.myAgent();
    }

    public int getFreeSpace() {
        return SimulationConstants.CAPACITY_STOCK - amountInStock;
    }

    private void unloaderAStart(MessageForm message) {
        if (!machineA && rowBeforeB.size() > 0 && !machinesSleep) {
            if (amountInStock < SimulationConstants.CAPACITY_STOCK) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processUnloaderA);
                notice(msg);
                vehInA = msg.getVehicle();
                machineA = true;
            } else {
                machineAWaiting = true;
            }
        }
    }

    private void unloaderBStart(MessageForm message) {
        if (!machineB && rowBeforeB.size() > 0 && !machinesSleep) {
            if (amountInStock < SimulationConstants.CAPACITY_STOCK) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processUnloaderB);
                notice(msg);
                vehInB = msg.getVehicle();
                machineB = true;
            } else {
                machineBWaiting = true;
            }
        }
    }

    private void endOfWaiting(MessageForm message) {
        if (!machinesSleep) {
            if (machineAWaiting) {
                if (machineA) {
                    VehicleMessage msg = new VehicleMessage((MyMessage) message);
                    msg.setResources(getResources(vehInA));
                    msg.setAddressee(Id.processUnloaderA);
                    msg.setCode(Mc.start);
                    msg.resetVehicles();
                    startContinualAssistant(msg);
                } else {
                    unloaderAStart(message);
                }
                machineAWaiting = false;
            }
            if (machineBWaiting) {
                if (machineB) {
                    VehicleMessage msg = new VehicleMessage((MyMessage) message);
                    msg.setResources(getResources(vehInB));
                    msg.setAddressee(Id.processUnloaderB);
                    msg.setCode(Mc.start);
                    msg.resetVehicles();
                    startContinualAssistant(msg);
                } else {
                    unloaderBStart(message);
                }
                machineBWaiting = false;
            }
        }
    }

    private int getResources(Vehicle paVehicle) {
        int min = 0;
        if (paVehicle.getMomentalFilled() > getFreeSpace()) {
            min = getFreeSpace();
        } else {
            min = paVehicle.getMomentalFilled();
        }

        storageChange();
        amountInStock += min;

        ((MySimulation) mySim()).getStats().addResourcesToB(min);

        ((MySimulation) mySim()).sendStorageBInformation(amountInStock);

        return min;
    }

    private VehicleMessage createMessageFirstInRow(MessageForm message, int processId) {
        VehicleMessage msg = new VehicleMessage((MyMessage) message);
        msg.addVehicle(rowBeforeB.getFirstVehicle(mySim().currentTime()));
        ((MySimulation) mySim()).getStats().addRowB(msg.getVehicle().endOfActionRow(mySim().currentTime()));
        msg.setResources(getResources(msg.getVehicle()));
        msg.setAddressee(processId);
        msg.setCode(Mc.start);

        return msg;
    }

    private void sendMachineAState() {
        if (machineA && !machineAWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderA", MachineState.working);
            return;
        }
        if (machinesSleep) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderA", MachineState.sleeping);
            return;
        }
        if (machineAWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderA", MachineState.waiting);
            return;
        }
        ((MySimulation) mySim()).updateMachineStatus("unloaderA", MachineState.dontWorking);
    }

    private void sendMachineBState() {
        if (!machineBBought) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.turnOff);
            return;
        }
        if (machineB && !machineBWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.working);
            return;
        }
        if (machinesSleep) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.sleeping);
            return;
        }
        if (machineBWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.waiting);
            return;
        }
        ((MySimulation) mySim()).updateMachineStatus("unloaderB", MachineState.dontWorking);
    }

    @Override
    public double[] getSleepTime() {
        if (timeOfLastStatCompute != mySim().currentTime()) {
            double[] temp = new double[3];

            if (machineAStatVar == -1) {
                temp[0] = sleepTimeMachineA;
            } else {
                sleepTimeMachineA += mySim().currentTime() - machineAStatVar;
                machineAStatVar = mySim().currentTime();
                temp[0] = sleepTimeMachineA;
            }

            if (machineBBought) {
                if (machineBStatVar == -1) {
                    temp[1] = sleepTimeMachineB;
                } else {
                    sleepTimeMachineB += mySim().currentTime() - machineBStatVar;
                    machineBStatVar = mySim().currentTime();
                    temp[1] = sleepTimeMachineB;
                }
            } else {
                temp[1] = -1;
            }

            if (machineAllStatVar == -1) {
                temp[2] = sleepTimeAll;
            } else {
                sleepTimeAll += mySim().currentTime() - machineAllStatVar;
                machineAllStatVar = mySim().currentTime();
                temp[2] = sleepTimeAll;
            }

            statResult = temp;
            timeOfLastStatCompute = mySim().currentTime();
        }
        return statResult;
    }

    private void turnOffStats() {
        if (vehInA != null) {
            vehInA.pauseCycle(mySim().currentTime());
        }
        if (vehInB != null) {
            vehInB.pauseCycle(mySim().currentTime());
        }
        rowBeforeB.stopRow(mySim().currentTime());

        machineAllStatVar = mySim().currentTime();
    }

    private void turnOnStats() {
        if (vehInA != null) {
            vehInA.startActionCycle(mySim().currentTime());
        }
        if (vehInB != null) {
            vehInB.startActionCycle(mySim().currentTime());
        }
        rowBeforeB.startRow(mySim().currentTime());

        getSleepTime();
        machineAllStatVar = -1;
    }

    private void storageChange() {
        ((MySimulation) mySim()).getStats().addStorageB(mySim().currentTime(), amountInStock);
    }
}
