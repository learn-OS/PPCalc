package cat.company.ppcalc;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import cat.company.ppcalc.calculator.PointsCalculator;

public class PPCalcActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainmenu, menu);
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
		AlertDialog.Builder bd = new AlertDialog.Builder(this);
		bd.setTitle(R.string.points);
		bd.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		int points = PointsCalculator
				.CreateInstance()
				.setCarbs(
						tCarbs.getText().length() > 0 ? Double
								.parseDouble(tCarbs.getText().toString()) : 0)
				.setProteins(
						tProtein.getText().length() > 0 ? Double
								.parseDouble(tProtein.getText().toString()) : 0)
				.setFat(tFat.getText().length() > 0 ? Double.parseDouble(tFat
						.getText().toString()) : 0)
				.setFibre(
						tFibre.getText().length() > 0 ? Double
								.parseDouble(tFibre.getText().toString()) : 0)
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