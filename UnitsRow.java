package sample;

/**
 * Created by Nykyta on 7/19/2017.
 */
public class UnitsRow {
    int ID;
    private String unit;
    private String range;
    private Double relation;
    UnitsRow(int ID, String unit, String range, Double relation){
        this.ID=ID;
        this.unit=unit;
        this.range=range;
        this.relation=relation;
    }
    public Integer getID(){return ID;}
    public String getUnit(){
        return  unit;
    }
    public String getRange(){
        return range;
    }
    public Double getRelation(){return relation;}
}
