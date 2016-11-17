package Managers;

import Simulation.Mc;
import Simulation.Id;
import Simulation.MyMessage;
import Agents.AgentModel;
import OSPABA.*;

//meta! id="13"
public class ManagerModel extends Manager {

    public ManagerModel(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="AgentOkolia", id="116", type="Notice"
    public void processExportResource(MessageForm message) {
        message.setAddressee(Id.agentTraffic);
        notice(message);
    }

    //meta! sender="AgentOkolia", id="35", type="Notice"
    public void processImportResource(MessageForm message) {
        message.setAddressee(Id.agentTraffic);
        notice(message);
    }
    
    public void initialisation(MessageForm message){
        message.setAddressee(Id.agentTraffic);
        message.setCode(Mc.initialisation);
        notice(message);
        
        MyMessage okolieMessage = new MyMessage((MyMessage)message);
        okolieMessage.setAddressee(Id.agenEnviroment);
        okolieMessage.setCode(Mc.initialisation);
        notice(okolieMessage);
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
            case Mc.initialisation:
                initialisation(message);
                break;
            case Mc.importResource:
                processImportResource(message);
                break;

            case Mc.exportResource:
                processExportResource(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentModel myAgent() {
        return (AgentModel) super.myAgent();
    }

}
