package k.kilg.creditapp.model;

import java.util.List;
import java.util.Set;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:46
 */
public interface CreditAppModelInterface {

    void getInitialData();
    void initDbListener();
    void addCredit(Credit credit);
    void removeCredit(Credit credit);
    void updateCredit(Credit credit);
    List<Credit> getCreditsList();

}
