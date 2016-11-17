package Agents;

import Simulation.Id;
import Simulation.Mc;
import Simulation.MySimulation;
import Simulation.VehicleMessage;
import Managers.ManagerModel;
import OSPABA.*;

//meta! id="13"
public class AgentModel extends Agent {
    
    public AgentModel(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        
        VehicleMessage message = new VehicleMessage(mySim());
        message.addVehicles(((MySimulation)mySim()).getConfigVehicles());
        message.setAddressee(this);
        message.setCode(Mc.initialisation);
        manager().notice(message);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerModel(Id.managerModelu, mySim(), this);
        addOwnMessage(Mc.exportResource);
        addOwnMessage(Mc.importResource);
        addOwnMessage(Mc.initialisation);
    }
	//meta! tag="end"
}
