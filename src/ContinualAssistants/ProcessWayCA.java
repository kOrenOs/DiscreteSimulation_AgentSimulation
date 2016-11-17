package ContinualAssistants;

import Simulation.Mc;
import Constants.SimulationConstants;
import Entity.CarPossition;
import Entity.NarrowWay;
import OSPABA.*;
import OSPABA.Process;
import Simulation.Id;
import Simulation.MySimulation;
import Simulation.VehicleMessage;
import agents.AgentWays;

//meta! id="105"
public class ProcessWayCA extends Process {

    private NarrowWay wayCA;

    public ProcessWayCA(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        wayCA = new NarrowWay(SimulationConstants.WAY_CA_DISTANCE);
    }

    //meta! sender="AgentDopravy", id="106", type="Start"
    public void processStart(MessageForm message) {
        double result = wayCA.addCar(((VehicleMessage) message).getVehicle(), mySim().currentTime());

        if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
            System.out.println(((VehicleMessage) message).getVehicle().getName() + "- cesta C-A v case " + mySim().currentTime());
        }

        if (result != -1) {
            ((MySimulation) mySim()).sendCarInformation(((VehicleMessage) message).getVehicle().getName(), CarPossition.cestaCA, result);
        } else {
            ((MySimulation) mySim()).sendCarInformation(((VehicleMessage) message).getVehicle().getName(), CarPossition.cestaCA,
                    wayCA.getTimeOfLastBunch() - mySim().currentTime());
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
        msg.addVehicles(wayCA.getBunch());
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
