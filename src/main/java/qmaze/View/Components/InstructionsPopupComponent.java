package qmaze.View.Components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import qmaze.View.ViewController;

/**
 *
 * @author katharine
 */
public class InstructionsPopupComponent extends Component {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 600;
    
    public InstructionsPopupComponent(ViewController controller) {
        super(controller);
    }
        
    @Override
    public Pane build() {
        HBox hbox = new HBox();
        Button btnShowInstructions = new Button();
        btnShowInstructions.setText("Instructions");
        hbox.getChildren().add(btnShowInstructions);
        
        DialogPane dp = new DialogPane();
        
        Dialog info = new Dialog();
        info.setWidth(WIDTH);
        info.setHeight(HEIGHT);
        info.setResizable(true);
        
        info.setTitle("Instructions");
        
        dp.setHeaderText("Configuring the Maze");
        
        Rectangle r = new Rectangle(50,50);
        r.setFill(assets.getAgentAtGoalImage());
        dp.setGraphic(r);
        ButtonType loginButtonType = new ButtonType("Got it!", ButtonData.OK_DONE);
        dp.getButtonTypes().add(loginButtonType);
        
        btnShowInstructions.setOnAction((ActionEvent event) -> {
            WebView webView = new WebView();
            StringBuilder sb = new StringBuilder();
            try {
                Path p = Paths.get("src/resources/Instructions.txt");
                Files.readAllLines(p).stream().map((line) -> {
                    sb.append(line);
                    return line;
                }).forEachOrdered((_item) -> {
                    sb.append("\n");
                });
               
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            webView.getEngine().loadContent(sb.toString());
            
            dp.setContent(webView);
            info.setDialogPane(dp);
            info.showAndWait();
            
        });
        
        return hbox;
    }

    @Override
    public void reset() {
        //Always available
    }
  
}
