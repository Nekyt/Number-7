package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Application {

    static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        startup();
        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Number 7");
        primaryStage.setScene(new Scene(root, 603, 430));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
    private void  checkIfNotFistRun(){
        Path secretNumbersPath= Paths.get("secretNumbers.db");
        if(Files.exists(secretNumbersPath)){
            System.out.println("Not first run");
            Database.connectDatabase("jdbc:sqlite:secretNumbers.db");
            return;
        }
        else{
            System.out.println("First run");
            //First use script
            Database.connectDatabase("jdbc:sqlite:secretNumbers.db");
            try{
                PreparedStatement preparedFormula=Database.c.prepareStatement("CREATE TABLE Formulas (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,FormulaName TEXT NOT NULL,Formula TEXT NOT NULL, OutputUnit TEXT NOT NULL)");
                preparedFormula.execute();
                PreparedStatement preparedParameters =Database.c.prepareStatement("CREATE TABLE Parameters (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,FormulaName TEXT NOT NULL, Parameter TEXT NOT NULL, Unit TEXT NOT NULL)");
                preparedParameters.execute();
                PreparedStatement preparedUnits=Database.c.prepareStatement("CREATE TABLE Units (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Unit TEXT NOT NULL,Range TEXT NOT NULL,Relation REAL NOT NULL)");
                preparedUnits.execute();

            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
    private void startup(){
        checkIfNotFistRun();
        Database.instantiateTables();




    }
}


