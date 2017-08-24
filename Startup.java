package sample;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Nykyta on 8/22/2017.
 */
public class Startup {
     static void startup(){
        checkIfNotFistRun();
        Database.instantiateTables();


    }
    static void  checkIfNotFistRun(){
        Path secretNumbersPath= Paths.get("secretNumbers.db");
        if(Files.exists(secretNumbersPath)){
            Database.connectDatabase("jdbc:sqlite:secretNumbers.db");
            return;
        }
        else{
            //First use script
            Database.connectDatabase("jdbc:sqlite:secretNumbers.db");
            try{
                PreparedStatement preparedFormula=Database.c.prepareStatement("CREATE TABLE Formulas (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,FormulaName TEXT NOT NULL,Formula TEXT NOT NULL, OutputUnit TEXT NOT NULL)");
                preparedFormula.execute();
                PreparedStatement preparedParameters =Database.c.prepareStatement("CREATE TABLE Parameters (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,FormulaName TEXT NOT NULL, Parameter TEXT NOT NULL, Unit TEXT NOT NULL)");
                preparedParameters.execute();
                PreparedStatement preparedUnits=Database.c.prepareStatement("CREATE TABLE Units (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Unit TEXT NOT NULL,Range TEXT NOT NULL,Relation REAL NOT NULL)");
                preparedUnits.execute();
                ///////
                Database.addUnit("UA","UA",1.0);

            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
}
