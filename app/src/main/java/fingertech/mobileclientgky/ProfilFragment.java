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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    /*private EditText dateET;*/
    private Button ubahButton;
    private int pYear;
    private int pMonth;
    private int pDay;
    /*private String now = null;
    private String tglLahir = null;*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfilFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        /*dateET = (EditText) rootView.findViewById(R.id.profil_editTanggalLahir);*/
        ubahButton = (Button) rootView.findViewById(R.id.profil_ubah);

        ubahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDate(v);
                ubahClicked(rootView);
            }
        });

/*        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDate(v);
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getFragmentManager(), "datePicker");
//                Toast.makeText(getActivity(), "clicking datepicker..", Toast.LENGTH_LONG).show();
                *//*Log.d("tanggal yg dipilih, tahun:"+ Integer.toString(datePicker.getpYear()) +" bulan:"+Integer.toString(datePicker.getpMonth())+" hari:"+Integer.toString(datePicker.getpDay()),"..");*//*
            }
        });*/

        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_profil,, container, false);*/
        generateProfilContent();
        return rootView;
    }
    public void ubahClicked(View v){
        Controller cont= new Controller(getActivity().getApplicationContext());

        EditText namaET = (EditText) rootView.findViewById(R.id.profil_editNama);
        EditText alamatET = (EditText) rootView.findViewById(R.id.profil_editAlamat);
        EditText emailET = (EditText) rootView.findViewById(R.id.profil_editEmail);
        EditText teleponET = (EditText) rootView.findViewById(R.id.profil_editTelepon);
        EditText idbaptisET = (EditText) rootView.findViewById(R.id.profil_editIdBaptis);
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
            boolean checked_komisiAnak = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiAnak)).isChecked();
            boolean checked_komisiKaleb = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiKaleb)).isChecked();
            boolean checked_komisiPasutri = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiPasutri)).isChecked();
            boolean checked_komisiPemuda = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiPemudaDewasa)).isChecked();
            boolean checked_komisiRemaja = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiRemajaPemuda)).isChecked();
            boolean checked_komisiWanita = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiWanita)).isChecked();

            boolean checked_pelayananAnak = ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananAnak)).isChecked();
            boolean checked_pelayananKaleb = ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananKaleb)).isChecked();
            boolean checked_pelayananPasutri = ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiPasutri)).isChecked();
            boolean checked_pelayananPemuda = ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananPemudaDewasa)).isChecked();
            boolean checked_pelayananRemaja = ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananRemajaPemuda)).isChecked();
            boolean checked_pelayananWanita = ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananWanita)).isChecked();

            String komisi = "";
            Log.d("komisi", komisi);

            String pelayanan = "";
            JSONArray komisiArr = new JSONArray();
            JSONArray pelayananArr = new JSONArray();
            // Check which radio button was clicked
//            switch(v.getId()) {
//                case R.id.register_checkboxKomisiAnak:
            if (checked_komisiAnak){
                if(komisi!="")
                    komisi+=",";
                komisi+="1";
                komisiArr.put(1);
            }
            if (checked_komisiKaleb){
                if(komisi!="")
                    komisi+=",";
                komisi+="2";
                komisiArr.put(2);
            }
            if (checked_komisiPasutri){
                if(komisi!="")
                    komisi+=",";
                komisi+="3";
                komisiArr.put(3);
            }
//                case R.id.register_checkboxKomisiPemudaDewasa:
            if (checked_komisiPemuda){
                if(komisi!="")
                    komisi+=",";
                komisi+="4";
                komisiArr.put(4);
            }
            if (checked_komisiRemaja){
                if(komisi!="")
                    komisi+=",";
                komisi+="5";
                komisiArr.put(5);
            }
            if (checked_komisiWanita){
                if(komisi!="")
                    komisi+=",";
                komisi+="6";
                komisiArr.put(6);
            }


            if (checked_pelayananAnak){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="1";
                pelayananArr.put(1);
            }

            if (checked_pelayananKaleb){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="2";
                pelayananArr.put(2);
            }
            if (checked_pelayananPasutri){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="3";
                pelayananArr.put(3);
            }
            if (checked_pelayananPemuda){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="4";
                pelayananArr.put(4);
            }
            if (checked_pelayananRemaja){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="5";
                pelayananArr.put(5);
            }
            if (checked_pelayananWanita){
                if(pelayanan!="")
                    pelayanan+=",";
                pelayanan+="6";
                pelayananArr.put(6);
            }

