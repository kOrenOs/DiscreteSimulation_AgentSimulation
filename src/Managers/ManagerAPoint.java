package Managers;

import Simulation.Id;
import Simulation.Mc;
import Simulation.MyMessage;
import Simulation.MySimulation;
import Simulation.VehicleMessage;
import Simulation.ResourceMessage;
import Entity.Row;
import Entity.Vehicle;
import Entity.MachineUsage;
import Agents.AgentAPoint;
import Constants.SimulationConstants;
import Entity.CarPossition;
import Entity.MachineState;
import Entity.StatInterface;
import OSPABA.*;

//meta! id="17"
public class ManagerAPoint extends Manager implements StatInterface {

    private Row rowBeforeA;
    private int amountInStock = SimulationConstants.START_LOAD_A;

    private boolean machineA = false;
    private boolean machineB = false;

    private boolean machineAWaiting = false;
    private boolean machineBWaiting = false;

    private boolean machineASleep = false;
    private boolean machineBSleep = false;

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

    private boolean vehicleFull = false;

    public ManagerAPoint(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();

    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        rowBeforeA = new Row();
        ((MySimulation) mySim()).getStats().addRowA(rowBeforeA);

        amountInStock = SimulationConstants.START_LOAD_A;
        storageChange();

        machineA = false;
        machineB = false;

        machineAWaiting = false;
        machineBWaiting = false;

        machineASleep = false;
        machineBSleep = false;

        vehInB = null;
        vehInA = null;

        vehicleFull = ((MySimulation) mySim()).getLoadingStyle();

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

        if (SimulationConstants.START_TIME < SimulationConstants.LOAD_MACHINE1_START_TIME
                || SimulationConstants.START_TIME >= SimulationConstants.LOAD_MACHINE1_END_TIME) {
            machineASleep = true;
            machineAStatVar = 0;
        }

        if (SimulationConstants.START_TIME < SimulationConstants.LOAD_MACHINE2_START_TIME
                || SimulationConstants.START_TIME >= SimulationConstants.LOAD_MACHINE2_END_TIME) {
            machineBSleep = true;
            machineBStatVar = 0;
        }

        ((MySimulation) mySim()).sendStorageAInformation(amountInStock);
        ((MySimulation) mySim()).getStats().addPointAUsage(new MachineUsage(this));
    }

    //meta! sender="AgentDopravy", id="123", type="Notice"
    public void processImportResource(MessageForm message) {
        storageChange();
        amountInStock += ((ResourceMessage) message).getAmount();

        ((MySimulation) mySim()).getStats().addResourcesToA((long) ((ResourceMessage) message).getAmount());

        ((MySimulation) mySim()).sendStorageAInformation(amountInStock);

        endOfWaiting(message);
    }

