package cat.company.ppcalc.ads;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by company on 28/03/2014.
 */
public class MyAdListener extends AdListener {
    Tracker tracker;

    public MyAdListener(Tracker tr){
        tracker=tr;
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();

        // Send a screen view.
        tracker.send(new HitBuilders
                .EventBuilder()
                .setCategory("ad")
                .setAction("click")
                .setLabel(getClass().getName()).build());
    }
}
