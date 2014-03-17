package cat.company.ppcalc.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import cat.company.ppcalc.R;
import cat.company.ppcalc.calculator.ProPointsCalculator;
import cat.company.ppcalc.util.TitleProvider;

/**
 * Created by carles on 13/01/14.
 * Propoints Calculator.
 */
public class ProPointsCalculatorFragment extends Fragment implements TitleProvider,SharedPreferences.OnSharedPreferenceChangeListener {
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.propoints, container,false);
        if(v==null)
            return null;
        Button bCalc = ((Button) v.findViewById(R.id.bCalculate));
        bCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
        SetUnits(v);
        return v;
    }

    private void SetUnits(View view) {
        String unit= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("selected_unit","grams");
        String[] unitsValues=getResources().getStringArray(R.array.unitsValues);
        String[] units=getResources().getStringArray(R.array.units);
        int index= Arrays.asList(unitsValues).indexOf(unit);
        String unitName=units[index];
        TextView carbsView = (TextView) view.findViewById(R.id.unitCarbs);
        carbsView.setText(unitName);
        TextView fatView=(TextView) view.findViewById(R.id.unitFat);
        fatView.setText(unitName);
        TextView fibreView=(TextView) view.findViewById(R.id.unitFibre);
        fibreView.setText(unitName);
        TextView proteinView=(TextView) view.findViewById(R.id.unitProtein);
        proteinView.setText(unitName);
    }

    public void calculate() {
        EditText tCarbs = (EditText) v.findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) v.findViewById(R.id.editProtein);
        EditText tFat = (EditText) v.findViewById(R.id.editFat);
        EditText tFibre = (EditText) v.findViewById(R.id.editFibre);
        AlertDialog.Builder bd = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        bd.setTitle(R.string.propoints);
        bd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Editable carbsText = tCarbs.getText();
        Editable proteinText = tProtein.getText();
        Editable fatText = tFat.getText();
        Editable fibreText = tFibre.getText();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unit=sharedPreferences.getString("selected_unit", "grams");
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
    }

    public void init() {
        EditText tCarbs = (EditText) v.findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) v.findViewById(R.id.editProtein);
        EditText tFat = (EditText) v.findViewById(R.id.editFat);
        EditText tFibre = (EditText) v.findViewById(R.id.editFibre);

        tCarbs.setText("");
        tProtein.setText("");
        tFat.setText("");
        tFibre.setText("");
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SetUnits(getView());
    }
}
