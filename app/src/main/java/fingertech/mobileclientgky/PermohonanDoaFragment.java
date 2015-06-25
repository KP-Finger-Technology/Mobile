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
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PermohonanDoaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PermohonanDoaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PermohonanDoaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Controller cont = new Controller();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PermohonanDoaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PermohonanDoaFragment newInstance(String param1, String param2) {
        PermohonanDoaFragment fragment = new PermohonanDoaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PermohonanDoaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /*EditText ET2 = (EditText) findViewById(R.id.);*/
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

    // TODO: Rename method, update argument and hook method into UI event
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

    public void tulisDataDoa(){
        EditText namaET = (EditText) rootView.findViewById(R.id.permohonanDoa_editNama);
//        EditText umurET = (EditText) rootView.findViewById(R.id.permohonanDoa_editUmur);
        EditText emailET = (EditText) rootView.findViewById(R.id.permohonanDoa_editEmail);
        EditText teleponET = (EditText) rootView.findViewById(R.id.permohonanDoa_editTelepon);

        try {
            SessionManager sm = new SessionManager(getActivity().getApplicationContext());
            namaET.setText(sm.pref.getAll().get("name").toString(), TextView.BufferType.EDITABLE);
//            umurET.setText("1", TextView.BufferType.EDITABLE);
            emailET.setText(sm.pref.getAll().get("email").toString(), TextView.BufferType.EDITABLE);
            teleponET.setText(sm.pref.getAll().get("telepon").toString(), TextView.BufferType.EDITABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
