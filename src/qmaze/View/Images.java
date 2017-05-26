package qmaze.View;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author katharine
 */
public class Images {
    ImagePattern agent = new ImagePattern(new Image("/resources/agent.png"));
    ImagePattern agentAtGoal = new ImagePattern(new Image("/resources/agentAtGoal.png"));
    ImagePattern goal = new ImagePattern(new Image("/resources/goal.png")); 
    
    public ImagePattern getAgent() {
        return agent;
    }
    
    public ImagePattern getAgentAtGoal() {
        return agentAtGoal;
    }
        
    public ImagePattern getGoal() {
        return goal;
    }
}
