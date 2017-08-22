package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Nykyta on 7/11/2017.
 */
public class Addformulalogic implements Initializable {
@FXML ChoiceBox<String> outputUnitChoiceBox;
@FXML TextField formulaContentIN;
@FXML TextFlow formulaPreview;
@FXML Label legalLabel;
@FXML TextField formulaNameIN;
@FXML VBox vBox1;
@FXML VBox vBox2;
@FXML Button addButton;
@FXML Pane addFormulaPane;
@FXML CheckBox bypassCheckBox;
private static final Pattern LITTLE_STAR=Pattern.compile("([\\d][A-Za-z])+|([A-Za-z][\\d])+|([\\d][\\(])+|([\\)][\\d])+|([\\)][A-Za-z])+");
private static final Pattern LETTERS=Pattern.compile("[A-Za-z]+");
ObservableList<Label> labels=FXCollections.observableArrayList();
ObservableList<ChoiceBox<String>> unitChoiceBoxes =FXCollections.observableArrayList();
ObservableList<String> parameters= FXCollections.observableArrayList();
ArrayList<Text> formulaPreviewArray=new ArrayList();
private boolean isLegal=false;
StringBuilder worked;
Matcher matcher;
Pattern oneSymbolAtOnce=Pattern.compile("([\\dA-Za-z\\(\\)]+[\\*\\-\\/\\+\\.\\^])+([\\dA-Za-z\\(\\)])+");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateFormulaPreview();
        outputUnitChoiceBox.getItems().addAll(Database.unitsNamesForChoiceBoxList);
        outputUnitChoiceBox.getSelectionModel().select(0);
        legalLabel.setFont(Font.font("System",FontWeight.BOLD,16));
    }
    @FXML void writeFormulaToDatabase(){
        if(isLegal) {
            Database.addFormula(formulaNameIN.getText(), worked.toString(), outputUnitChoiceBox.getValue());
            for(int z=0;z<unitChoiceBoxes.size();z++) {

                     Database.addParameter(formulaNameIN.getText(),labels.get(z).getText(),unitChoiceBoxes.get(z).getValue());

            }
            Database.instantiateTables();
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
            Dialogs.createDialog("Formula successfully saved!","information");


        }

    }

    @FXML void updateFormulaName(){
       checkIfLegal();
    }
    @FXML void updateFormulaPreview() {
        //Adds *
        worked=new StringBuilder(formulaContentIN.getText());
        matcher=LITTLE_STAR.matcher(worked.toString());
        while(matcher.find()){

                worked.insert(matcher.start() + 1, '*');
            matcher=LITTLE_STAR.matcher(worked.toString());
        }

        //Filters texts
        formulaPreviewArray.clear();
        for(int i=0;i<worked.length();i++){
            Text temporary=new Text();
            if(Character.toString(worked.charAt(i)).matches("[A-Za-z]")){
                temporary.setFill(Color.BLUE);
                temporary.setFont(Font.font("System",FontWeight.BOLD,16));
                temporary.setText(Character.toString(worked.charAt(i)));
                formulaPreviewArray.add(temporary);
            }
            else {
                temporary.setFont(Font.font("System",16));
                temporary.setText(Character.toString(worked.charAt(i)));
                formulaPreviewArray.add(temporary);
            }
        }
        //Adds all children to TextFlow
        formulaPreview.getChildren().clear();
        for(int i=0;i<formulaPreviewArray.size();i++){
            formulaPreview.getChildren().add(formulaPreviewArray.get(i));
        }
        //update Hboxes
        updateHboxes();
        //check if legal
        checkIfLegal();

    }
    void updateHboxes(){
        parameters.clear();
        vBox1.getChildren().clear();
        vBox2.getChildren().clear();
        unitChoiceBoxes.clear();
        labels.clear();
        matcher=LETTERS.matcher(worked);
        while(matcher.find()){
            if(!(worked.substring(matcher.start(), matcher.end()).matches("sin|cos|tan|sqrt"))) {
                parameters.add(worked.substring(matcher.start(), matcher.end()));
            }
        }
        parameters = FXCollections.observableArrayList(parameters.stream().distinct().collect(Collectors.toList()));

        for(int i=0;i<parameters.size();i++){
            labels.add(new Label(parameters.get(i)));
            labels.get(i).setTextFill(Color.BLUE);
            labels.get(i).setFont(Font.font("System",FontWeight.BOLD,15));
            unitChoiceBoxes.add(new ChoiceBox<String>());
            unitChoiceBoxes.get(i).setTooltip(new Tooltip("Select unit please"));
            unitChoiceBoxes.get(i).getItems().addAll(Database.unitsNamesForChoiceBoxList);
            unitChoiceBoxes.get(i).getSelectionModel().select(0);
            unitChoiceBoxes.get(i).getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> checkIfLegal());
        }
                vBox1.getChildren().addAll(labels);
                vBox2.getChildren().addAll(unitChoiceBoxes);


    }
    boolean performSameParametersCheck(){
        for(int y=0;y<parameters.size();y++){
            int test=0;
            for(int x=0;x<parameters.size();x++){
                if(parameters.get(x).equals(parameters.get(y))){
                    test++;
                    if(test==2){
                    return false;
                    }
                }

            }
        }
        return  true;
    }
   public void checkIfLegal(){

        boolean onlyLettersInName=true;
        boolean noSameFormulaName=true;
        boolean noNoData=true;
        boolean isFormulaValid=true;
        boolean onlyOneSymbolAtOnce=true;
        boolean atLeastOneParam=true;
        performMax8ParametersCheck();

        if(performNo2SignsInARowCheck()){
            onlyOneSymbolAtOnce=false;
            legalLabel.setText("Illegal - 2 signs in a row (or something else)");

        }


        if(checkForDatas()){
            noNoData=false;
            legalLabel.setText("Illegal - create a unit before !");


        }
        if(performNoSameFormulaCheck()){
            noSameFormulaName=false;
            legalLabel.setText("Illegal - formula with this name already exists ");
        }


        if(performLetterCheckInFormulName()){
            onlyLettersInName=false;
            legalLabel.setText("Illegal - invalid formula name field");
        }
        if(atLeastOneParameter()){
            atLeastOneParam=false;
            legalLabel.setText("Illegal - one parameter minimum");
        }
        if(checkExpression()){
            isFormulaValid=false;
            legalLabel.setText("Illegal - something is not right in the formula");
        }


            if((atLeastOneParam&&onlyLettersInName&&noSameFormulaName&&isFormulaValid&&onlyOneSymbolAtOnce&&noNoData)||bypassCheckBox.isSelected()==true){

                isLegal=true;
                legalLabel.setTextFill(Color.GREEN);
                legalLabel.setText("Congratulations, you are in law !");
                addButton.setDisable(false);
            }
            else {
                legalLabel.setTextFill(Color.RED);
                addButton.setDisable(true);
                isLegal=false;
            }



    }
    boolean performNo2SignsInARowCheck() {
        matcher=oneSymbolAtOnce.matcher(worked.toString());
        if(matcher.matches()){
            return false;
        }
        else {
            return true;
        }
    }
    boolean performNoSameFormulaCheck(){
        long amount= Database.formulaList.stream().filter(formula-> formula.getFormulaName().equals(formulaNameIN.getText())).count();
        System.out.println("amount"+amount);
        if(amount==0){
            return  false;
        }
        else {return  true;}
    }
    boolean atLeastOneParameter(){
        if(parameters.isEmpty()) {
            return true;
        }
        else { return false;}
    }
    boolean atLeastOneParaameter(){
     for(int i=0;i<unitChoiceBoxes.size();i++){
         if(!(unitChoiceBoxes.get(i).getValue().equals("Select a unit, don't change anything if it isn't a parameter(notations supported by exp4j)"))){
             return false;
         }
     }
     return true;

    }
    boolean checkForDatas(){
        if(Database.unitsNamesForChoiceBoxList.get(0).equals("(No Data)")){
            return  true;

        }
        else {return false;}

    }
    void performMax8ParametersCheck() {
        if (parameters.size() >= 8) {
            worked.setLength(0);
            vBox1.getChildren().clear();
            vBox2.getChildren().clear();
            formulaContentIN.clear();
            formulaPreview.getChildren().clear();
            Dialogs.createDialog("Max 8 parameters!!!", "error");
        }
    }
    boolean checkExpression(){
            try {
                Expression e = new ExpressionBuilder(worked.toString()).variables(new HashSet<String>(parameters)).build();
                ValidationResult val = e.validate(false);
                if (val.isValid()) {
                    return false;
                }
                else{ return true; }
            }
            catch (RuntimeException ex){
                return true;
            }



    }
    boolean performLetterCheckInFormulName(){
        matcher= LETTERS.matcher(formulaNameIN.getText());
        if(matcher.matches()){
            return false;
        }
        else {return true;}
    }





}
