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
import android.widget.LinearLayout;
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
public class LiturgiMingguanFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Untuk komponen-komponen
    private LinearLayout myLinearLayout;
    private TextView idLiturgiTV;
    private TextView judulAcaraTV;
    private TextView keteranganTV;
    private TextView idSubAcaraTV;
    private TextView subAcaraTV;
    private View rootView;

    public static LiturgiMingguanFragment newInstance(String param1, String param2) {
        LiturgiMingguanFragment fragment = new LiturgiMingguanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LiturgiMingguanFragment() {}

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
        rootView = inflater.inflate(R.layout.fragment_liturgi_mingguan, container, false);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    class Viewer extends AsyncTask<String, String, String> {
        JSONArray arr = new JSONArray();

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {};

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String statu ="";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_liturgi.php");
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

        @Override
        protected void onPostExecute(String result) {
            String idLiturgi = null, judulAcara = null, subAcara = null, keterangan = null, idSubAcara = null;

            // Add LinearLayout
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_liturgi_mingguan);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);

            // Add LayoutParams
            LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params0.setMargins(0, 0, 0, 0);

            LinearLayout.LayoutParams paramsJarakIDLiturgiDenganAcara = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakIDLiturgiDenganAcara.setMargins(10, 0, 0, 0);

            LinearLayout.LayoutParams paramsJarakIDLiturgiDenganIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakIDLiturgiDenganIsi.setMargins(0, 10, 0, 0);

            LinearLayout.LayoutParams paramsJarakAntarIsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakAntarIsi.setMargins(0, 0, 0, 0);

            LinearLayout.LayoutParams paramsJarakAntarKolom = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsJarakAntarKolom.setMargins(30, 0, 0, 0);

            LinearLayout rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Membuat linear layout vertical untuk menampung kata-kata
            LinearLayout colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0, 0, 0, 0);

            LinearLayout subRowLayout = new LinearLayout(getActivity());
            subRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int defaultColor = getResources().getColor(R.color.defaultFont);
            int dataLength = arr.length();

            // Generate konten Liturgi Mingguan dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    JSONArray jsonArr = jsonobj.getJSONArray("atribut");

                    idLiturgi = jsonobj.getString("idliturgi");

                    // Add textView idLiturgiTV
                    idLiturgiTV = new TextView(getActivity());
                    idLiturgiTV.setText(idLiturgi);
                    idLiturgiTV.setLayoutParams(paramsJarakIDLiturgiDenganIsi);
                    idLiturgiTV.setTextColor(getResources().getColor(R.color.defaultFont));
                    subRowLayout.addView(idLiturgiTV);
                    rowLayout.addView(subRowLayout);

                    // Add textView judulAcaraTV
                    subRowLayout = new LinearLayout(getActivity());
                    judulAcaraTV = new TextView(getActivity());
                    judulAcaraTV.setText(judulAcara);
                    judulAcaraTV.setLayoutParams(paramsJarakIDLiturgiDenganIsi);
                    judulAcaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                    subRowLayout.addView(judulAcaraTV);
                    rowLayout.addView(subRowLayout);
                    colLayout.addView(rowLayout);

                    for(int j = 0; j < jsonArr.length(); j++) {
                        judulAcara = jsonArr.getJSONObject(j).getString("judulacara");
                        keterangan = jsonArr.getJSONObject(j).getString("keterangan");
                        idSubAcara = jsonArr.getJSONObject(j).getString("idsubacara");
                        subAcara = jsonArr.getJSONObject(j).getString("subacara");

                        // Add textView idSubAcaraTV
                        rowLayout = new LinearLayout(getActivity());
                        subRowLayout = new LinearLayout(getActivity());
                        idSubAcaraTV = new TextView(getActivity());
                        idSubAcaraTV.setText(idSubAcara);
                        idSubAcaraTV.setLayoutParams(paramsJarakAntarIsi);
                        idSubAcaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                        subRowLayout.addView(idSubAcaraTV);
                        rowLayout.addView(subRowLayout);

                        // Add textView subAcaraTV
                        subRowLayout = new LinearLayout(getActivity());
                        subAcaraTV = new TextView(getActivity());
                        subAcaraTV.setText(subAcara);
                        subAcaraTV.setLayoutParams(paramsJarakAntarKolom);
                        subAcaraTV.setTextColor(getResources().getColor(R.color.defaultFont));
                        subRowLayout.addView(subAcaraTV);
                        rowLayout.addView(subRowLayout);

                        // Add textView keteranganTV
                        subRowLayout = new LinearLayout(getActivity());
                        keteranganTV = new TextView(getActivity());
                        keteranganTV.setText(keterangan);
                        keteranganTV.setLayoutParams(paramsJarakAntarKolom);
                        keteranganTV.setTextColor(getResources().getColor(R.color.defaultFont));
                        subRowLayout.addView(keteranganTV);
                        rowLayout.addView(subRowLayout);
                        colLayout.addView(rowLayout);

                        myLinearLayout.addView(colLayout);
                        rowLayout = new LinearLayout(getActivity());
                        subRowLayout = new LinearLayout(getActivity());
                        colLayout = new LinearLayout(getActivity());
                        colLayout.setOrientation(LinearLayout.VERTICAL);

                        if (j != jsonArr.length()) {
                            rowLayout.addView(colLayout);
                            myLinearLayout.addView(rowLayout);
                            rowLayout = new LinearLayout(getActivity());
                            colLayout = new LinearLayout(getActivity());
                            colLayout.setOrientation(LinearLayout.VERTICAL);
                            subRowLayout = new LinearLayout(getActivity());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                myLinearLayout.addView(colLayout);
                rowLayout = new LinearLayout(getActivity());
                subRowLayout = new LinearLayout(getActivity());
                colLayout = new LinearLayout(getActivity());
                colLayout.setOrientation(LinearLayout.VERTICAL);

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