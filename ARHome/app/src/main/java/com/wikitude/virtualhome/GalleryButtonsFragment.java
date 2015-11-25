package com.wikitude.virtualhome;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GalleryButtonsFragment extends Fragment implements View.OnClickListener {

        Button livingRoom;
        Button diningRoom;
        Button kitchen;
        Button bedroom;
        Button misc;
        Button kids;
        Button homeDecor;
        Button homeOffice;

        View v;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.gallery_items_buttons, container, false);

            // add more listeners here when more buttons are added

            livingRoom = (Button)v.findViewById(R.id.LivingRoomGalleryButton);
            diningRoom = (Button)v.findViewById(R.id.DiningRoomGalleryButton);
            kitchen = (Button)v.findViewById(R.id.KitchenGalleryButton);
            bedroom = (Button)v.findViewById(R.id.BedroomGalleryButton);
            misc = (Button)v.findViewById(R.id.MiscGalleryButton);
            kids = (Button)v.findViewById(R.id.KidsGalleryButton);
            homeDecor = (Button)v.findViewById(R.id.HomeDecorGalleryButton);
            homeOffice = (Button)v.findViewById(R.id.HomeOfficeGalleryButton);

            livingRoom.setOnClickListener(this);
            diningRoom.setOnClickListener(this);
            kitchen.setOnClickListener(this);
            bedroom.setOnClickListener(this);
            misc.setOnClickListener(this);
            kids.setOnClickListener(this);
            homeDecor.setOnClickListener(this);
            homeOffice.setOnClickListener(this);

            setHasOptionsMenu(true);
            ActionBar actionbar = getActivity().getActionBar();
            actionbar.setTitle("Gallery");
            return v;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.LivingRoomGalleryButton:
                    LivingRoomGalleryFragment sgf= new LivingRoomGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, sgf)
                            .addToBackStack("livingRoomfragment")
                            .commit();
                    break;

                case R.id.BedroomGalleryButton:
                    BedroomGalleryFragment bgf= new BedroomGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                        .replace(R.id.your_placeholder, bgf)
                        .addToBackStack("bedroomfragment")
                        .commit();
                    break;

                case R.id.DiningRoomGalleryButton:
                    DiningRoomGalleryFragment drgf= new DiningRoomGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, drgf)
                            .addToBackStack("diningroomfragment")
                            .commit();
                    break;

                case R.id.KitchenGalleryButton:
                    KitchenGalleryFragment kgf= new KitchenGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, kgf)
                            .addToBackStack("kitchenfragment")
                            .commit();
                    break;

                case R.id.KidsGalleryButton:
                    KidsGalleryFragment kidsgf= new KidsGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, kidsgf)
                            .addToBackStack("kidsfragment")
                            .commit();
                    break;

                case R.id.HomeDecorGalleryButton:
                    HomeDecorGalleryFragment hdgf= new HomeDecorGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, hdgf)
                            .addToBackStack("homedecorfragment")
                            .commit();
                    break;

                case R.id.HomeOfficeGalleryButton:
                    HomeOfficeGalleryFragment hogf= new HomeOfficeGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, hogf)
                            .addToBackStack("homeofficefragment")
                            .commit();
                    break;

                case R.id.MiscGalleryButton:
                    MiscGalleryFragment mgf= new MiscGalleryFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, mgf)
                            .addToBackStack("miscfragment")
                            .commit();
                    break;
            }
        }

}
