package Agents;

import Simulation.Id;
import Simulation.Mc;
import Managers.ManagerTraffic;
import OSPABA.*;

//meta! id="19"
public class AgentTraffic extends Agent {

    public AgentTraffic(int id, Simulation mySim, Agent parent) {
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
        new ManagerTraffic(Id.managerTraffic, mySim(), this);
        addOwnMessage(Mc.exportResource);
        addOwnMessage(Mc.importResource);
        addOwnMessage(Mc.initialisation);
        addOwnMessage(Mc.load);
        addOwnMessage(Mc.unload);        
        addOwnMessage(Mc.endMove);
    }
	//meta! tag="end"
}
