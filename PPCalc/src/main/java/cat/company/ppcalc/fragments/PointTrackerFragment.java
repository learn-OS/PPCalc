package cat.company.ppcalc.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;

import cat.company.ppcalc.R;
import cat.company.ppcalc.adapters.DayPointsCursorAdapter;
import cat.company.ppcalc.db.DayPointsProviderMetadata;
import cat.company.ppcalc.fragments.dialogs.AddPointDialogFragment;
import cat.company.ppcalc.interfaces.IRefreshable;

/**
 * Fragment for the point tracker day view.
 */

public class PointTrackerFragment extends Fragment implements IRefreshable {

    private View v;
    private final Uri uri = DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI;
    private DayPointsCursorAdapter adapter;
    private Cursor cursor;

    public PointTrackerFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_point_tracker, container, false);
        ListView list= (ListView) v.findViewById(R.id.pointsList);
        Activity a = getActivity();

        adapter=new DayPointsCursorAdapter(getActivity(),null,0);
        reload();
        list.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.point_tracker, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addPoint){
            AddPointDialogFragment df=new AddPointDialogFragment(this);
            df.show(getFragmentManager(),"addDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reload() {
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            cursor = getActivity().getContentResolver().acquireContentProviderClient(uri).query(uri, null, null, null, null);
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
        }
        catch (Exception ex){
            Log.e("PointTrackerFragment", "Error loading.", ex);
        }
    }

    @Override
    public void Refresh() {
        reload();
    }
}
