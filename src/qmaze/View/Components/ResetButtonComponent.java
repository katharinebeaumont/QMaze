package qmaze.View.Components;

import qmaze.View.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author katharine
 */
public class ResetButtonComponent extends Component {
    
    public ResetButtonComponent(ViewController controller) {
        super(controller);
    }

    @Override
    public Pane build() {
             
        HBox hbox = new HBox();
        Button btn = new Button();
        btn.setText("Reset");
        btn.setOnAction((ActionEvent event) -> {
            System.out.println("Resetting");
            controller.hardReset();
        });
        hbox.getChildren().add(btn);
        return hbox;
    }

    @Override
    public void reset() {
        //Do nothing
    }
}
