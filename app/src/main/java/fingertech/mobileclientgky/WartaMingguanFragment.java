package fingertech.mobileclientgky;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
/*import android.app.Fragment;*/
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

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
 * {@link WartaMingguanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WartaMingguanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WartaMingguanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WartaMingguanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WartaMingguanFragment newInstance(String param1, String param2) {
        WartaMingguanFragment fragment = new WartaMingguanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WartaMingguanFragment() {
        // Required empty public constructor
    }

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
        rootView = inflater.inflate(R.layout.fragment_warta_mingguan, container, false);

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_warta_mingguan, container, false);
        return rootView;
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
        // Untuk komponen-komponen
        private LinearLayout myLinearLayout;
        private TableLayout myTableLayout;
        private TableRow TR;
        private TextView JudulTabel;
        private TextView IsiTabel;
        private TextView judulTV;
        private TextView deskripsiTV;
        private LinearLayout.LayoutParams params;
        private LinearLayout.LayoutParams paramsDeskripsi;

        JSONObject obj = new JSONObject();

        public JSONObject getObj() {
            return obj;
        }

//        public JSONArray getArr() {
//            return arr;
//        }

        @Override
        protected void onPreExecute()
        {
        };

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String statu = "";
//            for (String urlp : params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://192.168.1.108/gky_web_service/view_wartamingguan.php"); // ngikutin ip disini loh
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
                    // data
                    JSONObject res = new JSONObject(result);
                    Log.d("bikin res",res.toString());
                    obj = res.getJSONObject("data");
//                    Log.d("jooo" , test.toString());

//                    for (int i=0; i<arr.length(); i++) {
//                        JSONObject z = arr.getJSONObject(i);
//                        for (int j=0; j<z.length(); j++) {
//                            Log.d("kode i="+i+" dan j="+j,z.get);
//                        }
//                    }

//                    JSONObject b = arr.getJSONObject(1);
//                    Log.d("array warta atau 1", b.toString());
//                    JSONObject c = arr.getJSONObject(2);
//                    Log.d("array warta atau 2",c.toString());
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.d("excep frm try2 Bckgrnd", "..");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            }
            return "";
        }

        private void IsiTabel (String text) {
            IsiTabel = new TextView(getActivity());
            IsiTabel.setText(text);
            IsiTabel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            IsiTabel.setBackground(getResources().getDrawable(R.drawable.cell_shape));
            TR.addView(IsiTabel);
        }

        @Override
        protected void onPostExecute(String result) {
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_wartaMingguan);

            // Add LayoutParams
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 0, 0);

            // Param untuk deskripsi
            paramsDeskripsi = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            paramsDeskripsi.setMargins(0, 0, 0, 0);

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            int colorBlack = Color.BLACK;
            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableLayout.LayoutParams rowTableParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            HorizontalScrollView HSV;

            // Untuk tag "warta"
            LinearLayout rowLayout = new LinearLayout(getActivity());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Membuat linear layout vertical untuk menampung kata-kata
            LinearLayout colLayout = new LinearLayout(getActivity());
            colLayout.setOrientation(LinearLayout.VERTICAL);
            colLayout.setPadding(0, 5, 0, 0);

            LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            String tanggal=null, kebaktian=null, pengkotbah=null, judul=null, deskripsi=null;

            JSONArray jadwal = new JSONArray();
            JSONArray warta = new JSONArray();
            try {
                jadwal = obj.getJSONArray("jadwal");
                warta = obj.getJSONArray("warta");
            }
            catch (JSONException e) {
                Log.d("excp olah atribut jdwal","..");
            }
            int dataLength = obj.length();
//            int dataLength = jadwal.length();
            Log.d("length obj.length",Integer.toString(obj.length()));
            Log.d("length jadwal.length",Integer.toString(jadwal.length()));

            // Generate konten Warta Mingguan dalam loop for
            for (int i=0; i < dataLength; i++){
                try {
                    JSONArray jsonAtribut = jadwal.getJSONObject(i).getJSONArray("atribut");
                    Log.d("jsonAtribut: ", jsonAtribut.toString());

                    tanggal = jadwal.getJSONObject(i).getString("tanggal");
                    Log.d("jsonTanggal: ", tanggal);

                    Log.d("tanggal di iterasi-"+Integer.toString(i),tanggal);
                    // ============= mungkin salah di tanggal, perhatiin lagi sama ambil tanggal di mana

                    myTableLayout = new TableLayout(getActivity());
                    myTableLayout.setLayoutParams(tableParams);
                    HSV = new HorizontalScrollView(getActivity());

                    TR = new TableRow(getActivity());
                    TR.setLayoutParams(rowTableParams);

                    // Judul kolom
                    IsiTabel("Kebaktian");  // Kebaktian
                    IsiTabel("Pengkotbah"); // Pengkotbah
                    myTableLayout.addView(TR, tableParams);  // Add row to table

                    JudulTabel = new TextView(getActivity());
                    JudulTabel.setText("Jadwal khotbah pada tanggal "+tanggal);
                    JudulTabel.setLayoutParams(params);
                    myLinearLayout.addView(JudulTabel);

                    int length2 = jsonAtribut.length();
                    for (int j=0; j<length2; j++) {
                        pengkotbah = jsonAtribut.getJSONObject(j).getString("pengkotbah");
                        kebaktian = jsonAtribut.getJSONObject(j).getString("kebaktianumum");

                        TR = new TableRow(getActivity());
                        TR.setLayoutParams(rowTableParams);
                        // Kebaktian
                        IsiTabel(kebaktian);
                        // Pengkotbah
                        IsiTabel(pengkotbah);
                        // Add row to table
                        myTableLayout.addView(TR, tableParams);
                    }
                    HSV.addView(myTableLayout);
                    myLinearLayout.addView(HSV);
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.d("excp di try atribut","..");
                }
            }

            dataLength = warta.length();

            for (int i=0; i < dataLength; i++){
                JSONObject jsonobj = null;
                try {
                    jsonobj = warta.getJSONObject(i);
                    Log.d("JSONObject", warta.getJSONObject(i).toString());
                    judul = jsonobj.getString("judul");
                    deskripsi = jsonobj.getString("deskripsi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Judul warta
                TextView wartaTV = new TextView(getActivity());
                wartaTV.setText("Judul: " + judul);
                wartaTV.setLayoutParams(params);
                myLinearLayout.addView(wartaTV);

                // Deskripsi warta
                wartaTV = new TextView(getActivity());
                wartaTV.setText("Deskripsi: " + deskripsi);
                wartaTV.setLayoutParams(paramsDeskripsi);
                myLinearLayout.addView(wartaTV);
            }
        }
    }
}