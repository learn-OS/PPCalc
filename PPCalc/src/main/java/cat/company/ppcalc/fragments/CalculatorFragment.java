package cat.company.ppcalc.fragments;



import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import cat.company.ppcalc.PagerProvider;
import cat.company.ppcalc.PointsCalculatorActivity;
import cat.company.ppcalc.R;
import cat.company.ppcalc.util.TitleProvider;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CalculatorFragment extends Fragment implements PreferenceChangeListener,TitleProvider {


    private Fragment fragment;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        changeCalculator();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeCalculator();
    }

    private void changeCalculator() {
        String page = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("default_page", "propoints");
        if(page.equals("propoints"))
            fragment =new ProPointsCalculatorFragment();
        else if(page.equals("flexipoints"))
            fragment =new FlexiPointsCalculatorFragment();
        else if(page.equals("pointsplus"))
            fragment =new PointsPlusCalculatorFragment();
        else
            fragment =new ProPointsCalculatorFragment();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calculator_container, fragment);
        transaction.commit();
        if(getActivity() instanceof PagerProvider) {
            PagerProvider activity = (PagerProvider) getActivity();
            activity.getPagerAdapter().notifyDataSetChanged();
            TitleProvider currentFragment = (TitleProvider)((PointsCalculatorActivity) activity).getCurrentFragment();
            ((ActionBarActivity)activity).getSupportActionBar().setTitle(currentFragment.getTitle());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent preferenceChangeEvent) {
        changeCalculator();
    }

    @Override
    public String getTitle() {
        if(fragment instanceof TitleProvider)
            return ((TitleProvider)fragment).getTitle();
        return "Calculator";
    }
}
