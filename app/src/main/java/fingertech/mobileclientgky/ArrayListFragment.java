package fingertech.mobileclientgky;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by clive on 25-May-14.
 * www.101apps.co.za
 */
public class ArrayListFragment extends ListFragment {
    private int mNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ArrayListFragment", "onCreate");
        super.onCreate(savedInstanceState);
//        get this fragment's number
        if (getArguments() != null) {
            mNum = getArguments().getInt("num");
        } else {
            mNum = 1;
        }
    }

    /**
     * Create a new instance of our fragment, providing "num"
     * as an argument - it determines which page to display
     */
    static ArrayListFragment createNewFragmentToDisplay(int num) {
        Log.d("ArrayListFragment", "createNewFragment");
        ArrayListFragment displayFragment = new ArrayListFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        displayFragment.setArguments(args);
        return displayFragment;
    }

    /*called each time the fragment's activity is created - which is
    each time a new page is displayed*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("ArrayListFragment", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getMyListArray()));
    }

    //    our list arrays to be used - one per page
    private String[] getMyListArray() {
        Log.d("ArrayListFragment", "getMyListArray");
        String[] returnArray = {"No lists."};
        switch (mNum) {
            case 0:
                returnArray = NavigationDrawerData.komisiArray;
                break;
            case 1:
                returnArray = NavigationDrawerData.pelayananArray;
                break;
            case 2:
                returnArray = NavigationDrawerData.pembinaanArray;
                break;
            case 3:
                returnArray = NavigationDrawerData.eventsArray;
                break;
        }
        return returnArray;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("ArrayListFragment", "onListItemClick");
        Toast.makeText(getActivity(), "List " + (mNum + 1) + " selected item: " + (position + 1),Toast.LENGTH_SHORT).show();
    }
}