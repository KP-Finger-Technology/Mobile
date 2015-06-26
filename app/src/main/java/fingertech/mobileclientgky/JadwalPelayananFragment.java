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


/**
 * Created by William Stefan Hartono
 */
public class JadwalPelayananFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button buttonFetchData;
    private View rootView;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Viewer v = new Viewer();
        v.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_jadwal_pelayanan, container, false);

//        buttonFetchData = (Button)rootView.findViewById(R.id.jadwalPelayanan_fetchData);
//        buttonFetchData.setOnClickListener(this);

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

    class Viewer extends AsyncTask<String, String, String> {
        private LinearLayout myLinearLayout;
        private TableLayout myTableLayout;
        private TableRow TR;
        private TextView JudulTabel;
        private TextView IsiTabelHeader;
        private TextView IsiTabel;
        private LinearLayout.LayoutParams params;

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

            Log.d("jadwalpel",sm.pref.getAll().toString());
            Log.d("Nm",sm.pref.getAll().get("name").toString());
            Log.d("ID", sm.pref.getAll().get("id").toString());
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
                    Log.d("Array", arr.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//            }
            return "";
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
            IsiTabel.setTextColor(getResources().getColor(R.color.fontTabel));
            TR.addView(IsiTabel);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_jadwalPelayanan);

            // Add LayoutParams
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableLayout.LayoutParams rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            HorizontalScrollView HSV;

            String jenisPelayanan = null, tanggal = null, gedung = null, kebaktian = null, waktuMulai = null, waktuSelesai = null, judulLagu = null;

            // Generate konten Event dalam loop for
            for (int i = 0; i < dataLength; i++){
                JSONObject jsonobj = null;

                myTableLayout = new TableLayout(getActivity());
                myTableLayout.setLayoutParams(tableParams);
                HSV = new HorizontalScrollView(getActivity());

                TR = new TableRow(getActivity());
                TR.setLayoutParams(tableParams);

                // Tanggal
                IsiTabelHeader("Tanggal");

                // Gedung
                IsiTabelHeader("Gedung");

                // Kebaktian
                IsiTabelHeader("Kebaktian");

                // Waktu
                IsiTabelHeader("Waktu");

                // Judul Lagu
                IsiTabelHeader("Judul Lagu");

                // Add row to table
                myTableLayout.addView(TR);

                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject",arr.getJSONObject(i).toString());
                    jenisPelayanan = jsonobj.getString("jenispelayanan");
                    JSONArray jsonArr = jsonobj.getJSONArray("atribut");

                    JudulTabel = new TextView(getActivity());
                    JudulTabel.setText(jenisPelayanan);
                    JudulTabel.setLayoutParams(params);
                    myLinearLayout.addView(JudulTabel);

                    int length2 = jsonArr.length();
                    for (int j = 0; j < length2; j++) {
                        tanggal = jsonArr.getJSONObject(j).getString("tanggal");
                        gedung = jsonArr.getJSONObject(j).getString("gedung");
                        kebaktian = jsonArr.getJSONObject(j).getString("kebaktian");
                        waktuMulai = jsonArr.getJSONObject(j).getString("waktumulai");
                        waktuSelesai = jsonArr.getJSONObject(j).getString("waktuselesai");
                        judulLagu = jsonArr.getJSONObject(j).getString("judul");

                        TR = new TableRow(getActivity());
                        TR.setLayoutParams(rowTableParams);

                        // Tanggal
                        IsiTabel(tanggal);

                        // Gedung
                        IsiTabel(gedung);

                        // Kebaktian
                        IsiTabel(kebaktian);

                        // Waktu
                        String waktu = waktuMulai + " - " + waktuSelesai;
                        IsiTabel(waktu);

                        // Judul Lagu
                        IsiTabel(judulLagu);

                        // Add row to table
                        myTableLayout.addView(TR, tableParams);
                    }
                    HSV.addView(myTableLayout);
                    myLinearLayout.addView(HSV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
