package fingertech.mobileclientgky;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

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
    private int year_chosen, month_chosen, day_chosen;

    // Untuk ViewPager
    private SmartFragmentStatePagerAdapter adapterViewPager;
    static final int NUMBER_OF_KOLPORTASE = 4;
    ViewPager mPager;

    // Untuk Map
    private double latitude = -6.113887;
    private double longitude = 106.791796;

/*    // Untuk toggle switch
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Untuk Navigation Drawer
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

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
                    frag = new Home.PlaceholderFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
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
                    /*frag = new JadwalPelayananFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();*/
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
                    /*frag = new EventFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();*/
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
                // Sub menu dari menu Komisi
                // Komisi Anak
                if (groupPosition == 2 && childPosition == 0) {
                    frag = new KomisiAnakFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Komisi Kaleb
                else if (groupPosition == 2 && childPosition == 1) {
                    frag = new KomisiKalebFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Komisi Pasutri
                else if (groupPosition == 2 && childPosition == 2) {
                    frag = new KomisiPasutriFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Komisi Pemuda Dewasa
                else if (groupPosition == 2 && childPosition == 3) {
                    frag = new KomisiPemudaDewasaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Komisi Remaja & Pemuda
                else if (groupPosition == 2 && childPosition == 4) {
                    frag = new KomisiRemajaDanPemudaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Komisi Wanita
                else if (groupPosition == 2 && childPosition == 5) {
                    frag = new KomisiWanitaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Sub menu dari menu Pelayanan
                // Diakonia dan Oikumene
                else if (groupPosition == 3 && childPosition == 0) {
                    frag = new DiakoniaDanOikumeneFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Kebaktian Doa
                else if (groupPosition == 3 && childPosition == 1) {
                    frag = new KebaktianDoaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Kebaktian Umum
                else if (groupPosition == 3 && childPosition == 2) {
                    frag = new KebaktianUmumFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Kelompok Kecil
                else if (groupPosition == 3 && childPosition == 3) {
                    frag = new KelompokKecilFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Misi / Pengabaran Injil
                else if (groupPosition == 3 && childPosition == 4) {
                    frag = new MisiPengabaranInjilFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Sub menu dari menu Pembinaan
                // Katekisasi
                else if (groupPosition == 4 && childPosition == 0) {
                    frag = new KatekisasiFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Pre-marital
                else if (groupPosition == 4 && childPosition == 1) {
                    frag = new PreMaritalClassFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Renungan Gema
                else if (groupPosition == 4 && childPosition == 2) {
                    frag = new RenunganGemaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Rekaman Khotbah
                else if (groupPosition == 4 && childPosition == 3) {
                    frag = new RekamanKhotbahFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Permohonan Doa
                else if (groupPosition == 4 && childPosition == 4) {
                    frag = new PermohonanDoaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // KPPK
                else if (groupPosition == 4 && childPosition == 5) {
                    frag = new KPPKFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Lirik Lagu Rohani
                else if (groupPosition == 4 && childPosition == 6) {
                    frag = new LirikLaguRohaniFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Pengakuan Iman
                else if (groupPosition == 4 && childPosition == 7) {
                    frag = new PengakuanImanFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Kolportase
                else if (groupPosition == 4 && childPosition == 8) {
                    frag = new KolportaseFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Jadwal Pelayanan
                else if (groupPosition == 4 && childPosition == 9) {
                    frag = new JadwalPelayananFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }

                // Sub menu dari menu Events
                // Warta Mingguan
                else if (groupPosition == 5 && childPosition == 0) {
                    frag = new WartaMingguanFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Liturgi Mingguan
                else if (groupPosition == 5 && childPosition == 1) {
                    frag = new LiturgiMingguanFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Past and Upcoming Events
                else if (groupPosition == 5 && childPosition == 2) {
                    frag = new PastAndUpcomingEventsFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                // Jadwal Ibadah
                else if (groupPosition == 5 && childPosition == 3) {
                    frag = new JadwalIbadahFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment();
                }
                return false;
            }
        });

/*        // Untuk toggle switch
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();*/

        // Untuk ViewPager
        /*setContentView(R.layout.fragment_kolportase);

        Log.d("Home", "onCreate");
        adapterViewPager = new SmartFragmentStatePagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.vpPager);
        mPager.setAdapter(adapterViewPager);*/
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
            year_chosen = arg1;
            month_chosen = arg2+1;
            day_chosen = arg3;
            EditText ET = (EditText) findViewById(R.id.datePickerEdit);
            EditText ET2 = (EditText) findViewById(R.id.register_editTanggalLahir);
            String temp = Integer.toString(day_chosen) + "/" +Integer.toString(month_chosen) + "/" + Integer.toString(year_chosen);
            ET.setText(temp);
            ET2.setText(temp);
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

        /*@Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }*/
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


    /*public void openMapApps(View v) {
        Intent intent = null;
        intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + latitude + "," + longitude));
        startActivity(intent);
    }*/

    public void ambilDataDoa(){
        EditText namaET = (EditText) findViewById(R.id.permohonanDoa_editNama);
        EditText umurET = (EditText) findViewById(R.id.permohonanDoa_editUmur);
        EditText emailET = (EditText) findViewById(R.id.permohonanDoa_editEmail);
        EditText teleponET = (EditText) findViewById(R.id.permohonanDoa_editTelepon);
        EditText doaET = (EditText) findViewById(R.id.permohonanDoa_editDoa);
//        RadioButton jenisKelamin = (RadioButton) findViewById((R.id.permohonanDoa_editJenisKelamin));

        String nama = null,email = null,telepon = null,doa = null;
        int umur = 0;
        try {
            nama = URLEncoder.encode(namaET.getText().toString(), "utf-8");
            umur = Integer.parseInt(umurET.getText().toString());
            email = URLEncoder.encode(emailET.getText().toString(), "utf-8");
            telepon = URLEncoder.encode(teleponET.getText().toString(), "utf-8");
            doa = URLEncoder.encode(doaET.getText().toString(), "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        cont.addDoa(nama,umur,email,telepon,"p", doa);
        frag = new AlkitabFragment();
        switchFragment();
    }

    public void registerClicked(View v){
        EditText namaET = (EditText) findViewById(R.id.register_editNama);
        EditText passET = (EditText) findViewById(R.id.register_editPassword);
        EditText passconET = (EditText) findViewById(R.id.register_editKonfirmasiPassword);
        EditText alamatET = (EditText) findViewById(R.id.register_editAlamat);
        EditText emailET = (EditText) findViewById(R.id.register_editEmail);
        EditText teleponET = (EditText) findViewById(R.id.register_editTelepon);
        EditText idbaptisET = (EditText) findViewById(R.id.register_editIdBaptis);

        String pass = passET.getText().toString();
        String passcon = passconET.getText().toString();
        if (pass.equals(passcon)) {
        String nama = null,email = null,telepon = null,alamat = null,idbaptis = null;
            try {
                nama = URLEncoder.encode(namaET.getText().toString(), "utf-8");
                email = URLEncoder.encode(emailET.getText().toString(), "utf-8");
                telepon = URLEncoder.encode(teleponET.getText().toString(),"utf-8");
                alamat = URLEncoder.encode(alamatET.getText().toString(), "utf-8");
                idbaptis = URLEncoder.encode(idbaptisET.getText().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Date x = new Date();

            cont.register(nama,pass,email,telepon,alamat,x,idbaptis);
            switchFragment();
        }else{
            //password dan konfirmasi tidak sama, keluarin toast.
            Toast.makeText(Home.this, "Re-enter Password", Toast.LENGTH_LONG).show();

        }
    }

    public void loginClicked(View v){
        Toast.makeText(Home.this, "login clicked", Toast.LENGTH_LONG).show();
        EditText namaET = (EditText) findViewById(R.id.login_editNama);
        EditText passET = (EditText) findViewById(R.id.login_editPassword);

        String nama = namaET.getText().toString().replace(" ", "%20");
        String pass = passET.getText().toString();

        Log.d("nama",nama);
        Log.d("pass",pass);

        cont.login(nama,pass);
        switchFragment();
    }

    public void fetchDataJadwalPelayanan(View v){

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
