package fingertech.mobileclientgky;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Andarias Silvanus
 */
public class KPPKFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    private SearchView sv;
    private LinearLayout cllr;
    private String keyword = null;

    private ArrayList<String> arrKPPK;
    private Boolean adaKPPK = false;

    public static KPPKFragment newInstance(String param1, String param2) {
        KPPKFragment fragment = new KPPKFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Viewer v = new Viewer();

    public KPPKFragment() {}

    private DataBaseHelper DB;
    private Button kppk_download;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayList<String> kppkSaved;
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("kppkSaved",kppkSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataBaseHelper DBH = new DataBaseHelper(getActivity().getApplicationContext());
        DBH.openDataBase();
        if (savedInstanceState != null) {
            // Probably orientation change
            kppkSaved = savedInstanceState.getStringArrayList("kppkSaved");
            if (!DBH.isTableExists("kppk"))
                generateKontenKPPK(false);
        }
        else {
            if (kppkSaved != null) {
                // Returning from backstack, data is fine, do nothing
                if (!DBH.isTableExists("kppk"))
                    generateKontenKPPK(false);
            }
            else {
                // Newly created, compute data
                if (!DBH.isTableExists("kppk")) {
                    v = new Viewer();
                    v.execute();
                }
            }
        }
    }

    private LinearLayout myLinearLayout;

    private void generateKontenKPPK(boolean mode) {
        // Mode == true untuk load dari database
        // Mode == false untuk load dari konten yang telah disave dari server
        ArrayList<String> containerString = new ArrayList<String>();
        if (mode)
            containerString = DB.getKPPK();
        else
            containerString = kppkSaved;

        myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_kppk);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        int dataLength = containerString.size();
        Button ListKPPK;

        int cnt = 1;
        for (int i = 0; i < dataLength; i = i + 2) {
            String container = "KPPK " + Integer.toString(cnt) + " - " + containerString.get(i);
            cnt++;

            // Add Button Judul KPPk
            ListKPPK = new Button(getActivity());
            ListKPPK.setText(container);
            ListKPPK.setLayoutParams(params);
            ListKPPK.setBackgroundColor(0);

            final String _judul = containerString.get(i+1);
            final String _isi = containerString.get(i+1);

            // Add button listener here
            ListKPPK.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Masuk ke kosntruktor parameter kppkLengkapFragment dengan parameter judul dan isi
                        frag = new KPPKLengkapFragment(_judul, _isi);
                        fragManager = getActivity().getSupportFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container, frag);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }
                }
            );

            myLinearLayout.addView(ListKPPK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_kppk, container, false);
        kppk_download = (Button) rootView.findViewById(R.id.kppk_download);
        kppk_download.setOnClickListener(this);

        DB = new DataBaseHelper(getActivity().getApplicationContext());
        DB.openDataBase();
        if (DB.isTableExists("KPPK")) {
            // Jika tabel KPPK exist, berarti sudah pernah di-download. Tampilkan daftar KPPK dari database
            kppk_download.setVisibility(View.INVISIBLE);
            generateKontenKPPK(true);
        }
        else {
            // Belum pernah download KPPK, maka tampilkan dari ambil JSON ke server
        }

        sv = (SearchView) rootView.findViewById(R.id.kppk_searchview);
        cllr = (LinearLayout) rootView.findViewById(R.id.container_kppk);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                keyword = s;
                Toast.makeText(getActivity(), "KPPK yang Anda cari: " + keyword, Toast.LENGTH_LONG).show();

                if (DB.isTableExists("KPPK")) {
                    arrKPPK = DB.searchKPPK(keyword);

                    if (arrKPPK.size() != 0) {
                        adaKPPK = true;
                    }
                }

                cllr.removeAllViews();
                ViewerSearch vs = new ViewerSearch();
                vs.execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "Downloading..", Toast.LENGTH_LONG).show();
        v.downloadKPPK();
        Toast.makeText(getActivity(), "Download Success!", Toast.LENGTH_LONG).show();
    }
    
	public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    class Viewer extends AsyncTask<String, String, String> {
        private Button ListKPPK;

        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_kppk.php");
            HttpResponse response;

            try {
                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        public void downloadKPPK() {
            int dataLength = arr.length();
            ArrayList<String> tmp = new ArrayList<String>();

            String judul = null, isi = null;

            // Generate konten KPPK dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tmp.add(judul);
                    tmp.add(isi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (DB.isTableExists("KPPK")) {
                DB.deleteTableKPPK();
            }
            DB.createTableKPPK();
            DB.insertDataKPPK(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_kppk);
            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            int defaultColor = getResources().getColor(R.color.defaultFont);
            String container, judul, isi = null;

            kppkSaved = new ArrayList<String>();

            // Generate konten KPPK dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                judul = "";
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");

                    kppkSaved.add(judul);
                    kppkSaved.add(isi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                container = "KPPK " + Integer.toString(i + 1) + " - " + judul;

                // Add Button Judul KPPk
                ListKPPK = new Button(getActivity());
                ListKPPK.setText(container);
                ListKPPK.setLayoutParams(params);
                ListKPPK.setTextColor(getResources().getColor(R.color.defaultFont));
                ListKPPK.setBackgroundColor(0);

                final String _judul = judul;
                final String _isi = isi;

                // Add button listener here
                ListKPPK.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Masuk ke konstruktor parameter kppkLengkapFragment dengan parameter judul dan isi
                            frag = new KPPKLengkapFragment(_judul, _isi);
                            fragManager = getActivity().getSupportFragmentManager();
                            fragTransaction = fragManager.beginTransaction();
                            fragTransaction.replace(R.id.container, frag);
                            fragTransaction.addToBackStack(null);
                            fragTransaction.commit();
                        }
                    }
                );

                myLinearLayout.addView(ListKPPK);
            }
        }
    }

    class ViewerSearch extends AsyncTask<String, String, String> {
        private Button ListKPPK;

        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_kppksearch.php?kw=" + keyword);
            HttpResponse response;

            try {
                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        public void downloadKPPK() {
            int dataLength = arr.length();
            ArrayList<String> tmp = new ArrayList<String>();

            String judul = null, isi = null;

            // Generate konten KPPK dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tmp.add(judul);
                    tmp.add(isi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (DB.isTableExists("KPPK")) {
                DB.deleteTableKPPK();
            }
            DB.createTableKPPK();
            DB.insertDataKPPK(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_kppk);

            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            int defaultColor = getResources().getColor(R.color.defaultFont);

            String container, judul = null, isi = null;

            kppkSaved = new ArrayList<String>();

            // Cari dari server
            if (!adaKPPK) {
                for (int i = 0; i < dataLength; i++) {
                    JSONObject jsonobj = null;
                    judul = "";
                    try {
                        jsonobj = arr.getJSONObject(i);
                        judul = jsonobj.getString("judul");
                        isi = jsonobj.getString("isi");

                        kppkSaved.add(judul);
                        kppkSaved.add(isi);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    container = "KPPK " + Integer.toString(i + 1) + " - " + judul;

                    // Add Button Judul KPPk
                    ListKPPK = new Button(getActivity());
                    ListKPPK.setText(container);
                    ListKPPK.setLayoutParams(params);
                    ListKPPK.setTextColor(getResources().getColor(R.color.defaultFont));
                    ListKPPK.setBackgroundColor(0);

                    final String _judul = judul;
                    final String _isi = isi;

                    // Add button listener here
                    ListKPPK.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter kppkLengkapFragment dengan parameter judul dan isi
                                    frag = new KPPKLengkapFragment(_judul, _isi);
                                    fragManager = getActivity().getSupportFragmentManager();
                                    fragTransaction = fragManager.beginTransaction();
                                    fragTransaction.replace(R.id.container, frag);
                                    fragTransaction.addToBackStack(null);
                                    fragTransaction.commit();
                                }
                            }
                    );

                    myLinearLayout.addView(ListKPPK);
                }
            }

            // Cari dari basis data KPPK
            else {
                Log.d("adaKPPK", "generate dari DB");
                Toast.makeText(getActivity(), "KPPK yang Anda cari: " + keyword + " digenerate dari DB", Toast.LENGTH_LONG).show();
                // Generate konten KPPK dalam loop for
                for (int i = 0; i < arrKPPK.size(); i = i + 2) {
                    try {
                        judul = arrKPPK.get(i);
                        isi = arrKPPK.get(i + 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    container = "KPPK " + Integer.toString(i + 1) + " - " + judul;

                    // Add Button Judul KPPK
                    ListKPPK = new Button(getActivity());
                    ListKPPK.setText(container);
                    ListKPPK.setLayoutParams(params);
                    ListKPPK.setTextColor(getResources().getColor(R.color.defaultFont));
                    ListKPPK.setBackgroundColor(0);

                    final String _judul = judul;
                    final String _isi = isi;

                    // Add button listener here
                    ListKPPK.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter KPPKLengkapFragment dengan parameter judul dan isi
                                    frag = new KPPKLengkapFragment(_judul, _isi);
                                    fragManager = getActivity().getSupportFragmentManager();
                                    fragTransaction = fragManager.beginTransaction();
                                    fragTransaction.replace(R.id.container, frag);
                                    fragTransaction.addToBackStack(null);
                                    fragTransaction.commit();
                                }
                            }
                    );

                    myLinearLayout.addView(ListKPPK);
                }
            }
        }
    }

}