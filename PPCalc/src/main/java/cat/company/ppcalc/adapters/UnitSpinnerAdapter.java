package cat.company.ppcalc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cat.company.ppcalc.R;
import cat.company.ppcalc.calculator.Unit;

/**
 * Custom adapter for the UnitSpinner.
 * Created by carles on 11/01/14.
 */
public class UnitSpinnerAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<Unit> units;

    public UnitSpinnerAdapter(Context context,ArrayList<Unit> units){
        this.units=units;
        this.context=context;
    }

    @Override
    public int getCount() {
        return units.size();
    }

    @Override
    public Object getItem(int position) {
        return units.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unit_spinner_item,
                    parent, false);
        }

        if (convertView != null) {
            TextView unitsTv = (TextView) convertView.findViewById(android.R.id.text1);
            unitsTv.setText(units.get(position).getName());
        }

        return convertView;

    }
}
