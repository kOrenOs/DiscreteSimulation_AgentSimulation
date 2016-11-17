package Managers;

import Simulation.Id;
import Simulation.Mc;
import Simulation.VehicleMessage;
import Agents.AgentTraffic;
import Entity.Vehicle;
import OSPABA.*;
import Simulation.MyMessage;
import java.util.ArrayList;

//meta! id="19"
public class ManagerTraffic extends Manager {

    private ArrayList<Vehicle> vehicleList;

    public ManagerTraffic(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        vehicleList = new ArrayList<Vehicle>();

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="AgentModelu", id="121", type="Notice"
    public void processExportResource(MessageForm message) {
        message.setAddressee(Id.agentBPoint);
        notice(message);
    }

    //meta! sender="AgentModelu", id="122", type="Notice"
    public void processImportResource(MessageForm message) {
        message.setAddressee(Id.agentAPoint);
        notice(message);
    }

    //meta! sender="AgentModelu", id="91", type="Call"
    public void processInitialisation(MessageForm message) {
        vehicleList.addAll(((VehicleMessage) message).getVehicles());

        MyMessage msg = new MyMessage(mySim());
        msg.setCode(Mc.initialisation);
        msg.setAddressee(Id.agentAPoint);
        notice(msg);

        MyMessage msg1 = new MyMessage(mySim());
        msg1.setCode(Mc.initialisation);
        msg1.setAddressee(Id.agentBPoint);
        notice(msg1);

        message.setAddressee(Id.agentAPoint);
        message.setCode(Mc.load);
        request(message);
        
        MyMessage msg3 = new MyMessage(mySim());
        msg3.setCode(Mc.initialisation);
        msg3.setAddressee(Id.agentWays);
        notice(msg3);
    }

    //meta! sender="AgentSkladky", id="36", type="Response"
    public void processLoad(MessageForm message) {
        ((VehicleMessage) message).setResources(1);
        message.setAddressee(Id.agentWays);
        message.setCode(Mc.move);
        notice(message);
    }

    //meta! sender="AgentStavby", id="37", type="Response"
    public void processUnload(MessageForm message) {
        ((VehicleMessage) message).setResources(2);
        message.setAddressee(Id.agentWays);
        message.setCode(Mc.move);
        notice(message);
    }

    public void processEndMove(MessageForm message) {
        switch (((VehicleMessage) message).getResources()) {
            case 1:                                         //to B
                message.setAddressee(Id.agentBPoint);
                message.setCode(Mc.unload);
                request(message);
                break;
            case 2:                                         //ToC
                message.setAddressee(Id.agentAPoint);
                message.setCode(Mc.load);
                request(message);
                break;
        }
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
            case Mc.importResource:
                processImportResource(message);
                break;

            case Mc.load:
                processLoad(message);
                break;

            case Mc.initialisation:
                processInitialisation(message);
                break;

            case Mc.unload:
                processUnload(message);
                break;

            case Mc.exportResource:
                processExportResource(message);
                break;

            case Mc.endMove:
                processEndMove(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentTraffic myAgent() {
        return (AgentTraffic) super.myAgent();
    }

}
