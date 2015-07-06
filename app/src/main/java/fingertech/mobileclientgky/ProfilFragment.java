package fingertech.mobileclientgky;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParsePush;
import com.parse.SaveCallback;

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
import java.util.ArrayList;


/**
 * Created by William Stefan Hartono
 */
public class ProfilFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private Button Button;
    private int pYear;
    private int pMonth;
    private int pDay;

    private int jumKomisi;
    private int jumPelayanan;
    private ArrayList<Integer> idxPelayanan;
    private ArrayList<Boolean> checkedArray;

    private Boolean[] checkedKomisi;
    private String[] namaKomisiArr;
    private Boolean[] checkedPelayanan;
    private ArrayList<String> namaPelayananArr;

    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfilFragment() {}

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

        Viewer v = new Viewer();
        v.execute();

        /*dateET = (EditText) rootView.findViewById(R.id.profil_editTanggalLahir);*/
        Button ubahButton = (Button) rootView.findViewById(R.id.profil_ubah);

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

        unsubscribePush();

        String komisi = "";
        String pelayanan = "";
        JSONArray komisiArr = new JSONArray();
        JSONArray pelayananArr = new JSONArray();


        for (int i=0; i<checkedKomisi.length; i++) {
            if (checkedKomisi[i]) {
                if (komisi != "")
                    komisi += ",";
                komisi += Integer.toString(i+1);
                komisiArr.put(i+1);
//                Log.d("iterasi ke-"+Integer.toString(i)+", isi string komisi:"+komisi,"..");
                ParsePush.subscribeInBackground(namaKomisiArr[i].replace(" ", "").replace("&",""), new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("com.parse.push", "successfully subscribed to the komisi channel.");
                        } else {
                            Log.e("com.parse.push", "failed to subscribe for push to the komisi", e);
                        }
                    }
                });
            }
        }

        for (int i=0; i<checkedPelayanan.length; i++) {
            if (checkedPelayanan[i]) {
                if (pelayanan != "")
                    pelayanan += ",";
                pelayanan += Integer.toString(i+1);
                pelayananArr.put(i+1);
                Log.d("iterasi ke-"+Integer.toString(i)+", isi string pelayanan:"+pelayanan,"..");
                ParsePush.subscribeInBackground(namaPelayananArr.get(i).replace(" ", "").replace("&", ""), new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("com.parse.push", "successfully subscribed to the pelayanan channel.");
                        } else {
                            Log.e("com.parse.push", "failed to subscribe for push to the pelayanan", e);
                        }
                    }
                });
            }
        }

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
            cont.editprofil(nama, email, telepon, alamat, idbaptis, komisi, pelayanan);
        Log.d("edit profilkomisi ",komisi);
        Log.d("edit profil pel",pelayanan);
            SessionManager smn = new SessionManager(getActivity().getApplicationContext());
           smn.editLoginSession(namaET.getText().toString(),emailET.getText().toString(),alamatET.getText().toString(),teleponET.getText().toString(),idbaptisET.getText().toString(),komisiArr.toString(),pelayananArr.toString());
        Log.d("pelayananArr.tostring",pelayananArr.toString());



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
    class Viewer extends AsyncTask<String, String, String> {
        private LinearLayout myLinearLayout;
        private LinearLayout.LayoutParams params;
        private int sumPelayanan;
        JSONArray arr = new JSONArray();

        ProgressDialog progressDialog;

        public JSONArray getArr() {
            return arr;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Koneksi ke server");
        }

        ;

        @Override
        protected String doInBackground(String... params) {
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());

            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url + "view_komisipelayanan.php");
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
                    sumPelayanan = res.getInt("count");
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
            progressDialog.dismiss();
            myLinearLayout = (LinearLayout) rootView.findViewById(R.id.checkbox_profil);

            // Add LayoutParams
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);
            LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            childParams.setMargins(0, -10, 0, 0);

            int dataLength = arr.length();
            checkedKomisi = new Boolean[dataLength];
            namaKomisiArr = new String[dataLength];
            checkedPelayanan = new Boolean[sumPelayanan+1];
            namaPelayananArr = new ArrayList<String>();
            for (int idx = 0; idx < checkedKomisi.length; idx++)
                checkedKomisi[idx] = false;
            for (int idx = 0; idx < checkedPelayanan.length; idx++)
                checkedPelayanan[idx] = false;

            String namaKomisi = null;
            jumPelayanan = 0;
            idxPelayanan = new ArrayList<Integer>();
            jumKomisi = dataLength;

            LinearLayout child;
            int idViewPelayanan = 999;
            checkedArray = new ArrayList<Boolean>();

            int idx_pelayanan = 0;
            int it = 0;
            int idKomisi;

            SessionManager sm = new SessionManager(getActivity().getApplicationContext());

            String komisistring = sm.pref.getAll().get("komisi").toString();
            Log.d("isi string komisi",komisistring);
            String pelayananstring = sm.pref.getAll().get("pelayanan").toString();
            Log.d("isi string pelayanan",pelayananstring);

            JSONArray arrKomisi = null;
            JSONArray arrPelayanan = null;
            ArrayList<String> arrayPelayanan = null;
            arrayPelayanan = new ArrayList<String>();

            try {
                arrKomisi = new JSONArray(komisistring);
                arrPelayanan = new JSONArray(pelayananstring);

                Log.d("arpelayanan",arrPelayanan.toString());

                if(arrPelayanan.length()>0) {
                    for (int j = 0; j < arrPelayanan.length(); j++) {
                        arrayPelayanan.add(arrPelayanan.getString(j));
                        //aray dari shared preferences
                    }
                }

                Log.d("isi arr pelayanan", arrayPelayanan.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Generate konten Register dalam loop for
            for (int i = 0; i < dataLength; i++) {
                JSONObject jsonobj = null;

                try {
                    if(arrKomisi.length()!=0){
                       //ada isi array komisi

                    }else{
                        //array komisi kosong
                        it=99;
                        Log.d("iterator","99");
                    }

                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject", arr.getJSONObject(i).toString());
                    namaKomisi = jsonobj.getString("namakomisi");
                    idKomisi = jsonobj.getInt("idkomisi");
                    JSONArray jsonArr = jsonobj.getJSONArray("atribut");
                    namaKomisiArr[i] = namaKomisi;
                    final int length2 = jsonArr.length();
                    idxPelayanan.add(length2);

                    // Membuat checkbox komisi
                    final CheckBox komisi = new CheckBox(getActivity());
                    komisi.setText(namaKomisi);
                    komisi.setLayoutParams(params);
                    komisi.setId(i);
                    final String finalNamaKomisi = namaKomisi;
                    final int finalI = i;
                    final int finalIdViewPelayanan = idViewPelayanan;

                    if(it<=idKomisi && idKomisi<=checkedKomisi.length){
                        if(arrKomisi.get(it).toString().equals(Integer.toString(idKomisi))) {
                            komisi.setChecked(true);
                            checkedKomisi[idKomisi - 1]= true;
                            if(it + 1 == arrKomisi.length()){
                            }
                            else {
                                it++;
                            }
                        }else {
                        komisi.setChecked(false);
                       }
                    }else{
                        it=0;
                    }

                    // Set listener pada setiap checkbox
                    komisi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            checkedArray.add(komisi.isChecked());
                            checkedKomisi[finalI] = komisi.isChecked();
                            Log.d("from register-> checkedKomisi[" + Integer.toString(finalI) + "] = " + Boolean.toString(checkedKomisi[finalI]), "..");
                            int id = finalIdViewPelayanan;
                            for (int i = 0; i < length2; i++) {
                                CheckBox tmp_pelayanan = (CheckBox) rootView.findViewById(id);
                                Log.d("iterasi ke-" + Integer.toString(i) + " utk mencoba setEnabled anak2 checkbox", "");
                                if (tmp_pelayanan != null) {
                                    Log.d("tmp_pelayanan tidak null", "..");
                                    if (komisi.isChecked())
                                        tmp_pelayanan.setEnabled(true);
                                    else {
                                        tmp_pelayanan.setChecked(false);
                                        tmp_pelayanan.setEnabled(false);
                                    }
                                }
                                id--;
                            }
                        }
                    });

                    myLinearLayout.addView(komisi);

                    child = new LinearLayout(getActivity());
                    child.setOrientation(LinearLayout.VERTICAL);
                    child.setPadding(30, 0, 0, 0);

                    String namaPelayanan = null, tanggal = null, gedung = null, waktuMulai = null, waktuSelesai = null;

                    for (int j = 0; j < length2; j++) {
                        namaPelayanan = jsonArr.getJSONObject(j).getString("jenispelayanan");
                        waktuMulai = jsonArr.getJSONObject(j).getString("waktumulai");
                        waktuSelesai = jsonArr.getJSONObject(j).getString("waktuselesai");
                        int idPelayanan = jsonArr.getJSONObject(j).getInt("idpelayanan");
                        //dpt in id pelayanan dari json

//                        idPelayanan = jsonArr.getJSONObject(j).getInt("idpelayanan");

                        namaPelayananArr.add(namaPelayanan);
                        jumPelayanan++;

                        //  Membuat checkbox pelayanan
                        if ((namaPelayanan != null) && (namaPelayanan != "null")) {
                            final CheckBox pelayanan = new CheckBox(getActivity());
                            pelayanan.setText(namaPelayanan + " (" + waktuMulai + "-" + waktuSelesai + ")");
                            pelayanan.setLayoutParams(childParams);
                            pelayanan.setId(idViewPelayanan);

                            if(arrayPelayanan.contains(Integer.toString(idPelayanan))){
                                pelayanan.setChecked(true);
                                checkedPelayanan[idPelayanan -1 ] = true;
//                                Log.d("pelayanan contains",Integer.toString(idPelayanan -1 ));
                            }
                            else {
//                                pelayanan.setChecked(false);
//                                checkedPelayanan[idPelayanan -1 ] = false;
                            }

//                            pelayanan.setEnabled(false);

                            idViewPelayanan--;
                            final int idx_pelayanan_tmp = idPelayanan-1;
//                            final int idx_pelayanan_tmp = idx_pelayanan;

                            // Set listener pada setiap checkbox
                            final String finalNamaPelayanan = namaPelayanan;
                            pelayanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (finalNamaPelayanan != "null")
                                        checkedPelayanan[idx_pelayanan_tmp] = pelayanan.isChecked();
//                                Log.d("from register-> checkedPelayanan["+Integer.toString(finalI)+"] = "+Boolean.toString(checkedKomisi[finalI]),"..");
                                }
                            });
                            idx_pelayanan++;
                            child.addView(pelayanan);
                        }
                    }
                    myLinearLayout.addView(child);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void unsubscribePush() {

        SessionManager smn = new SessionManager(getActivity().getApplicationContext());
        try {
            JSONArray arrKomisi = new JSONArray(smn.pref.getAll().get("namakomisi").toString());

            for ( int i = 0 ; i < arrKomisi.length(); i++){
//                Log.d("iterasi ke-" + i, "isi komisi:" + arrKomisi.get(i).toString());
                ParsePush.unsubscribeInBackground(arrKomisi.get(i).toString().replace(" ","").replace("&",""), new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("com.parse.push", "successfully subscribed to the komisi channel.");
                        } else {
                            Log.e("com.parse.push", "failed to subscribe for push to the komisi", e);
                        }
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}