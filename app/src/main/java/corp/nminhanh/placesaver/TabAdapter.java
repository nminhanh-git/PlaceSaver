package corp.nminhanh.placesaver;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Minh Anh on 1/10/2018.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final int PAGE_COUNT = 3;

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FoodsFragment();
            case 1:
                return new ClothesFragment();
            case 2:
                return new PlacesFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Foods";
            case 1:
                return "Clothes";
            case 2:
                return "Places";
            default:
                return null;
        }
    }
}
