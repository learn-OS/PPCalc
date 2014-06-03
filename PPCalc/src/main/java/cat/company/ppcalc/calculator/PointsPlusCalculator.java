package cat.company.ppcalc.calculator;

/**
 * PointsPlus calculator.
 * Created by carles on 03/06/14.
 */
public class PointsPlusCalculator extends ProPointsCalculator {
    private PointsPlusCalculator() {
        super();
    }

    @Override
    public int calculate() {
        double points = Math.max(Math.round((16 * adaptUnits(protein) + 19 * adaptUnits(carbs) + 45 * adaptUnits(fat) - 14 * adaptUnits(fibre)) / 175), 0);
        return (int) (points*adaptPortion(portion));
    }
}
