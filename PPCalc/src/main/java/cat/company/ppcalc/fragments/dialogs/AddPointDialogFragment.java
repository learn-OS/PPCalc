package cat.company.ppcalc.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cat.company.ppcalc.R;
import cat.company.ppcalc.db.DayPointsProviderMetadata;
import cat.company.ppcalc.interfaces.IRefreshable;

/**
 * Created by carles on 05/08/14.
 * Dialog to add points to the list.
 */
public class AddPointDialogFragment extends DialogFragment {

    public static final String TAG = "AddDialog";
    Uri uri= DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI;
    IRefreshable refreshable;

    public AddPointDialogFragment(IRefreshable refreshable){
        this.refreshable=refreshable;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_point, null);
        builder.setView(view)
                .setTitle(getActivity().getString(R.string.add_points));
        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText pointsEt= (EditText) view.findViewById(R.id.points);
                EditText commentEt= (EditText) view.findViewById(R.id.comment);
                ContentValues contentValues=new ContentValues();
                contentValues.put(DayPointsProviderMetadata.DayPointsTableMetadata.COMMENT,commentEt.getText().toString());
                contentValues.put(DayPointsProviderMetadata.DayPointsTableMetadata.POINTS, Integer.parseInt(pointsEt.getText().toString()));
                try {
                    getActivity().getContentResolver().acquireContentProviderClient(uri).insert(uri, contentValues);
                    refreshable.Refresh();
                }
                catch (RemoteException ex){
                    Log.e(TAG,"Error adding.",ex);
                }
                dialogInterface.dismiss();
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }
}
