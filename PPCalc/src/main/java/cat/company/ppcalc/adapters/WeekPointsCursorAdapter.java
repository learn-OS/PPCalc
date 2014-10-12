package cat.company.ppcalc.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cat.company.ppcalc.R;
import cat.company.ppcalc.db.DayPointsProviderMetadata;

/**
 * Created by carles on 05/08/14.
 * Adapter for the Day Points list.
 */
public class WeekPointsCursorAdapter extends CursorAdapter {
    LayoutInflater mInflater;

    public WeekPointsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.day_points_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateTv = (TextView) view.findViewById(R.id.date);
        TextView commentTv= (TextView) view.findViewById(R.id.comment);
        TextView pointTv= (TextView) view.findViewById(R.id.points);
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfIn=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String day = cursor.getString(cursor.getColumnIndex(DayPointsProviderMetadata.DayPointsTableMetadata.DATE));
            if(day!=null) {
                Date date = sdfIn.parse(day);
                dateTv.setText(sdf.format(date));
            }
        }
        catch (ParseException ex){
            dateTv.setVisibility(View.INVISIBLE);
        }
        pointTv.setText(String.format("%d",cursor.getInt(cursor.getColumnIndex("POINTSUM"))));
    }
}
