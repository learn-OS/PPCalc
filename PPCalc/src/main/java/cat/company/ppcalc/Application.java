package cat.company.ppcalc;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by company on 28/03/2014.
 * Main application class.
 */
public class Application extends android.app.Application {
    Tracker tracker;

    public synchronized Tracker getTracker(){
        if(tracker==null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker=analytics.newTracker("UA-33333816-1");
        }
        return tracker;
    }
}
