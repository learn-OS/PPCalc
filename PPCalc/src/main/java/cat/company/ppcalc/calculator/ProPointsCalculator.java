package cat.company.ppcalc.calculator;

import android.text.Editable;

public class ProPointsCalculator {
    private double protein;
    private double carbs;
    private double fat;
    private double fibre;
    private String unit;

    private ProPointsCalculator() {
        protein = 0;
        carbs = 0;
        fat = 0;
        fibre = 0;
        unit= "grams";
    }

    public static ProPointsCalculator CreateInstance() {
        return new ProPointsCalculator();
    }

    public ProPointsCalculator setUnit(String unit){
        this.unit=unit;
        return this;
    }

    public ProPointsCalculator addProteins(double protein) {
        this.protein += protein;
        return this;
    }

    public ProPointsCalculator addProteins(Editable proteinText) {
        this.carbs += EditableToDouble(proteinText);
        return this;
    }

    public ProPointsCalculator addCarbs(double carbs) {
        this.carbs += carbs;
        return this;
    }

    public ProPointsCalculator addCarbs(Editable carbsText) {
        this.carbs += EditableToDouble(carbsText);
        return this;
    }

    public ProPointsCalculator addFat(double fat) {
        this.fat += fat;
        return this;
    }

    public ProPointsCalculator addFat(Editable fatText) {
        this.fat += EditableToDouble(fatText);
        return this;
    }

    public ProPointsCalculator addFibre(double fibre) {
        this.fibre += fibre;
        return this;
    }

    public ProPointsCalculator addFibre(Editable fibre) {
        this.fibre += EditableToDouble(fibre);
        return this;
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

    public int calculate() {
        double points = Math.max(Math.round((16 * adaptUnits(protein) + 19 * adaptUnits(carbs) + 45 * adaptUnits(fat) + 5 * adaptUnits(fibre)) / 175), 0);
        return (int) points;
    }
}