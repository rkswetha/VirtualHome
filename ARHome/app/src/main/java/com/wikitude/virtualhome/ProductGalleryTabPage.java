package com.wikitude.virtualhome;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by anusha on 10/23/15.
 */
public class ProductGalleryTabPage extends FragmentActivity {

    private ViewPager pager;
    private TabPage TabAdapter;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        TabAdapter = new TabPage(getSupportFragmentManager());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabpage_main);

        TabAdapter = new TabPage(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(TabAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

    }
}
