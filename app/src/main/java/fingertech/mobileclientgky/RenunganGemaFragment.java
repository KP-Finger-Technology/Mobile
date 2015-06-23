package fingertech.mobileclientgky;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RenunganGemaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RenunganGemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RenunganGemaFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout myLinearLayout;
    private View rootView;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private int pYear;
    private int pDay;
    private int pMonth;
    private String now = null;
    private EditText dateET;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RenunganGemaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RenunganGemaFragment newInstance(String param1, String param2) {
        RenunganGemaFragment fragment = new RenunganGemaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RenunganGemaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Viewer v = new Viewer();
        v.execute();
    }

    public void generateRenunganContent() {
        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_renunganGema);

        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,30);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);

        // Isi Ayat
        String IsiAyat = "Karena begitu besar kasih Allah akan dunia ini, sehingga Ia telah mengaruniakan Anak-Nya yang tunggal, supaya setiap orang yang percaya kepada-Nya tidak binasa, melainkan beroleh hidup yang kekal. - Yohanes 3:16";
        TextView ayatRenungan = new TextView(getActivity());
        ayatRenungan.setText(IsiAyat);
        ayatRenungan.setLayoutParams(params);
        ayatRenungan.setGravity(1);
        myLinearLayout.addView(ayatRenungan);

        // Isi Renungan
        String IsiRenungan = "ROMA sedang berada di puncak kejayaannya pada abad pertama M. Kekuatan legiun-legiun Romawi memungkinkan kota itu mengendalikan sebagian besar dunia yang dikenal kala itu. Seorang sejarawan melukiskan bala tentara ini sebagai organisasi militer yang paling sukses sepanjang sejarah. Bala tentara profesional Romawi terdiri dari para prajurit berdisiplin tinggi yang menjalani pelatihan yang berat, tetapi keberhasilan mereka sebagai mesin perang yang efektif juga bergantung pada perlengkapan senjata mereka. Rasul Paulus menggunakan perlengkapan senjata prajurit Romawi untuk menggambarkan perlengkapan rohani yang dibutuhkan orang Kristen agar berhasil dalam perang melawan Iblis.\n Kita membaca uraian perlengkapan senjata rohani ini di Efesus 6:14-17. Paulus menulis, Berdirilah teguh, dengan pinggangmu berikatkan kebenaran, dan mengenakan pelindung dada keadilbenaran, dan kakimu berkasutkan kabar baik tentang perdamaian. Di atas segala hal, ambillah perisai besar iman, yang dengannya kamu akan sanggup memadamkan semua senjata lempar yang berapi dari si fasik. Juga, terimalah ketopong keselamatan, dan pedang roh, yaitu firman Allah. Dari sudut pandang manusia, perlengkapan senjata yang diuraikan Paulus bisa melindungi seorang prajurit Romawi dengan sangat baik. Selain itu, sang prajurit diperlengkapi pedang, senjata utamanya untuk pertarungan jarak dekat.";
        TextView isiRenungan = new TextView(getActivity());
        isiRenungan.setText(IsiRenungan);
        isiRenungan.setLayoutParams(params);
        isiRenungan.setGravity(0);
        myLinearLayout.addView(isiRenungan);
    }

    private Button setDateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_renungan_gema, container, false);

        dateET = (EditText) rootView.findViewById(R.id.datePickerEdit);
        setDateBtn = (Button) rootView.findViewById(R.id.datePickerBtn);

        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicking submit datepicker..", Toast.LENGTH_LONG).show();
                myLinearLayout = (LinearLayout) rootView.findViewById(R.id.container_renunganGema);
                myLinearLayout.removeAllViews();
                Viewer newViewer = new Viewer();
                newViewer.execute();
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getFragmentManager(), "datePicker");
            }
        });
        return rootView;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        getActivity().showDialog(999);
        Toast.makeText(getActivity().getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

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

    public void setET(String _s) {
        this.dateET.setText(_s);
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

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet(Controller.url+"view_gema.php?Tanggal="+now); // ngikutin ip disini loh
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
                // Add LinearLayout
                myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_renunganGema);

                // Add LayoutParams
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,30);
                myLinearLayout.setOrientation(LinearLayout.VERTICAL);

                JSONObject jsonobj = null;
                try {
                    jsonobj = arr.getJSONObject(0);
                    Log.d("JSONObject", arr.getJSONObject(0).toString());
                    judul = jsonobj.getString("judul");
                    IsiAyat = jsonobj.getString("firman");
                    IsiRenungan = jsonobj.getString("deskripsi");
                    linkGambar = jsonobj.getString("gambar");

                    Log.d("Isi renungan",IsiRenungan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView ayatRenungan = new TextView(getActivity());
                ayatRenungan.setText(IsiAyat);
                ayatRenungan.setLayoutParams(params);
                ayatRenungan.setGravity(1);
                myLinearLayout.addView(ayatRenungan);

                // Isi Renungan
                TextView isiRenungan = new TextView(getActivity());
                isiRenungan.setText(IsiRenungan);
                isiRenungan.setLayoutParams(params);
                isiRenungan.setGravity(0);
                myLinearLayout.addView(isiRenungan);
            }
    }

    public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private DatePickerDialog.OnDateSetListener mDateSetListener;

        public DatePickerDialogFragment() {
            // nothing to see here, move along
        }

        public DatePickerDialogFragment(DatePickerDialog.OnDateSetListener callback) {
            mDateSetListener = (DatePickerDialog.OnDateSetListener) callback;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d("DatePicker", "masuk create");

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            Log.d("DatePicker", "keluar create");
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.d("DatePicker", "masuk set");
            pYear = year;
            pDay = day;
            pMonth = month;

            Log.d("DatePicker", "keluar set");
            Log.d("DatePicker pyear", Integer.toString(pYear));
            Log.d("DatePicker pday", Integer.toString(pDay));
            Log.d("DatePicker pmonth", Integer.toString(pMonth));

            Toast.makeText(getActivity(), "Tanggal yang Anda pilih: " + Integer.toString(pDay) + "/" + Integer.toString(pMonth + 1) + "/" + Integer.toString(pYear), Toast.LENGTH_LONG).show();

            now = Integer.toString(pYear) + "-" + Integer.toString(pMonth + 1) + "-" + Integer.toString(pDay);
            dateET.setText(Integer.toString(pDay) + "/" + Integer.toString(pMonth + 1) + "/" + Integer.toString(pYear));
        }
    }
}