package sample;

import javafx.scene.control.Alert;

/**
 * Created by Nykyta on 7/7/2017.
 */
public class Dialogs {
    static void createDialog(String message, String type){
        Alert alert=null;
        switch(type){
            case "warning":
                alert=new Alert(Alert.AlertType.WARNING);
                break;
            case "information":
                alert=new Alert(Alert.AlertType.INFORMATION);
                break;
            case "error":
                alert=new Alert(Alert.AlertType.ERROR);
                break;
            default:
                Alert secondAlert=new Alert(Alert.AlertType.ERROR);
                secondAlert.setContentText("Error in message dialog setup");
                secondAlert.showAndWait();
                return;

        }
        alert.setContentText(message);
        alert.showAndWait();
    }
}
