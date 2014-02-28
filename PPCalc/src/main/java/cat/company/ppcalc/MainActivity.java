package cat.company.ppcalc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.List;
import java.util.Vector;

import cat.company.ppcalc.adapters.PagerAdapter;
import cat.company.ppcalc.calculator.Unit;
import cat.company.ppcalc.fragments.FlexiPointsCalculatorFragment;
import cat.company.ppcalc.fragments.ProPointsCalculatorFragment;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
    private Unit.UnitEnum unit;

    final Context context;
    private ViewPager pager;

    public MainActivity(){
        context=this;
    }
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this,
                ProPointsCalculatorFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,
                FlexiPointsCalculatorFragment.class.getName()));
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.content);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        pager.setAdapter(mPagerAdapter);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(context).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(context).activityStop(this);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}