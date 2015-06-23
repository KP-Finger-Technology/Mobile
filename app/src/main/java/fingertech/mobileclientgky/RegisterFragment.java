package fingertech.mobileclientgky;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private EditText dateET;
    private Button daftarButton;
    private int pYear;
    private int pDay;
    private int pMonth;
    private String now = null;
    public Controller cont;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        cont  = new Controller(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);

        dateET = (EditText) rootView.findViewById(R.id.register_editTanggalLahir);
        daftarButton = (Button) rootView.findViewById(R.id.register_daftar);

        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDate(v);
                registerClicked(rootView);
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDate(v);
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getFragmentManager(), "datePicker");
//                Toast.makeText(getActivity(), "clicking datepicker..", Toast.LENGTH_LONG).show();
                /*Log.d("tanggal yg dipilih, tahun:"+ Integer.toString(datePicker.getpYear()) +" bulan:"+Integer.toString(datePicker.getpMonth())+" hari:"+Integer.toString(datePicker.getpDay()),"..");*/
            }
        });

        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_register, container, false);*/
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

    @Override
    public void onClick(View v) {

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

    public void registerClicked(View v){
        EditText namaET = (EditText) rootView.findViewById(R.id.register_editNama);
        EditText passET = (EditText) rootView.findViewById(R.id.register_editPassword);
        EditText passconET = (EditText) rootView.findViewById(R.id.register_editKonfirmasiPassword);

        EditText alamatET = (EditText) rootView.findViewById(R.id.register_editAlamat);
        EditText emailET = (EditText) rootView.findViewById(R.id.register_editEmail);
        EditText teleponET = (EditText) rootView.findViewById(R.id.register_editTelepon);
        EditText idbaptisET = (EditText) rootView.findViewById(R.id.register_editIdBaptis);

        String pass = passET.getText().toString();
        String passcon = passconET.getText().toString();
        if (pass.equals(passcon)) {
            String nama = null,email = null,telepon = null,alamat = null,idbaptis = null;
            try {
                nama = URLEncoder.encode(namaET.getText().toString(), "utf-8");
                email = URLEncoder.encode(emailET.getText().toString(), "utf-8");
                telepon = URLEncoder.encode(teleponET.getText().toString(),"utf-8");
                alamat = URLEncoder.encode(alamatET.getText().toString(), "utf-8");
                idbaptis = URLEncoder.encode(idbaptisET.getText().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

//            boolean checked = ((CheckBox) v).isChecked();
            boolean checked_komisiAnak = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiAnak)).isChecked();
            boolean checked_komisiKaleb = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiKaleb)).isChecked();
            boolean checked_komisiPasutri = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiPasutri)).isChecked();
            boolean checked_komisiPemuda = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiPemudaDewasa)).isChecked();
            boolean checked_komisiRemaja = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiRemajaPemuda)).isChecked();
            boolean checked_komisiWanita = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiWanita)).isChecked();

            boolean checked_pelayananAnak = ((CheckBox) rootView.findViewById(R.id.register_checkboxPelayananAnak)).isChecked();
            boolean checked_pelayananKaleb = ((CheckBox) rootView.findViewById(R.id.register_checkboxPelayananKaleb)).isChecked();
            boolean checked_pelayananPasutri = ((CheckBox) rootView.findViewById(R.id.register_checkboxKomisiPasutri)).isChecked();
            boolean checked_pelayananPemuda = ((CheckBox) rootView.findViewById(R.id.register_checkboxPelayananPemudaDewasa)).isChecked();
            boolean checked_pelayananRemaja = ((CheckBox) rootView.findViewById(R.id.register_checkboxPelayananRemajaPemuda)).isChecked();
            boolean checked_pelayananWanita = ((CheckBox) rootView.findViewById(R.id.register_checkboxPelayananWanita)).isChecked();

            String komisi = "";
            Log.d("komisi", komisi);

            String pelayanan = "";

            // Check which radio button was clicked
//            switch(v.getId()) {
//                case R.id.register_checkboxKomisiAnak:
            if (checked_komisiAnak){
                if(komisi!="")
                    komisi+=",";
                komisi+="1";
//                        break;
            }
//                case R.id.register_checkboxKomisiKaleb:
            if (checked_komisiKaleb){
                if(komisi!="")
                    komisi+=",";
                komisi+="2";
//                        break;
            }
            if (checked_komisiPasutri){
                if(komisi!="")
                    komisi+=",";
                komisi+="3";
                //                        break;
            }
//                case R.id.register_checkboxKomisiPemudaDewasa:
            if (checked_komisiPemuda){
                if(komisi!="")
                    komisi+=",";
                komisi+="4";
//                        break;
            }
            if (checked_komisiPemuda){
                if(komisi!="")
                    komisi+=",";
                komisi+="4";
                //                        break;
            }
            if (checked_komisiRemaja){
                if(komisi!="")
                    komisi+=",";
                komisi+="5";
            }
            if (checked_komisiRemaja){
                if(komisi!="")
                    komisi+=",";
                komisi+="6";
            }


            if (checked_pelayananAnak){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="1";
            }

            if (checked_pelayananKaleb){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="2";
            }
            if (checked_pelayananPasutri){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="3";
            }
            if (checked_pelayananPemuda){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="4";
            }
            if (checked_pelayananPemuda){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="4";
            }
            if (checked_pelayananRemaja){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="5";
            }
            if (checked_pelayananRemaja){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="6";
            }

//            }
            Log.d("komisi selected",komisi);
            Log.d("pelayanan selected",pelayanan);

            /*Date date = new Date();*/
            String dateInString = null;

            try {
                /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");*/
                dateInString = now;
                /*date = formatter.parse(dateInString);
                Log.d("now", now);*/
                Log.d("registerdateee", dateInString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            cont.register(nama,pass,email,telepon,alamat,dateInString,idbaptis,komisi,pelayanan);
        }else{
            //password dan konfirmasi tidak sama, keluarin toast.
            Toast.makeText(getActivity(), "Re-enter Password", Toast.LENGTH_LONG).show();
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
            Calendar cal = Calendar.getInstance();

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

            String bulan = null;
            // Januari
            if (pMonth == 0) {
                bulan = "01";
            }
            // Februari
            else if (pMonth == 1) {
                bulan = "02";
            }
            // Maret
            else if (pMonth == 2) {
                bulan = "03";
            }
            // April
            else if (pMonth == 3) {
                bulan = "04";
            }
            // Mei
            else if (pMonth == 4) {
                bulan = "05";
            }
            // Juni
            else if (pMonth == 5) {
                bulan = "06";
            }
            // Juli
            else if (pMonth == 6) {
                bulan = "07";
            }
            // Agustus
            else if (pMonth == 7) {
                bulan = "08";
            }
            // September
            else if (pMonth == 8) {
                bulan = "09";
            }
            // Oktober
            else if (pMonth == 9) {
                bulan = "10";
            }
            // November
            else if (pMonth == 10) {
                bulan = "11";
            }
            // Desember
            else if (pMonth == 11) {
                bulan = "12";
            }

            now = Integer.toString(pYear) + "-" + bulan + "-" + Integer.toString(pDay);
            dateET.setText(Integer.toString(pDay) + "/" + bulan + "/" + Integer.toString(pYear));
        }
    }
}
