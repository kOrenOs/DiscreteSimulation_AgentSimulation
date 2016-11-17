# DiscreteSimulation_AgentSimulation

This project was one of the schoolworks for the course Discrete simulation. The main point was to create agent based simulation to simulate importing of the material to the building. For this simulation was used special agent simulation core.

There were 5 types of cars with the properties. Task was to create configuration with these cars, which would be the cheapest and successful to supply the building with demanding amount of material.

It was necessary to analyse input data of material producer, who comes with material to storage randomly. It was necessary to find distribution, which can describe this action.

There is the track from the storage to the building and back, where are cars located. Before the building and the storage are queues. Cars are waiting in these queues, when another car is in the process. On the one part of track is not allowed car overrunning. Machines, which provide loading and unloading, have special work hours. After work hours are not working. 

Project is possible to run in the GUI mode with GUI class. There are a lot of statistics, which tell everything about actual state of model. Some statistics are working just in the observer mode (untick Max speed checkbox).

In the project are also chart GUI statistics. It is necessary to import JFreeChart library. There is also used JCommon library and ABACore (agent orientated simulation core).

In spring 2016
