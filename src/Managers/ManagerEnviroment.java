package Managers;

import Simulation.Id;
import Simulation.Mc;
import Simulation.MyMessage;
import Agents.AgentEnviroment;
import OSPABA.*;

//meta! id="15"
public class ManagerEnviroment extends Manager {

    public ManagerEnviroment(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="PlanovacDovozMaterialu", id="55", type="Finish"
    public void processFinishResourceImport(MessageForm message) {
        message.setCode(Mc.importResource);
        message.setAddressee(Id.agentModel);
        notice(message);
    }

    //meta! sender="PlanovacOdvozMaterialu", id="114", type="Finish"
    public void processFinishResourceExport(MessageForm message) {
        message.setCode(Mc.exportResource);
        message.setAddressee(Id.agentModel);
        notice(message);
    }

    //meta! sender="AgentModelu", id="88", type="Call"
    public void processInitialisation(MessageForm message) {
        message.setAddressee(Id.resourceImport);
        startContinualAssistant(message);

        MyMessage secondMes = new MyMessage((MyMessage) message);
        secondMes.setAddressee(Id.resourceExport);
        startContinualAssistant(secondMes);
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
                processInitialisation(message);
                break;

            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.resourceImport:
                        processFinishResourceImport(message);
                        break;

                    case Id.resourceExport:
                        processFinishResourceExport(message);
                        break;
                }
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentEnviroment myAgent() {
        return (AgentEnviroment) super.myAgent();
    }
}
