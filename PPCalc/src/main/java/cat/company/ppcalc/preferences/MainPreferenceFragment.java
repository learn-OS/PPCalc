package cat.company.ppcalc.preferences;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import cat.company.ppcalc.R;

/**
 * Created by carles on 18/03/14.
 */
@TargetApi(11)
@SuppressLint("NewApi")
public class MainPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_main);
    }
}
