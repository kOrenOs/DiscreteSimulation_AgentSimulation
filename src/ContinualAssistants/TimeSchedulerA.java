package continualAssistants;

import Agents.AgentAPoint;
import Constants.SimulationConstants;
import Entity.SchedulerTimer;
import OSPABA.*;
import Simulation.MyMessage;
import Simulation.Mc;

//meta! id="211"
public class TimeSchedulerA extends Scheduler {

    private SchedulerTimer scheduleA = new SchedulerTimer(SimulationConstants.LOAD_MACHINE1_START_TIME,
            SimulationConstants.LOAD_MACHINE1_END_TIME);
    private SchedulerTimer scheduleB = new SchedulerTimer(SimulationConstants.LOAD_MACHINE2_START_TIME,
            SimulationConstants.LOAD_MACHINE2_END_TIME);
    private boolean initialised = false;

    public TimeSchedulerA(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        initialised = false;
    }

    //meta! userInfo="Removed from model"
    public void processStart(MessageForm message) {
        if (!initialised || ((MyMessage) message).getMsg().compareTo("A") == 0) {
            MyMessage msg3 = new MyMessage((MyMessage) message);
            msg3.setCode(Mc.start);
            msg3.setMsg("A");
            hold(scheduleA.getTimeOfSleep(mySim().currentTime()), msg3);
            if (initialised) {
                MyMessage msg1 = new MyMessage((MyMessage) message);
                msg1.setMsg("A");
                assistantFinished(msg1);
            }
        }

        if (!initialised || ((MyMessage) message).getMsg().compareTo("B") == 0) {
            MyMessage msg3 = new MyMessage((MyMessage) message);
            msg3.setCode(Mc.start);
            msg3.setMsg("B");
            hold(scheduleB.getTimeOfSleep(mySim().currentTime()), msg3);
            if (initialised) {
                MyMessage msg2 = new MyMessage((MyMessage) message);
                msg2.setMsg("B");
                assistantFinished(msg2);
            }
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
    public AgentAPoint myAgent() {
        return (AgentAPoint) super.myAgent();
    }

}