//            }
            Log.d("komisi selected",komisi);
            Log.d("pelayanan selected",pelayanan);

            /*Date date = new Date();*/
            String dateInString = null;

            /*try {
                *//*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");*//*
                dateInString = tglLahir;
                *//*date = formatter.parse(dateInString);
                Log.d("now", now);*//*
                Log.d("registerdate", dateInString);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            cont.editprofil(nama, email, telepon, alamat,idbaptis, komisi, pelayanan);
        Log.d("edit profilkomisi ",komisi);
        Log.d("edit profil pel",pelayanan);
            SessionManager smn = new SessionManager(getActivity().getApplicationContext());
           smn.editLoginSession(nama,email,alamat,telepon,idbaptis,komisiArr.toString(),pelayananArr.toString());
    }

    public void generateProfilContent(){
        EditText namaET = (EditText) rootView.findViewById(R.id.profil_editNama);
        EditText alamatET = (EditText) rootView.findViewById(R.id.profil_editAlamat);
        EditText emailET = (EditText) rootView.findViewById(R.id.profil_editEmail);
        EditText teleponET = (EditText) rootView.findViewById(R.id.profil_editTelepon);
        EditText idbaptisET = (EditText) rootView.findViewById(R.id.profil_editIdBaptis);
//        EditText tglLahirET = (EditText) rootView.findViewById(R.id.profil_editTanggalLahir);

        SessionManager sm = new SessionManager(getActivity().getApplicationContext());

        namaET.setText(sm.pref.getAll().get("name").toString(), TextView.BufferType.EDITABLE);
        alamatET.setText(sm.pref.getAll().get("alamat").toString(), TextView.BufferType.EDITABLE);
        emailET.setText(sm.pref.getAll().get("email").toString(), TextView.BufferType.EDITABLE);
        teleponET.setText(sm.pref.getAll().get("telepon").toString(), TextView.BufferType.EDITABLE);

        /*tglLahir = sm.pref.getAll().get("telepon").toString();
        int year = Integer.parseInt(tglLahir.substring(0, 3));
        int month = Integer.parseInt(tglLahir.substring(5, 6));
        int day = Integer.parseInt(tglLahir.substring(8, 9));*/

        /*tglLahirET.setText(sm.pref.getAll().get("tgllahir").toString(),TextView.BufferType.EDITABLE);*/

        idbaptisET.setText(sm.pref.getAll().get("idbaptis").toString(),TextView.BufferType.EDITABLE);

        String komisi = sm.pref.getAll().get("komisi").toString();
        Log.d("isi string komisi",komisi);
        String pelayanan = sm.pref.getAll().get("pelayanan").toString();
        Log.d("isi string pelayanan",pelayanan);
        try {
            JSONArray arrKomisi = new JSONArray(komisi);
            JSONArray arrPelayanan = new JSONArray(pelayanan);
            Log.d("arrKomisi",arrKomisi.toString());
            int length2 = arrKomisi.length();
            for (int j=0; j<length2; j++) {
                Log.d("isi array komisi",Integer.toString(arrKomisi.getInt(j)));

                if (arrKomisi.getInt(j) == 1){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiAnak)).setChecked(true);
                }
                if (arrKomisi.getInt(j) == 2){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiKaleb)).setChecked(true);
                }
                if (arrKomisi.getInt(j) == 3){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiPasutri)).setChecked(true);
                }
                if (arrKomisi.getInt(j) == 4){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiPemudaDewasa)).setChecked(true);
                }
                if (arrKomisi.getInt(j) == 5){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiRemajaPemuda)).setChecked(true);
                }
                if (arrKomisi.getInt(j) == 6){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxKomisiWanita)).setChecked(true);
                }

            }
            int length3 = arrPelayanan.length();
            for (int j=0; j<length3; j++) {
                Log.d("isi array pelayanan",Integer.toString(arrPelayanan.getInt(j)));
                if (arrPelayanan.getInt(j) == 1){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananAnak)).setChecked(true);
                }
                if (arrPelayanan.getInt(j) == 2){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananKaleb)).setChecked(true);
                }
                if (arrPelayanan.getInt(j) == 3){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananPasutri)).setChecked(true);
                }
                if (arrPelayanan.getInt(j) == 4){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananPemudaDewasa)).setChecked(true);
                }
                if (arrPelayanan.getInt(j) == 5){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananRemajaPemuda)).setChecked(true);
                }
                if (arrPelayanan.getInt(j) == 6){
                    ((CheckBox) rootView.findViewById(R.id.profil_checkboxPelayananWanita)).setChecked(true);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    /*public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
    }*/
}