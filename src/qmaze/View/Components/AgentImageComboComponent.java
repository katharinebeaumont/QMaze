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
public class AgentImageComboComponent extends Component {

    private ComboBox agentImage = new ComboBox();
    
    public AgentImageComboComponent(ViewController controller) {
        super(controller);
    }
    
    @Override
    public Pane build() {
        
        HBox hbox = new HBox();
        ObservableList<String> options = assets.getAgentOptions();
        agentImage = new ComboBox(options);
        agentImage.setPromptText("Toggle Agent");
        
        agentImage.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) { 
                assets.setTheme(t1);
                controller.roomReset();
            }    
        });
        hbox.getChildren().add(agentImage);
        return hbox;
    }

    @Override
    public void reset() {
        //Always available
    }

}
