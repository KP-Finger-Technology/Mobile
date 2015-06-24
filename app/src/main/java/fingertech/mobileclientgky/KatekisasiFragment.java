package fingertech.mobileclientgky;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KatekisasiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KatekisasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KatekisasiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KatekisasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KatekisasiFragment newInstance(String param1, String param2) {
        KatekisasiFragment fragment = new KatekisasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KatekisasiFragment() {
        // Required empty public constructor
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

//        View rootView = inflater.inflate(R.layout.fragment_katekisasi, container, false);
//        LinearLayout myLinearLayout;
//
//        Log.d("masuk 1:", "yes");
//        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_katekisasi);
//        Log.d("masuk 2:", "yes");
//        //add LayoutParams
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
//        Log.d("masuk 3:", "yes");
//
//        Button dummy = new Button(getActivity());
//        dummy.setText("dummy!");
//        dummy.setLayoutParams(params);
//        myLinearLayout.addView(dummy);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_katekisasi, container, false);
//        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
        private LinearLayout myLinearLayout;

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
            String now = null;
            now = "2015-06-16";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_katekisasi"); // ngikutin ip disini loh
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

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            String judul= null,IsiAyat=null,kitab,pasal,ayat,IsiRenungan=null,linkGambar;
            //add LinearLayout
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_renunganGema);
            //add LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,30);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);

            int colorBlack = Color.BLACK;

            JSONObject jsonobj = null;
            try {
                jsonobj = arr.getJSONObject(1);
                Log.d("JSONObject", arr.getJSONObject(1).toString());
                judul = jsonobj.getString("judul");
                IsiAyat = jsonobj.getString("firman");
                IsiRenungan = jsonobj.getString("deskripsi");
                linkGambar = jsonobj.getString("gambar");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Isi Ayat
            TextView ayatRenungan = new TextView(getActivity());
            ayatRenungan.setText(IsiAyat);
            ayatRenungan.setLayoutParams(params);
//        ayatRenungan.setTextColor(colorWhite);
            ayatRenungan.setGravity(1);
            myLinearLayout.addView(ayatRenungan);

            // Isi Renungan
            TextView isiRenungan = new TextView(getActivity());
            isiRenungan.setText(IsiRenungan);
            isiRenungan.setLayoutParams(params);
            isiRenungan.setGravity(0);
//        isiRenungan.setTextColor(colorWhite);
            myLinearLayout.addView(isiRenungan);

//        } catch(JSONException e){e.printStackTrace();}

        }
    }

}
