package cat.company.ppcalc.preferences;

import android.annotation.TargetApi;
import android.preference.PreferenceActivity;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.List;

import cat.company.ppcalc.R;

/**
 * Created by carles on 18/03/14.
 */
public class PreferencesActivity extends PreferenceActivity {

    @Override
    @TargetApi(11)
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preference_headers,target);
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
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
