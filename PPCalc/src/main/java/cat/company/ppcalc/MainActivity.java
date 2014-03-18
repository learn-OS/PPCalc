package cat.company.ppcalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;
import java.util.Vector;

import cat.company.ppcalc.adapters.PagerAdapter;
import cat.company.ppcalc.calculator.Unit;
import cat.company.ppcalc.fragments.FlexiPointsCalculatorFragment;
import cat.company.ppcalc.fragments.ProPointsCalculatorFragment;
import cat.company.ppcalc.preferences.PreferencesActivity;
import cat.company.ppcalc.util.TitleProvider;

public class MainActivity extends ActionBarActivity {
    private Unit.UnitEnum unit;

    final Context context;
    private ViewPager pager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Vector<String> drawerMenu;
    private ActionBarDrawerToggle mDrawerToggle;

    private int previousSelectedDrawer=0;

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
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this,
                ProPointsCalculatorFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,
                FlexiPointsCalculatorFragment.class.getName()));
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.content);

        pager.setAdapter(mPagerAdapter);
        drawerMenu = new Vector<String>();
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            drawerMenu.add(mPagerAdapter.getPageTitle(i));
        }

        drawerMenu.add(getString(R.string.settings));

        actionBar.setTitle(((TitleProvider)fragments.get(0)).getTitle());
        if(Build.VERSION.SDK_INT>=11)
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, drawerMenu));
        else
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item_old, drawerMenu));
        mDrawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2) {
                    startActivity(new Intent(context, PreferencesActivity.class));
                    mDrawerList.setItemChecked(previousSelectedDrawer, true);
                }
                else{
                    pager.setCurrentItem(position);
                    previousSelectedDrawer=position;
                }
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setTitle(((TitleProvider)fragments.get(position)).getTitle());
                mDrawerList.setItemChecked(position, true);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putInt("defaultPage", position);
                editor.commit();
            }
        });

        mDrawerList.setItemChecked(0, true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        int page = PreferenceManager.getDefaultSharedPreferences(this).getInt("defaultPage", 0);
        pager.setCurrentItem(page,true);
        previousSelectedDrawer=page;

        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}