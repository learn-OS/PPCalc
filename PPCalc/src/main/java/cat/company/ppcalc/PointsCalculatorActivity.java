package cat.company.ppcalc;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cat.company.ppcalc.adapters.PagerAdapter;
import cat.company.ppcalc.ads.MyAdListener;
import cat.company.ppcalc.calculator.Unit;
import cat.company.ppcalc.fragments.CalculatorFragment;
import cat.company.ppcalc.fragments.PointTrackerFragment;
import cat.company.ppcalc.interfaces.IRefreshable;
import cat.company.ppcalc.preferences.PreferencesActivity;
import cat.company.ppcalc.util.TitleProvider;

public class PointsCalculatorActivity extends ActionBarActivity implements ActionBar.TabListener,IRefreshable {
    private Unit.UnitEnum unit;

    final Context context;

    private final static String TAG = "PointsCalculatorActivity";
    private ViewPager pager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Boolean bound;
    private ActionBarDrawerToggle mDrawerToggle;

    private IInAppBillingService mService;

    private ServiceConnection mServiceConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            RetrievePurchase();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
            mService = null;
        }
    };

    private void setPurchased(boolean purchased) {
        this.purchased = purchased;
        InitAds();
        InitDrawer();
    }

    private Boolean purchased;
    private AdView adView;
    private ActionBar actionBar;
    private List<Fragment> fragments;

    public PointsCalculatorActivity() {
        context = this;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        bound = false;
        purchased = false;
        getApplicationContext().bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
                mServiceConn, Context.BIND_AUTO_CREATE);

        actionBar = getSupportActionBar();

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        fragments = new ArrayList<Fragment>();
        fragments.add(Fragment.instantiate(this,
                CalculatorFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,
                PointTrackerFragment.class.getName()));

        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.content);

        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setTitle(((TitleProvider) fragments.get(position)).getTitle());
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
                            .setTabListener(this)
            );
        }

        actionBar.setTitle(((TitleProvider) fragments.get(0)).getTitle());
        InitDrawer();

        PagerTabStrip ts = (PagerTabStrip) findViewById(R.id.titles);
        ts.setTextColor(getResources().getColor(R.color.blau_fosc));
        ts.setTabIndicatorColorResource(R.color.blau_fosc);

        adView = (AdView) this.findViewById(R.id.adView);

        InitAds();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void RetrievePurchase() {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    if (mService != null) {
                        Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
                        int response = ownedItems.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ArrayList<String> ownedSkus =
                                    ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                            ArrayList<String> purchaseDataList =
                                    ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                            String continuationToken =
                                    ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                            for (int i = 0; i < purchaseDataList.size(); ++i) {
                                String purchaseData = purchaseDataList.get(i);
                                String sku = ownedSkus.get(i);
                                if (sku.equals(params[0])) {
                                    return true;
                                }
                            }
                        }
                    }
                } catch (RemoteException ex) {
                    Log.e(TAG, "Error retrieving purchase.", ex);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean purchased) {
                setPurchased(purchased);
            }
        };

        task.execute("ppcalcpro");
    }

    private void InitAds() {
        if (purchased)
            adView.setVisibility(View.INVISIBLE);
        else {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
            MyAdListener adListener = new MyAdListener(((Application) getApplication()).getTracker());
            adView.setAdListener(adListener);
        }
    }

    private void InitDrawer() {
        Vector<String> drawerMenu = new Vector<String>();
        drawerMenu.add(getString(R.string.review));
        drawerMenu.add(getString(R.string.settings));

        if (mService != null && !purchased)
            drawerMenu.add(getString(R.string.purchase));

        if (Build.VERSION.SDK_INT >= 11) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, drawerMenu);
            mDrawerList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item_old, drawerMenu);
            mDrawerList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(context, PointTrackerActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(context, PreferencesActivity.class));
                } else if (position == 2) {
                    if (mService != null) {
                        try {
                            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                                    "ppcalcpro", "inapp", "");
                            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                            startIntentSenderForResult(pendingIntent.getIntentSender(),
                                    1001, new Intent(), 0, 0, 0);
                        } catch (RemoteException ex) {
                            Log.e(TAG, "Error purchasing.", ex);
                            setPurchased(false);
                        } catch (IntentSender.SendIntentException ex) {
                            Log.e(TAG, "Error starting purchase.", ex);
                            setPurchased(false);
                        }
                    }
                }
                mDrawerLayout.closeDrawer(mDrawerList);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    if (sku.equals("ppcalcpro")) {
                        setPurchased(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            getApplicationContext().unbindService(mServiceConn);
        }
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

    @Override
    public void Refresh() {
        for (Fragment fragment : fragments) {
            if (fragment instanceof IRefreshable) {
                ((IRefreshable) fragment).Refresh();
            }
        }
    }
}