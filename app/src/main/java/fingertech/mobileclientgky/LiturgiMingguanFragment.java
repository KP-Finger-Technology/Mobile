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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiturgiMingguanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiturgiMingguanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiturgiMingguanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiturgiMingguanFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            String statu ="";
//            for (String urlp : params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_liturgi.php"); // ngikutin ip disini loh
            HttpResponse response;

            try {

                response = client.execute(request);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
//            result = result.substring(result.indexOf("{"), result.indexOf("}") + 1);
                Log.d("Result", result);

                try {
                    JSONObject res = new JSONObject(result);
                    arr = res.getJSONArray("data");
                    Log.d("Array", arr.toString());
                    statu = "ok";

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

        @Override
        protected void onPostExecute(String result) {
            String idLiturgi = null, judulAcara = null, subAcara = null, keterangan = null, idSubAcara = null;

            // Add LinearLayout
            View v = rootView.findViewById(R.id.container_liturgi_mingguan);
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_liturgi_mingguan);

            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 0, 0);

            LinearLayout rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Membuat linear layout vertical untuk menampung kata-kata
            LinearLayout colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0, 5, 0, 0);

            LinearLayout subRowLayout = new LinearLayout(getActivity());
            subRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            int colorBlack = Color.BLACK;

            int dataLength = arr.length();

            // Generate konten Liturgi Mingguan dalam loop for

            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(i);
                    JSONArray jsonArr = jsonobj.getJSONArray("atribut");

                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    idLiturgi = jsonobj.getString("idliturgi");
                    Log.d("jsonf_idliturgi:", idLiturgi);

                    // Add textView idLiturgiTV
                    idLiturgiTV = new TextView(getActivity());
                    idLiturgiTV.setText(idLiturgi);
                    idLiturgiTV.setLayoutParams(params);
                    idLiturgiTV.setTextColor(colorBlack);
                    subRowLayout.addView(idLiturgiTV);
                    rowLayout.addView(subRowLayout);

                    // Add textView judulAcaraTV
                    subRowLayout = new LinearLayout(getActivity());
                    judulAcaraTV = new TextView(getActivity());
                    judulAcaraTV.setText(judulAcara);
                    judulAcaraTV.setLayoutParams(params);
                    judulAcaraTV.setTextColor(colorBlack);
                    subRowLayout.addView(judulAcaraTV);
                    rowLayout.addView(subRowLayout);
                    colLayout.addView(rowLayout);

                    for(int j = 0; j < jsonArr.length(); j++) {
                        judulAcara = jsonArr.getJSONObject(j).getString("judulacara");
                        keterangan = jsonArr.getJSONObject(j).getString("keterangan");
                        idSubAcara = jsonArr.getJSONObject(j).getString("idsubacara");
                        subAcara = jsonArr.getJSONObject(j).getString("subacara");

                        Log.d("jsonf_judul:", judulAcara);
                        Log.d("jsonf_ket:", keterangan);
                        Log.d("jsonf_idsub:", idSubAcara);
                        Log.d("jsonf_sub:", subAcara);

                        // Add textView idSubAcaraTV
                        rowLayout = new LinearLayout(getActivity());
                        subRowLayout = new LinearLayout(getActivity());
                        idSubAcaraTV = new TextView(getActivity());
                        idSubAcaraTV.setText(idSubAcara);
                        idSubAcaraTV.setLayoutParams(params);
                        idSubAcaraTV.setTextColor(colorBlack);
                        subRowLayout.addView(idSubAcaraTV);
                        rowLayout.addView(subRowLayout);

                        // Add textView subAcaraTV
                        subRowLayout = new LinearLayout(getActivity());
                        subAcaraTV = new TextView(getActivity());
                        subAcaraTV.setText(subAcara);
                        subAcaraTV.setLayoutParams(params);
                        subAcaraTV.setTextColor(colorBlack);
                        subRowLayout.addView(subAcaraTV);
                        rowLayout.addView(subRowLayout);

                        // Add textView keteranganTV
                        subRowLayout = new LinearLayout(getActivity());
                        keteranganTV = new TextView(getActivity());
                        keteranganTV.setText(keterangan);
                        keteranganTV.setLayoutParams(params);
                        keteranganTV.setTextColor(colorBlack);
                        subRowLayout.addView(keteranganTV);
                        rowLayout.addView(subRowLayout);
                        colLayout.addView(rowLayout);

                        myLinearLayout.addView(colLayout);
                        rowLayout = new LinearLayout(getActivity());
                        subRowLayout = new LinearLayout(getActivity());
                        colLayout = new LinearLayout(getActivity());
                        colLayout.setOrientation(LinearLayout.VERTICAL);

                        if (j!=jsonArr.length()) {
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

                if (i!=dataLength) {
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