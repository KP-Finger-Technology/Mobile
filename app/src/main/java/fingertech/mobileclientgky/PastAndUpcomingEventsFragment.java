package fingertech.mobileclientgky;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Andarias Silvanus
 */
public class PastAndUpcomingEventsFragment extends Fragment {
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
    private LinearLayout cll;
    private String keyword = null;

    private LinearLayout myLinearLayout;
    private TextView TitleEventTV;
    private TextView JudulEventTV;
    private TextView TitleTanggalTV;
    private TextView JudulTanggalTV;
    private TextView TitleWaktuTV;
    private TextView JudulWaktuTV;
    private TextView TitleKeteranganTV;
    private TextView IsiKeteranganTV;
    private Button SelengkapnyaBtn;
    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams paramsJarakAntarEvent;
    private LinearLayout.LayoutParams paramsJarakAntarIsi;
    private LinearLayout.LayoutParams paramsJarakIsiDenganButton;
    private LinearLayout rowLayout;
    private LinearLayout colLayout;
    private LinearLayout subRowLayout;
//    ProgressBar pb;

    public static PastAndUpcomingEventsFragment newInstance(String param1, String param2) {
        PastAndUpcomingEventsFragment fragment = new PastAndUpcomingEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PastAndUpcomingEventsFragment() {}

    private ArrayList<String> judulSaved;
    private ArrayList<String> tanggalSaved;
    private ArrayList<String> keteranganSaved;
    private ArrayList<String> linkSaved;
    private ArrayList<ContainerKomisiEvent> cntKomEv;

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("judulSaved",judulSaved);
        outState.putStringArrayList("tanggalSaved",tanggalSaved);
        outState.putStringArrayList("keteranganSaved",keteranganSaved);
        outState.putStringArrayList("linkSaved", linkSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //  Probably orientation change
            judulSaved = savedInstanceState.getStringArrayList("judulSaved");
            tanggalSaved = savedInstanceState.getStringArrayList("tanggalSaved");
            keteranganSaved = savedInstanceState.getStringArrayList("keteranganSaved");
            linkSaved = savedInstanceState.getStringArrayList("linkSaved");
            generateKontenEvent();
        } else {
            if ((judulSaved != null) && (tanggalSaved != null) && (keteranganSaved != null) && (linkSaved != null) && (cntKomEv != null)) {
                // Returning from backstack, data is fine, do nothing
                generateKontenEvent();
                addItemsOnSpinner();
            } else {
                Viewer v = new Viewer();
                v.execute();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_past_and_upcoming_events, container, false);

        sv = (SearchView) rootView.findViewById(R.id.pastupcoming_searchView);
        cll = (LinearLayout) rootView.findViewById(R.id.container_pastupcoming);
//        pb = (ProgressBar) rootView.findViewById(R.id.pbDefault);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    keyword = URLEncoder.encode(s, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cll.removeAllViews();
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    // Untuk mengecek apakah ada koneksi internet
    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    // Untuk generate tampilan
    public void setUpLayout(){
        myLinearLayout = (LinearLayout)rootView.findViewById(R.id.container_pastupcoming);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        myLinearLayout.removeAllViews();

        // Add LayoutParams
        paramsJarakAntarEvent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJarakAntarEvent.setMargins(0, 15, 20, 0);

        paramsJarakAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJarakAntarIsi.setMargins(5, 0, 0, 0);

        paramsJarakIsiDenganButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsJarakIsiDenganButton.setMargins(5, 5, 0, 15);

        rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Buat linear layout vertical untuk menampung kata-kata
        colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        colLayout.setPadding(0, 10, 10, 0);

        subRowLayout = new LinearLayout(getActivity());
        subRowLayout.setOrientation(LinearLayout.HORIZONTAL);
    }

    public void generateKontenEvent() {
        setUpLayout();

        int dataLength = judulSaved.size();
        for (int i = 0; i < dataLength; i++) {
            generateUI(judulSaved.get(i), tanggalSaved.get(i), keteranganSaved.get(i), linkSaved.get(i));
            if (i != dataLength) {
                rowLayout.addView(colLayout);
                myLinearLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
                colLayout = new LinearLayout(getActivity());
                colLayout.setOrientation(LinearLayout.VERTICAL);
                subRowLayout = new LinearLayout(getActivity());
            }
        }
    }

    public void generateUI (String judul, String tanggal, String keterangan, String linkGambar) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int image_width = display.getWidth()/3;
        int image_height = (int) (display.getHeight()/4.3);

        int defaultColor = getResources().getColor(R.color.defaultFontColor);

        // Add image View
        ImageView GambarIV = new ImageView(getActivity());

        // Loading image from below url into imageView
        Picasso.with(getActivity())
                .load(linkGambar)
                .resize(image_height, image_width)
                .into(GambarIV);
        GambarIV.setLayoutParams(paramsJarakAntarEvent);
        rowLayout.addView(GambarIV);

        // Add text View TitleEventTV
        TitleEventTV = new TextView(getActivity());
        TitleEventTV.setText("Event: ");
        TitleEventTV.setTextColor(defaultColor);
        TitleEventTV.setLayoutParams(paramsJarakAntarIsi);
        TitleEventTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        subRowLayout.addView(TitleEventTV);

        // Add text View JudulEventTV
        JudulEventTV = new TextView(getActivity());
        JudulEventTV.setText(judul);
        JudulEventTV.setTextColor(defaultColor);
        JudulEventTV.setLayoutParams(paramsJarakAntarIsi);

        if (subRowLayout.getParent() != null) {
            ((ViewGroup) subRowLayout.getParent()).removeView(subRowLayout);
        }

        subRowLayout.addView(JudulEventTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View TitleTanggalTV
        TitleTanggalTV = new TextView(getActivity());
        TitleTanggalTV.setText("Tanggal: ");
        TitleTanggalTV.setTextColor(defaultColor);
        TitleTanggalTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        TitleTanggalTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(TitleTanggalTV);

        // Add text View JudulTanggalTV
        JudulTanggalTV= new TextView(getActivity());
        JudulTanggalTV.setText(tanggal);
        JudulTanggalTV.setTextColor(defaultColor);
        JudulTanggalTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(JudulTanggalTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View TitleWaktuTV
        TitleWaktuTV = new TextView(getActivity());
        TitleWaktuTV.setText("Waktu: ");
        TitleWaktuTV.setTextColor(defaultColor);
        TitleWaktuTV.setTextColor(getResources().getColor(R.color.defaultFontColor));
        TitleWaktuTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(TitleWaktuTV);

        // Add text View JudulWaktuTV
        JudulWaktuTV = new TextView(getActivity());
        JudulWaktuTV.setText(tanggal);
        JudulWaktuTV.setTextColor(defaultColor);
        JudulWaktuTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(JudulWaktuTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add text View TitleKeteranganTV
        TitleKeteranganTV = new TextView(getActivity());
        TitleKeteranganTV.setText("Keterangan: ");
        TitleKeteranganTV.setTextColor(defaultColor);
        TitleKeteranganTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(TitleKeteranganTV);

        // Add text View IsiKeteranganTV
        IsiKeteranganTV = new TextView(getActivity());
        if (keterangan.length() > 80) {
            keterangan = keterangan.substring(0, 80);
            keterangan = keterangan + "...";
        }
        IsiKeteranganTV.setText(keterangan);
        IsiKeteranganTV.setTextColor(defaultColor);
        IsiKeteranganTV.setLayoutParams(paramsJarakAntarIsi);
        subRowLayout.addView(IsiKeteranganTV);
        colLayout.addView(subRowLayout);
        subRowLayout = new LinearLayout(getActivity());

        // Add selengkapnya button
        SelengkapnyaBtn = new Button(getActivity());
        SelengkapnyaBtn.setText("Selengkapnya");
        SelengkapnyaBtn.setTextColor(getResources().getColor(R.color.white));
        SelengkapnyaBtn.setLayoutParams(paramsJarakIsiDenganButton);
        SelengkapnyaBtn.setBackgroundColor(getResources().getColor(R.color.header));
        subRowLayout.addView(SelengkapnyaBtn);
        colLayout.addView(subRowLayout);

        final String finalJudul = judul;
        final String finalTanggal = tanggal;
        final String finalKeterangan = keterangan;
        final String finalLinkGambar = linkGambar;
        SelengkapnyaBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Masuk ke konstruktor parameter EventLengkapFragment dengan parameter isi
                        frag = new EventLengkapFragment(finalJudul, finalTanggal, finalKeterangan, finalLinkGambar);
                        fragManager = getActivity().getSupportFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container, frag);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }
                }
        );
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        Spinner dropdownKomisi = (Spinner) rootView.findViewById(R.id.dropdownEvent);
        List<String> list = new ArrayList<String>();
        list.add("Semua komisi");
        for (int i = 0; i < cntKomEv.size(); i++) {
            list.add(cntKomEv.get(i).getNamaKomisi());
        }

        // Set adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownKomisi.setAdapter(dataAdapter);

        dropdownKomisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                if (position != 0) {
                    setUpLayout();
                    ArrayList<JSONObject> jsonKomisi = cntKomEv.get(position-1).getJSON();
                    for (int i = 0; i < jsonKomisi.size(); i++) {
                        try {
                            generateUI(jsonKomisi.get(i).getString("judul"), jsonKomisi.get(i).getString("tanggal"), jsonKomisi.get(i).getString("keterangan"), jsonKomisi.get(i).getString("linkGambar"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (i!=jsonKomisi.size()) {
                            rowLayout.addView(colLayout);
                            myLinearLayout.addView(rowLayout);
                            rowLayout = new LinearLayout(getActivity());
                            colLayout = new LinearLayout(getActivity());
                            colLayout.setOrientation(LinearLayout.VERTICAL);
                            subRowLayout = new LinearLayout(getActivity());
                        }
                    }
                } else {

                    // Terpilih menu dropdown "Semua Komisi", generate UI semuanya
                    generateKontenEvent();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Log.v("dropdownKomisi", "nothing selected");
            }
        });
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

//        ProgressDialog progressDialog;
//        ProgressBar progressBar;

        public JSONArray getArr() {
            return arr;
        }

//        public Viewer(ProgressBar pb) {
//            progressBar = pb;
//        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//            progressDialog = ProgressDialog.show(getActivity(),"Wait","Downloading..");
        };

        @Override
        protected void onProgressUpdate(String... progress) {

//            super.onProgressUpdate(String.valueOf(progress[0]));
//            if (this.progressBar != null) {
//                progressBar.setProgress(Integer.parseInt(progress[0]));
//            }
//            progressBar.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected String doInBackground(String... params) {
            if (isNetworkAvailable()) {
                String result = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url + "view_event.php");
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
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return "";
        }

        private int isExistContainerKomisiEvent(String _namaKomisi){
            int len = cntKomEv.size();
            int idx = -999;
            boolean Mark = false;
            int i = 0;
            while (i < len && !Mark) {
                if (cntKomEv.get(i).getNamaKomisi().equals(_namaKomisi))
                    Mark=true;
                else
                    i++;
            }
            if (Mark)
                idx = i;
            return idx;
        }

        @Override
        protected void onPostExecute(String result) {
            if (arr.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada event", Toast.LENGTH_SHORT).show();
            }

            setUpLayout();

            int dataLength = arr.length();

            String judul = null, tanggal = null, keterangan = null, linkGambar = null;
            judulSaved = new ArrayList<String>();
            tanggalSaved = new ArrayList<String>();
            keteranganSaved = new ArrayList<String>();
            linkSaved = new ArrayList<String>();

            cntKomEv = new ArrayList<ContainerKomisiEvent>();

            // Generate konten Event dalam loop for
            for (int i = 0; i < dataLength; i++){
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    judul = jsonobj.getString("judul");
                    tanggal = jsonobj.getString("tanggal");
                    keterangan = jsonobj.getString("keterangan");
                    linkGambar = Controller.urlgambar ;
                    linkGambar += jsonobj.getString("gambarevent");

                    JSONArray tmp_komisi = jsonobj.getJSONArray("atribut");
                    int length2 = tmp_komisi.length();

                    for (int j=0; j<length2; j++) {
                    // Mengisi nama komisi dan event
                        String tmp_namaKomisi = tmp_komisi.getJSONObject(j).getString("namakomisi");
                        int idx = isExistContainerKomisiEvent(tmp_namaKomisi);

                        JSONObject tmpJSON = new JSONObject();
                        tmpJSON.put("judul",judul);
                        tmpJSON.put("tanggal", tanggal);
                        tmpJSON.put("keterangan",keterangan);
                        tmpJSON.put("linkGambar",linkGambar);

                        if (idx != -999) {
                            // Nama komisi sudah exist
                            cntKomEv.get(idx).setJSON(tmpJSON);
                        } else {
                            // Nama komisi belum exist
                            ContainerKomisiEvent container = new ContainerKomisiEvent();
                            container.setNamaKomisi(tmp_namaKomisi);
                            container.setJSON(tmpJSON);
                            cntKomEv.add(container);
                        }
                    }

                    judulSaved.add(judul);
                    tanggalSaved.add(tanggal);
                    keteranganSaved.add(keterangan);
                    linkSaved.add(linkGambar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                generateUI(judul, tanggal, keterangan, linkGambar);

                if (i!=dataLength) {
                    rowLayout.addView(colLayout);
                    myLinearLayout.addView(rowLayout);
                    rowLayout = new LinearLayout(getActivity());
                    colLayout = new LinearLayout(getActivity());
                    colLayout.setOrientation(LinearLayout.VERTICAL);
                    subRowLayout = new LinearLayout(getActivity());
                }
            }
            addItemsOnSpinner();
        }
    }

    class ViewerSearch extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            if (isNetworkAvailable()) {
                String result = "";
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(Controller.url+"view_eventsearch.php?kw=" + keyword);
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
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            setUpLayout();
            int dataLength = arr.length();

            if (arr.length() == 0 && isNetworkAvailable()){
                Toast.makeText(getActivity().getApplicationContext(), "Event yang Anda cari tidak ditemukan", Toast.LENGTH_SHORT).show();
            }

            String judul = null, tanggal = null, keterangan = null, linkGambar = null;

            // Generate konten Event dalam loop for
            for (int i=0; i<dataLength; i++){
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject",arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    tanggal = jsonobj.getString("tanggal");
                    keterangan = jsonobj.getString("keterangan");
                    linkGambar = Controller.urlgambar ;
                    linkGambar += jsonobj.getString("gambarevent");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                generateUI(judul, tanggal, keterangan, linkGambar);

                if (i != dataLength) {
                    rowLayout.addView(colLayout);
                    myLinearLayout.addView(rowLayout);
                    rowLayout = new LinearLayout(getActivity());
                    colLayout = new LinearLayout(getActivity());
                    colLayout.setOrientation(LinearLayout.VERTICAL);
                    subRowLayout = new LinearLayout(getActivity());
                }
            }
        }
    }
}
