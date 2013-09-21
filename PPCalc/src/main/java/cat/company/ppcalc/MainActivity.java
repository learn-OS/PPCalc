package cat.company.ppcalc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;

import cat.company.ppcalc.calculator.PointsCalculator;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SherlockActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.init:
                init();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calculate(View view) {
        EditText tCarbs = (EditText) findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) findViewById(R.id.editProtein);
        EditText tFat = (EditText) findViewById(R.id.editFat);
        EditText tFibre = (EditText) findViewById(R.id.editFibre);
        AlertDialog.Builder bd = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Sherlock___Theme));
        bd.setTitle(R.string.points);
        bd.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Editable carbsText = tCarbs.getText();
        Editable proteinText = tProtein.getText();
        Editable fatText = tFat.getText();
        Editable fibreText = tFibre.getText();
        int points = PointsCalculator
                .CreateInstance()
                .setCarbs(carbsText != null &&
                        carbsText.length() > 0 ? Double
                        .parseDouble(carbsText.toString()) : 0)
                .setProteins(
                        proteinText != null && proteinText.length() > 0 ? Double
                                .parseDouble(proteinText.toString()) : 0)
                .setFat(fatText != null && fatText.length() > 0 ? Double.parseDouble(fatText.toString()) : 0)
                .setFibre(fibreText != null &&
                        fibreText.length() > 0 ? Double
                        .parseDouble(fibreText.toString()) : 0)
                .calculate();
        bd.setMessage(getResources().getQuantityString(R.plurals.num_points,
                points, points));
        bd.show();
    }

    public void init() {
        EditText tCarbs = (EditText) findViewById(R.id.editCarbs);
        EditText tProtein = (EditText) findViewById(R.id.editProtein);
        EditText tFat = (EditText) findViewById(R.id.editFat);
        EditText tFibre = (EditText) findViewById(R.id.editFibre);
        tCarbs.setText("");
        tProtein.setText("");
        tFat.setText("");
        tFibre.setText("");
    }
}