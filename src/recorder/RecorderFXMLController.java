
package recorder;
 

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;  
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.awt.Robot;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class RecorderFXMLController implements Initializable {
    private Recorder REC_OBJ;
    @FXML
    private JFXSlider fpsSlider;
    @FXML
    private ImageView closeBTN_IMAGE;
    @FXML
    private ImageView minimizeBTN_IMAGE;
    @FXML
    private JFXButton startBTN;
    @FXML
    private JFXButton stopBTN;
    @FXML
    private GridPane Pane;
    @FXML
    private JFXTextField outputFolderTextArea;
    @FXML
    private JFXButton outputBTN;
    
    private Robot screen;
    private Rectangle screenDimention;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRECORDER();
        try {
            screen = new Robot();
        } catch (Exception e) {}
        screenDimention = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        outputFolderTextArea.setText(System.getProperty("user.dir"));
    }    
    
    void initRECORDER(){
          try{
            this.REC_OBJ = new Recorder("./");
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    void log(String message){System.out.println(message);}
    
    @FXML
    void startRecording(){
        log("Started Recording");
        if(REC_OBJ.startRecording()){
            startBTN.setDisable(true);
            stopBTN.setDisable(false);
            fpsSlider.setDisable(true);
            outputFolderTextArea.setDisable(true);
            closeBTN_IMAGE.setDisable(true);
            outputBTN.setDisable(true);
        }
    }
    
    @FXML
    void stopRecording(){
        log("Stopped Recording");
        if(REC_OBJ.stopRecording()){
            startBTN.setDisable(false);
            stopBTN.setDisable(true);
            fpsSlider.setDisable(false);
            outputFolderTextArea.setDisable(false);
            closeBTN_IMAGE.setDisable(false);
            outputBTN.setDisable(false);
        }
    }
    
    @FXML
    private void closeWindow(MouseEvent event) {
        log("Closing Program");
        getStage().close();
        System.exit(0);
    }

    @FXML
    private void minimizeWindow(MouseEvent event) {
        log("Minimizing Program");
        getStage().setIconified(true);
    }

    @FXML
    private void changeOutputFolder(ActionEvent event) {
        
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(getStage()); 
        if (file != null) { 
            
            String FOLDER_PATH = file.getAbsolutePath();
            REC_OBJ.changeOutputFolder(FOLDER_PATH);
            log("Selecting / Changing Output Folder to "+FOLDER_PATH);

            outputFolderTextArea.setText(FOLDER_PATH);
        } 
        
        directoryChooser = null;
    }
    
    private Stage getStage(){
        return (Stage)Pane.getScene().getWindow();
    }
    
    @FXML
    private void captureScreenShot(ActionEvent event) {
        log("Capturing a Screenshot");
        getStage().hide();
        setTimeoutSync(() -> {
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
                
                String FILE_NAME = outputFolderTextArea.getText()+"\\Screenshot "+dtf.format(LocalDateTime.now()).replace(" "," at ")+".png";
                log(FILE_NAME);
                ImageIO.write(screen.createScreenCapture(screenDimention), "png", new File(FILE_NAME));
            } catch (IOException ex) {
                Logger.getLogger(RecorderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                 getStage().show();
            }
        },50);
        
    }

    @FXML
    private void changeFPS(MouseEvent event) {
        int FPS_CHANGE = (int)fpsSlider.getValue();
        log("Changed FPS to "+FPS_CHANGE);
        REC_OBJ.change_FRAMERATE(FPS_CHANGE);
    }

    @FXML
    private void changeOutputFolderTextAreaHandler(KeyEvent event) {
          try{
           String FOLDER_PATH = outputFolderTextArea.getText();
           Boolean existss = Files.isDirectory(Paths.get(FOLDER_PATH));
           if(existss){
                REC_OBJ.changeOutputFolder(FOLDER_PATH); 
                outputFolderTextArea.setStyle("-fx-text-inner-color: lightgrey");
           }
           else{
               outputFolderTextArea.setStyle("-fx-text-inner-color: red");
           }
        }catch(Exception e){}
    }

    @FXML
    private void uncheckedCloseBTNImage(MouseEvent event) {
        closeBTN_IMAGE.setImage(new Image(getClass().getResourceAsStream("assets/close-unchecked.png")));
        closeBTN_IMAGE.setEffect(null);

    }

    @FXML
    private void checkedCloseBTNImage(MouseEvent event) {
        closeBTN_IMAGE.setImage(new Image(getClass().getResourceAsStream("assets/close-checked-white.png")));
        closeBTN_IMAGE.setEffect(new Glow(0.45));
    }

    @FXML
    private void uncheckedMinimizeBTNImage(MouseEvent event) {
        minimizeBTN_IMAGE.setImage(new Image(getClass().getResourceAsStream("assets/minimize-unchecked.png")));
        minimizeBTN_IMAGE.setEffect(null);

    }

    @FXML
    private void checkedMinimizeBTNImage(MouseEvent event) {
        minimizeBTN_IMAGE.setImage(new Image(getClass().getResourceAsStream("assets/minimize-checked-white.png")));
        minimizeBTN_IMAGE.setEffect(new Glow(0.45));
    }

    public static void setTimeoutSync(Runnable runnable, int delay) {
        try {
            Thread.sleep(delay);
            runnable.run();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }
}
