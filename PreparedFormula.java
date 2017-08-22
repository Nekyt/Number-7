package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.awt.*;
import java.util.HashSet;

/**
 * Created by Nykyta on 7/22/2017.
 */
public class PreparedFormula {
    FormulasRow preparedFormula;
    ObservableList<ParametersRow> preparedParameters = FXCollections.observableArrayList();
    ObservableList<Double> convertedParametersValues =FXCollections.observableArrayList();
    StringBuilder logging =new StringBuilder();
    Double solvedExpression;

    Expression expression;

    PreparedFormula(String formulaName){
        getFormulaWithName(formulaName);
        getFormulaParameters(preparedFormula);
    }
    void getFormulaWithName(String formulaName){

        Database.formulaList.stream().filter(formula->formula.getFormulaName().equals(formulaName))
                .forEach(formula->{
            preparedFormula=formula;
        });
        logging.append("Evaluating expression "+preparedFormula.getFormulaName());
        logging.append(System.lineSeparator());
        }
        void solve(){
            System.out.println(preparedFormula.getFormulaName());
            preparedParameters.forEach(parameter->System.out.println(parameter.getParameter()));
            convertedParametersValues.forEach(System.out::println);
            HashSet<String> noName=new HashSet<>();
            preparedParameters.forEach(parameter -> noName.add(parameter.getParameter()));

            expression=new ExpressionBuilder(preparedFormula.getFormula()).variables(noName).build();
            for(int i=0;i<preparedParameters.size();i++){
                expression.setVariable(preparedParameters.get(i).getParameter(),convertedParametersValues.get(i));
            }
            solvedExpression=expression.evaluate();
            logging.append("Solution: "+solvedExpression+" in "+preparedFormula.getOutputUnit());
            logging.append(System.lineSeparator());
            logging.append("---------------------------------------------------------------");
            logging.append(System.lineSeparator());
        }

    void getFormulaParameters(FormulasRow formulasRow){
        Database.parametersList.stream().filter(parameter->parameter.getFormulaName().equals(formulasRow.getFormulaName())).forEach(parameter->{
            preparedParameters.add(parameter);
        });

    }
    void acceptParametersValues (ObservableList<Double> parameters, ObservableList<String> units){
       for(int u=0;u<units.size();u++){
           if(units.get(u).equals(preparedParameters.get(u).getUnit())){
               convertedParametersValues.add(parameters.get(u));
           }
           else {
               convertParameterValues(parameters.get(u),units.get(u),preparedParameters.get(u).getUnit());
               logging.append("Converting "+preparedParameters.get(u).getParameter()+" from "+units.get(u)+" to "+preparedParameters.get(u).getUnit()+". Result: "+convertedParametersValues.get(u));
               logging.append(System.lineSeparator());
           }
       }

    }



    void convertParameterValues(Double value, String from,String  to){
        UnitsRow fromUnit =Database.unitsList.stream().filter(unit -> unit.getUnit().equals(from)).findFirst().get();
        UnitsRow toUnit=Database.unitsList.stream().filter(unit -> unit.getUnit().equals(to)).findFirst().get();
        Double coefficient= toUnit.getRelation()/fromUnit.getRelation();
        convertedParametersValues.add(value*coefficient);

    }











}

