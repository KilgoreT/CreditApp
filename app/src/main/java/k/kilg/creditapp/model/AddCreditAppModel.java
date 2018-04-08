package k.kilg.creditapp.model;

import android.util.Log;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 05.04.2018.
 * k.kilg.creditapp.model
 * 05.04.2018
 * 10:56
 */
public class AddCreditAppModel implements AddCreditAppModelInterface {
    private Credit mCredit;

    @Override
    public Credit getCredit() {
        if (mCredit == null) {
            Log.d("###", ">>" + getClass().getSimpleName() + ":getCredit mCredit = null");
        }
        return mCredit;
    }

    @Override
    public void setCredit(Credit credit) {
        Log.d("###", ">>" + getClass().getSimpleName() + "setCredit mCredit");
        if (credit != null) {
            mCredit = credit;
            Log.d("###", ">>" + getClass().getSimpleName() + "setCredit mCredit != null");
        }
    }

    @Override
    public boolean isModelEmpty() {
        return mCredit == null;
    }
}
