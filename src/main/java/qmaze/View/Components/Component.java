package qmaze.View.Components;

import javafx.scene.layout.Pane;
import qmaze.View.Assets;
import qmaze.View.ViewController;

/**
 *
 * @author katharine
 * Some fairly unpleasant coupling.
 */
public abstract class Component {
    
    public final ViewController controller;
    public final Assets assets = Assets.getInstance();
    
    public Component(ViewController controller) {
        this.controller = controller;
        controller.register(this);
    }
    
    public static String RESET_STATE = "Reset"; //Hard reset everything to initial values
    public static String TRAINED_STATE = "Trained"; //We have trained the algorithm, so we can show optimal path, heatmap
    public static String ADJUST_PARAM_STATE = "PreTrain"; //Playing with variables, so need to resize maze, etc
    public static String ADJUST_MAZE_STATE = "PreTrainNoAdjust"; //Playing with maze rooms, no need to resize but need to disable optimal path etc
    
    public abstract Pane build();
    public abstract void reset();
}
