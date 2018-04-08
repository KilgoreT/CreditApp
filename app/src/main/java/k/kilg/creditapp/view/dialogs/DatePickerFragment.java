package k.kilg.creditapp.view.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import k.kilg.creditapp.R;
import k.kilg.creditapp.view.fragments.AddCreditFragment;
import k.kilg.creditapp.view.fragments.AddCreditSimpleFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {



    public DatePickerFragment() {
    }

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p>
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     * <p>
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override {@link #onCancel(DialogInterface)}
     * and {@link #onDismiss(DialogInterface)}.</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */


    //todo: сделать ограничения по дате

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
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
        Log.d("###", ">>" + getClass().getName() + " String dateFormated = " + dateFormated);
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.creditFragment);
        if (fragment instanceof AddCreditSimpleFragment) {
            TextView textView = fragment.getView().findViewById(R.id.addCredit_tvDate);
            textView.setText(dateFormated);
        }
    }
}
