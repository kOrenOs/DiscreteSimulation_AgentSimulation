package ContinualAssistants;

import Simulation.Mc;
import Agents.AgentEnviroment;
import Constants.SimulationConstants;
import OSPABA.*;
import Simulation.MyMessage;
import Simulation.ResourceMessage;
import Simulation.CompanyMessage;
import Simulation.MySimulation;
import Entity.CompanyATimerGenerator;
import Entity.CompanyBTimerGenerator;
import Entity.CompanyCTimerGenerator;

//meta! id="54"
public class ResourceImport extends Scheduler {

    private CompanyATimerGenerator generatorA;
    private CompanyBTimerGenerator generatorB;
    private CompanyCTimerGenerator generatorC;

    private boolean turnOffCompanyA = false;

    public ResourceImport(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        generatorA = new CompanyATimerGenerator(MySimulation.seedGenerator);
        generatorB = new CompanyBTimerGenerator(MySimulation.seedGenerator);
        generatorC = new CompanyCTimerGenerator(MySimulation.seedGenerator);

        turnOffCompanyA = ((MySimulation) mySim()).getCompanyTurnOff();
    }

    //meta! sender="AgentOkolia", id="55", type="Start"
    public void processStart(MessageForm message) {
        CompanyMessage mesA = new CompanyMessage((MyMessage) message);
        mesA.setCode(Mc.importResource);
        mesA.setMessage("A");
        hold(generatorA.generateTime(), mesA);

        CompanyMessage mesB = new CompanyMessage((MyMessage) message);
        mesB.setCode(Mc.importResource);
        mesB.setMessage("B");
        hold(generatorB.generateTime(), mesB);

        CompanyMessage mesC = new CompanyMessage((MyMessage) message);
        mesC.setCode(Mc.importResource);
        mesC.setMessage("C");
        hold(generatorC.generateTime(), mesC);
    }

    public void processResourceImport(MessageForm message) {
        switch (((CompanyMessage) message).getMessage()) {
            case "A":
                sentResourceAndCompanyMes(message, generatorA.generateLoad(),
                        (CompanyMessage) message, generatorA.generateTime());
                break;
            case "B":
                sentResourceAndCompanyMes(message, generatorB.generateLoad(),
                        (CompanyMessage) message, generatorB.generateTime());
                break;
            case "C":
                sentResourceAndCompanyMes(message, generatorC.generateLoad(),
                        (CompanyMessage) message, generatorC.generateTime());
                break;
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;
            case Mc.importResource:
                processResourceImport(message);
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

    private void sentResourceAndCompanyMes(MessageForm paMessage, double paResources, CompanyMessage paCompany, double paNext) {
        if (paCompany.getMessage().compareTo("A") != 0 || !turnOffCompanyA
                || mySim().currentTime() < SimulationConstants.COMPANY_A_TURN_OFF + SimulationConstants.START_TIME) {
            ResourceMessage mesRes = new ResourceMessage((MyMessage) paMessage);
            mesRes.setAmount(paResources);
            assistantFinished(mesRes);

            hold(paNext, paCompany);
        }
    }
}
