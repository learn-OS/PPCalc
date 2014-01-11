package cat.company.ppcalc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import java.util.ArrayList;

import cat.company.ppcalc.adapters.UnitSpinnerAdapter;
import cat.company.ppcalc.calculator.PointsCalculator;
import cat.company.ppcalc.calculator.Unit;

public class MainActivity extends ActionBarActivity {
    private Unit.UnitEnum unit;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unit= Unit.UnitEnum.Grams;
        setContentView(R.layout.main);
        Button bCalc = ((Button) findViewById(R.id.bCalculate));
        bCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate(v);
            }
        });
        ArrayList<Unit> itemsUnit=new ArrayList<Unit>();
        itemsUnit.add(new Unit(Unit.UnitEnum.Grams,this.getResources().getString(R.string.grams)));
        itemsUnit.add(new Unit(Unit.UnitEnum.Kilos,this.getResources().getString(R.string.kilos)));
        Spinner spUnits=(Spinner) findViewById(R.id.unit);
        UnitSpinnerAdapter spinner_adapter = new UnitSpinnerAdapter(this,itemsUnit);
        spUnits.setAdapter(spinner_adapter);
        spUnits.setSelection(0);
        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.unit) {
                    Unit item = (Unit) parent.getItemAtPosition(position);
                    if (item != null) {
                        unit = item.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.init:
                init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calculate(View view) {
        EditText tCarbs = (EditText) findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) findViewById(R.id.editProtein);
        EditText tFat = (EditText) findViewById(R.id.editFat);
        EditText tFibre = (EditText) findViewById(R.id.editFibre);
        AlertDialog.Builder bd = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        bd.setTitle(R.string.points);
        bd.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Editable carbsText = tCarbs.getText();
        Editable proteinText = tProtein.getText();
        Editable fatText = tFat.getText();
        Editable fibreText = tFibre.getText();
        int points = PointsCalculator
                .CreateInstance()
                .setUnit(unit)
                .addCarbs(carbsText)
                .addProteins(proteinText)
                .addFat(fatText)
                .addFibre(fibreText)
                .calculate();
        bd.setMessage(getResources().getQuantityString(R.plurals.num_points,
                points, points));
        bd.show();
        EasyTracker easyTracker = EasyTracker.getInstance(this);

        easyTracker.send(MapBuilder
                .createEvent("ui_action",
                        "button_press",
                        "calculate_points",
                        (long) points)
                .build());
    }

    public void init() {
        EditText tCarbs = (EditText) findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) findViewById(R.id.editProtein);
        EditText tFat = (EditText) findViewById(R.id.editFat);
        EditText tFibre = (EditText) findViewById(R.id.editFibre);
        Spinner spUnits=(Spinner) findViewById(R.id.unit);

        tCarbs.setText("");
        tProtein.setText("");
        tFat.setText("");
        tFibre.setText("");
        spUnits.setSelection(0);
        tProtein.requestFocus();
    }
}