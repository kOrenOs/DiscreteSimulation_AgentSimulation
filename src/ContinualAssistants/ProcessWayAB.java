package ContinualAssistants;

import Simulation.Mc;
import Entity.NarrowWay;
import Constants.SimulationConstants;
import Entity.CarPossition;
import OSPABA.*;
import OSPABA.Process;
import Simulation.Id;
import Simulation.MySimulation;
import Simulation.VehicleMessage;
import agents.AgentWays;


//meta! id="107"
public class ProcessWayAB extends Process {

    private NarrowWay wayAB;

    public ProcessWayAB(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        wayAB = new NarrowWay(SimulationConstants.WAY_AB_DISTANCE);
    }

    //meta! sender="AgentDopravy", id="108", type="Start"
    public void processStart(MessageForm message) {
        double result = wayAB.addCar(((VehicleMessage) message).getVehicle(), mySim().currentTime());

        if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
            System.out.println(((VehicleMessage) message).getVehicle().getName() + "- cesta A-B v case " + mySim().currentTime());
        }

        if (result != -1) {
            ((MySimulation) mySim()).sendCarInformation(((VehicleMessage) message).getVehicle().getName(), CarPossition.cestaAB, result);
        } else {
            ((MySimulation) mySim()).sendCarInformation(((VehicleMessage) message).getVehicle().getName(), CarPossition.cestaAB,
                    wayAB.getTimeOfLastBunch() - mySim().currentTime());
        }

        if (result != -1) {
            message.setCode(Mc.done);
            message.setAddressee(this);
            hold(result, message);
        }
    }

    public void processDone(MessageForm message) {
        VehicleMessage msg = ((VehicleMessage) message);
        msg.resetVehicles();
        msg.addVehicles(wayAB.getBunch());
        msg.setAddressee(Id.agentTraffic);
        assistantFinished(msg);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.done:
                processDone(message);
                break;
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
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
}
