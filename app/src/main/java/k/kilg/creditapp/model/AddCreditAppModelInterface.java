package k.kilg.creditapp.model;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 05.04.2018.
 * k.kilg.creditapp.model
 * 05.04.2018
 * 10:55
 */
public interface AddCreditAppModelInterface {
    Credit getCredit();
    void setCredit(Credit credit);
    boolean isModelEmpty();
}
