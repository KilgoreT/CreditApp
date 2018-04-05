package k.kilg.creditapp.model;

import java.util.ArrayList;
import java.util.List;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:51
 */
public class CreditAppModel implements CreditAppModelInterface {

    private List<Credit> credits;

    public CreditAppModel() {
        this.credits = new ArrayList<>();
        credits.add(new Credit("First", true, 24, 5000000, 10));
    }

    @Override
    public List<Credit> getCredits() {
        return credits;
    }

    @Override
    public void addCredit(Credit credit) {
        credits.add(credit);
    }
}
