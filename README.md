# QMaze

Build a maze! Teach your robot to navigate the maze, by playing with Q Learning configuration. Q Learning is an approach in Reinforcement Learning.

The basics: [see this great blog](http://mnemstudio.org/path-finding-q-learning-tutorial.htm).

Dive in: [see this great book](https://mitpress.mit.edu/books/reinforcement-learning).

## Reinforcement Learning?

Yes. This is a branch of Machine Learning, considered distinct from Supervised and Unsupervised Learning.
Instead of looking for hidden structure, like unsupervised Clustering algorithms, we're trying to maximise a reward structure. We have an agent (here, the robot) in an unknown environment (here, the maze). There is a goal that the agent needs to reach: when they do reach it, it's obvious.
We find the goal with a trade off between exploration and exploitation. To get a lot of reward, the robot has to prefer actions that it has already tried and tested. But it has to explore to make better selections in the future.

## Get started

You will need: [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and preferably, [Netbeans](https://netbeans.org/) which has support for JavaFX projects and Gradle.

Once you have coded things, either build the jar using gradle, then run `java -jar build/lib/QMaze.jar` or (in an IDE) run the project from src/main/java/qmaze/QMaze.java.

In the meantime, see a working version in example/QMaze.jar (run `java -jar example/QMaze.jar`). Try to get a feel for how the hyperparameters work.

## Hyperparameters

The Learning Rate controls how fast the robot builds up a memory of the rewards it encounters in the Q Table.

Press 'Instructions' to see more information about these.

Have a play with these, and see what the effect is on learning and the optimal path. (See 'Creating your maze' below for info on how to use the UI).

## Get coding!

Complete the code in:

AgentMemory

Agent

See hints.txt for... well, hints.

Extras? Add some artwork! Check out the Assets class.

## Creating your maze

The boxes act like rooms. Unless they are at the edge of the maze, they have 4 entrances: up, down, left, right.

Increase/decrease the rows and columns using the sliders on the third row of buttons. 

Hold down control and click on the white grid boxes to open or close rooms. Click on a room to change the starting point (denoted with 'X') or the goal room.

Click 'start training' to run through the Q Learning process, using the configurable variables.

The Q Table on the right of the application shows the values in the Q Table. To see the values to a higher degree of precision, hover your mouse over the square.

Once trained, click 'Show optimal path' to show the robot moving through the maze. This takes a maximum of 30 seconds. Change the heatmap colours to get an idea of the rooms most visited during training. You can also see the visit percentage (over all of the episodes) in the tooltip.

Please note - if training is not complete, in that the Q Table doesn't have values for earlier rooms, the optimal path will still involve a high element of randomness, so the optimal path won't be consistent. This will happen with larger mazes.


## Problems, bugs, feature requests, questions?

Open an issue or tweet/DM me on [Twitter](https://twitter.com/katharineCodes) for my e-mail address.
