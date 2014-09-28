package cat.company.ppcalc;

import android.support.v4.view.ViewPager;
import cat.company.ppcalc.adapters.PagerAdapter;

/**
 * Created by carles on 28/09/14.
 * Returns the pager adapter or the view pager.
 */
public interface PagerProvider {
    public PagerAdapter getPagerAdapter();
    public ViewPager getViewPager();
}
