package managers;

import OSPABA.*;
import agents.*;
import Simulation.VehicleMessage;
import Simulation.Id;
import Simulation.Mc;

//meta! id="232"
public class ManagerWays extends Manager {

    public ManagerWays(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="AgentTraffic", id="236", type="Notice"
    public void processMove(MessageForm message) {
        switch (((VehicleMessage) message).getResources()) {
            case 1:                                         //to B
                message.setAddressee(Id.processWayAB);
                message.setCode(Mc.start);
                notice(message);
                break;
            case 2:                                         //ToC
                message.setAddressee(Id.processWayBC);
                message.setCode(Mc.start);
                notice(message);
                break;
        }
    }

    //meta! sender="ProcessWayCA", id="244", type="Finish"
    public void processFinishProcessWayCA(MessageForm message) {
        ((VehicleMessage) message).setResources(2);
        message.setAddressee(Id.agentTraffic);
        message.setCode(Mc.endMove);
        notice(message);
    }

    //meta! sender="ProcessWayAB", id="240", type="Finish"
    public void processFinishProcessWayAB(MessageForm message) {
        ((VehicleMessage) message).setResources(1);
        message.setAddressee(Id.agentTraffic);
        message.setCode(Mc.endMove);
        notice(message);
    }

    //meta! sender="ProcessWayBC", id="242", type="Finish"
    public void processFinishProcessWayBC(MessageForm message) {
        message.setAddressee(Id.processWayCA);
        message.setCode(Mc.start);
        notice(message);
    }
    
    public void init(MessageForm message){
        message.setAddressee(Id.processWayBC);
        message.setCode(Mc.initialisation);
        notice(message);
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
            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.processWayCA:
                        processFinishProcessWayCA(message);
                        break;

                    case Id.processWayAB:
                        processFinishProcessWayAB(message);
                        break;

                    case Id.processWayBC:
                        processFinishProcessWayBC(message);
                        break;
                }
                break;

            case Mc.move:
                processMove(message);
                break;

            case Mc.initialisation:
                init(message);
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
