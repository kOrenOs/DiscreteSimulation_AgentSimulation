package Agents;

import Simulation.Id;
import Simulation.Mc;
import Managers.ManagerAPoint;
import ContinualAssistants.ProcessLoaderB;
import ContinualAssistants.ProcessLoaderA;
import OSPABA.*;
import continualAssistants.TimeSchedulerA;

//meta! id="17"
public class AgentAPoint extends Agent {

    public AgentAPoint(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerAPoint(Id.managerAPoint, mySim(), this);
        new ProcessLoaderB(Id.processLoaderB, mySim(), this);
        new ProcessLoaderA(Id.processLoaderA, mySim(), this);
        new TimeSchedulerA(Id.timeSchedulerA, mySim(), this);
        addOwnMessage(Mc.importResource);
        addOwnMessage(Mc.load);
        addOwnMessage(Mc.initialisation);
        addOwnMessage(Mc.done);
    }
	//meta! tag="end"
}
