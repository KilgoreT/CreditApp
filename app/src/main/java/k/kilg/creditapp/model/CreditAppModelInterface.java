package k.kilg.creditapp.model;

import java.util.List;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:46
 */
public interface CreditAppModelInterface {
    List<Credit> getCredits();
    void addCredit(Credit credit);
    void removeCredit(Credit credit);
    void updateCredit(Credit credit);

    void setData(List<Credit> data);
}
