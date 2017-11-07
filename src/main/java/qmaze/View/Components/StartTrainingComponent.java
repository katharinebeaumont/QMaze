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
public class StartTrainingComponent extends Component {

    public StartTrainingComponent(ViewController controller) {
        super(controller);
    }
    
    @Override
    public Pane build() {
        HBox hbox = new HBox();
        Button btn = new Button();
        btn.setText("Start training");
        btn.setOnAction((ActionEvent event) -> {
            controller.startTraining();
        });
        hbox.getChildren().add(btn);
        return hbox;
    }

    @Override
    public void reset() {
        //DO NOTHING
    }
}
