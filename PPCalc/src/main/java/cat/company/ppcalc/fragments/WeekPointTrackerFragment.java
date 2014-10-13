package cat.company.ppcalc.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import cat.company.ppcalc.Application;
import cat.company.ppcalc.R;
import cat.company.ppcalc.adapters.WeekPointsCursorAdapter;
import cat.company.ppcalc.db.DayPointsProviderMetadata;
import cat.company.ppcalc.interfaces.IRefreshable;
import cat.company.ppcalc.util.TitleProvider;

/**
 * Fragment for the point tracker day view.
 */

public class WeekPointTrackerFragment extends Fragment implements IRefreshable,TitleProvider {

    public static final String TAG = "WeekPointTrackerFragment";
private final Uri uri = DayPointsProviderMetadata.DayPointsTableMetadata.WEEK_URI;
    private WeekPointsCursorAdapter adapter;
    private View view;
    private Date date;
    private GregorianCalendar gregorianCalendar;

    public WeekPointTrackerFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_point_tracker, container, false);
        ListView list = (ListView) view.findViewById(R.id.pointsList);
        adapter = new WeekPointsCursorAdapter(getActivity(), null, 0);

        reload();
        list.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Cursor cursor = getActivity().getContentResolver().acquireContentProviderClient(uri).query(uri, null,
                    null, null, null);
            adapter.swapCursor(cursor);

            TextView tvTotal = (TextView) view.findViewById(R.id.totalPoints);
            Integer daily_allowance = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("point_allowance", "27"));
            tvTotal.setText("44");
        } catch (Exception ex) {
            Log.e(TAG, "Error loading.", ex);
        }
    }

    @Override
    public void Refresh() {
        reload();
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((Application) getActivity().getApplication()).getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("DayTracker");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public String getTitle() {
        return Application.getContext().getString(R.string.week);
    }
}
