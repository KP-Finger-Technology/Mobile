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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UbahPasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UbahPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UbahPasswordFragment extends Fragment implements View.OnClickListener{
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
     * @return A new instance of fragment ProfilPribadiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UbahPasswordFragment newInstance(String param1, String param2) {
        UbahPasswordFragment fragment = new UbahPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UbahPasswordFragment() {
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

    public void ubahPassClicked(View v){
        EditText lamaET = (EditText) rootView.findViewById(R.id.ubahPassword_editPasswordLama);
        EditText baruET = (EditText) rootView.findViewById(R.id.ubahPassword_editPasswordBaru);
        EditText baruconET = (EditText) rootView.findViewById(R.id.ubahPassword_editKonfirmasiPasswordBaru);

        SessionManager sm = new SessionManager(getActivity().getApplicationContext());

        String lama = lamaET.getText().toString();
        if(lama.equals(sm.pref.getAll().get("pass").toString())){

        String pass = baruET.getText().toString();
        String passcon = baruconET.getText().toString();
        if (pass.equals(passcon)) {
            Controller cont = new Controller(getActivity().getApplicationContext());
            cont.editPass(pass);
        }else{
            Toast.makeText(getActivity(), "Re-enter New Password", Toast.LENGTH_LONG).show();
        }

        }else {
            Toast.makeText(getActivity(), "Re-enter Old Password", Toast.LENGTH_LONG).show();
        }



        }
}
