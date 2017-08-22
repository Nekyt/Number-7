package sample;

/**
 * Created by Nykyta on 7/20/2017.
 */
public class ParametersRow {
    private int x;
    private int ID;
    private String formulaName;
    private  String parameter;
    private String  unit;

    public ParametersRow(int ID,String formulaName, String parameter, String unit) {
        this.ID = ID;
        this.parameter = parameter;
        this.formulaName=formulaName;
        this.unit = unit;
    }

    public int getID() {
        return ID;
    }

    public String getParameter() {
        return parameter;
    }

    public String getUnit() {
        return unit;
    }
    public  String getFormulaName() {return formulaName;}
}
