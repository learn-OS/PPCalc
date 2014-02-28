package cat.company.ppcalc.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cat.company.ppcalc.util.TitleProvider;

/**
 * Created by carles on 28/02/14.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public String getPageTitle(int position) {
        Fragment fr=this.fragments.get(position);
        if(fr instanceof TitleProvider){
            return ((TitleProvider) fr).getTitle();
        }
        else{
            return "Undefined";
        }
    }

}
