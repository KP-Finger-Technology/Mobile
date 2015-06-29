package fingertech.mobileclientgky;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Andarias Silvanus
 */
public class PasalAlkitabFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String kitab;
    private int pasal;

    private View rootView;
    private LinearLayout myLinearLayout;

    private OnFragmentInteractionListener mListener;
    private Fragment frag;
    private FragmentTransaction fragTransaction;
    private FragmentManager fragManager;

    public static PasalAlkitabFragment newInstance(String param1, String param2) {
        PasalAlkitabFragment fragment = new PasalAlkitabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PasalAlkitabFragment() {}

    public PasalAlkitabFragment(String _kitab, int _pasal) {
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

    private void generateBtnPasal(){
        // Add LinearLayout
        myLinearLayout=(LinearLayout)rootView.findViewById(R.id.container_pasalAlkitab);

        // Add LayoutParams
        myLinearLayout.setOrientation(LinearLayout.VERTICAL);
//        myLinearLayout.setHorizontalGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        myLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        int btnWidth = 150;
        int btnHeight = 100;
        int rightMargin = 5;
        int sumMargin = rightMargin;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnWidth, btnHeight);
        params.setMargins(0, 0, rightMargin, 5);

        // Judul Kitab yang dipilih
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView namaKitab = new TextView(getActivity());
        namaKitab.setTextAppearance(getActivity().getApplicationContext(), R.style.judulPasal);
        namaKitab.setText(kitab);
        namaKitab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.addView(namaKitab);
        myLinearLayout.addView(rowLayout);

        // Tambah garis divider
        View divider = new View(getActivity());
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5);
        LP.setMargins(0,0,0,20);
        divider.setLayoutParams(LP);
        divider.setBackgroundColor(getResources().getColor(R.color.dividerLine));
        myLinearLayout.addView(divider);

        // Tambah text view utk memberi penjelasan jika sedang berada di halaman pasal
        TextView halPasal = new TextView(getActivity());
        halPasal.setTextAppearance(getActivity().getApplicationContext(), R.style.judulPasal);
        halPasal.setText("Pasal");
        halPasal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout = new LinearLayout(getActivity());
        rowLayout.addView(halPasal);
        myLinearLayout.addView(rowLayout);

        // Button-button untuk kitab tersebut sebanyak jumlah pasal
        rowLayout = new LinearLayout(getActivity());
        LinearLayout colLayout = new LinearLayout(getActivity());
        colLayout.setOrientation(LinearLayout.VERTICAL);
        int cnt = 0;
        Button pasalBtn;

        // Untuk menghitung batas horisontal button yang dibuat
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        int sumPadding = 35;
        int jumlahDraw = ((displayWidth-sumPadding-sumMargin)/btnWidth);

        for (int i = 0; i < pasal; i++) {
            cnt++;
            pasalBtn = new Button(getActivity());
            pasalBtn.setText(Integer.toString(i+1));
            pasalBtn.setBackground(getResources().getDrawable(R.drawable.alkitabbuttonstyle));
            pasalBtn.setTextAppearance(getActivity().getApplicationContext(), R.style.pasalAyatButtonStyle);
            pasalBtn.setLayoutParams(params);

            final int finalI = i+1;
            pasalBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frag = new KumpulanBtnAyatAlkitabFragment(kitab, finalI);
                        fragManager = getActivity().getSupportFragmentManager();
                        fragTransaction = fragManager.beginTransaction();
                        fragTransaction.replace(R.id.container, frag);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }
                }
            );

            // Coba-coba
            if (pasalBtn.getParent()!=null)
                ((ViewGroup)pasalBtn.getParent()).removeView(pasalBtn);
            rowLayout.addView(pasalBtn);

            if (cnt >= jumlahDraw) {
                cnt = 0;
                colLayout.addView(rowLayout);
                rowLayout = new LinearLayout(getActivity());
            }
        }

        if (pasal > jumlahDraw)
            myLinearLayout.addView(colLayout);
        else
            myLinearLayout.addView(rowLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pasal_alkitab, container, false);
        generateBtnPasal();
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
