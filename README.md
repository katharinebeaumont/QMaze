# QMaze

You will need: Java 8 and preferably, Netbeans which has support for JavaFX projects and Ant.

Either build the jar using build.xml, or run the project from src/qmaze/QMaze.java.

The basics: [see this great blog](http://mnemstudio.org/path-finding-q-learning-tutorial.htm).

## Creating your maze

Increase/decrease the rows and columns using the sliders on the third row of buttons. 

Hold down control and click on the white grid boxes to open or close rooms. Click on a room to change the starting point (denoted with 'X') or the goal room.

Click 'start training' to run through the Q Learning process, using the configurable variables.

The Q Table on the right of the application shows the values in the Q Table. To see the values to a higher degree of precision, hover your mouse over the square.

Once trained, click 'Show optimal path' to show the robot moving through the maze. This takes a maximum of 30 seconds. Change the heatmap colours to get an idea of the rooms most visited during training. You can also see the visit percentage (over all of the episodes) in the tooltip.

Please note - if training is not complete, in that the Q Table doesn't have values for earlier rooms, the optimal path will still involve a high element of randomness, so the optimal path won't be consistent. This will happen with larger mazes.

## Hyperparameters

The Learning Rate controls how fast the robot builds up a memory of the rewards it encounters in the Q Table.

Press 'Instructions' to see more information about these.

Have a play with these, and see what the effect is on learning and the optimal path.

## Problems, bugs, feature requests, questions?

Open an issue or DM me on [Twitter](https://twitter.com/katharineCodes) for my e-mail address.
