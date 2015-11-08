package com.wikitude.virtualhome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by anusha on 10/3/2015.
 *
 * NOT IN USE
 */
public class TabPage extends FragmentPagerAdapter {


    public TabPage(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = null;
        if(i == 0) {
            f = new LivingRoomGalleryFragment();
        }
        else if(i ==1){
            f = new BedroomGalleryFragment();

        }
        return f;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2; //No of Tabs
    }
@Override
    public CharSequence getPageTitle(int position){
    if(position == 0){
        return "Sofa Gallery";
    }else if(position == 1){
        return "Bedroom Gallery";
    }
    return super.getPageTitle(position);
}

}
