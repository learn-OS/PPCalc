package cat.company.ppcalc.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import java.util.ArrayList;

import cat.company.ppcalc.R;
import cat.company.ppcalc.adapters.UnitSpinnerAdapter;
import cat.company.ppcalc.calculator.ProPointsCalculator;
import cat.company.ppcalc.calculator.Unit;
import cat.company.ppcalc.util.TitleProvider;

/**
 * Created by carles on 13/01/14.
 */
public class ProPointsCalculatorFragment extends Fragment implements TitleProvider {

    private Unit.UnitEnum unit;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.propoints, container,false);
        if(v==null)
            return null;
        unit= Unit.UnitEnum.Grams;
        Button bCalc = ((Button) v.findViewById(R.id.bCalculate));
        bCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
        ArrayList<Unit> itemsUnit=new ArrayList<Unit>();
        itemsUnit.add(new Unit(Unit.UnitEnum.Grams,this.getResources().getString(R.string.grams)));
        itemsUnit.add(new Unit(Unit.UnitEnum.Kilos,this.getResources().getString(R.string.kilos)));
        itemsUnit.add(new Unit(Unit.UnitEnum.Ounce,this.getResources().getString(R.string.ounces)));
        itemsUnit.add(new Unit(Unit.UnitEnum.Pounds,this.getResources().getString(R.string.pounds)));
        Spinner spUnits=(Spinner) v.findViewById(R.id.unit);
        UnitSpinnerAdapter spinner_adapter = new UnitSpinnerAdapter(getActivity(),itemsUnit);
        spUnits.setAdapter(spinner_adapter);
        spUnits.setSelection(getActivity().getPreferences(Context.MODE_PRIVATE).getInt("spinnerPosition",0));
        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.unit) {
                    Unit item = (Unit) parent.getItemAtPosition(position);
                    if (item != null) {
                        unit = item.getId();
                        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
                        editor.putInt("spinnerPosition", position);
                        editor.commit();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    public void calculate() {
        EditText tCarbs = (EditText) v.findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) v.findViewById(R.id.editProtein);
        EditText tFat = (EditText) v.findViewById(R.id.editFat);
        EditText tFibre = (EditText) v.findViewById(R.id.editFibre);
        AlertDialog.Builder bd = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        bd.setTitle(R.string.points);
        bd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Editable carbsText = tCarbs.getText();
        Editable proteinText = tProtein.getText();
        Editable fatText = tFat.getText();
        Editable fibreText = tFibre.getText();
        int points = ProPointsCalculator
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
        EasyTracker easyTracker = EasyTracker.getInstance(getActivity());

        easyTracker.send(MapBuilder
                .createEvent("ui_action",
                        "button_press",
                        "calculate_points",
                        (long) points)
                .build());
    }

    public void init() {
        EditText tCarbs = (EditText) v.findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) v.findViewById(R.id.editProtein);
        EditText tFat = (EditText) v.findViewById(R.id.editFat);
        EditText tFibre = (EditText) v.findViewById(R.id.editFibre);
        Spinner spUnits=(Spinner) v.findViewById(R.id.unit);

        tCarbs.setText("");
        tProtein.setText("");
        tFat.setText("");
        tFibre.setText("");
        spUnits.setSelection(getActivity().getPreferences(Context.MODE_PRIVATE).getInt("spinnerPosition",0));
        tProtein.requestFocus();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public String getTitle() {
        return "ProPoints";
    }
}
