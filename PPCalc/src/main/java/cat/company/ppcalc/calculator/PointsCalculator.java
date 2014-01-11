package cat.company.ppcalc.calculator;

import android.text.Editable;

public class PointsCalculator {
    private double protein;
    private double carbs;
    private double fat;
    private double fibre;
    private Unit.UnitEnum unit;

    private PointsCalculator() {
        protein = 0;
        carbs = 0;
        fat = 0;
        fibre = 0;
        unit= Unit.UnitEnum.Grams;
    }

    public static PointsCalculator CreateInstance() {
        return new PointsCalculator();
    }

    public PointsCalculator setUnit(Unit.UnitEnum unit){
        this.unit=unit;
        return this;
    }

    public PointsCalculator addProteins(double protein) {
        this.protein += protein;
        return this;
    }

    public PointsCalculator addProteins(Editable proteinText) {
        this.carbs += EditableToDouble(proteinText);
        return this;
    }

    public PointsCalculator addCarbs(double carbs) {
        this.carbs += carbs;
        return this;
    }

    public PointsCalculator addCarbs(Editable carbsText) {
        this.carbs += EditableToDouble(carbsText);
        return this;
    }

    public PointsCalculator addFat(double fat) {
        this.fat += fat;
        return this;
    }

    public PointsCalculator addFat(Editable fatText) {
        this.fat += EditableToDouble(fatText);
        return this;
    }

    public PointsCalculator addFibre(double fibre) {
        this.fibre += fibre;
        return this;
    }

    public PointsCalculator addFibre(Editable fibre) {
        this.fibre += EditableToDouble(fibre);
        return this;
    }

    private double EditableToDouble(Editable editable) {
        return editable != null && editable.length() > 0
                ? Double.parseDouble(editable.toString())
                : 0;
    }

    private double adaptUnits(double value){
        switch (unit){
            case Kilos:
                return  value*1000;
            case Ounce:
                return value*28.349523125;
            case Pounds:
                return value*453.59237;
        }
        return value;
    }

    public int calculate() {
        double points = Math.max(Math.round((16 * adaptUnits(protein) + 19 * adaptUnits(carbs) + 45 * adaptUnits(fat) + 5 * adaptUnits(fibre)) / 175), 0);
        return (int) points;
    }
}