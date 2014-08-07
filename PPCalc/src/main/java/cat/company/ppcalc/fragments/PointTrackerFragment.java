package cat.company.ppcalc.fragments;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

    public static final String TAG = "PointTrackerFragment";
    private final Uri uri = DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI;
    private DayPointsCursorAdapter adapter;
    private ListView list;

    public PointTrackerFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point_tracker, container, false);
        list = (ListView) view.findViewById(R.id.pointsList);
        adapter=new DayPointsCursorAdapter(getActivity(),null,0);
        reload();
        list.setAdapter(adapter);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
          AddMultiselection(list);
        }
        else {
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            list.setOnCreateContextMenuListener(this);
        }
        return view;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.delete) {
            try {
                getActivity().getContentResolver().acquireContentProviderClient(uri).delete(uri, DayPointsProviderMetadata.DayPointsTableMetadata._ID + "=?", new String[]{String.format("%d", list.getSelectedItemId())});
            } catch (RemoteException ex) {
                Log.e(TAG, "Error deleting.", ex);
            }
            reload();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_point_list_selection,menu);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void AddMultiselection(final ListView list) {
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                actionMode.setSubtitle(String.format("%d selected elements.",list.getCheckedItemCount()));
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle(R.string.deleting);
                MenuInflater inflater = actionMode.getMenuInflater();
                if (inflater != null) {
                    inflater.inflate(R.menu.menu_point_list_selection, menu);
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.delete){
                    long[] items=list.getCheckedItemIds();
                    for(long item:items){
                        try {
                            getActivity().getContentResolver().acquireContentProviderClient(uri).delete(uri, DayPointsProviderMetadata.DayPointsTableMetadata._ID + "=?", new String[]{String.format("%d", item)});
                        }
                        catch (RemoteException ex){
                            Log.e(TAG,"Error deleting.",ex);
                        }
                    }
                    reload();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.point_tracker, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addPoint){
            AddPointDialogFragment df=new AddPointDialogFragment();
            Bundle args=new Bundle();
            args.putSerializable("refreshable",this);
            df.setArguments(args);
            df.show(getFragmentManager(),"addDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reload() {
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            Cursor cursor = getActivity().getContentResolver().acquireContentProviderClient(uri).query(uri, null,
                    "DATE("+DayPointsProviderMetadata.DayPointsTableMetadata.DATE+")=DATE('now')", null, null);
            adapter.swapCursor(cursor);
        }
        catch (Exception ex){
            Log.e(TAG, "Error loading.", ex);
        }
    }

    @Override
    public void Refresh() {
        reload();
    }
}
