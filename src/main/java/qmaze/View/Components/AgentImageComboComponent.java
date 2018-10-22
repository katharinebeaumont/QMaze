package qmaze.View.Components;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import qmaze.View.ViewController;

/**
 * @author katharine
 */
public class AgentImageComboComponent extends Component {

    private ComboBox<String> agentImage = new ComboBox<>();

    public AgentImageComboComponent(ViewController controller) {
        super(controller);
    }

    @Override
    public Pane build() {

        HBox hbox = new HBox();
        ObservableList<String> options = assets.getAgentOptions();
        agentImage = new ComboBox<>(options);
        agentImage.setPromptText("Toggle Agent");

        agentImage.valueProperty()
                .addListener((ov, t, t1) -> {
                    assets.setTheme(t1);
                    controller.roomReset();
                });
        hbox.getChildren()
                .add(agentImage);
        return hbox;
    }

    @Override
    public void reset() {
        //Always available
    }
}
