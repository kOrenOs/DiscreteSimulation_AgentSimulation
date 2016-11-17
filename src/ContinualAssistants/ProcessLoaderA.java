package ContinualAssistants;

import Simulation.Mc;
import Simulation.VehicleMessage;
import Simulation.MySimulation;
import Entity.Vehicle;
import Agents.AgentAPoint;
import Constants.SimulationConstants;
import Entity.CarPossition;
import OSPABA.*;
import OSPABA.Process;
import Simulation.MyMessage;

//meta! id="63"
public class ProcessLoaderA extends Process {

    private Vehicle actualVehicle = null;
    private Vehicle lastVehicle = null;

    public ProcessLoaderA(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        actualVehicle = null;
        lastVehicle = null;
    }

    //meta! sender="AgentSkladky", id="64", type="Start"
    public void processStart(MessageForm message) {
        if (((VehicleMessage) message).getVehicle() != null) {
            actualVehicle = ((VehicleMessage) message).getVehicle();
        }

        if (actualVehicle == null) {
            actualVehicle = lastVehicle;
        }

        if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
            System.out.println(actualVehicle.getName() + "- nakladac A v case " + mySim().currentTime());
        }

        int resources = ((VehicleMessage) message).getResources();
        double timeOfAction = resources / SimulationConstants.LOAD_MACHINE1_SPEED;

        message.setCode(Mc.done);
        actualVehicle.setMomentalnFilled(actualVehicle.getMomentalFilled() + resources);
        hold(timeOfAction, message);

        ((MySimulation) mySim()).getStats().getPointAUsage().addSampleA(timeOfAction);

        ((MySimulation) mySim()).updateFilled(actualVehicle.getName(), actualVehicle.getMomentalFilled());
        ((MySimulation) mySim()).sendCarInformation(actualVehicle.getName(), CarPossition.nakladkaA, timeOfAction);
    }

    private void doneLoad(MessageForm message) {
        VehicleMessage mes = new VehicleMessage((MyMessage) message);
        mes.addVehicle(actualVehicle);
        assistantFinished(mes);
        lastVehicle = actualVehicle;
        actualVehicle = null;
        return;
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.done:
                doneLoad(message);
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
    public AgentAPoint myAgent() {
        return (AgentAPoint) super.myAgent();
    }

}
