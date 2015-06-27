package fingertech.mobileclientgky;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.Calendar;


/**
 * Created by William Stefan Hartono
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    private int jumKomisi;
    private int jumPelayanan;
    private ArrayList<Integer> idxPelayanan;
    private ArrayList<Boolean> checkedArray;

    private Boolean[] checkedKomisi;
    private String[] namaKomisiArr;
    private Boolean[] checkedPelayanan;
    private ArrayList<String> namaPelayananArr;

    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {}

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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register, container, false);

        dateET = (EditText) rootView.findViewById(R.id.register_editTanggalLahir);
        daftarButton = (Button) rootView.findViewById(R.id.register_daftar);

        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
            datePicker.show(getFragmentManager(), "datePicker");
            }
        });

        Viewer v = new Viewer();
        v.execute();

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

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void register() {
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
            String nama = null, email = null, telepon = null, alamat = null, idbaptis = null;
            try {
                nama = URLEncoder.encode(namaET.getText().toString(), "utf-8");
                email = URLEncoder.encode(emailET.getText().toString(), "utf-8");
                telepon = URLEncoder.encode(teleponET.getText().toString(), "utf-8");
                alamat = URLEncoder.encode(alamatET.getText().toString(), "utf-8");
                idbaptis = URLEncoder.encode(idbaptisET.getText().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String komisi = "";
            Log.d("komisi", komisi);

            String pelayanan = "";

            for (int i=0; i<checkedKomisi.length; i++) {
                if (checkedKomisi[i]) {
                    if (komisi != "")
                        komisi += ",";
                    komisi += Integer.toString(i+1);
                    Log.d("iterasi ke-"+Integer.toString(i)+", isi string komisi:"+komisi,"..");
                    ParsePush.subscribeInBackground(namaKomisiArr[i], new SaveCallback() {
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
                    Log.d("iterasi ke-"+Integer.toString(i)+", isi string pelayanan:"+pelayanan,"..");
                    ParsePush.subscribeInBackground(namaPelayananArr.get(i), new SaveCallback() {
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

            String dateInString = null;

            try {
                dateInString = now;
                Log.d("registerdateee", dateInString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            cont.register(nama, pass, email, telepon, alamat, dateInString, idbaptis, komisi, pelayanan);
            frag = new Home.PlaceholderFragment();
            switchFragment();

        } else {
            // Password dan konfirmasi tidak sama, tampilkan toast.
            Toast.makeText(getActivity(), "Re-enter Password", Toast.LENGTH_LONG).show();
        }
    }

    private void switchFragment() {
        fragManager = getActivity().getSupportFragmentManager();
        fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.container, frag);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

    public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private DatePickerDialog.OnDateSetListener mDateSetListener;

        public DatePickerDialogFragment() {}

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
    }

    class Viewer extends AsyncTask<String, String, String> {
        private LinearLayout myLinearLayout;
        private LinearLayout.LayoutParams params;
        private int sumPelayanan;
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
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());

            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Controller.url+"view_komisipelayanan.php");
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
            myLinearLayout=(LinearLayout)rootView.findViewById(R.id.checkbox_register);

            // Add LayoutParams
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(0, 10, 20, 0);
            LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            childParams.setMargins(0,-10,0,0);

            int dataLength = arr.length();
            checkedKomisi = new Boolean[dataLength];
            namaKomisiArr = new String[dataLength];
            checkedPelayanan = new Boolean[sumPelayanan];
            namaPelayananArr = new ArrayList<String>();
            for (int idx=0; idx<checkedKomisi.length; idx++)
                checkedKomisi[idx] = false;
            for (int idx=0; idx<checkedPelayanan.length; idx++)
                checkedPelayanan[idx] = false;

            String namaKomisi = null;
            jumPelayanan = 0; idxPelayanan = new ArrayList<Integer>();
            jumKomisi = dataLength;

            LinearLayout child;
            int idViewPelayanan = 999;
            checkedArray = new ArrayList<Boolean>();

            int idx_pelayanan = 0;

            // Generate konten Register dalam loop for
            for (int i = 0; i < dataLength; i++){
                JSONObject jsonobj = null;

                try {
                    jsonobj = arr.getJSONObject(i);
                    Log.d("JSONObject",arr.getJSONObject(i).toString());
                    namaKomisi = jsonobj.getString("namakomisi");
//                    idKomisi = jsonobj.getInt("idkomisi");
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

                    // Set listener pada setiap checkbox
                    komisi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//                            checkedArray.add(komisi.isChecked());
                            checkedKomisi[finalI] = komisi.isChecked();
                            Log.d("from register-> checkedKomisi["+Integer.toString(finalI)+"] = "+Boolean.toString(checkedKomisi[finalI]),"..");
                            int id = finalIdViewPelayanan;
                            for (int i = 0; i < length2; i++) {
                                CheckBox tmp_pelayanan = (CheckBox) rootView.findViewById(id);
                                Log.d("iterasi ke-"+Integer.toString(i)+" utk mencoba setEnabled anak2 checkbox","");
                                if (tmp_pelayanan != null) {
                                    Log.d("tmp_pelayanan tidak null","..");
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
                    child.setPadding(30,0,0,0);

                    String namaPelayanan = null, tanggal = null, gedung = null, waktuMulai = null, waktuSelesai = null;

                    for (int j = 0; j < length2; j++) {
                        namaPelayanan = jsonArr.getJSONObject(j).getString("jenispelayanan");
                        waktuMulai = jsonArr.getJSONObject(j).getString("waktumulai");
                        waktuSelesai = jsonArr.getJSONObject(j).getString("waktuselesai");
//                        idPelayanan = jsonArr.getJSONObject(j).getInt("idpelayanan");

                        Log.d("nama pelayanan"+namaPelayanan,"..");
                        namaPelayananArr.add(namaPelayanan);
                        jumPelayanan++;

                        //  Membuat checkbox pelayanan
                        if (namaPelayanan != null) {
                            final CheckBox pelayanan = new CheckBox(getActivity());
                            pelayanan.setText(namaPelayanan + " (" + waktuMulai + "-" + waktuSelesai + ")");
                            pelayanan.setLayoutParams(childParams);
                            pelayanan.setId(idViewPelayanan);
                            pelayanan.setEnabled(false);
                            idViewPelayanan--;
                            final int idx_pelayanan_tmp = idx_pelayanan;

                            // Set listener pada setiap checkbox
                            final String finalNamaPelayanan = namaPelayanan;
                            pelayanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (finalNamaPelayanan != "null")
                                    checkedPelayanan[idx_pelayanan_tmp] = komisi.isChecked();
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
}