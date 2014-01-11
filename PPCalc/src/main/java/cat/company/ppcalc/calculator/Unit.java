package cat.company.ppcalc.calculator;

/**
 * Class to contain unit and text for the Unit Spinner.
 * Created by carles on 11/01/14.
 */
public class Unit {
    public UnitEnum getId() {
        return id;
    }

    public void setId(UnitEnum id) {
        this.id = id;
    }

    public String getName() {
        return text;
    }

    public void setName(String name) {
        this.text = name;
    }

    public enum UnitEnum {
        Grams,
        Kilos
    }

    private UnitEnum id;
    private String text;

    public Unit(UnitEnum id, String text){
        this.id=id;
        this.text = text;
    }
}
