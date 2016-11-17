package Agents;

import Simulation.Id;
import Simulation.Mc;
import Managers.ManagerEnviroment;
import ContinualAssistants.ResourceExport;
import ContinualAssistants.ResourceImport;
import OSPABA.*;

//meta! id="15"
public class AgentEnviroment extends Agent {

    public AgentEnviroment(int id, Simulation mySim, Agent parent) {
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
        new ManagerEnviroment(Id.managerEnviroment, mySim(), this);
        new ResourceImport(Id.resourceImport, mySim(), this);
        new ResourceExport(Id.resourceExport, mySim(), this);
        addOwnMessage(Mc.initialisation);
        addOwnMessage(Mc.importResource);
        addOwnMessage(Mc.exportResource);
    }
	//meta! tag="end"
}
