package fingertech.mobileclientgky;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Andarias Silvanus
 */
public class JadwalIbadahFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;

    private OnFragmentInteractionListener mListener;

    public static JadwalIbadahFragment newInstance(String param1, String param2) {
        JadwalIbadahFragment fragment = new JadwalIbadahFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public JadwalIbadahFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Viewer viewer = new Viewer();
        viewer.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_jadwal_ibadah, container, false);
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

    class Viewer extends AsyncTask<String, String, String> {
        // Untuk komponen-komponen
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
            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_jadwalibadah.php");
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
                    // Data
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array data", arr.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

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
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_jadwalIbadah);

            // Add LayoutParams
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);

            int dataLength = arr.length();
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            int defaultColor = getResources().getColor(R.color.defaultFont);

            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableLayout.LayoutParams rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            HorizontalScrollView HSV;

            String tanggal = null, isi = null;

            myTableLayout = new TableLayout(getActivity());
            myTableLayout.setLayoutParams(tableParams);
            HSV = new HorizontalScrollView(getActivity());

            TR = new TableRow(getActivity());
            TR.setLayoutParams(tableParams);

            // Judul kolom
            // Tanggal
            IsiTabelHeader("Tanggal");

            // Isi
            IsiTabelHeader("Isi");

            // Add row to table
            myTableLayout.addView(TR);

            // Generate konten Jadwal Ibadah dalam loop for
            for (int i = 0; i < dataLength; i++){
                JSONObject jsonobj = null;

                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    tanggal = jsonobj.getString("tanggal");
                    isi = jsonobj.getString("isi");

                    TR = new TableRow(getActivity());
                    TR.setLayoutParams(rowTableParams);

                    // Tanggal
                    IsiTabel(tanggal);

                    // Isi
                    IsiTabel(isi);

                    // Add row to table
                    myTableLayout.addView(TR, tableParams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            HSV.addView(myTableLayout);
            myLinearLayout.addView(HSV);
        }
    }
}
