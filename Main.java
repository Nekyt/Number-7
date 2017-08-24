package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Main extends Application {

    static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Startup.startup();
        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Number 7");
        primaryStage.setScene(new Scene(root, 603, 430));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:number7logo.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}

