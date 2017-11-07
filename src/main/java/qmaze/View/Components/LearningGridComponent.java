package qmaze.View.Components;

import qmaze.View.ViewController;
import java.util.HashMap;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class LearningGridComponent extends Component {

    private BorderPane border = new BorderPane();
    
    public LearningGridComponent(ViewController controller) {
        super(controller);
    }
    
    @Override
    public Pane build() {
        HashMap<Coordinates, HashMap<Coordinates,Double>> roomLearnings = controller.getLearnings();
        Coordinates goalState = controller.getGoalState();
        return addQTable(roomLearnings, goalState);
    }
    
    @Override
    public void reset() {
        if (controller.STATE.equals(TRAINED_STATE)) {
            build();
        } else {
            Node qTable = border.getCenter();
            border.getChildren().remove(qTable);
        }
    }
    
    private Pane addQTable(HashMap<Coordinates, HashMap<Coordinates,Double>> roomLearnings, Coordinates goalState) {
        
        if (roomLearnings.isEmpty()) {
            return border;
        }
        ScrollPane sp = new ScrollPane();
        
        GridPane grid = new GridPane();
        grid.setBorder(Border.EMPTY);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setStyle(assets.getLightGreenBackground()); 
        
        Set<Coordinates> roomCoordinates = roomLearnings.keySet();
        for (Coordinates roomCoordinate: roomCoordinates) {
            Pane textPane = new Pane();
            
            int columnIndex = roomCoordinate.getXCoordinate();
            int rowIndex = roomCoordinate.getYCoordinate();
            StringBuilder sb = new StringBuilder();
            sb.append("Room ");
            sb.append(rowIndex);
            sb.append(",");
            sb.append(columnIndex);
            sb.append("\n");
            HashMap<Coordinates,Double> actions = roomLearnings.get(roomCoordinate);
            String toolTipText = "";
            if (goalState.equals(roomCoordinate)) {
                sb.append("GOAL");
                toolTipText = "Yay!";
                textPane.setStyle(assets.getGoalRoomBackground());
            } else if (actions == null || actions.isEmpty()) {
                sb.append("No info");
                toolTipText = "Maybe we didn't visit this room?";
                textPane.setStyle(assets.getUnvisitedRoomBackground());
            }
            else {
                for (HashMap.Entry<Coordinates,Double> entry : actions.entrySet()) {
                    Coordinates nextRoom = entry.getKey();
                    String qValueForText = String.format("%.2f", entry.getValue());
                    sb.append(qValueForText);
                    sb.append(getArrowDirection(roomCoordinate, nextRoom));
                    sb.append("\n");
                    textPane.setStyle(assets.getWhiteBackground());
                    String qValueForToolTip = String.format("%.4f", entry.getValue());
                    toolTipText = toolTipText + "Moving " + getDirectionDesc(roomCoordinate, nextRoom) + " for " + qValueForToolTip + "\n";
                }
            }
            Text t = new Text(sb.toString());
            
            Tooltip tp = new Tooltip(toolTipText);
            Tooltip.install(textPane, tp);
                    
            textPane.getChildren().add(t);
            textPane.setMinSize(60,60);
            textPane.setMaxSize(70,70);
            
            grid.add(textPane, columnIndex, rowIndex);
        }
        sp.setContent(grid);
        Text title = new Text("Q Table");
        border.setTop(title);
        border.setCenter(sp);
        return border;
    }
    
    private String getArrowDirection(Coordinates currentRoom, Coordinates nextRoom) {
        int currentRow = currentRoom.getYCoordinate();
        int currentColumn = currentRoom.getXCoordinate();
        int nextRow = nextRoom.getYCoordinate();
        int nextColumn = nextRoom.getXCoordinate();
        if (currentRow == nextRow && currentColumn > nextColumn) {
            return " <- ";
        } else if (currentRow == nextRow && currentColumn < nextColumn) {
            return " -> ";
        } else if (currentRow > nextRow && currentColumn == nextColumn) {
            return " ^ ";
        } else if (currentRow < nextRow && currentColumn == nextColumn) {
            return " v ";
        }
        return nextRoom.toString();
    }
    
    private String getDirectionDesc(Coordinates currentRoom, Coordinates nextRoom) {
        int currentRow = currentRoom.getYCoordinate();
        int currentColumn = currentRoom.getXCoordinate();
        int nextRow = nextRoom.getYCoordinate();
        int nextColumn = nextRoom.getXCoordinate();
        if (currentRow == nextRow && currentColumn > nextColumn) {
            return "left";
        } else if (currentRow == nextRow && currentColumn < nextColumn) {
            return "right";
        } else if (currentRow > nextRow && currentColumn == nextColumn) {
            return "up";
        } else if (currentRow < nextRow && currentColumn == nextColumn) {
            return "down";
        }
        return nextRoom.toString();
    }
    
}
