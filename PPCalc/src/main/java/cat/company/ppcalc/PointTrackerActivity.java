package cat.company.ppcalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Vector;

import cat.company.ppcalc.adapters.PagerAdapter;
import cat.company.ppcalc.fragments.DayPointTrackerFragment;
import cat.company.ppcalc.fragments.PointTrackerFragment;
import cat.company.ppcalc.interfaces.IRefreshable;

public class PointTrackerActivity extends ActionBarActivity implements IRefreshable {

    private Vector<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_tracker);
        ActionBar actionBar = getSupportActionBar();

        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this,
                PointTrackerFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,
                DayPointTrackerFragment.class.getName()));
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.content);

        pager.setAdapter(mPagerAdapter);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_point_tracker,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void Refresh() {
        for(Fragment fragment : fragments){
            if(fragment instanceof IRefreshable){
                ((IRefreshable)fragment).Refresh();
            }
        }
    }
}
