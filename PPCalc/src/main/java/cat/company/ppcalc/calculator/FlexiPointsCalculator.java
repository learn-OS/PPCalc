package cat.company.ppcalc.calculator;

import android.text.Editable;

/**
 * Created by carles on 27/02/14.
 * Flexipoints calculator class.
 */
public class FlexiPointsCalculator {
    private double kiloCalories;
    private double fat;
    private String unit;
    private double portion;

    FlexiPointsCalculator(){
        kiloCalories=0;
        fat=0;
        portion=0;
        unit="grams";
    }

    public static FlexiPointsCalculator CreateInstance(){
        return new FlexiPointsCalculator();
    }

    public FlexiPointsCalculator setUnit(String unit){
        this.unit=unit;
        return this;
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
        double points = Math.max(Math.round((kiloCalories/60)+(adaptUnits(fat)/9)), 0);
        return (int) (points*adaptPortion(portion));
    }

    private double adaptPortion(double portion) {
        return portion/100;
    }

    private double EditableToDouble(Editable editable) {
        return editable != null && editable.length() > 0
                ? Double.parseDouble(editable.toString())
                : 0;
    }

    private double adaptUnits(double value) {
        if (unit.equals("kilograms"))
            return value * 1000;
        if (unit.equals("ounces"))
            return value * 28.349523125;
        if (unit.equals("pounds"))
            return value * 453.59237;
        return value;
    }

    public FlexiPointsCalculator setPortion(Editable portion) {
        this.portion=EditableToDouble(portion);
        return this;
    }
}
