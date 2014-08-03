package cat.company.ppcalc.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cat.company.ppcalc.R;
import cat.company.ppcalc.db.DayPointsContentProvider;
import cat.company.ppcalc.db.DayPointsProviderMetadata;

/**
 * A placeholder fragment containing a simple view.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PointTrackerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private View v;
    private final Uri uri = DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI;
    private final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.day_points_list_item, null,
            new String[]{DayPointsProviderMetadata.DayPointsTableMetadata.COMMENT, DayPointsProviderMetadata.DayPointsTableMetadata.POINTS},
            new int[]{R.id.comment, R.id.points});

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_point_tracker, container, false);
        ListView list= (ListView) v.findViewById(R.id.pointsList);
        Activity a = getActivity();
        ContentValues values=new ContentValues();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            values.put(DayPointsProviderMetadata.DayPointsTableMetadata.DATE, sdf.parse("2014-08-03").getTime());
            values.put(DayPointsProviderMetadata.DayPointsTableMetadata.POINTS,3);
            values.put(DayPointsProviderMetadata.DayPointsTableMetadata.COMMENT,"Hola");
            getActivity().getContentResolver().insert(uri,values);
        }
        catch (ParseException ex){
            Log.e("PointTrackerFragment","Error parsing date.",ex );
        }
        getActivity().getLoaderManager().initLoader(0,null,this);

        list.setAdapter(adapter);
        return v;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
