package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    ListView<String> formulaList;
    @FXML
    Label formulaLabel;
    @FXML
    VBox vBox1;
    @FXML VBox vBox2;
    @FXML
    Button calculateButton;
    @FXML VBox vBox3;
    @FXML TextArea resultArea;

    PreparedFormula preparedFormula;
    ObservableList<Label>labels= FXCollections.observableArrayList();
    ObservableList<ChoiceBox<String >> choiceBoxes=FXCollections.observableArrayList();
    ObservableList<TextField> textFields=FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshFormulalist();
    }

    void refreshFormulalist() {
        formulaList.getItems().clear();
        for (int i = 0; i < Database.formulaList.size(); i++) {
            formulaList.getItems().add(Database.formulaList.get(i).getFormulaName());
        }
        formulaList.getSelectionModel().select(0);
        itemSelected();
    }
    boolean checkIfLegal(){
        boolean isLegal=true;
         for(int i=0;i<textFields.size();i++){
             if(textFields.get(i).getText().matches("(-?(\\d)+)|(-?(\\d)+\\.(\\d)+)")){
                 ;
             }
             else {
                 isLegal=false;
             }

         }
         return  isLegal;
        }
    @FXML void calculate(){
        if(checkIfLegal()) {
            ObservableList<Double> parameterValues = FXCollections.observableArrayList();
            ObservableList<String> userUnits = FXCollections.observableArrayList();
            textFields.stream().forEach(value -> parameterValues.add(Double.parseDouble(value.getText())));
            choiceBoxes.stream().forEach(unit -> userUnits.add(unit.getValue()));
            preparedFormula.acceptParametersValues(parameterValues, userUnits);
            preparedFormula.solve();
            resultArea.appendText(preparedFormula.logging.toString());
            preparedFormula = new PreparedFormula(formulaList.getSelectionModel().getSelectedItem());
        }
        else {
            Dialogs.createDialog("Please enter only numbers in parameters textFields.\nThanks a lot!","error");
        }
    }
    void clearEverything(){
        vBox1.getChildren().clear();
        vBox2.getChildren().clear();
        vBox3.getChildren().clear();
        choiceBoxes.clear();
        labels.clear();
        textFields.clear();
    }
    public void itemSelected() {
        clearEverything();
        preparedFormula=new PreparedFormula(formulaList.getSelectionModel().getSelectedItem());
        int index = formulaList.getSelectionModel().getSelectedIndex();
        formulaLabel.setText(Database.formulaList.get(index).getFormula());
        String formulaName = formulaList.getSelectionModel().getSelectedItem();
        preparedFormula.preparedParameters.stream().forEach(parameter->{
            //labels

            Label temp=new Label(parameter.getParameter()+" in "+parameter.getUnit());
            temp.setFont(Font.font("System",FontWeight.BOLD,16));
            labels.add(temp);
            textFields.add(new TextField());
            String searchedRange=Database.unitsList.stream().filter(unit -> unit.getUnit().equals(parameter.getUnit())).map(unit -> unit.getRange()).findFirst().get();
            ObservableList<String> sortedUnitsRanges = FXCollections.observableArrayList(Database.unitsList.stream().filter(unit -> unit.getRange().equals(searchedRange)).map(unit->unit.getUnit()).collect(Collectors.toSet()));
            ChoiceBox<String> tempChoice=new ChoiceBox();
            tempChoice.getItems().addAll(sortedUnitsRanges);
            tempChoice.setValue(parameter.getUnit());
            choiceBoxes.add(tempChoice);
        });
        vBox1.getChildren().addAll(labels);
        vBox2.getChildren().addAll(textFields);
        vBox3.getChildren().addAll(choiceBoxes);



    }


    @FXML
    void displayRemoveFormulaForm() {
        try {
            Parent deleteFormula = FXMLLoader.load(getClass().getResource("Deleteformula.fxml"));
            Stage deleteFormulaStage = new Stage();
            deleteFormulaStage.setTitle("Number 7 : Remove Formula");
            deleteFormulaStage.setResizable(false);
            deleteFormulaStage.setScene(new Scene(deleteFormula, 424, 400));
            deleteFormulaStage.initModality(Modality.WINDOW_MODAL);
            deleteFormulaStage.initOwner(Main.primaryStage);
            deleteFormulaStage.getIcons().add(new Image(getClass().getResourceAsStream("/number7logo.png")));

            deleteFormulaStage.showAndWait();
            refreshFormulalist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void displayUnitsForm() {
        try {
            Parent units = FXMLLoader.load(getClass().getResource("Units.fxml"));
            Stage unitsStage = new Stage();
            unitsStage.setTitle("Number 7 : Units");
            unitsStage.setScene(new Scene(units, 426, 430));
            unitsStage.setResizable(false);
            unitsStage.initModality(Modality.WINDOW_MODAL);
            unitsStage.initOwner(Main.primaryStage);
            unitsStage.getIcons().add(new Image(getClass().getResourceAsStream("/number7logo.png")));
            unitsStage.showAndWait();
            refreshFormulalist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayAddFormulaForm() {
        try {
            Parent addFormula = FXMLLoader.load(getClass().getResource("Addformula.fxml"));
            Stage addFormulaStage = new Stage();
            addFormulaStage.setTitle("Number 7 : Add Formula");
            addFormulaStage.setScene(new Scene(addFormula, 448, 479));
            addFormulaStage.setResizable(false);
            addFormulaStage.initModality(Modality.WINDOW_MODAL);
            addFormulaStage.initOwner(Main.primaryStage);
            addFormulaStage.getIcons().add(new Image(getClass().getResourceAsStream("/number7logo.png")));
            addFormulaStage.showAndWait();
            refreshFormulalist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML void  showFastCalc(){
        try {
            Parent fastCalc = FXMLLoader.load(getClass().getResource("FastCalc.fxml"));
            Stage fastCalcStage=new Stage();
            fastCalcStage.setScene(new Scene(fastCalc));
            fastCalcStage.setResizable(false);
            fastCalcStage.setTitle("Number 7 : FastCalc");
            fastCalcStage.getIcons().add(new Image(getClass().getResourceAsStream("/number7logo.png")));
            fastCalcStage.show();
        }
        catch (IOException e){
             e.printStackTrace();
        }
    }
   @FXML void close(){
        System.exit(0);
    }
}
