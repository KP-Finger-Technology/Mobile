package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by William Stefan Hartono
 */
public class PermohonanDoaFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Controller cont = new Controller();

    public static PermohonanDoaFragment newInstance(String param1, String param2) {
        PermohonanDoaFragment fragment = new PermohonanDoaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PermohonanDoaFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_permohonan_doa, container, false);
        tulisDataDoa();
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    public void switchFragment() {
        fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

	// Fungsi untuk menuliskan data user secara otomatis pada form permohonan doa
	// Hanya bisa digunakan apabila user sudah login terlebih dahulu
    public void tulisDataDoa(){
        EditText namaET = (EditText) rootView.findViewById(R.id.permohonanDoa_editNama);
        EditText emailET = (EditText) rootView.findViewById(R.id.permohonanDoa_editEmail);
        EditText teleponET = (EditText) rootView.findViewById(R.id.permohonanDoa_editTelepon);

        try {
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());
            namaET.setText(sm.pref.getAll().get("name").toString(), TextView.BufferType.EDITABLE);
            emailET.setText(sm.pref.getAll().get("email").toString(), TextView.BufferType.EDITABLE);
            teleponET.setText(sm.pref.getAll().get("telepon").toString(), TextView.BufferType.EDITABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
