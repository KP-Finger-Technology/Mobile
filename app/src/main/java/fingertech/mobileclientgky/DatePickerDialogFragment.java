package fingertech.mobileclientgky;

import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSetListener mDateSetListener;

    public DatePickerDialogFragment() {
        // nothing to see here, move along
    }

    public DatePickerDialogFragment(OnDateSetListener callback) {
        mDateSetListener = (OnDateSetListener) callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        return new DatePickerDialog(getActivity(),
                mDateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    public int year_chosen, month_chosen, day_chosen;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
//        EditText ET = (EditText) getActivity().findViewById()
//        Log.d("dr datePicker tanggal yg dipilih, tahun:"+Integer.toString(year)+" bulan:"+Integer.toString(month)+" hari:"+Integer.toString(day),"..");
        Log.d("masuk onDateSet dialogFragment","..");
        year_chosen = year;
        month_chosen = month + 1;
        day_chosen = day;
        Toast.makeText(getParentFragment().getActivity(), "masuk dateSetListener..", Toast.LENGTH_LONG).show();
    }

}