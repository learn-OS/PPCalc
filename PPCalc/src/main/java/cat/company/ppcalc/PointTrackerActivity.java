package cat.company.ppcalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Vector;

import cat.company.ppcalc.adapters.PagerAdapter;
import cat.company.ppcalc.fragments.DayPointTrackerFragment;
import cat.company.ppcalc.fragments.MonthPointTrackerFragment;
import cat.company.ppcalc.fragments.WeekPointTrackerFragment;
import cat.company.ppcalc.interfaces.IRefreshable;

public class PointTrackerActivity extends ActionBarActivity implements IRefreshable {

    private Vector<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this,
                DayPointTrackerFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, WeekPointTrackerFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, MonthPointTrackerFragment.class.getName()));
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.content);

        PagerTabStrip ts = (PagerTabStrip) findViewById(R.id.titles);
        ts.setTextColor(getResources().getColor(R.color.black));
        ts.setTabIndicatorColorResource(R.color.colorPrimaryDark);

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
    public void Refresh() {
        for(Fragment fragment : fragments){
            if(fragment instanceof IRefreshable){
                ((IRefreshable)fragment).Refresh();
            }
        }
    }
}
