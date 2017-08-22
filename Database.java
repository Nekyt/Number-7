package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Nykyta on 7/7/2017.
 */
public class Database {
    static ObservableList<UnitsRow> unitsList = FXCollections.observableArrayList();
    static Connection c = null;
    static ObservableList<FormulasRow> formulaList = FXCollections.observableArrayList();
    static ObservableList<ParametersRow> parametersList =FXCollections.observableArrayList();
    static ObservableList<String> unitsRangeList=FXCollections.observableArrayList();
    static ObservableList<String> unitsNamesForChoiceBoxList= FXCollections.observableArrayList();

    static void connectDatabase(String connectionString) {
        try {
            c = DriverManager.getConnection(connectionString);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            Dialogs.createDialog("Could not connect to database", "error");
            System.out.println("Sql exception");
            e.printStackTrace();
            System.exit(1);
        }
    }
    static void addParameter(String formulaName,String parameterName,String parameterUnit){
        try {
            PreparedStatement parameterParameterAdd = c.prepareStatement("INSERT INTO Parameters (FormulaName,Parameter,Unit) VALUES (?,?,?)");
            parameterParameterAdd.setString(1,formulaName);
            parameterParameterAdd.setString(2,parameterName);
            parameterParameterAdd.setString(3,parameterUnit);
            parameterParameterAdd.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    static void addFormula(String FormulaName, String Formula, String OutputUnit) {
        try {
            PreparedStatement preparedFormulaAdd = c.prepareStatement("INSERT INTO Formulas (FormulaName,Formula,OutputUnit) VALUES (?,?,?)");
            preparedFormulaAdd.setString(1, FormulaName);
            preparedFormulaAdd.setString(2, Formula);
            preparedFormulaAdd.setString(3, OutputUnit);
            preparedFormulaAdd.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static void instantiateTables() {
        formulaList.clear();
        unitsList.clear();
        parametersList.clear();
        unitsRangeList.clear();
        unitsNamesForChoiceBoxList.clear();

        try {
            //instantiate Formulas
            PreparedStatement prepared = c.prepareStatement("SELECT * FROM FORMULAS");
            ResultSet rs = prepared.executeQuery();
            if(rs.isBeforeFirst()){
                while (rs.next()) {
                    formulaList.add(new FormulasRow(rs.getInt("ID"),rs.getString("FormulaName"),rs.getString("Formula"),rs.getString("OutputUnit")));
                }
            }
            else {
               formulaList.add(new FormulasRow(1,"(No Data)","(No Data)","(No Data)"));
            }
            //instantiate Units
            prepared=c.prepareStatement("SELECT * FROM Units");
            rs=prepared.executeQuery();
            if(rs.isBeforeFirst()){
                while (rs.next()) {
                    unitsList.add(new UnitsRow(rs.getInt("ID"), rs.getString("Unit"), rs.getString("Range"), rs.getDouble("Relation")));
                }
            }
            else {
                unitsList.add(new UnitsRow(1,"(No Data)","(No Data)",0.0));
            }
            //instantiate Parameters
            prepared=c.prepareStatement("SELECT * FROM PARAMETERS");
            rs=prepared.executeQuery();
            if(rs.isBeforeFirst()){
                while (rs.next()) {
                    parametersList.add(new ParametersRow(rs.getInt("ID"),rs.getString("FormulaName"),rs.getString("Parameter"),rs.getString("Unit")));
                }
            }
            else {
                ;
            }
            rs.close();
            //unitsRangeList
            for(int i=0;i<unitsList.size();i++){
                if(unitsList.get(i).getRelation()==1.0){
                    unitsRangeList.add(unitsList.get(i).getRange());
                }
                else {;}
            }
            if(unitsRangeList.isEmpty()){
                unitsRangeList.add("(No Data)");
            }
            // unitsNamesForChoiceBoxList
            for(int u=0;u<unitsList.size();u++){
                unitsNamesForChoiceBoxList.add(unitsList.get(u).getUnit());
            }
            if (unitsNamesForChoiceBoxList.isEmpty()){
                unitsNamesForChoiceBoxList.add("(No Data)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteFromDatabase(String tableName,String Identifier,int location){
        try{
        PreparedStatement deleteRecord=c.prepareStatement("DELETE FROM "+tableName+" WHERE "+Identifier+"="+location);
        deleteRecord.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    static void deleteFromDatabase(String tableName,String Identifier,String location){
        try{
            PreparedStatement deleteRecord=c.prepareStatement("DELETE FROM "+tableName+" WHERE "+Identifier+"="+"'"+location+"'");
            deleteRecord.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    static void addUnit(String unitName,String unitRange,Double unitRelation){
        try {
            PreparedStatement addUnit = c.prepareStatement("INSERT INTO Units(Unit,Range,Relation) VALUES (?,?,?)");
            addUnit.setString(1,unitName);
            addUnit.setString(2,unitRange);
            addUnit.setDouble(3,unitRelation);
            addUnit.execute();
        }
        catch (SQLException e){e.printStackTrace();}
    }



}