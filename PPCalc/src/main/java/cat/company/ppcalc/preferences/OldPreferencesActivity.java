package cat.company.ppcalc.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import cat.company.ppcalc.R;

/**
 * Created by carles on 15/03/14.
 * Preference for pre-v9.
 */
public class OldPreferencesActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_main);
    }
}
