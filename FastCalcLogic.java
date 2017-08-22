package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


/**
 * Created by Nykyta on 8/22/2017.
 */
public class FastCalcLogic {
    @FXML TextField expressionField;
    @FXML Label answerLabel;

    @FXML void close(){
        Stage stage=(Stage) expressionField.getScene().getWindow();
        stage.close();

    }

    @FXML void calculate(){
        try {
            Expression e =new ExpressionBuilder(expressionField.getText()).build();
            Double result=e.evaluate();
            answerLabel.setText(result.toString());
        }
        catch (RuntimeException ee){
            ee.printStackTrace();
            answerLabel.setText("Error");
        }

    }


}
