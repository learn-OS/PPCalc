package cat.company.ppcalc.preferences;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import cat.company.ppcalc.R;

/**
 * Created by carles on 08/08/14.
 * Preferences for the point tracker.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TrackerPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_tracker);
    }
}
