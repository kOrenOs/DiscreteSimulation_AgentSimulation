package Agents;

import Simulation.Id;
import Simulation.Mc;
import Managers.ManagerBPoint;
import ContinualAssistants.ProcessUnloaderA;
import ContinualAssistants.ProcessUnloaderB;
import OSPABA.*;
import continualAssistants.TimeSchedulerB;

//meta! id="18"
public class AgentBPoint extends Agent {

    public AgentBPoint(int id, Simulation mySim, Agent parent) {
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
        new ManagerBPoint(Id.managerBPoint, mySim(), this);
        new ProcessUnloaderB(Id.processUnloaderB, mySim(), this);
        new ProcessUnloaderA(Id.processUnloaderA, mySim(), this);
        new TimeSchedulerB(Id.timeSchedulerB, mySim(), this);
        addOwnMessage(Mc.exportResource);
        addOwnMessage(Mc.unload);
        addOwnMessage(Mc.initialisation);
        addOwnMessage(Mc.done);
    }
	//meta! tag="end"
}
