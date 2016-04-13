package processes_project.lootandrun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ListView;

/**
 * Created by danielhealy on 4/12/16.
 */
public class PlaceholderFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    int tabNum;
    private static final String ARG_SECTION_NUMBER = "section_number";

    //private int tabNum = 0;

    private static int sNum;
    private String[] loot;
    private ArrayList<Item> lootC;

    public PlaceholderFragment() {
       // this.loot = loot;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, String[] loot, ArrayList<Item> lootC, int choice) {
        sNum = sectionNumber-1;
        PlaceholderFragment fragment = new PlaceholderFragment();
        if(choice == 1)
            fragment.setArguments(loot);
        else
            fragment.setArguments(lootC);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void setArguments( String[] loot )
    {
        this.loot = loot;
    }

    public void setArguments( ArrayList<Item> lootC ) { this.lootC = lootC; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

//        if( sNum == 0 )
//        {
            rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

            //String[] lootW = {"Pistol", "Knife", "Bow", "Machine Gun", "Chain Saw"};                                                                    // Implemented by daniel healy
            ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getContext(), android.R.layout.simple_list_item_1, lootC);
            ListView listView = (ListView) rootView.findViewById(R.id.section_list_label);
            listView.setAdapter(adapter);

//            listView.getOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                {
//
//                }
//            });

            tabNum++;

            return rootView;
//        }
//        else if( sNum == 1 )
//        {
//            rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
//
//            //ArrayList<Item> playerInv;
//            //playerInv = MainMap.getPlayer().getInventory();
//            String[] lootA = {"Bulletproof Vest", "Knee Pads", "Elbow Pads", "Helmet", "Ball Cap"};
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lootA);
//            ListView listView = (ListView) rootView.findViewById(R.id.section_list_label);
//            listView.setAdapter(adapter);
//
//            tabNum++;
//
//            return rootView;
//        }
//        else
//            rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
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
//        return rootView;
    }
}

