package cat.company.ppcalc.calculator;

import android.text.Editable;

/**
 * Created by carles on 27/02/14.
 */
public class FlexiPointsCalculator {
    private double kiloCalories;
    private double fat;

    FlexiPointsCalculator(){
        kiloCalories=0;
        fat=0;
    }

    public static FlexiPointsCalculator CreateInstance(){
        return new FlexiPointsCalculator();
    }

    public FlexiPointsCalculator addCalories(double kiloCalories) {
        this.kiloCalories += kiloCalories;
        return this;
    }

    public FlexiPointsCalculator addCalories(Editable kiloCalories) {
        this.kiloCalories += EditableToDouble(kiloCalories);
        return this;
    }

    public FlexiPointsCalculator addFat(double fat) {
        this.fat += fat;
        return this;
    }

    public FlexiPointsCalculator addFat(Editable fat) {
        this.fat += EditableToDouble(fat);
        return this;
    }

    public int calculate() {
        double points = Math.max(Math.round((kiloCalories/60)+(fat/9)), 0);
        return (int) points;
    }

    private double EditableToDouble(Editable editable) {
        return editable != null && editable.length() > 0
                ? Double.parseDouble(editable.toString())
                : 0;
    }
}
