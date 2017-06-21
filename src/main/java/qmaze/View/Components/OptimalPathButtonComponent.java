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
public class OptimalPathButtonComponent extends Component {
    
    private final Button btnOptimalPath = new Button();
    
    public OptimalPathButtonComponent(ViewController controller) {
        super(controller);
    }

    @Override
    public Pane build() {
        HBox hbox = new HBox();
        btnOptimalPath.setText("Show optimal path");
        btnOptimalPath.setOnAction((ActionEvent eventOpt) -> {
            controller.showOptimalPath();
        });
        btnOptimalPath.setDisable(true);
        hbox.getChildren().add(btnOptimalPath);
        return hbox;
    }

    @Override
    public void reset() {
        if (controller.STATE.equals(TRAINED_STATE)) {
            btnOptimalPath.setDisable(false);
        } else {
            btnOptimalPath.setDisable(true);
        }
    }
}
