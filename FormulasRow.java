package sample;

/**
 * Created by Nykyta on 7/20/2017.
 */
public class FormulasRow {
    private int ID;
    private String formulaName;
    private String formula;
    private String outputUnit;

    public FormulasRow(int ID, String formulaName, String formula, String outputUnit) {
        this.ID = ID;
        this.formulaName = formulaName;
        this.formula = formula;
        this.outputUnit = outputUnit;
    }

    public int getID() {
        return ID;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public String getFormula() {
        return formula;
    }

    public String getOutputUnit() {
        return outputUnit;
    }
}
