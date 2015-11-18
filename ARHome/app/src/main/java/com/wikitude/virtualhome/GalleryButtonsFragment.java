package com.wikitude.virtualhome;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by anusha on 11/4/15.
 */
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
            actionbar.setTitle("Product Gallery Category");
            return v;
        }

        @Override
        public void onClick(View v) {
            Gallery g= new Gallery();
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.LivingRoomGalleryButton:
                    //Gallery g= new Gallery();
                    //Bundle bundle = new Bundle();
                    bundle.putString("category", "livingroom");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("livingRoomfragment")
                            .commit();
                    break;

                case R.id.BedroomGalleryButton:
                    bundle.putString("category", "bedroom");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                        .addToBackStack("bedroomfragment")
                        .commit();
                    break;

                case R.id.DiningRoomGalleryButton:
                    bundle.putString("category", "diningroom");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("diningroomfragment")
                            .commit();
                    break;

                case R.id.KitchenGalleryButton:
                    bundle.putString("category", "kitchen");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("kitchenfragment")
                            .commit();
                    break;

                case R.id.KidsGalleryButton:
                    bundle.putString("category", "kids");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("kidsfragment")
                            .commit();
                    break;

                case R.id.HomeDecorGalleryButton:
                    bundle.putString("category", "homedecor");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("homedecorfragment")
                            .commit();
                    break;

                case R.id.HomeOfficeGalleryButton:
                    bundle.putString("category", "homeoffice");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("homeofficefragment")
                            .commit();
                    break;

                case R.id.MiscGalleryButton:
                    bundle.putString("category", "misc");
                    g.setArguments(bundle);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.your_placeholder, g)
                            .addToBackStack("miscfragment")
                            .commit();
                    break;
            }
        }


        /*
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            menu.clear();
            inflater.inflate(R.menu.menu_sofa_gallery, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            System.out.println("Something clicked");

            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
        */
}
