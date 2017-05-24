/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze;

import java.util.ArrayList;
import java.util.HashMap;
import qmaze.View.QMazeLearning;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import qmaze.Controller.LearningController;
import qmaze.Environment.Coordinates;
import qmaze.View.QMazeConfig;
import qmaze.View.QMazeGrid;

/**
 *
 * @author katharine
 */
public class QMaze extends Application {

    private final LearningController controller = new LearningController();   
    private final QMazeLearning mazeLearning = new QMazeLearning();
    
    private QMazeGrid mazeGrid;
    private BorderPane border;
    
    private final int SCREEN_WIDTH = 900;
    private final int SCREEN_HEIGHT = 600;
    
    private final double initialGamma = 0.7;
    private final double initialEpsilon = 0.1;
    private final double initialAlpha = 0.1;
    private final int initialEpisodes = 50;
    final Slider gamma = new Slider(0, 1, initialGamma); 
    final Slider epsilon = new Slider(0, 1, initialEpsilon);    
    final Slider alpha = new Slider(0, 1, initialAlpha);  
    private String heatMapColor = "";
    private final int initialRows = 4;
    private final int initialColumns = 4;
    final IntegerSpinnerValueFactory mazeSpinnerRows = new IntegerSpinnerValueFactory(2,16,initialRows);
    final IntegerSpinnerValueFactory mazeSpinnerColumns = new IntegerSpinnerValueFactory(2,16,initialColumns);
    final IntegerSpinnerValueFactory mazeSpinnerEpisodes = new IntegerSpinnerValueFactory(1,100,initialEpisodes);
    
    private final Button btnOptimalPath = new Button();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        border = new BorderPane();
        addButtonPane();
        
        resetMaze();
        addMaze();
                        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(border);
        
        primaryStage.setTitle("Q Learning");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void reset() {
        addButtonPane();
        gamma.setValue(initialGamma);
        epsilon.setValue(initialEpsilon);
        alpha.setValue(initialAlpha);
        mazeSpinnerEpisodes.setValue(initialEpisodes);
        mazeSpinnerRows.setValue(initialRows);
        mazeSpinnerColumns.setValue(initialColumns);
        resetMaze();
        addMaze();
    }
    
    private void resetMaze() {
        mazeGrid = new QMazeGrid(mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue(), border);
        resetQTable();
    }

    private void resetQTable() {
        btnOptimalPath.setDisable(true);
        removeQTable();
    }
    
    private void addButtonPane() {
        
        FlowPane flow = new FlowPane(Orientation.VERTICAL);
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setStyle("-fx-background-color: #a5ea8a;");
        flow.setMaxHeight(200);
        
        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        
        btnOptimalPath.setText("Show optimal path");
        btnOptimalPath.setOnAction((ActionEvent eventOpt) -> {
            showOptimalPath();
        });
        btnOptimalPath.setDisable(true);
            
        Button btn = new Button();
        btn.setText("Start training");
        btn.setOnAction((ActionEvent event) -> {
            startTraining();
        });
        
        Button btnReset = new Button();
        btnReset.setText("Reset");
        btnReset.setOnAction((ActionEvent event) -> {
            System.out.println("Resetting");
            reset();
        });
        
        ComboBox heatMap = buildHeatMapCombo();
        
        hboxTop.getChildren().addAll(btn, btnOptimalPath, btnReset, heatMap);
        
        HBox hboxEp = buildSlider(epsilon, "Probability Explore", 1);
        HBox hboxG = buildSlider(gamma, "Reward Discount", 1);
        HBox hboxA = buildSlider(alpha, "Learning Rate", 1);
        HBox hboxSliders = new HBox();
        hboxSliders.getChildren().addAll(hboxEp,hboxG,hboxA);
        
        HBox hboxsp = buildSpinners();
        
        flow.getChildren().addAll(hboxTop,hboxSliders,hboxsp);
        border.setTop(flow);
    }
    
    private ComboBox buildHeatMapCombo() {
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
        ComboBox heatMapOptions = new ComboBox(options);
        heatMapOptions.setPromptText("Heat map colour");
        
         heatMapOptions.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                heatMapColor = t1;                
            }    
        });
        return heatMapOptions;
    }

    private void showOptimalPath() {
        System.out.println("Finding optimal path...");
        ArrayList<Coordinates> optimalPath = controller.getOptimalPath(mazeGrid.getStartingState());
        mazeGrid.animateMap(optimalPath);
    }
    
    private HBox buildSlider(Slider slider, String labelTitle, double max) {
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        
        final Label title = new Label(
        labelTitle);
        
        double majorTickUnit = max/2;
        double minorTickUnit = max/10;
        
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(majorTickUnit);
        slider.setBlockIncrement(minorTickUnit);
        
        final Label label = new Label(
        Double.toString(slider.getValue()));
                
        slider.valueProperty().addListener((
            ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                label.setText(String.format("%.1f", new_val));
                resetQTable();
        });
        hbox.getChildren().addAll(title, slider, label);
        return hbox;
    }
    
    private HBox buildSpinners() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        Spinner rowSpinner = new Spinner(mazeSpinnerRows);
        Spinner columnSpinner = new Spinner(mazeSpinnerColumns);
        Spinner episodeSpinner = new Spinner(mazeSpinnerEpisodes);
        episodeSpinner.setEditable(true);
        final Label labelRows = new Label("Rows");
        final Label labelCols = new Label("Columns");
        final Label labelEpisodes = new Label("Episodes");
        rowSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
               resetMaze(); 
               addMaze();
        });
        columnSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
               resetMaze(); 
               addMaze();
        });
        episodeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
               mazeSpinnerEpisodes.setValue((int)newValue);
        });
        
        hbox.getChildren().addAll(labelRows, rowSpinner, labelCols, columnSpinner, labelEpisodes, episodeSpinner);
        return hbox;
    }
    
    private void addMaze() {
        mazeGrid.buildMaze();
    }

    private void removeQTable() {
        Node qTable = border.getRight();
        border.getChildren().remove(qTable);
    }
    
    private void addQTable() {
        final HashMap<Coordinates, HashMap<Coordinates, Double>> learnings = controller.getLearnings(mazeGrid.getRooms());
        Pane qTable = mazeLearning.addQTable(learnings, mazeGrid.getGoalState());
        border.setRight(qTable);
    }

    public void startTraining() {
        System.out.println("Training");
        QMazeConfig config = new QMazeConfig(mazeSpinnerEpisodes.getValue(), gamma.getValue(), epsilon.getValue(), alpha.getValue());
        controller.startLearning(mazeGrid.getRooms(),
                mazeGrid.getRows(), mazeGrid.getColumns(), mazeGrid.getStartingState(), config);
        btnOptimalPath.setDisable(false);
        addQTable();
        HashMap heatMap = controller.getHeatMap();
        mazeGrid.showVisitCount(heatMap, heatMapColor);
    }
}
