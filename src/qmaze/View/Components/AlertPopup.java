package qmaze.View.Components;

import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;
import qmaze.View.Assets;

/**
 *
 * @author katharine
 */
public class AlertPopup {

    private final Assets assets = Assets.getInstance();
    
    private AlertPopup() {
        
    }
            
    private void build(String message) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle("Bad News");
       alert.setHeaderText(message);
       Rectangle r = new Rectangle(50,50);
       r.setFill(assets.getAgentDeathImage());
       alert.setGraphic(r);
       alert.setContentText("There's no goal state I can get to. You're killing me!");
       alert.showAndWait();
    }
    
    public static void popup(String message) {
        AlertPopup popup = new AlertPopup();
        popup.build(message);
    }
}
