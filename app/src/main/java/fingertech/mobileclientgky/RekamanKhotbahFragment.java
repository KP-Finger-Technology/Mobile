package fingertech.mobileclientgky;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RekamanKhotbahFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RekamanKhotbahFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RekamanKhotbahFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private LinearLayout myLinearLayout;
    private LinearLayout rowLayout;
    private LinearLayout subRowLayout;
    private LinearLayout colLayout;

    // Untuk komponen-komponen
    private TextView TitleJudulTV;
    private TextView JudulTV;
    private TextView TitleIsiTV;
    private TextView IsiTV;
    private TextView TitleTanggalTV;
    private TextView TanggalTV;
    private TextView TitlePembicaraTV;
    private TextView PembicaraTV;

    private OnFragmentInteractionListener mListener;

    private Viewer v = new Viewer();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RekamanKhotbahFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RekamanKhotbahFragment newInstance(String param1, String param2) {
        RekamanKhotbahFragment fragment = new RekamanKhotbahFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RekamanKhotbahFragment() {}

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
        rootView = inflater.inflate(R.layout.fragment_rekaman_khotbah, container, false);
        v.execute();
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
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String status = "";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_kotbah.php");
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

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_khotbah);

            // Add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 0, 0, 0);

            LinearLayout.LayoutParams paramsSpasi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 0, 0);

            rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Buat linear layout vertical untuk menampung kata-kata
            colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0, 10, 10, 0);

            subRowLayout = new LinearLayout(getActivity());
            subRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            int dataLength = arr.length();
            int colorBlack = Color.BLACK;
            String container, judul = null, isi = null, tanggal = null, pembicara = null;

            // Generate konten Khotbah dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;
                judul = "";
                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    isi = jsonobj.getString("isi");
                    tanggal = jsonobj.getString("tanggal");
                    pembicara = jsonobj.getString("pembicara");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Add text View TitleJudulTV
                TitleJudulTV = new TextView(getActivity());
                TitleJudulTV.setText("Judul: ");
                TitleJudulTV.setLayoutParams(paramsSpasi);
                TitleJudulTV.setTextColor(colorBlack);
                subRowLayout.addView(TitleJudulTV);

                // Add text View JudulTV
                JudulTV = new TextView(getActivity());
                JudulTV.setText(judul);
                JudulTV.setLayoutParams(paramsSpasi);
                subRowLayout.addView(JudulTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitleIsiTV
                TitleIsiTV = new TextView(getActivity());
                TitleIsiTV.setText("Isi ");
                TitleIsiTV.setLayoutParams(params);
                TitleIsiTV.setTextColor(colorBlack);
                subRowLayout.addView(TitleIsiTV);

                // Add text View IsiTV
                IsiTV = new TextView(getActivity());
                IsiTV.setText(Html.fromHtml("<a href=\"" + isi + "\">" + "dapat di download di sini" + "</a>"));
                IsiTV.setClickable(true);
                IsiTV.setMovementMethod(LinkMovementMethod.getInstance());
                IsiTV.setLayoutParams(params);
                subRowLayout.addView(IsiTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitleTanggalTV
                TitleTanggalTV = new TextView(getActivity());
                TitleTanggalTV.setText("Tanggal: ");
                TitleTanggalTV.setLayoutParams(params);
                TitleTanggalTV.setTextColor(colorBlack);
                subRowLayout.addView(TitleTanggalTV);

                // Add text View TanggalTV
                TanggalTV = new TextView(getActivity());
                TanggalTV.setText(tanggal);
                TanggalTV.setLayoutParams(params);
                subRowLayout.addView(TanggalTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

                // Add text View TitlePembicaraTV
                TitlePembicaraTV = new TextView(getActivity());
                TitlePembicaraTV.setText("Pembicara: ");
                TitlePembicaraTV.setLayoutParams(params);
                TitlePembicaraTV.setTextColor(colorBlack);
                subRowLayout.addView(TitlePembicaraTV);

                // Add text View PembicaraTV
                PembicaraTV = new TextView(getActivity());
                PembicaraTV.setText(pembicara);
                PembicaraTV.setLayoutParams(params);
                subRowLayout.addView(PembicaraTV);
                colLayout.addView(subRowLayout);
                subRowLayout = new LinearLayout(getActivity());

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
