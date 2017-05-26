package qmaze.View.Components;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import qmaze.View.ViewController;

/**
 *
 * @author katharine
 */
public class InstructionsPopupComponent extends Component {

    public InstructionsPopupComponent(ViewController controller) {
        super(controller);
    }
        
    @Override
    public Pane build() {
        Button btnShowInstructions = new Button();
        Alert info = new Alert(AlertType.INFORMATION);
        Rectangle r = new Rectangle(50,50);
        r.setFill(images.getAgentAtGoal());
        info.setGraphic(r);
        info.setTitle("Instructions");
        info.setHeaderText("Configuring the Maze");
        info.setContentText("Hold down 'control' and click on a maze room to open or close a room."
                + "etc");
        btnShowInstructions.setText("Instructions");
        
        HBox hbox = new HBox();
        
        btnShowInstructions.setOnAction((ActionEvent event) -> {
            info.show();
        });
        
        hbox.getChildren().add(btnShowInstructions);
        return hbox;
    }

    @Override
    public void reset() {
        //Always available
    }
    
}
