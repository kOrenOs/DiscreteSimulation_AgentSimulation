package agents;

import OSPABA.*;
import managers.*;
import Simulation.Id;
import Simulation.Mc;
import ContinualAssistants.ProcessWayCA;
import ContinualAssistants.ProcessWayAB;
import ContinualAssistants.ProcessWayBC;

//meta! id="232"
public class AgentWays extends Agent
{
	public AgentWays(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerWays(Id.managerWays, mySim(), this);
		new ProcessWayBC(Id.processWayBC, mySim(), this);
		new ProcessWayAB(Id.processWayAB, mySim(), this);
		new ProcessWayCA(Id.processWayCA, mySim(), this);
		addOwnMessage(Mc.move);
                addOwnMessage(Mc.done);
                addOwnMessage(Mc.zmena);
                addOwnMessage(Mc.canteena);
                addOwnMessage(Mc.poCanteena);
                addOwnMessage(Mc.initialisation);
	}
	//meta! tag="end"
}
