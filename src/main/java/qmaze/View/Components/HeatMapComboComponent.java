package qmaze.View.Components;

import qmaze.View.ViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author katharine
 */
public class HeatMapComboComponent extends Component {

    private ComboBox heatMap = new ComboBox();
    
    public HeatMapComboComponent(ViewController controller) {
        super(controller);
    }
    
    @Override
    public Pane build() {
        
        HBox hbox = new HBox();
        ObservableList<String> options = 
            FXCollections.observableArrayList(
                "Green",
                "Yellow",
                "Pink",
                "Blue",
                "Cyan",
                "Red",
                "None"
            );
        heatMap = new ComboBox(options);
        heatMap.setPromptText("Heat map colour");
        
        heatMap.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                controller.heatMapReset(t1);
            }    
        });
        hbox.getChildren().add(heatMap);
        return hbox;
    }

    @Override
    public void reset() {
        if (controller.STATE.equals(TRAINED_STATE)) {
           heatMap.setDisable(false);
        } else {
            heatMap.setDisable(true);
        }
    }

}
