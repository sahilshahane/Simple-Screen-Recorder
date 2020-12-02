package recorder;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class mainRecorder extends Application{

    private double xOffset = 0;
    private double yOffset = 0;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("RecorderFXML.fxml"));

         root.setOnMousePressed((MouseEvent event) -> {
             xOffset = event.getSceneX();
             yOffset = event.getSceneY();
        });
        
        //move around here
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        
        Scene scene = new Scene(root);
        stage.setTitle("Screen Recorder");
        
        String App_iconPath = "assets/icon.png";
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App_iconPath)));
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    
    public static void main(String args[]){
        launch(args);
    }
    
}
