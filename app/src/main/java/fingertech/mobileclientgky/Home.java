package fingertech.mobileclientgky;

// Untuk dialog-dialog dan memberi notify
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

// Untuk fragment manager
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

// Untuk View
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

// Untuk Navigation Drawer
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// Untuk form
import android.widget.EditText;
import android.widget.RadioButton;

import android.os.Bundle;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by William Stefan Hartono
 */
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

    // Untuk fragment
    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    // Untuk controller server
    Controller cont = new Controller(this);

    // Untuk Map
    private double latitude = -6.113887;
    private double longitude = 106.791796;

    private Bundle bundleState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Untuk Navigation Drawer
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bundleState = savedInstanceState;
        generateMenu();
    }

    public void generateMenu() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mDrawerList = (ExpandableListView)findViewById(R.id.navList); // Membuat expandable list
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationDrawerDataProvider navProvider = new NavigationDrawerDataProvider(this);

        SessionManager sm = new SessionManager(this);
        boolean isLogin ;

        // Cek apakah pada saat menu dibuat, user sudah login atau belum
        if(sm.pref.getAll().get("IsLoggedIn").toString().equals("true")){
            isLogin = true;
        }
        else if(sm.pref.getAll() == null){
            isLogin = false;
        }
        else{
            isLogin = false;
        }

        // Pilih menu yang akan digunakan (menu saat sudah login atau belum)
        parentHashMap = navProvider.getDataHashMap(isLogin);
        parentHashMapKeys = new ArrayList<String>(parentHashMap.keySet());

        adapter = new NavigationDrawerAdapter(this, parentHashMap, parentHashMapKeys);
        mDrawerList.setAdapter(adapter);

        final boolean isLoginFinal = isLogin;

        // Pasang listener pada parent
        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Beranda
                if (groupPosition == 0) {
                    frag = new Home.PlaceholderFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                    return true;
                }
                // Alkitab
                else if(groupPosition == 1){
                    frag = new AlkitabFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                    return true;
                }// Tentang Kami
                else if (groupPosition == 6) {
                    frag = new TentangKamiFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                    return true;
                }
                // Hubungi Kami
                else if (groupPosition == 7) {
                    frag = new HubungiKamiFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                    return true;
                }
                // Login
                else if (groupPosition == 8 && !isLoginFinal) {
                    frag = new LoginFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                    return true;
                }
                // Register
                else if (groupPosition == 9 && !isLoginFinal) {
                    frag = new RegisterFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        // Pasang listener pada child
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View clickedView, int groupPosition, int childPosition, long id) {
                // Sub menu dari menu Komisi
                // Komisi Anak
                if (groupPosition == 2 && childPosition == 0) {
                    frag = new KomisiAnakFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Komisi Kaleb
                else if (groupPosition == 2 && childPosition == 1) {
                    frag = new KomisiKalebFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Komisi Pasutri
                else if (groupPosition == 2 && childPosition == 2) {
                    frag = new KomisiPasutriFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Komisi Pemuda Dewasa
                else if (groupPosition == 2 && childPosition == 3) {
                    frag = new KomisiPemudaDewasaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Komisi Remaja & Pemuda
                else if (groupPosition == 2 && childPosition == 4) {
                    frag = new KomisiRemajaDanPemudaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Komisi Wanita
                else if (groupPosition == 2 && childPosition == 5) {
                    frag = new KomisiWanitaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Sub menu dari menu Pelayanan
                // Diakonia dan Oikumene
                else if (groupPosition == 3 && childPosition == 0) {
                    frag = new DiakoniaDanOikumeneFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Kebaktian Doa
                else if (groupPosition == 3 && childPosition == 1) {
                    frag = new KebaktianDoaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Kebaktian Umum
                else if (groupPosition == 3 && childPosition == 2) {
                    frag = new KebaktianUmumFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Kelompok Kecil
                else if (groupPosition == 3 && childPosition == 3) {
                    frag = new KelompokKecilFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Misi / Pengabaran Injil
                else if (groupPosition == 3 && childPosition == 4) {
                    frag = new MisiPengabaranInjilFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Sub menu dari menu Pembinaan
                // Katekisasi
                else if (groupPosition == 4 && childPosition == 0) {
                    frag = new KatekisasiFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Pre-marital
                else if (groupPosition == 4 && childPosition == 1) {
                    frag = new PreMaritalClassFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Renungan Gema
                else if (groupPosition == 4 && childPosition == 2) {
                    frag = new RenunganGemaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Rekaman Khotbah
                else if (groupPosition == 4 && childPosition == 3) {
                    frag = new RekamanKhotbahFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Permohonan Doa
                else if (groupPosition == 4 && childPosition == 4) {
                    SessionManager sm = new SessionManager(getApplicationContext());
                    frag = new PermohonanDoaFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // KPPK
                else if (groupPosition == 4 && childPosition == 5) {
                    frag = new KPPKFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Lirik Lagu Rohani
                else if (groupPosition == 4 && childPosition == 6) {
                    frag = new LirikLaguRohaniFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Pengakuan Iman
                else if (groupPosition == 4 && childPosition == 7) {
                    frag = new PengakuanImanFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Kolportase
                else if (groupPosition == 4 && childPosition == 8) {
                    frag = new KolportaseFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Sub menu dari menu Events
                // Warta Mingguan
                else if (groupPosition == 5 && childPosition == 0) {
                    frag = new WartaMingguanFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Liturgi Mingguan
                else if (groupPosition == 5 && childPosition == 1) {
                    frag = new LiturgiMingguanFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Past and Upcoming Events
                else if (groupPosition == 5 && childPosition == 2) {
                    frag = new PastAndUpcomingEventsFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Jadwal Ibadah
                else if (groupPosition == 5 && childPosition == 3) {
                    frag = new JadwalIbadahFragment();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    switchFragment(frag);
                }

                // Konten Sesudah Login
                if (isLoginFinal) {
                    // Jadwal Pelayanan
                    if (groupPosition == 8 && childPosition == 0) {
                        frag = new JadwalPelayananFragment();
                        mDrawerLayout.closeDrawer(Gravity.START);
                        switchFragment(frag);
                    }
                    // Pengaturan Profil
                    else if (groupPosition == 8 && childPosition == 1) {
                        frag = new ProfilFragment();
                        mDrawerLayout.closeDrawer(Gravity.START);
                        switchFragment(frag);
                    }
                    // Ubah Password
                    else if (groupPosition == 8 && childPosition == 2) {
                        frag = new UbahPasswordFragment();
                        mDrawerLayout.closeDrawer(Gravity.START);
                        switchFragment(frag);
                    }
                    // Logout
                    else if (groupPosition == 8 && childPosition == 3) {
                        new AlertDialog.Builder(Home.this)
                                .setTitle("Logout")
                                .setMessage("Apakah Anda yakin ingin logout dari aplikasi?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        SessionManager sm = new SessionManager(getApplicationContext());
                                        sm.logoutUser();
                                        invalidateOptionsMenu();
                                        Toast.makeText(Home.this, "Anda berhasil logout", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }

        // Jika backstack hanya berisi 1 fragment, langsung keluar dari aplikasi
        /*if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }*/
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    // Untuk menyiapkan menu sebelum digambar
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        generateMenu();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            mDrawerList.setGroupIndicator(null); // Menghilangkan anak panah default yang diberikan pada semua parent
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

        return super.onOptionsItemSelected(item);
    }

    // Untuk button-button di halaman utama
    public void renunganClicked(View v) {
        frag = new RenunganGemaFragment();
        switchFragment(frag);
    }

    public void maritalClicked(View v) {
        frag = new PreMaritalClassFragment();
        switchFragment(frag);
    }

    public void katekisasiClicked(View v) {
        frag = new KatekisasiFragment();
        switchFragment(frag);
    }

    public void permohonanClicked(View v) {
        frag = new PermohonanDoaFragment();
        switchFragment(frag);
    }

    public void alkitabClicked(View v) {
        frag = new AlkitabFragment();
        switchFragment(frag);
    }

    public void kelompokClicked(View v) {
        frag = new KelompokKecilFragment();
        switchFragment(frag);
    }

    // Mengirimkan doa ke server
    public void KirimDoa(View v) {
        ambilDataDoa(v);
        frag = new Home.PlaceholderFragment();
        switchFragment(frag);
    }

    // Mengambil data yang berada di formulir permohonan doa
    public void ambilDataDoa(View view){
        EditText namaET = (EditText) findViewById(R.id.permohonanDoa_editNama);
        EditText umurET = (EditText) findViewById(R.id.permohonanDoa_editUmur);
        EditText emailET = (EditText) findViewById(R.id.permohonanDoa_editEmail);
        EditText teleponET = (EditText) findViewById(R.id.permohonanDoa_editTelepon);
        EditText doaET = (EditText) findViewById(R.id.permohonanDoa_editDoa);
        String jenisKelamin = null;

        boolean checked = ((RadioButton)findViewById(R.id.jenisKelaminPria)).isChecked();
        if (checked)
            jenisKelamin = "p";
        else
            jenisKelamin = "w";

        String nama = null, email = null, telepon = null, doa = null;
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

        cont.addDoa(nama,umur,email,telepon,jenisKelamin, doa);
    }

    // Login
    public void loginClicked(View v) {
        EditText namaET = (EditText) findViewById(R.id.login_editNama);
        EditText passET = (EditText) findViewById(R.id.login_editPassword);

        String nama = null, pass = null;
        try {
            nama = URLEncoder.encode(namaET.getText().toString(), "utf-8");
            pass = URLEncoder.encode(passET.getText().toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        cont.login(nama, pass);
        invalidateOptionsMenu();
        SessionManager sm = new SessionManager(this);

        if(sm.pref.getAll().get("IsLoggedIn").toString().equals("true")) {
            frag = new Home.PlaceholderFragment();
            switchFragment(frag);
        }
    }

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
//        private ListView mDrawerList;

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

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }
    }

    // Untuk mengganti tampilan fragment
    public void switchFragment(Fragment fragment) {
        /*fragManager = getSupportFragmentManager();
        fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.container, frag);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();*/

        // Resume fragment bila ada di backstack dan hapus semua fragment sisanya
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        FragmentTransaction ft = manager.beginTransaction();
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { // fragment not in back stack, create it.
            /*ft.replace(R.id.container, fragment, fragmentTag);*/
            ft.replace(R.id.container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        /*else {
            ft.detach(backStateName);
            ft.addToBackStack(backStateName);
        }*/
    }

    /*private android.app.FragmentManager.OnBackStackChangedListener getListener() {
        android.app.FragmentManager.OnBackStackChangedListener result = new android.app.FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {
                        finish();
                    }
                    Fragment fragment = manager.getFragments()
                            .get(backStackEntryCount - 1);
                    fragment.onResume();
                }
            }
        };
        return result;
    }*/
}