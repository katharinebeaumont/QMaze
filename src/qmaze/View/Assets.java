package qmaze.View;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author katharine
 */
public class Assets {
    ImagePattern agent = new ImagePattern(new Image("/resources/agent.png"));
    ImagePattern agentAtGoal = new ImagePattern(new Image("/resources/agentAtGoal.png"));
    ImagePattern agentDeath = new ImagePattern(new Image("/resources/agentDeath.png")); 
    ImagePattern goal = new ImagePattern(new Image("/resources/goal.png")); 
    
    String learningPanelBackground = "-fx-background-color: #e4f9db";
    String unvisitedRoom = "-fx-background-color: #f2f9ef";
    String goldBackground = "-fx-background-color: #FFFF9A";
    String whiteBackground = "-fx-background-color: #ffffff";
    String buttonPanelBackground = "-fx-background-color: #a5ea8a;";
    
    public ImagePattern getAgentImage() {
        return agent;
    }
    
    public ImagePattern getAgentAtGoalImage() {
        return agentAtGoal;
    }
        
    public ImagePattern getAgentDeathImage() {
        return agentDeath;
    }
        
    public ImagePattern getGoalImage() {
        return goal;
    }
    
    public String getLightGreenBackground() {
        return learningPanelBackground;
    }
    
    public String getUnvisitedRoomBackground() {
        return unvisitedRoom;
    }
    
    public String getGoalRoomBackground() {
        return goldBackground;
    }
    
    public String getWhiteBackground() {
        return whiteBackground;
    }
    
    public String getRichGreenBackground() {
        return buttonPanelBackground;
    }
}
