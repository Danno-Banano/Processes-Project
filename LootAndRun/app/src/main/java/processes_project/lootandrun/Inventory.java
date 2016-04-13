package processes_project.lootandrun;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import org.mortbay.jetty.servlet.Context;

import java.util.ArrayList;

public class Inventory extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;

    private static ListView weapons;
    private ListView armor;
    private ListView firstAid;

    private static int tabNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        tabNum = 0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //tabLayout.addView((ListView)findViewById(R.id.view1), 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        //private int tabNum = 0;
//
//        private static int sNum;
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            sNum = sectionNumber-1;
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView;
//
//            if( sNum == 0 )
//            {
//                rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
//
//                String[] lootW = {"Pistol", "Knife", "Bow", "Machine Gun", "Chain Saw"};                                                                    // Implemented by daniel healy
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lootW);
//                ListView listView = (ListView) rootView.findViewById(R.id.section_list_label);
//                listView.setAdapter(adapter);
//
//                tabNum++;
//
//                return rootView;
//            }
//            else if( sNum == 1 )
//            {
//                rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
//
//                //ArrayList<Item> playerInv;
//                //playerInv = MainMap.getPlayer().getInventory();
//                String[] lootA = {"Bulletproof Vest", "Knee Pads", "Elbow Pads", "Helmet", "Ball Cap"};
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lootA);
//                ListView listView = (ListView) rootView.findViewById(R.id.section_list_label);
//                listView.setAdapter(adapter);
//
//                tabNum++;
//
//                return rootView;
//            }
//            else
//                rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
//
//
////            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
////            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//
//
//
//
//
//        //    listView.setEnabled(true);
//
//
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            ArrayList<Item> loot = MainMap.getMainPlayer().getInventory();

            switch( position )
            {
                case 0:
                    String[] lootW = {"Pistol", "Knife", "Bow", "Machine Gun", "Chain Saw"};

//                    Item x = new Item();
//                    x.setName("Pistol");
//                    Item y = new Item();
//                    x.setName("Knife");
//                    ArrayList<Item> testW = new ArrayList();
//                    testW.add(x);
//                    testW.add(y);
//                    MainMap.getMainPlayer().setInventory(testW);
//                    //ArrayList<Item> lootWc = MainMap.getMainPlayer().getInventory();


                    ArrayList<Item> lootWc = new ArrayList<Item>();
                    for( int i = 0; i < loot.size(); i++ )
                    {
                        if( loot.get(i).getItemType() == "Weapon" )
                        {
                            lootWc.add(loot.get(i));
                        }
                    }



                    return PlaceholderFragment.newInstance(position+1, lootW, lootWc, 2);

                case 1:
                    String[] lootA = {"Bulletproof Vest", "Knee Pads", "Elbow Pads", "Helmet", "Ball Cap"};

//                    Item xx = new Item();
//                    xx.setName("Bulletproof Vest");
//                    Item yy = new Item();
//                    yy.setName("Knew Pads");
//                    ArrayList<Item> testA = new ArrayList();
//                    testA.add(xx);
//                    testA.add(yy);
//                    MainMap.getMainPlayer().setInventory(testA);
//                    ArrayList<Item> lootAc = MainMap.getMainPlayer().getInventory();



                    ArrayList<Item> lootAc = new ArrayList<Item>();
                    for( int i = 0; i < loot.size(); i++ )
                    {
                        if( loot.get(i).getItemType() == "Armor" )
                        {
                            lootAc.add(loot.get(i));
                        }
                    }



                    return PlaceholderFragment.newInstance(position+1, lootA, lootAc, 2);
                case 2:
                    String[] lootH = {"Water", "Bandaid", "Beans", "Morphin"};

//                    Item xxx = new Item();
//                    xxx.setName("Water");
//                    Item yyy = new Item();
//                    yyy.setName("Bandaid");
//                    ArrayList<Item> testH = new ArrayList();
//                    testH.add(xxx);
//                    testH.add(yyy);
//                    MainMap.getMainPlayer().setInventory(testH);
//                    ArrayList<Item> lootHc = MainMap.getMainPlayer().getInventory();



                    ArrayList<Item> lootHc = new ArrayList<Item>();
                    for( int i = 0; i < loot.size(); i++ )
                    {
                        if( loot.get(i).getItemType() == "First Aid" )
                        {
                            lootHc.add(loot.get(i));
                        }
                    }



                    return PlaceholderFragment.newInstance(position+1, lootH, lootHc, 2);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Weapons";
                case 1:
                    return "Armor";
                case 2:
                    return "First Aid";
            }
            return null;
        }
    }
}
