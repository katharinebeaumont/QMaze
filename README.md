# QMaze

You will need: Java 8 and preferably, Netbeans which has support for JavaFX projects and Ant.

Either build the jar using build.xml, or run the project from src/qmaze/QMaze.java.

The basics: (see this great blog)[http://mnemstudio.org/path-finding-q-learning-tutorial.htm].

## Creating your maze

Increase/decrease the rows and columns using the sliders on the third row of buttons. 

Click on the white grid boxes to open or close rooms. 

Click 'start training' to run through the Q Learning process, using the variables configured.

The Q Table on the right of the application shows the values in the Q Table. 
To output the steps taken during training, do something like:
`System.out.println(qLearning.getQTable().toString());`
in `QMaze.java` after `qLearning.startLearning();` in `addButtonPane()`.

Once trained, click 'show optimal path' to show the robot moving through the maze. This takes a maximum of 30 seconds. 

Please note - if training is not 'complete', in that the Q Table doesn't have values for earlier rooms, the optimal path will still involve a high element of randomness, so the optimal path won't be consistent. 

## Hyperparameters

The Learning Rate controls how fast the robot builds up a memory of the rewards it encounters in the Q Table.

Reward Discount controls how much rewards 'count' when you are far away from them. 

'Probability Explore' is the probability that, with each step, the robot will pick the next action at random (exploring) or look in its Q Table ('memory') to see which action gives the greatest reward.
If there is no memory, then it defaults to random. Equally if there are 2 best actions, then it randomly picks one.

Have a play with these, and see what the effect is on learning and the optimal path.