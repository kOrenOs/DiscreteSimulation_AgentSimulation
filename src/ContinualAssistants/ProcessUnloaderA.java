package ContinualAssistants;

import Simulation.Mc;
import Agents.AgentBPoint;
import Constants.SimulationConstants;
import Entity.CarPossition;
import Entity.Vehicle;
import Entity.SchedulerTimer;
import Managers.ManagerBPoint;
import OSPABA.*;
import OSPABA.Process;
import Simulation.Id;
import Simulation.MyMessage;
import Simulation.MySimulation;
import Simulation.VehicleMessage;

//meta! id="66"
public class ProcessUnloaderA extends Process {

    private Vehicle actualVehicle = null;
    private Vehicle lastVehicle = null;

    public ProcessUnloaderA(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
        actualVehicle = null;
        lastVehicle = null;
    }

    //meta! sender="AgentStavby", id="67", type="Start"
    public void processStart(MessageForm message) {
        if (((VehicleMessage) message).getVehicle() != null) {
            actualVehicle = ((VehicleMessage) message).getVehicle();
        }

        if (actualVehicle == null) {
            actualVehicle = lastVehicle;
        }

        if (SimulationConstants.TURN_ON_POSSITION_NOTES) {
            System.out.println(actualVehicle.getName() + "- vykladac A v case " + mySim().currentTime());
        }

        int resources = ((VehicleMessage) message).getResources();
        double timeOfAction = resources / SimulationConstants.UNLOAD_MACHINE1_SPEED;

        message.setCode(Mc.done);
        actualVehicle.setMomentalnFilled(actualVehicle.getMomentalFilled() - resources);

        hold(timeOfAction, message);

        ((MySimulation) mySim()).getStats().getPointBUsage().addSampleA(timeOfAction);

        ((MySimulation) mySim()).updateFilled(actualVehicle.getName(), actualVehicle.getMomentalFilled());
        ((MySimulation) mySim()).sendCarInformation(actualVehicle.getName(), CarPossition.vykladkaA, timeOfAction);
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
    public AgentBPoint myAgent() {
        return (AgentBPoint) super.myAgent();
    }

}
