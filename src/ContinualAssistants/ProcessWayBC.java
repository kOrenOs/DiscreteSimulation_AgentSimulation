package ContinualAssistants;

import Simulation.Mc;
import Constants.SimulationConstants;
import Entity.CarPossition;
import Entity.Vehicle;
import OSPABA.*;
import Simulation.Id;
import Simulation.MySimulation;
import Simulation.VehicleMessage;
import agents.AgentWays;

//meta! id="105"
public class ProcessWayBC extends OSPABA.Process {

    private int wayBCantDistance = 15;
    private int wayCantCDistance = 15;
    private boolean canteeneOn = false;

    public ProcessWayBC(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

    }

    //meta! sender="AgentDopravy", id="106", type="Start"
    public void processStart(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();
        double timeOfTravel = wayBCantDistance / temp.getSpeed();
        if (temp.vehicleFail()) {
            timeOfTravel += temp.getRepairTime();
            ((MySimulation) mySim()).getStats().addFailureOfCarsStat(temp.getRepairTime());
            ((MySimulation) mySim()).sendCarInformation(temp.getName(), CarPossition.cestaBCanteenaFail, timeOfTravel);
        } else {
            ((MySimulation) mySim()).sendCarInformation(temp.getName(), CarPossition.cestaBCanteena, timeOfTravel);
        }

        if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
            System.out.println(temp.getName() + "- cesta B-C v case " + mySim().currentTime());
        }

        message.setCode(Mc.canteena);
        message.setAddressee(this);
        hold(timeOfTravel, message);
    }

    public void caneetna(MessageForm message) {
        if (bolTuDnes(message) || !canteeneOn) {
            message.setCode(Mc.poCanteena);
            message.setAddressee(this);
            notice(message);
        } else {
            Vehicle temp = ((VehicleMessage) message).getVehicle();
            temp.setLastCanteen(mySim().currentTime());

            ((MySimulation) mySim()).sendCarInformation(temp.getName(), CarPossition.Canteena, 25);
            message.setCode(Mc.poCanteena);
            message.setAddressee(this);
            hold(25, message);
        }
    }

    public void poCanteena(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();
        double timeOfTravel = wayCantCDistance / temp.getSpeed();
        if (temp.vehicleFail()) {
            timeOfTravel += temp.getRepairTime();
            ((MySimulation) mySim()).getStats().addFailureOfCarsStat(temp.getRepairTime());
            ((MySimulation) mySim()).sendCarInformation(temp.getName(), CarPossition.cestaCanceenaCFail, timeOfTravel);
        } else {
            ((MySimulation) mySim()).sendCarInformation(temp.getName(), CarPossition.cestaCanceenaC, timeOfTravel);
        }

        if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
            System.out.println(temp.getName() + "- cesta B-C v case " + mySim().currentTime());
        }

        message.setCode(Mc.done);
        message.setAddressee(this);
        hold(timeOfTravel, message);
    }

    public void processDone(MessageForm message) {

        message.setAddressee(Id.agentTraffic);
        assistantFinished(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.done:
                processDone(message);
                break;

            case Mc.canteena:
                caneetna(message);
                break;

            case Mc.poCanteena:
                poCanteena(message);
                break;
        }
    }

    public void zmena(MessageForm message) {
        sendRepeteAndResource(message);
    }

    public void init(MessageForm message) {
        double cas = 11 * 60 + 30 - SimulationConstants.START_TIME + mySim().currentTime();
        message.setCode(Mc.zmena);
        hold(cas, message);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;

            case Mc.initialisation:
                init(message);
                break;

            case Mc.zmena:
                zmena(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentWays myAgent() {
        return (AgentWays) super.myAgent();
    }

    private void sendRepeteAndResource(MessageForm paMessage) {
        if (SimulationConstants.START_TIME + mySim().currentTime() <= 11 * 60 + 30) {
            paMessage.setCode(Mc.zmena);
            hold(690, paMessage);
            canteeneOn = true;
        } else {
            paMessage.setCode(Mc.zmena);
            hold(750, paMessage);
            canteeneOn = false;
        }
    }

    private boolean bolTuDnes(MessageForm message) {
        Vehicle temp = ((VehicleMessage) message).getVehicle();

        double cas = temp.getCanteen();

        int day = (int) Math.floor(SimulationConstants.START_TIME + mySim().currentTime()) / (24 * 60);
        int lastDay = (int) Math.floor(SimulationConstants.START_TIME + cas) / (24 * 60);
        if (day != lastDay || cas == 0) {
            return false;
        } else {
            return true;
        }
    }
}
