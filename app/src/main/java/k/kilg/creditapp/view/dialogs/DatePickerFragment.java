package k.kilg.creditapp.view.dialogs;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import k.kilg.creditapp.R;
import k.kilg.creditapp.view.fragments.AddCreditSimpleFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {



    public DatePickerFragment() {
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker dp = dpd.getDatePicker();
        dp.setMaxDate(System.currentTimeMillis());
        c.set(2000, 0, 1);
        dp.setMinDate(c.getTimeInMillis());

        return dpd;
    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth th selected day of the month (1-31, depending on
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateFormated = sdf.format(c.getTime());

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.creditFragment);
        if (fragment instanceof AddCreditSimpleFragment) {
            TextView textView = fragment.getView().findViewById(R.id.addCredit_tvDate);
            textView.setText(dateFormated);
        }
    }
}
