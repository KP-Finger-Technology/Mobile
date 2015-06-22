/*
package fingertech.mobileclientgky;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

*/
/*
   Extension of FragmentStatePagerAdapter which intelligently caches 
   all active fragments and manages the fragment lifecycles. 
   Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
*//*

public class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    // Sparse array to keep track of registered fragments in memory
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("Adapter", "instatiateItem");
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("Adapter", "destroyItem");
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        Log.d("Adapter", "getRegisteredFragment");
        return registeredFragments.get(position);
    }

    //    returns the number of views available
    @Override
    public int getCount() {
        Log.d("Adapter", "getCount");
        return Home.NUMBER_OF_KOLPORTASE;
    }

    // when swiping returns a fragment with the object identified by position
    @Override
    public Fragment getItem(int position) {
        Log.d("Adapter", "getItem");
        return ArrayListFragment.createNewFragmentToDisplay(position);
    }

    */
/*gets the title describing specified page
    and passes it to the PagerTitleStrip in
    fragment_pager.xml - displays either top or
    bottom of screen*//*

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("Adapter", "getPageTitle");
        return "Halaman " + (position + 1);
    }
}
*/
