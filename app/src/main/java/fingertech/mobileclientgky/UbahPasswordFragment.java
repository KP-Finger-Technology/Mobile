package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by William Stefan Hartono
 */
public class UbahPasswordFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View rootView;


    private OnFragmentInteractionListener mListener;

    public static UbahPasswordFragment newInstance(String param1, String param2) {
        UbahPasswordFragment fragment = new UbahPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UbahPasswordFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Button daftarButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ubah_password, container, false);
        daftarButton = (Button) rootView.findViewById(R.id.ubahPassword_ubah);
        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahPassClicked(rootView);
            }
        });
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
    public void onClick(View v) {}

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void ubahPassClicked(View v) {
        EditText lamaET = (EditText) rootView.findViewById(R.id.ubahPassword_editPasswordLama);
        EditText baruET = (EditText) rootView.findViewById(R.id.ubahPassword_editPasswordBaru);
        EditText baruconET = (EditText) rootView.findViewById(R.id.ubahPassword_editKonfirmasiPasswordBaru);

        SessionManager sm = new SessionManager(getActivity().getApplicationContext());

        String lama = lamaET.getText().toString();
        if(lama.equals(sm.pref.getAll().get("pass").toString())) {
            String pass = baruET.getText().toString();
            String passcon = baruconET.getText().toString();
			
			if (pass.equals(passcon)) {
				Controller cont = new Controller(getActivity().getApplicationContext());
				cont.editPass(pass);
			} else {
				Toast.makeText(getActivity(), "Masukkan lagi password baru Anda", Toast.LENGTH_LONG).show();
			}    
		} else {
			Toast.makeText(getActivity(), "Masukkan lagi password lama Anda", Toast.LENGTH_LONG).show();
		}
    }
}
