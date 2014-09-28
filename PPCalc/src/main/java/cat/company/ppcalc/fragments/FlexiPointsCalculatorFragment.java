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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Arrays;

import cat.company.ppcalc.Application;
import cat.company.ppcalc.R;
import cat.company.ppcalc.calculator.FlexiPointsCalculator;
import cat.company.ppcalc.util.TitleProvider;

/**
 * Created by carles on 28/02/14.
 * Fragment for the flexipoint calculator.
 */
public class FlexiPointsCalculatorFragment extends Fragment implements TitleProvider,SharedPreferences.OnSharedPreferenceChangeListener {

    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_flexipoints, container,false);
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

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((Application) getActivity().getApplication()).getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Flexipoints");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void calculate() {
        EditText tFat = (EditText) v.findViewById(R.id.editFat);
        EditText tCal = (EditText) v.findViewById(R.id.editKCal);
        EditText tPortion =(EditText) v.findViewById(R.id.editPortion);
        AlertDialog.Builder bd = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        bd.setTitle(R.string.flexipoints);
        bd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Editable fatText = tFat.getText();
        Editable calText = tCal.getText();
        Editable portionText=tPortion.getText();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unit=sharedPreferences.getString("selected_unit", "grams");
        int points= FlexiPointsCalculator.CreateInstance()
                .setUnit(unit)
                .setPortion(portionText)
                .addFat(fatText)
                .addCalories(calText)
                .calculate();
        bd.setMessage(getResources().getQuantityString(R.plurals.num_points,
                points, points));
        bd.show();
    }

    public void init() {
        EditText tFat = (EditText) v.findViewById(R.id.editFat);
        EditText tKcal = (EditText) v.findViewById(R.id.editKCal);
        EditText tPortion=(EditText) v.findViewById(R.id.editPortion);

        tFat.setText("");
        tKcal.setText("");
        tPortion.setText("100");
        tFat.requestFocus();
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
    public String getTitle() {
        return "Flexipoints";
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        View view = getView();
        if(view!=null)
            SetUnits(view);
    }

    private void SetUnits(View view) {
        String unit= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("selected_unit","grams");
        String[] unitsValues=getResources().getStringArray(R.array.unitsValues);
        String[] units=getResources().getStringArray(R.array.units);
        int index= Arrays.asList(unitsValues).indexOf(unit);
        String unitName=units[index];
        TextView fatView = (TextView) view.findViewById(R.id.unitFat);
        fatView.setText(unitName);
    }
}
