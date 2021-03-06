package cat.company.ppcalc.preferences;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import cat.company.ppcalc.Application;
import cat.company.ppcalc.R;

/**
 * Created by carles on 18/03/14.
 * Preference activity.
 */
public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hola");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            addPreferencesFromResource(R.xml.preference_main);
            addPreferencesFromResource(R.xml.preference_tracker);
        }
    }

    @Override
    @TargetApi(11)
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preference_headers,target);
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((Application) getApplication()).getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Preferences");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
