package cat.company.ppcalc.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import cat.company.ppcalc.R;
import cat.company.ppcalc.db.DayPointsProviderMetadata;

/**
 * A placeholder fragment containing a simple view.
 */
public class PointTrackerFragment extends Fragment {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_point_tracker, container, false);
        ListView list= (ListView) v.findViewById(R.id.pointsList);
        Uri uri = DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI;
        Activity a = getActivity();
        Cursor c = getActivity().getContentResolver().query(uri,
                null, //projection
                null, //selection string
                null, //selection args array of strings
                null); //sort order
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(getActivity(),R.layout.day_points_list_item,c,
                new String[]{DayPointsProviderMetadata.DayPointsTableMetadata.DATE, DayPointsProviderMetadata.DayPointsTableMetadata.POINTS},
                new int[]{R.id.date,R.id.points});
        list.setAdapter(adapter);
        return v;
    }
}
