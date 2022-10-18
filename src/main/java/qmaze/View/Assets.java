package qmaze.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author katharine
 * Essentially a config class for themes etc. Ish.
 */
public class Assets {
    
    private static Assets assets = new Assets();
    
    private Assets() { }
    
    private String theme = "Robot";
    
    public static Assets getInstance( ) {
       return assets;
    }
    
    private final String resourcePath = "resources/";
    private final String agent = "/agent.png";
    private final String agentAtGoal = "/agentAtGoal.png";
    private final String agentDeath = "/agentDeath.png";
    private final String goal = "/goal.png";
    
    String learningPanelBackground = "-fx-background-color: #e4f9db";
    String unvisitedRoom = "-fx-background-color: #f2f9ef";
    String goldBackground = "-fx-background-color: #FFFF9A";
    String whiteBackground = "-fx-background-color: #ffffff";
    String buttonPanelBackground = "-fx-background-color: #a5ea8a;";
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public ImagePattern getAgentImage() {
        String path = resourcePath + theme + agent;
        return new ImagePattern(new Image(path));
    }
    
    public ImagePattern getAgentAtGoalImage() {
        String path = resourcePath + theme + agentAtGoal;
        return new ImagePattern(new Image(path));
    }
        
    public ImagePattern getAgentDeathImage() {
        String path = resourcePath + theme + agentDeath;
        return new ImagePattern(new Image(path));
    }
        
    public ImagePattern getGoalImage() {
        String path = resourcePath + theme + goal;
        return new ImagePattern(new Image(path));
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
    
    public ObservableList<String> getAgentOptions() {
        return FXCollections.observableArrayList(
                "Robot",
                "Sheep"
        );
    }
}
