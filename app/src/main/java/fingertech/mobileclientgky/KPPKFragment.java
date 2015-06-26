
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KPPKFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KPPKFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KPPKFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KPPKFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            Log.d("mencoba ambil arrayList yg disave..","..");
            kppkSaved = savedInstanceState.getStringArrayList("kppkSaved");
            if (!DBH.isTableExists("kppk"))
                generateKontenKPPK(false);
            Log.d("berhasil ambil arrayList yang telah di-save","..");
        }
        else {
            if (kppkSaved != null) {
                // Returning from backstack, data is fine, do nothing
                Log.d("from KPPK, si arrayList!=null","..");
                if (!DBH.isTableExists("kppk"))
                    generateKontenKPPK(false);
            }
            else {
                // Newly created, compute data
                Log.d("from KPPK, new Viewer & execute","..");
                if (!DBH.isTableExists("kppk")) {
                    v = new Viewer();
                    v.execute();
                }
            }
        }
    }

    private LinearLayout myLinearLayout;

    private void generateKontenKPPK(boolean mode) {
        // Mode == true utk load dari database
        // Mode == false utk load dari konten yg telah disave dari server
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
        for (int i=0; i<dataLength; i=i+2) {
            String container = "KPPK " + Integer.toString(cnt) + " - " + containerString.get(i);
            cnt++;

            // Add Button Judul KPPk
            ListKPPK = new Button(getActivity());
            ListKPPK.setText(container);
            ListKPPK.setLayoutParams(params);
            ListKPPK.setBackgroundColor(0);

            final String _isi = containerString.get(i+1);

            // Add button listener here
            ListKPPK.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // masuk ke kosntruktor parameter kppkLengkapFragment dengan parameter isi
                        frag = new KPPKLengkapFragment(_isi);
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
            Log.d("tabel KPPK sudah exist","..");
            generateKontenKPPK(true);
        }
        else {
            // Belum pernah download KPPK, maka tampilkan dari ambil JSON ke server
            /*v.execute();*/
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

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
            String status ="";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_kppk.php");
            HttpResponse response;

            try {
                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }

                Log.d("Result", result);

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                    status = "ok";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
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
                Log.d("tabel KPPK exist! persiapan delete tabel kppk..","..");
                DB.deleteTableKPPK();
            }
            Log.d("persiapan membuat tabel kppk baru..","..");
            DB.createTableKPPK();
            Log.d("persiapan insert data pada tabel kppk..","..");
            DB.insertDataKPPK(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_kppk);
            //add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            int colorBlack = Color.BLACK;
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
                ListKPPK.setTextColor(colorBlack);
                ListKPPK.setBackgroundColor(0);

                final String _isi = isi;

                // Add button listener here
                ListKPPK.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Masuk ke konstruktor parameter kppkLengkapFragment dengan parameter isi
                            frag = new KPPKLengkapFragment(_isi);
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
            String status = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_kppksearch.php?kw=" + keyword);
            HttpResponse response;

            try {
                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }

                Log.d("Result", result);

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                    status = "ok";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            }
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
                Log.d("tabel KPPK exist! persiapan delete tabel kppk..","..");
                DB.deleteTableKPPK();
            }
            Log.d("persiapan membuat tabel kppk baru..","..");
            DB.createTableKPPK();
            Log.d("persiapan insert data pada tabel kppk..","..");
            DB.insertDataKPPK(tmp);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_kppk);
            //add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            int colorBlack = Color.BLACK;

            String container, judul = null, isi = null;

            kppkSaved = new ArrayList<String>();

            // Cari dari server
            if (!adaKPPK) {
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
                    ListKPPK.setTextColor(colorBlack);
                    ListKPPK.setBackgroundColor(0);

                    final String _isi = isi;

                    // Add button listener here
                    ListKPPK.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter kppkLengkapFragment dengan parameter isi
                                    frag = new KPPKLengkapFragment(_isi);
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
                    ListKPPK.setTextColor(colorBlack);
                    ListKPPK.setBackgroundColor(0);

                    final String _isi = isi;

                    // Add button listener here
                    ListKPPK.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Masuk ke konstruktor parameter KPPKLengkapFragment dengan parameter isi
                                    frag = new KPPKLengkapFragment(_isi);
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