package ContinualAssistants;

import Simulation.Mc;
import Agents.AgentEnviroment;
import Constants.SimulationConstants;
import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.UniformDiscreteRNG;
import Simulation.MyMessage;
import Simulation.MySimulation;
import Simulation.ResourceMessage;
import java.util.Random;

//meta! id="113"
public class ResourceExport extends Scheduler {

    private EmpiricRNG resourceGenerator;

    public ResourceExport(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        resourceGenerator = new EmpiricRNG(new Random(MySimulation.seedGenerator.nextLong()),
                new EmpiricPair(new UniformDiscreteRNG(10, 20), 0.02),
                new EmpiricPair(new UniformDiscreteRNG(21, 48), 0.2),
                new EmpiricPair(new UniformDiscreteRNG(49, 65), 0.33),
                new EmpiricPair(new UniformDiscreteRNG(66, 79), 0.3),
                new EmpiricPair(new UniformDiscreteRNG(80, 99), 0.15));
    }

    //meta! sender="AgentOkolia", id="114", type="Start"
    public void processStart(MessageForm message) {
        MyMessage mesA = new MyMessage((MyMessage) message);
        mesA.setCode(Mc.exportResource);
        hold(SimulationConstants.MINUTES_BETWEEN_TAKE_OUT, mesA);
        
        ResourceMessage mesRes = new ResourceMessage((MyMessage) message);
        mesRes.setAmount(resourceGenerator.sample().intValue());
        assistantFinished(mesRes);
    }

    public void processResourceExport(MessageForm message) {
        sendRepeteAndresource(message, resourceGenerator.sample().doubleValue(), (MyMessage) message);
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
            case Mc.exportResource:
                processResourceExport(message);
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

    private void sendRepeteAndresource(MessageForm paMessage, double paResources, MyMessage paRepete) {
        if ((SimulationConstants.START_TIME + mySim().currentTime()) % (24 * 60) < SimulationConstants.TAKE_OUT_END_TIME
                && (SimulationConstants.START_TIME + mySim().currentTime()) % (24 * 60) >= SimulationConstants.TAKE_OUT_START_TIME) {
            hold(SimulationConstants.MINUTES_BETWEEN_TAKE_OUT, paRepete);
        } else {
            hold(24 * 60 - ((SimulationConstants.START_TIME + mySim().currentTime()) % (24 * 60)) + SimulationConstants.TAKE_OUT_START_TIME, paRepete);
        }

        ResourceMessage mesRes = new ResourceMessage((MyMessage) paMessage);
        mesRes.setAmount(paResources);
        assistantFinished(mesRes);
    }
}
