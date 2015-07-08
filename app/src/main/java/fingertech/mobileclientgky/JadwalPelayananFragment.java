package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by William Stefan Hartono
 */
public class JadwalPelayananFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private LinearLayout myLinearLayout;
    private TableLayout myTableLayout;
    private TableRow TR;
    private TextView JudulTabel;
    private TextView IsiTabelHeader;
    private TextView IsiTabel;
    private LinearLayout.LayoutParams params;
    private HorizontalScrollView HSV;
    private TableLayout.LayoutParams tableParams;
    private TableLayout.LayoutParams rowTableParams;

    private ArrayList<JSONObject> pelayananSaved;
    private ArrayList<String> jenisPelayananSaved;
    private int sumTable;
    private ArrayList<Integer> sumRowTable;

    public static JadwalPelayananFragment newInstance(String param1, String param2) {
        JadwalPelayananFragment fragment = new JadwalPelayananFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public JadwalPelayananFragment() {}

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {}
        else {
            if (pelayananSaved != null) {
                // Returning from backstack, data is fine, do nothing
                generateKontenPelayanan();
            }
            else {
                // Newly created, compute data
                Viewer viewer = new Viewer();
                viewer.execute();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_jadwal_pelayanan, container, false);
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
    public void onClick(View v) {
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void IsiTabelHeader (String text) {
        IsiTabelHeader = new TextView(getActivity());
        IsiTabelHeader.setText(text);
        IsiTabelHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabelHeader.setBackground(getResources().getDrawable(R.drawable.header_tabel));
        IsiTabelHeader.setTextColor(getResources().getColor(R.color.white));
        TR.addView(IsiTabelHeader);
    }

    private void IsiTabel (String text) {
        IsiTabel = new TextView(getActivity());
        IsiTabel.setText(text);
        IsiTabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        IsiTabel.setBackground(getResources().getDrawable(R.drawable.background_tabel));
//        IsiTabel.setBackground(getResources().getDrawable(R.drawable.background_tabel_border_topbottom));
        IsiTabel.setTextColor(getResources().getColor(R.color.fontTabel));
        TR.addView(IsiTabel);
    }

    private void setUpLayout() {
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_jadwalPelayanan);

        // Add LayoutParams
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 10, 20, 0);

        tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    }

    private void createHeaderTable() {
        myTableLayout = new TableLayout(getActivity());
        myTableLayout.setLayoutParams(tableParams);
        HSV = new HorizontalScrollView(getActivity());

        TR = new TableRow(getActivity());
        TR.setLayoutParams(tableParams);

        IsiTabelHeader("Tanggal");      // Tanggal
        IsiTabelHeader("Gedung");       // Gedung
        IsiTabelHeader("Kebaktian");    // Kebaktian
        IsiTabelHeader("Waktu");        // Waktu
        IsiTabelHeader("Judul Lagu");   // Judul Lagu
        myTableLayout.addView(TR);      // Add row to table
    }

    private void fillingTable(String tanggal, String gedung, String kebaktian, String waktu, String judulLagu) {
        TR = new TableRow(getActivity());
        TR.setLayoutParams(rowTableParams);
        IsiTabel(tanggal);      // Tanggal
        IsiTabel(gedung);       // Gedung
        IsiTabel(kebaktian);    // Kebaktian
        IsiTabel(waktu);        // Waktu
        IsiTabel(judulLagu);    // Judul Lagu

        // Add row to table
        myTableLayout.addView(TR, tableParams);
    }

    private void setTitleText(String jenisPelayanan) {
        JudulTabel = new TextView(getActivity());
        JudulTabel.setText(jenisPelayanan);
        JudulTabel.setLayoutParams(params);
        myLinearLayout.addView(JudulTabel);
    }

    private void generateKontenPelayanan() {
        setUpLayout();
        int cnt = 0;
        for (int i = 0; i < sumTable; i++) {
            setTitleText(jenisPelayananSaved.get(i));
            createHeaderTable();
			
            for (int j = 0; j < sumRowTable.get(i); j++) {
                String tanggal = null, gedung = null, kebaktian = null, waktu = null, judulLagu = null;
                try {
                    tanggal = pelayananSaved.get(cnt).getString("tanggal");
                    gedung = pelayananSaved.get(cnt).getString("gedung");
                    kebaktian = pelayananSaved.get(cnt).getString("kebaktian");
                    waktu = pelayananSaved.get(cnt).getString("waktu");
                    judulLagu = pelayananSaved.get(cnt).getString("judulLagu");

                    fillingTable(tanggal, gedung, kebaktian, waktu, judulLagu);
                    cnt++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            HSV.addView(myTableLayout);
            HSV.setScrollbarFadingEnabled(false);
            myLinearLayout.addView(HSV);
        }
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute()
        {
        };

        @Override
        protected String doInBackground(String... params) {
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());

            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_jadwalpelayanan.php?id="+sm.pref.getAll().get("id").toString()); // ngikutin ip disini loh
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

        @Override
        protected void onPostExecute(String result) {
            setUpLayout();

            pelayananSaved = new ArrayList<JSONObject>();
            sumRowTable = new ArrayList<Integer>();
            jenisPelayananSaved = new ArrayList<String>();

            int dataLength = arr.length();
            sumTable = dataLength;
            String jenisPelayanan = null, tanggal = null, gedung = null, kebaktian = null, waktuMulai = null, waktuSelesai = null, judulLagu = null;

            // Generate konten Event dalam loop for
            for (int i = 0; i < dataLength; i++){
                JSONObject jsonobj = null;

                createHeaderTable();

                try {
                    jsonobj = arr.getJSONObject(i);
                    jenisPelayanan = jsonobj.getString("jenispelayanan");
                    JSONArray jsonArr = jsonobj.getJSONArray("atribut");

                    setTitleText(jenisPelayanan);
                    jenisPelayananSaved.add(jenisPelayanan);

                    int length2 = jsonArr.length();
                    sumRowTable.add(length2);
                    for (int j = 0; j < length2; j++) {
                        tanggal = jsonArr.getJSONObject(j).getString("tanggal");
                        gedung = jsonArr.getJSONObject(j).getString("gedung");
                        kebaktian = jsonArr.getJSONObject(j).getString("kebaktian");
                        waktuMulai = jsonArr.getJSONObject(j).getString("waktumulai");
                        waktuSelesai = jsonArr.getJSONObject(j).getString("waktuselesai");
                        judulLagu = jsonArr.getJSONObject(j).getString("judul");
                        String waktu = waktuMulai + " - " + waktuSelesai;

                        JSONObject tmp_json = new JSONObject();
                        tmp_json.put("tanggal", tanggal);
                        tmp_json.put("gedung", gedung);
                        tmp_json.put("kebaktian", kebaktian);
                        tmp_json.put("waktu", waktu);
                        tmp_json.put("judulLagu", judulLagu);
                        pelayananSaved.add(tmp_json);
                        fillingTable(tanggal, gedung, kebaktian, waktu, judulLagu);
                    }
                    HSV.addView(myTableLayout);
                    HSV.setScrollbarFadingEnabled(false);
                    myLinearLayout.addView(HSV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}