    //meta! sender="ProcesNakladacB", id="70", type="Finish"
    public void processFinishProcessLoaderB(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();
        if (temp.getMomentalFilled() == temp.getCapacity() || !vehicleFull) {
            response(temp.getReqest());
            vehInB = null;

            if (rowBeforeA.size() == 0) {
                machineB = false;
                return;
            }

            if (amountInStock > 0 && !machineBSleep) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processLoaderB);
                notice(msg);
                vehInB = msg.getVehicle();
                return;
            } else {
                machineB = false;
                if (!machineBSleep) {
                    machineBWaiting = true;
                } else {
                    machineBStatVar = mySim().currentTime();
                }
            }
        } else {
            if (amountInStock > 0) {
                VehicleMessage msg = new VehicleMessage((MyMessage) message);
                msg.setResources(getResources(vehInB));
                msg.setAddressee(Id.processLoaderB);
                msg.setCode(Mc.start);
                msg.resetVehicles();
                notice(msg);
                return;
            } else {
                if (machineBSleep) {
                    response(temp.getReqest());
                    vehInB = null;
                    machineBStatVar = mySim().currentTime();
                    machineB = false;
                } else {
                    machineBWaiting = true;
                }
            }
        }
    }

    //meta! sender="ProcesNakladacA", id="64", type="Finish"
    public void processFinishProcessLoaderA(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();
        if (temp.getMomentalFilled() == temp.getCapacity() || !vehicleFull) {
            response(temp.getReqest());
            vehInA = null;

            if (rowBeforeA.size() == 0) {
                machineA = false;
                return;
            }

            if (amountInStock > 0 && !machineASleep) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processLoaderA);
                notice(msg);
                vehInA = msg.getVehicle();
                return;
            } else {
                machineA = false;
                if (!machineASleep) {
                    machineAWaiting = true;
                } else {
                    machineAStatVar = mySim().currentTime();
                }
            }
        } else {
            if (amountInStock > 0) {
                VehicleMessage msg = new VehicleMessage((MyMessage) message);
                msg.setResources(getResources(vehInA));
                msg.setAddressee(Id.processLoaderA);
                msg.setCode(Mc.start);
                msg.resetVehicles();
                notice(msg);
                return;
            } else {
                if (machineASleep) {
                    response(temp.getReqest());
                    vehInA = null;
                    machineAStatVar = mySim().currentTime();
                    machineA = false;
                } else {
                    machineAWaiting = true;
                }
            }
        }
    }

    //meta! sender="AgentDopravy", id="36", type="Request"
    public void processLoad(MessageForm message) {
        for (Vehicle vehicle : ((VehicleMessage) message).getVehicles()) {
            rowBeforeA.addVehicle(vehicle, mySim().currentTime());
            vehicle.setRequest(new VehicleMessage((MyMessage) message.createCopy()));

            if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
                System.out.println(vehicle.getName() + "- rad A v case " + mySim().currentTime());
            }

            double timeOfCycle = vehicle.endOfActionCycle(mySim().currentTime());
            if (timeOfCycle != 0) {
                ((MySimulation) mySim()).getStats().addAvgDurationOfOneCycleStat(timeOfCycle);
            }

            if (!machineASleep || !machineBSleep) {
                vehicle.startActionCycle(mySim().currentTime());
                vehicle.startActionRow(mySim().currentTime());
            }

            ((MySimulation) mySim()).sendCarInformation(vehicle.getName(), CarPossition.radA, -1);
        }

        loaderBStart(message);
        loaderAStart(message);
    }

    public void sleepChangeA(MessageForm message) {
        machineASleep = !machineASleep;
        if (!machineASleep) {
            if (machineBSleep) {
                turnOnStats();
            }
            loaderAStart(message);
            sleepTimeMachineA += mySim().currentTime() - machineAStatVar;
            machineAStatVar = -1;
        } else {
            endOfWaiting(message);
            machineAStatVar = mySim().currentTime();
            if (machineASleep && machineBSleep) {
                turnOffStats();
            }
        }
    }

    public void sleepChangeB(MessageForm message) {
        machineBSleep = !machineBSleep;
        if (!machineBSleep) {
            if (machineASleep) {
                turnOnStats();
            }
            loaderBStart(message);
            sleepTimeMachineB += mySim().currentTime() - machineBStatVar;
            machineBStatVar = -1;
        } else {
            endOfWaiting(message);
            machineBStatVar = mySim().currentTime();
            if (machineASleep && machineBSleep) {
                turnOffStats();
            }
        }
    }

    private void processInitialisation(MessageForm message) {
        MyMessage msg1 = new MyMessage(mySim());
        msg1.setCode(Mc.start);
        msg1.setAddressee(Id.timeSchedulerA);
        notice(msg1);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.load:
                processLoad(message);
                break;

            case Mc.initialisation:
                processInitialisation(message);
                break;

            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.processLoaderB:
                        processFinishProcessLoaderB(message);
                        break;

                    case Id.processLoaderA:
                        processFinishProcessLoaderA(message);
                        break;

                    case Id.timeSchedulerA:
                        switch (((MyMessage) message).getMsg()) {
                            case "A":
                                sleepChangeA(message);
                                break;
                            case "B":
                                sleepChangeB(message);
                                break;
                        }
                        break;
                }
                break;

            case Mc.importResource:
                processImportResource(message);
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
    public AgentAPoint myAgent() {
        return (AgentAPoint) super.myAgent();
    }

    private void endOfWaiting(MessageForm message) {
        if (machineAWaiting) {
            if (machineA) {
                VehicleMessage msg = new VehicleMessage((MyMessage) message);
                msg.setResources(getResources(vehInA));
                msg.setAddressee(Id.processLoaderA);
                msg.setCode(Mc.start);
                msg.resetVehicles();
                startContinualAssistant(msg);
            } else {
                loaderAStart(message);
            }
            machineAWaiting = false;
        }
        if (machineBWaiting) {
            if (machineB) {
                VehicleMessage msg = new VehicleMessage((MyMessage) message);
                msg.setResources(getResources(vehInB));
                msg.setAddressee(Id.processLoaderB);
                msg.setCode(Mc.start);
                msg.resetVehicles();
                startContinualAssistant(msg);
            } else {
                loaderBStart(message);
            }
            machineBWaiting = false;
        }
    }

    private void loaderAStart(MessageForm message) {
        if (!machineA && rowBeforeA.size() > 0 && !machineASleep) {
            if (amountInStock > 0) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processLoaderA);
                notice(msg);
                vehInA = msg.getVehicle();
                machineA = true;
            } else {
                machineAWaiting = true;
            }
        }
    }

    private void loaderBStart(MessageForm message) {
        if (!machineB && rowBeforeA.size() > 0 && !machineBSleep) {
            if (amountInStock > 0) {
                VehicleMessage msg = createMessageFirstInRow(message, Id.processLoaderB);
                notice(msg);
                vehInB = msg.getVehicle();
                machineB = true;
            } else {
                machineBWaiting = true;
            }
        }
    }

    private int getResources(Vehicle paVehicle) {
        int min = 0;
        if (paVehicle.getCapacity() - paVehicle.getMomentalFilled() > amountInStock) {
            min = amountInStock;
        } else {
            min = paVehicle.getCapacity() - paVehicle.getMomentalFilled();
        }
        
        storageChange();
        amountInStock -= min;     

        ((MySimulation) mySim()).sendStorageAInformation(amountInStock);

        return min;
    }

    private VehicleMessage createMessageFirstInRow(MessageForm message, int processId) {
        VehicleMessage msg = new VehicleMessage((MyMessage) message);
        msg.addVehicle(rowBeforeA.getFirstVehicle(mySim().currentTime()));
        ((MySimulation) mySim()).getStats().addRowA(msg.getVehicle().endOfActionRow(mySim().currentTime()));
        msg.setResources(getResources(msg.getVehicle()));
        msg.setAddressee(processId);
        msg.setCode(Mc.start);

        return msg;
    }

    private void sendMachineAState() {
        if (machineA && !machineAWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("loaderA", MachineState.working);
            return;
        }
        if (machineASleep) {
            ((MySimulation) mySim()).updateMachineStatus("loaderA", MachineState.sleeping);
            return;
        }
        if (machineAWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("loaderA", MachineState.waiting);
            return;
        }
        ((MySimulation) mySim()).updateMachineStatus("loaderA", MachineState.dontWorking);
    }

    private void sendMachineBState() {
        if (machineB && !machineBWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("loaderB", MachineState.working);
            return;
        }
        if (machineBSleep) {
            ((MySimulation) mySim()).updateMachineStatus("loaderB", MachineState.sleeping);
            return;
        }
        if (machineBWaiting) {
            ((MySimulation) mySim()).updateMachineStatus("loaderB", MachineState.waiting);
            return;
        }
        ((MySimulation) mySim()).updateMachineStatus("loaderB", MachineState.dontWorking);
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

            if (machineBStatVar == -1) {
                temp[1] = sleepTimeMachineB;
            } else {
                sleepTimeMachineB += mySim().currentTime() - machineBStatVar;
                machineBStatVar = mySim().currentTime();
                temp[1] = sleepTimeMachineB;
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
        rowBeforeA.stopRow(mySim().currentTime());
        machineAllStatVar = mySim().currentTime();
    }

    private void turnOnStats() {
        rowBeforeA.startRow(mySim().currentTime());
        getSleepTime();
        machineAllStatVar = -1;
    }

    private void storageChange() {
        ((MySimulation) mySim()).getStats().addStorageA(mySim().currentTime(), amountInStock);
    }
}
