package k.kilg.creditapp.model;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 05.04.2018.
 * k.kilg.creditapp.model
 * 05.04.2018
 * 10:56
 */
public class AddCreditAppModel implements AddCreditAppModelInterface {
    private Credit mCredit = new Credit();

    @Override
    public Credit getCredit() {
        return mCredit;
    }

    @Override
    public void setModel(Credit credit) {
        mCredit = credit;
    }

    @Override
    public boolean isModelEmpty() {
        return mCredit == null;
    }
}
