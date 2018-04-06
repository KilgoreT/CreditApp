package k.kilg.creditapp.model;

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
        if (!isModelEmpty()) {
            return mCredit;
        }
        return null;
        //todo: add message
        //throw new IllegalArgumentException("Model is empty");
    }

    @Override
    public void setModel(Credit credit) {
        if (credit != null) {
            mCredit = credit;
        }
    }

    @Override
    public boolean isModelEmpty() {
        return mCredit == null;
    }
}
