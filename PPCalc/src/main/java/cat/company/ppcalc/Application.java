package cat.company.ppcalc;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by company on 28/03/2014.
 * Main application class.
 */
public class Application extends android.app.Application {
    private static Context mContext;
    Tracker tracker;

    public static Context getContext() {
        return mContext;
    }

    public synchronized Tracker getTracker(){
        if(tracker==null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker=analytics.newTracker("UA-33333816-1");
        }
        return tracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
