package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JadwalPelayananFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JadwalPelayananFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KolportaseFragment extends Fragment {
    private String judul;
    private String pengarang;
    private String keterangan;
    private String gambar;

    public String getJudul() {
        return judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Controller cont = new Controller();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JadwalPelayananFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KolportaseFragment newInstance(String param1, String param2) {
        KolportaseFragment fragment = new KolportaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KolportaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /*cont.viewJadwalPelayanan();
        Log.d("Jadwal ", cont.getArrData().toString());*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View rootView = inflater.inflate(R.layout.fragment_jadwal_pelayanan, container, false);
//        LinearLayout myLinearLayout;
//        Log.d("masuk 1:", "yes");
//        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_jadwalPelayanan);
//        Log.d("masuk 2:", "yes");
//        //add LayoutParams
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
//        Log.d("masuk 3:", "yes");
//
//        Button dummy = new Button(getActivity());
//        dummy.setText("dummy!");
//        dummy.setLayoutParams(params);
//        myLinearLayout.addView(dummy);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kolportase, container, false);
//        return rootView;
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
}
