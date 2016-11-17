package continualAssistants;

import Agents.AgentBPoint;
import Constants.SimulationConstants;
import Entity.SchedulerTimer;
import OSPABA.*;
import Simulation.MyMessage;
import Simulation.Mc;

//meta! id="211"
public class TimeSchedulerB extends Scheduler {

    private SchedulerTimer scheduleA = new SchedulerTimer(SimulationConstants.UNLOAD_MACHINE1_START_TIME,
            SimulationConstants.UNLOAD_MACHINE1_END_TIME);
    private boolean initialised = false;

    public TimeSchedulerB(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        initialised = false;
    }

    //meta! userInfo="Removed from model"
    public void processStart(MessageForm message) {
        message.setCode(Mc.start);
        hold(scheduleA.getTimeOfSleep(mySim().currentTime()), message);
        if (initialised) {
            MyMessage msg1 = new MyMessage((MyMessage) message);
            assistantFinished(msg1);
        }

        if (!initialised) {
            initialised = true;
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

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentBPoint myAgent() {
        return (AgentBPoint) super.myAgent();
    }

}
