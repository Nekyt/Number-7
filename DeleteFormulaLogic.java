package sample;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nykyta on 7/20/2017.
 */
public class DeleteFormulaLogic implements Initializable {
    @FXML TableView formulaTableView;
    @FXML TableColumn<FormulasRow,Integer> IDColumn;
    @FXML TableColumn<FormulasRow,String> formulaNameColumn;
    @FXML TableColumn<FormulasRow,String> formulaColumn;
    @FXML TableColumn<FormulasRow,String> outputUnitColumn;
    @FXML Button deleteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        formulaNameColumn.setCellValueFactory(new PropertyValueFactory<>("formulaName"));
        formulaColumn.setCellValueFactory(new PropertyValueFactory<>("formula"));
        outputUnitColumn.setCellValueFactory(new PropertyValueFactory<>("outputUnit"));
        refreshFormulaTableView();
        if(Database.formulaList.get(0).getFormula().equals("(No Data)")){
            deleteButton.setDisable(true);
        }

    }
    void refreshFormulaTableView(){
        formulaTableView.setItems(Database.formulaList);
    }
    void tableEvent (){

    }
    @FXML void deleteFormula(){
        if(!(formulaTableView.getSelectionModel().isEmpty())) {
            int index = IDColumn.getCellData(formulaTableView.getSelectionModel().getSelectedIndex());
            System.out.println(index);
            System.out.println(formulaNameColumn.getCellData(formulaTableView.getSelectionModel().getSelectedIndex()));
            formulaNameColumn.getCellData(formulaTableView.getSelectionModel().getSelectedIndex());
            Database.deleteFromDatabase("Formulas", "ID", index);
            Database.deleteFromDatabase("Parameters", "FormulaName", formulaNameColumn.getCellData(formulaTableView.getSelectionModel().getSelectedIndex()));
            Database.instantiateTables();
            refreshFormulaTableView();
            if (Database.formulaList.get(0).getFormula().equals("(No Data)")) {
                deleteButton.setDisable(true);
            }
        }
        else {
            Dialogs.createDialog("Please make a selection first","error");
        }
    }

}
