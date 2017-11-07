package qmaze.View.Components;

import qmaze.View.ViewController;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import qmaze.View.TrainingConfig;

/**
 *
 * @author katharine
 */
public class LearningParameterComponent extends Component {
    
    private TrainingConfig config;
    
    private final int MAX_HEIGHT = 150;
    private final int MAX_EPISODES = 250;
    
    private final double initialGamma = 0.7;
    private final double initialEpsilon = 0.1;
    private final double initialAlpha = 0.1;
    final Slider gamma = new Slider(0, 1, initialGamma); 
    final Slider epsilon = new Slider(0, 1, initialEpsilon);    
    final Slider alpha = new Slider(0, 1, initialAlpha);  

    private final int initialRows = 4;
    private final int initialColumns = 4;
    private final int initialEpisodes = 50;
    final SpinnerValueFactory.IntegerSpinnerValueFactory mazeSpinnerRows = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,16,initialRows);
    final SpinnerValueFactory.IntegerSpinnerValueFactory mazeSpinnerColumns = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,16,initialColumns);
    final SpinnerValueFactory.IntegerSpinnerValueFactory mazeSpinnerEpisodes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,MAX_EPISODES,initialEpisodes);

    public LearningParameterComponent(ViewController controller) {
        super(controller);
        resetConfig();
    }
    
    private void resetConfig() {
        config = new TrainingConfig(mazeSpinnerEpisodes.getValue(), mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue(), gamma.getValue(), epsilon.getValue(), alpha.getValue());
        controller.configReset(config);
    }

    private void resetEpisodes() {
        config = new TrainingConfig(mazeSpinnerEpisodes.getValue(), mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue(), gamma.getValue(), epsilon.getValue(), alpha.getValue());
        controller.episodesReset(config);
    }
    
    @Override
    public Pane build() {
        FlowPane flow = new FlowPane(Orientation.VERTICAL);
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setStyle(assets.getRichGreenBackground());
        flow.setMaxHeight(MAX_HEIGHT);
        
        HBox hboxEp = buildSlider(epsilon, "Probability Explore", 1);
        HBox hboxG = buildSlider(gamma, "Reward Discount", 1);
        HBox hboxA = buildSlider(alpha, "Learning Rate", 1);
        HBox hboxSliders = new HBox();
        hboxSliders.getChildren().addAll(hboxEp,hboxG,hboxA);
        
        HBox hboxsp = buildSpinners();
        flow.getChildren().addAll(hboxSliders,hboxsp);
        return flow;
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
                resetConfig();
                
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
        addEpisodeValidation();
        
        final Label labelRows = new Label("Rows");
        final Label labelCols = new Label("Columns");
        final Label labelEpisodes = new Label("Episodes");
        rowSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            resetConfig();   
        });
        columnSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            resetConfig();   
        });
        episodeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (validEpisodeInput(newValue)) {
                mazeSpinnerEpisodes.setValue((int)newValue);
            } else {
                mazeSpinnerEpisodes.setValue((int)oldValue);
            }
            resetEpisodes();
        });
        
        
        hbox.getChildren().addAll(labelRows, rowSpinner, labelCols, columnSpinner, labelEpisodes, episodeSpinner);
        return hbox;
    }

    private boolean validEpisodeInput(Object newValue) {
        return 0 < (int)newValue && (int)newValue <= MAX_EPISODES;
    }
    
    private void addEpisodeValidation() {
    
        StringConverter<Integer> sc = new StringConverter<Integer>() {
           @Override
           public Integer fromString(String value)
           {
              try {
                 return Integer.parseInt(value);
              }
              catch (NumberFormatException nfe) {
                 System.out.println("Bad integer: " + value);
                 return initialEpisodes;
              }
           }

           @Override
           public String toString(Integer value) {
              return value.toString();
           }
        };
        mazeSpinnerEpisodes.setConverter(sc);
    }
    
    @Override
    public void reset() {
        if (controller.STATE.equals(RESET_STATE)) {
            gamma.setValue(initialGamma);
            epsilon.setValue(initialEpsilon);
            alpha.setValue(initialAlpha);
            mazeSpinnerEpisodes.setValue(initialEpisodes);
            mazeSpinnerRows.setValue(initialRows);
            mazeSpinnerColumns.setValue(initialColumns);
            resetConfig();    
        }
    }
}
