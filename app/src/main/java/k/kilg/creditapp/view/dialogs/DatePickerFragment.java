package k.kilg.creditapp.view.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import k.kilg.creditapp.R;
import k.kilg.creditapp.view.fragments.AddCreditFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private static final int MIN_CALENDAR_YEAR = 2000;
    private static final int MIN_CALENDAR_MONTH = 0;
    private static final int MIN_CALENDAR_DATE = 1;

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
        c.set(MIN_CALENDAR_YEAR, MIN_CALENDAR_MONTH, MIN_CALENDAR_DATE);
        dp.setMinDate(c.getTimeInMillis());

        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateFormated = sdf.format(c.getTime());

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.creditFragment);
        if (fragment instanceof AddCreditFragment) {
            TextView textView = fragment.getView().findViewById(R.id.addCredit_tvDate);
            textView.setText(dateFormated);
        }
    }
}
