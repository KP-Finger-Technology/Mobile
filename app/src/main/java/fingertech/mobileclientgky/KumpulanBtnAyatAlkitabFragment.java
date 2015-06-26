package fingertech.mobileclientgky;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KumpulanBtnAyatAlkitabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KumpulanBtnAyatAlkitabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KumpulanBtnAyatAlkitabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KumpulanBtnAyatAlkitabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KumpulanBtnAyatAlkitabFragment newInstance(String param1, String param2) {
        KumpulanBtnAyatAlkitabFragment fragment = new KumpulanBtnAyatAlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KumpulanBtnAyatAlkitabFragment() {}

    private String kitab;
    private int pasal;

    public KumpulanBtnAyatAlkitabFragment(String _kitab, int _pasal) {
        this.kitab = _kitab;
        this.pasal = _pasal;
    }

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
//        return inflater.inflate(R.layout.fragment_kumpulan_btn_ayat_alkitab, container, false);
        rootView = inflater.inflate(R.layout.fragment_kumpulan_btn_ayat_alkitab, container, false);
        generateBtnAyat();
        return rootView;
    }

    private LinearLayout myLinearLayout;
    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    private void generateBtnAyat() {
        DataBaseHelper DB = new DataBaseHelper(getActivity().getApplicationContext());
        DB.openDataBase();
        int jumAyat = DB.getJumlahAyat(kitab, pasal);

        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_btnAyatAlkitab);
        // Add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0, 0, 10, 15);

        // Judul Kitab yang dipilih
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView namaKitab = new TextView(getActivity());
        namaKitab.setText("Silakan pilih ayat dari kitab "+kitab+" pasal "+Integer.toString(pasal)+", dengan jumlah ayat:"+Integer.toString(jumAyat));
        namaKitab.setLayoutParams(params);
        rowLayout.addView(namaKitab);
        myLinearLayout.addView(rowLayout);

        // Button-button untuk pasal tersebut sebanyak jumlah ayat
        rowLayout = new LinearLayout(getActivity());
        LinearLayout colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        int cnt = 0;
        Button pasalBtn;

        Log.d("jumlah ayat yang dipilih",Integer.toString(jumAyat));

        for (int i=0; i<jumAyat; i++) {
            cnt++;
            pasalBtn = new Button(getActivity());
            pasalBtn.setText(Integer.toString(i+1));
            pasalBtn.setLayoutParams(params);

            final int finalI = i+1;
            pasalBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("From KumpulanBtnAlkitabFragment", "kitab=" + kitab + " & pasal=" + Integer.toString(pasal));
                        frag = new AyatAlkitabFragment(kitab, pasal, finalI);
                        fragManager = getActivity().getSupportFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container, frag);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }
                }
            );

            // Coba-coba
            if (pasalBtn.getParent()!=null) {
                ((ViewGroup) pasalBtn.getParent()).removeView(pasalBtn);
                Log.d("masuk removeView pasalBtn from KumpulanBtnAyat","..");
            }

            rowLayout.addView(pasalBtn);
            if (cnt>=5) {
                cnt = 0;
                colLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
            }
        }
        if (jumAyat>5)
            myLinearLayout.addView(colLayout);
        else
            myLinearLayout.addView(rowLayout);
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
