package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Nykyta on 7/19/2017.
 */
public class UnitsLogic implements Initializable {
    @FXML
    TextField relationTextField;
    @FXML
    TextField rangeTextField;
    @FXML
    TextField unitTextField;
    @FXML
    Button deleteUnitButton;
    @FXML
    TableView unitPreview;
    @FXML
    TableColumn<UnitsRow, Integer> ID;
    @FXML
    TableColumn<UnitsRow, String> Unit;
    @FXML
    TableColumn<UnitsRow, String> Range;
    @FXML
    TableColumn<UnitsRow, Double> Relation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        Range.setCellValueFactory(new PropertyValueFactory<>("range"));
        Relation.setCellValueFactory(new PropertyValueFactory<>("relation"));
        unitPreview.setItems(Database.unitsList);
        if (Database.unitsList.get(0).getUnit().equals("(No Data)")) {
            deleteUnitButton.setDisable(true);
        }
    }

    void refreshUnitPreview() {
        unitPreview.setItems(Database.unitsList);
    }

    boolean checkForUnits() {
        if (Database.parametersList.stream().filter(parameter -> parameter.getUnit().equals(Unit.getCellData(unitPreview.getSelectionModel().getSelectedIndex()))).findFirst().isPresent()) {
            Dialogs.createDialog("Some formulas use those units, delete those first", "error");
            return false;
        } else {
            return true;
        }
    }

    boolean checkForSelection() {
        if (unitPreview.getSelectionModel().isEmpty()) {
            Dialogs.createDialog("Please select an item", "error");
            return false;
        } else {
            return true;
        }
    }

    @FXML
    void deleteUnit() {
        if (checkForSelection() && checkForUnits()) {
            int index = ID.getCellData(unitPreview.getSelectionModel().getSelectedIndex());
            Database.deleteFromDatabase("Units", "ID", index);
            Database.instantiateTables();
            refreshUnitPreview();
            if (Database.unitsList.get(0).getUnit().equals("(No Data)")) {
                deleteUnitButton.setDisable(true);
            }
        }
    }

    boolean rangeConstraint() {
        if (unitTextField.getText().equals(rangeTextField.getText()) && !(relationTextField.getText().equals("1.0"))) {
            Dialogs.createDialog("If you declare a new range, the relation value should always be 1.0", "error");
            return false;
        }  else if (relationTextField.getText().equals("1.0") && !(unitTextField.getText().equals(rangeTextField.getText()))) {
            Dialogs.createDialog("If the relation value is 1.0, it should be a range", "error");
            return false;
        } else {

            return true;
        }
    }

    boolean checkValuesInTextFields() {
        if (!(unitTextField.getText().matches("[A-Za-z]+") && rangeTextField.getText().matches("[A-Za-z]+") && relationTextField.getText().matches("([\\d])+\\.([\\d])+"))) {
            Dialogs.createDialog("Please use only letters in the unit and range textFields. Also please use only decimal values in the relation textField. For example 10.0 instead of 10", "error");
            return false;
        } else {
            return true;
        }

    }

    @FXML
    void checkAndAdd() {
        if (checkValuesInTextFields() && rangeConstraint()) {

            Database.addUnit(unitTextField.getText(), rangeTextField.getText(), Double.parseDouble(relationTextField.getText()));
            unitTextField.clear();
            rangeTextField.clear();
            relationTextField.clear();
            Database.instantiateTables();
            refreshUnitPreview();
            deleteUnitButton.setDisable(false);

        }
    }
}