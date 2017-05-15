# QMaze

You will need: Java 8 and preferably, Netbeans which has support for JavaFX projects and Ant.

Either build the jar using build.xml, or run the project from src/qmaze/QMaze.java.

Click on the white grid boxes to open or close rooms.

Click 'start training' to run through the Q Learning process, using the variables configured with the sliders.

The console output shows the steps taken during training. The Q Table on the right of the application shows the values in the Q Table.

Click 'show optimal path' at the end of training to show the robot moving through the maze. This takes a maximum of 30 seconds. 

Please note - if training is not 'complete', in that the Q Table doesn't have values for earlier rooms, the optimal path will still involve a high element of randomness, so the optimal path won't be consistent. 
