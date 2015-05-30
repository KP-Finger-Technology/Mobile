package fingertech.mobileclientgky;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Home extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // Untuk Navigation Drawer
    private ExpandableListView mDrawerList;
    private LinkedHashMap<String, ArrayList<String>> parentHashMap;
    private ArrayList<String> parentHashMapKeys;
    private NavigationDrawerAdapter adapter;
    private DrawerLayout mDrawerLayout;

    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    Controller cont = new Controller();

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

/*    // Untuk toggle switch
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mDrawerList = (ExpandableListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        parentHashMap = NavigationDrawerDataProvider.getDataHashMap();
        parentHashMapKeys = new ArrayList<String>(parentHashMap.keySet());

        adapter = new NavigationDrawerAdapter(this, parentHashMap, parentHashMapKeys);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                // Beranda
                if (groupPosition == 0) {
                    /*Toast.makeText(Home.this,
                        parentHashMapKeys.get(groupPosition)
                                + " expanded", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "groupPosition : " + groupPosition, Toast.LENGTH_LONG).show();*/
                }
                // Alkitab
                else if (groupPosition == 1) {
                    frag = new AlkitabFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Komisi
                else if (groupPosition == 2) {
                    /*Toast.makeText(Home.this,
                            parentHashMapKeys.get(groupPosition)
                                    + " expanded", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "groupPosition : " + groupPosition, Toast.LENGTH_LONG).show();*/
                }
                // Pelayanan
                else if (groupPosition == 3) {
                    frag = new JadwalPelayananFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Pembinaan
                else if (groupPosition == 4) {
                    /*Toast.makeText(Home.this,
                            parentHashMapKeys.get(groupPosition)
                                    + " expanded", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "groupPosition : " + groupPosition, Toast.LENGTH_LONG).show();*/
                }
                // Events
                else if (groupPosition == 5) {
                    frag = new EventFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Tentang Kami
                else if (groupPosition == 6) {
                    frag = new TentangKamiFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Hubungi Kami
                else if (groupPosition == 7) {
                    frag = new HubungiKamiFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Login
                else if (groupPosition == 8) {
                    frag = new LoginFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Register
                else if (groupPosition == 9) {
                    frag = new RegisterFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Pengaturan
                else if (groupPosition == 10) {
                    /*Toast.makeText(Home.this,
                            parentHashMapKeys.get(groupPosition)
                                    + " expanded", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "groupPosition : " + groupPosition, Toast.LENGTH_LONG).show();*/
                }
            }
        });
;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        /*mDrawerList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(Home.this,
                        parentHashMapKeys.get(groupPosition)
                                + " collapsed", Toast.LENGTH_SHORT).show();
            }
        });*/

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View clickedView, int groupPosition, int childPosition, long id) {
                // Komisi Anak
                if (childPosition == 0) {
                    Toast.makeText(Home.this,
                            parentHashMapKeys.get(childPosition)
                                    + " selected", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "childPosition : " + childPosition, Toast.LENGTH_LONG).show();
                }
                // Komisi Kaleb
                else if (childPosition == 1) {
                    Toast.makeText(Home.this,
                            parentHashMapKeys.get(childPosition)
                                    + " selected", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "childPosition : " + childPosition, Toast.LENGTH_LONG).show();
                }
                // Komisi Pemuda Dewasa
                else if (childPosition == 2) {
                    Toast.makeText(Home.this,
                            parentHashMapKeys.get(childPosition)
                                    + " selected", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "childPosition : " + childPosition, Toast.LENGTH_LONG).show();
                }
                // Komisi Remaja & Pemuda
                else if (childPosition == 3) {
                    Toast.makeText(Home.this,
                            parentHashMapKeys.get(childPosition)
                                    + " selected", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "childPosition : " + childPosition, Toast.LENGTH_LONG).show();
                }
                // Komisi Wanita
                else if (childPosition == 4) {
                    Toast.makeText(Home.this,
                            parentHashMapKeys.get(childPosition)
                                    + " selected", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Home.this, "childPosition : " + childPosition, Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

/*        // Untuk toggle switch
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();*/
    }

    /*private void addDrawerItems() {
        String[] menuArray = { "Beranda", "Pelayanan", "Pembinaan", "Events", "Tentang Kami", "Hubungi Kami" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArray);
        mDrawerList.setAdapter(mAdapter);
    }*/

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            EditText ET = (EditText) findViewById(R.id.datePickerEdit);
            String temp = Integer.toString(arg3) + "/" +Integer.toString(arg2) + "/" + Integer.toString(arg1);
            ET.setText(temp);
        }
    };

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.beranda);
                break;
            case 2:
                mTitle = getString(R.string.pelayanan);
                break;
            case 3:
                mTitle = getString(R.string.pembinaan);
                break;
            case 4:
                mTitle = getString(R.string.events);
                break;
            case 5:
                mTitle = getString(R.string.tentang_kami);
                break;
            case 6:
                mTitle = getString(R.string.hubungi_kami);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

/*        // Untuk toggle switch
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /*public Context getActivity() {
        return activity;
    }*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayAdapter<String> mAdapter;
        private ListView mDrawerList;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    // Untuk button-button
    public void renunganClicked(View v) {
        frag = new RenunganGemaFragment();
        switchFragment();
    }

    public void maritalClicked(View v) {
        frag = new PreMaritalClassFragment();
        switchFragment();
    }

    public void katekisasiClicked(View v) {
        frag = new KatekisasiFragment();
        switchFragment();
    }

    public void permohonanClicked(View v) {
        frag = new PermohonanDoaFragment();
        switchFragment();
    }

    public void alkitabClicked(View v) {
        frag = new AlkitabFragment();
        switchFragment();
    }

    public void kelompokClicked(View v) {
        frag = new KelompokKecilFragment();
        switchFragment();
    }

    public void KirimDoa(View v) {
        frag = new AlkitabFragment();
        switchFragment();
        ambilDataDoa();
    }


    public void ambilDataDoa(){
        EditText namaET = (EditText) findViewById(R.id.permohonanDoa_editNama);
        EditText umurET = (EditText) findViewById(R.id.permohonanDoa_editUmur);
        EditText emailET = (EditText) findViewById(R.id.permohonanDoa_editEmail);
        EditText teleponET = (EditText) findViewById(R.id.permohonanDoa_editTelepon);
        EditText doaET = (EditText) findViewById(R.id.permohonanDoa_editDoa);
//        RadioButton jenisKelamin = (RadioButton) findViewById((R.id.permohonanDoa_editJenisKelamin));

        String nama = namaET.getText().toString();
        String umur = umurET.getText().toString();
        String email = emailET.getText().toString();
        String telepon = teleponET.getText().toString();
        String doa = doaET.getText().toString();

        Log.d("edit nama ", nama);
    }

    public void switchFragment() {
        fragManager = getSupportFragmentManager();
        fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.container, frag);
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

/*    // Untuk toggle switch
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
            }

            *//** Called when a drawer has settled in a completely closed state. *//*
            public void onDrawerClosed(View view) {
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/
}
