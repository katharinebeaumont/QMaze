/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import qmaze.View.Assets;
import qmaze.View.Components.AgentImageComboComponent;
import qmaze.View.ViewController;
import qmaze.View.Components.HeatMapComboComponent;
import qmaze.View.Components.InstructionsPopupComponent;
import qmaze.View.Components.LearningGridComponent;
import qmaze.View.Components.LearningParameterComponent;

import qmaze.View.Components.ResetButtonComponent;
import qmaze.View.Components.StartTrainingComponent;
import qmaze.View.Components.OptimalPathButtonComponent;

/**
 *
 * @author katharine
 */
public class QMaze extends Application {

    private BorderPane border;
    private final Assets assets = Assets.getInstance();
    
    
    private final int SCREEN_WIDTH = 1200;
    private final int SCREEN_HEIGHT = 600;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        border = new BorderPane();
        buildComponents();
        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(border);
        
        primaryStage.setTitle("Q Learning");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void buildComponents() {
        FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setStyle(assets.getRichGreenBackground());
        
        ViewController controller = new ViewController();
        
        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        
        StartTrainingComponent stc = new StartTrainingComponent(controller);
        ResetButtonComponent reset = new ResetButtonComponent(controller);
        OptimalPathButtonComponent optimal = new OptimalPathButtonComponent(controller);
        HeatMapComboComponent heatMap = new HeatMapComboComponent(controller);
        AgentImageComboComponent agentCombo = new AgentImageComboComponent(controller);
        InstructionsPopupComponent inst = new InstructionsPopupComponent(controller);
        hboxTop.getChildren().addAll(inst.build(), stc.build(), reset.build(), optimal.build(), heatMap.build(), agentCombo.build());
        
        LearningParameterComponent learningParam = new LearningParameterComponent(controller);
        Pane learningParamsPane = learningParam.build();
        
        flow.getChildren().addAll(hboxTop, learningParamsPane);
        border.setTop(flow);
        
        LearningGridComponent learningGridComponent = new LearningGridComponent(controller);
        Pane learningGridPane = learningGridComponent.build();
        learningGridPane.setMaxWidth(SCREEN_WIDTH/2);
        border.setRight(learningGridPane);
        
        Pane maze = controller.getMaze();
        border.setCenter(maze);
    }
}
