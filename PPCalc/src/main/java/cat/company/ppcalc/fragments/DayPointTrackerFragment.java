package cat.company.ppcalc.fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cat.company.ppcalc.Application;
import cat.company.ppcalc.R;
import cat.company.ppcalc.adapters.DayPointsCursorAdapter;
import cat.company.ppcalc.db.DayPointsProviderMetadata;
import cat.company.ppcalc.interfaces.IRefreshable;
import cat.company.ppcalc.util.TitleProvider;

/**
 * Fragment for the point tracker day view.
 */

public class DayPointTrackerFragment extends Fragment implements IRefreshable,TitleProvider {

    public static final String TAG = "DayPointTrackerFragment";
    private final Uri uri = DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI;
    private DayPointsCursorAdapter adapter;
    private View view;
    private Date date;
    private GregorianCalendar gregorianCalendar;

    public DayPointTrackerFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gregorianCalendar = new GregorianCalendar();
        date= gregorianCalendar.getTime();

        view = inflater.inflate(R.layout.fragment_day_point_tracker, container, false);
        ListView list = (ListView) view.findViewById(R.id.pointsList);
        adapter = new DayPointsCursorAdapter(getActivity(), null, 0);

        TextView tvDate= (TextView) view.findViewById(R.id.dateEdit);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dpd=new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        gregorianCalendar=new GregorianCalendar(i,i2,i3);
                        date=gregorianCalendar.getTime();
                        reload();
                    }
                }, gregorianCalendar.get(Calendar.YEAR),gregorianCalendar.get(Calendar.MONTH),gregorianCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.setMessage(getActivity().getString(R.string.date_picker_message));
                dpd.show();
            }
        });

        reload();
        list.setAdapter(adapter);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            AddMultiselection(list);
        } else {
            registerForContextMenu(list);
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            list.setOnCreateContextMenuListener(this);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            try {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                getActivity().getContentResolver().acquireContentProviderClient(uri).delete(uri, DayPointsProviderMetadata.DayPointsTableMetadata._ID + "=?", new String[]{String.format("%d", info.id)});
                // Get tracker.
                Tracker t = ((Application) getActivity().getApplication()).getTracker();
                // Build and send an Event.
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Tracker")
                        .setAction("delete")
                        .setLabel("click")
                        .build());
            } catch (RemoteException ex) {
                Log.e(TAG, "Error deleting.", ex);
            }
            ((IRefreshable)getActivity()).Refresh();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.menu_point_list_selection, menu);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void AddMultiselection(final ListView list) {
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                actionMode.setSubtitle(String.format("%d selected elements.", list.getCheckedItemCount()));
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
                if (menuItem.getItemId() == R.id.delete) {
                    long[] items = list.getCheckedItemIds();
                    for (long item : items) {
                        try {
                            getActivity().getContentResolver().acquireContentProviderClient(uri).delete(uri, DayPointsProviderMetadata.DayPointsTableMetadata._ID + "=?", new String[]{String.format("%d", item)});
                        } catch (RemoteException ex) {
                            Log.e(TAG, "Error deleting.", ex);
                        }
                    }
                    ((IRefreshable)getActivity()).Refresh();
                    // Get tracker.
                    Tracker t = ((Application) getActivity().getApplication()).getTracker();
                    // Build and send an Event.
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("Tracker")
                            .setAction("delete")
                            .setLabel("click")
                            .build());
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    private void reload() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            Cursor cursor = getActivity().getContentResolver().acquireContentProviderClient(uri).query(uri, null,
                    "DATE(" + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ")=DATE('"+sdf2.format(date)+"')", null, null);
            adapter.swapCursor(cursor);
            int total = CalculateTotal(cursor);
            TextView tvTotal = (TextView) view.findViewById(R.id.totalPoints);
            Integer daily_allowance = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("point_allowance", "27"));
            tvTotal.setText(getResources().getQuantityString(R.plurals.points_of, total, total, daily_allowance));
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setMax(daily_allowance);
            TextView tvDate= (TextView) view.findViewById(R.id.dateEdit);
            tvDate.setText(sdf.format(this.date));
            if(total >=daily_allowance)
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.red_progressbar));
            else
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.green_progressbar));
            progressBar.setProgress(Math.min(daily_allowance, total));
        } catch (Exception ex) {
            Log.e(TAG, "Error loading.", ex);
        }
    }

    private int CalculateTotal(Cursor cursor) {
        int total = 0;
        if (cursor.moveToFirst()) {
            total += cursor.getInt(cursor.getColumnIndex(DayPointsProviderMetadata.DayPointsTableMetadata.POINTS));
            while (cursor.moveToNext()) {
                total += cursor.getInt(cursor.getColumnIndex(DayPointsProviderMetadata.DayPointsTableMetadata.POINTS));
            }
        }
        return total;
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
        return "Viewer";
    }
}
